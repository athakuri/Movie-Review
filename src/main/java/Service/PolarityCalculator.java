package Service;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.PTBTokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static Service.DataDictionary.negationWord;

/**
 * Created by iam on 7/6/16.
 */
public class PolarityCalculator extends Thread{


    public double getMoviePolarity(String inputText){
        List<String> tokenizedText = getTokenizedWords(inputText);
        double textPolarity = calculatePolarity(tokenizedText);
        return textPolarity;
    }



    public List<String> getTokenizedWords(String content){


        TokenizerFactory<Word> tf = null;
        tf = PTBTokenizer.factory();

        List<Word> tokens_words = tf.getTokenizer(new StringReader(content)).tokenize();

        List<String> tokenizationCompleted = new ArrayList<>();
        Pattern p = Pattern.compile("[^a-z]");
        tokens_words.stream()
                .filter(item-> !p.matcher(item.word()).matches())
                .collect(Collectors.toList())
                .forEach(tokens->{
                    String tokenizedWord = tokens.word().trim();
                    if(!tokenizedWord.trim().equals("")){
                        Set<String> wordsTobeIgnored = DataDictionary.wordsTobeIgnored.keySet();
                        boolean unusable = false;
                        for(String word:wordsTobeIgnored){
                            if(tokenizedWord.equalsIgnoreCase(word)){
                               unusable = true;
                            }
                        }
                        if(!unusable){
                            tokenizationCompleted.add(tokenizedWord);
                        }
                    }
                });
        return tokenizationCompleted;
    }

    public double calculatePolarity(List<String> tokenizedWord){
        Stemmer s = new Stemmer();
        double polarity=0.0;
        int itemIndex = 0;
        for (String item:tokenizedWord) {
            System.out.println(item);
            String stemmeredWord = s.stem(item);
            System.out.println(stemmeredWord);
            if (DataDictionary.wordDictionary.get(stemmeredWord) != null) {
                double tempPolarity = DataDictionary.wordDictionary.get(stemmeredWord);
                if (itemIndex > 0) {
                    String previousWord = tokenizedWord.get(itemIndex - 1);
                    System.out.println("Previous Word: "+previousWord);
                    Set<String> negationWords = DataDictionary.negationWord.keySet();
                    for(String neg:negationWords){
                        if(neg.equalsIgnoreCase(previousWord)){
                            System.out.println("Inside Negation Flag.");
                            if (tempPolarity < 0) {
                                tempPolarity = Math.abs(tempPolarity);
                            } else {
                                tempPolarity = tempPolarity * -1;
                            }
                            break;
                        }
                    }

                }
                System.out.println("Key Word: " + stemmeredWord + " and Polarity: " + tempPolarity);
                polarity += tempPolarity;
            }

            itemIndex++;
        }
        return polarity;
    }

    /*public static void main(String[] args) {
       Set<String> negationWords = DataDictionary.negationWord.keySet();
        for(String neg:negationWords){
            System.out.println(neg);
            if(neg.equalsIgnoreCase()){

            }
        }
    }*/
}
