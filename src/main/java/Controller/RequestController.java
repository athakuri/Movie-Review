package Controller;

import Service.DataDictionary;
//import Service.InitiateOperation;
import Service.PolarityCalculator;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Created by iam on 7/6/16.
 */
public class RequestController extends AbstractVerticle{

    public static void main(String[] args) {

        DataDictionary.loadDictionary();
        DataDictionary.loadNegation();
        DataDictionary.loadStopWords();
        if (DataDictionary.wordDictionary.size()==0&&DataDictionary.negationWord.size()==0&&DataDictionary.wordsTobeIgnored.size()==0){
            System.out.println("Please View the Error Log and Restart Again");
            System.exit(0);
        }
        System.out.println("Deploying Verticle");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RequestController());

    }

    @Override
    public void start() throws Exception {
        System.out.println("Verticle Deployed Successfully.");

        Router router = Router.router(vertx);
        vertx.createHttpServer().requestHandler(router::accept).listen(9000);

        router.route("/").handler(rtx->{
           rtx.response().setChunked(true);
           rtx.response().sendFile("webroot/index.html");
        });

        router.route("/Js/*").handler(StaticHandler.create("webroot/Js"));
        router.route("/Css/*").handler(StaticHandler.create("webroot/Css"));
        router.route("/images/*").handler(StaticHandler.create("webroot/images"));


        router.route("/fetchNews/:movieReview/*").handler(rtx->{
           vertx.executeBlocking(objectFuture -> {
               String movieReview = rtx.request().getParam("movieReview");
               movieReview = movieReview.replaceAll("%20"," ");
               PolarityCalculator polarityCalculator = new PolarityCalculator();
               objectFuture.complete(polarityCalculator.getMoviePolarity(movieReview));

           },res->{
               rtx.response().setChunked(true);
               rtx.response().write(res.result().toString());
               System.out.println("Response Written Successfully. Now Closing Response");
               rtx.response().end();
           });
        });


    }
}
