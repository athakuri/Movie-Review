package Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: SACHIN
 * Date: 7/6/2016.
 */
public class DataDictionary {


    public static Hashtable<String,Double> wordDictionary = new Hashtable<>();
    public static Hashtable<String,Integer> wordsTobeIgnored = new Hashtable<>();
    public static Hashtable<String,Integer> negationWord = new Hashtable<>();

    public static void loadDictionary(){


        String projectDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
        String dataDirectory = projectDirectory+"/resources/sortedposnegword.txt";
        long startTime,endTime;
        try {
            startTime = System.currentTimeMillis();
            Files.newBufferedReader(Paths.get(dataDirectory)).lines().forEach(item->{
                String[] splittedItem = item.split(",");
                wordDictionary.put(splittedItem[0], Double.valueOf(splittedItem[1]));
            });
            endTime = System.currentTimeMillis();
            System.out.println("Dictionary Loaded Successfully.");
            System.out.println("Elapsed time to load dictionary "+(endTime-startTime)+" milliseconds.");
        } catch (IOException e) {
            System.out.println("Error Reading Data Dictionary From File.");
        }
        System.out.println("Total Number Of Words in Dictionary "+wordDictionary.size());
    }

    public static void loadNegation(){


        String projectDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
        String negationDirectory = projectDirectory+"/resources/negationword.txt";
        long startTime,endTime;
        try {
            startTime = System.currentTimeMillis();
            Files.newBufferedReader(Paths.get(negationDirectory)).lines().forEach(item->{
                negationWord.put(item,0);
            });
            endTime = System.currentTimeMillis();
            System.out.println("Negation Words Loaded Successfully.");
            System.out.println("Elapsed time to load negation words "+(endTime-startTime)+" milliseconds.");
        } catch (IOException e) {
            System.out.println("Error Reading Negation Words From File.");
        }
        System.out.println("Total Number Of Negation Words In The File "+negationWord.size());
    }

    public static void loadStopWords(){


        String projectDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
        String negationDirectory = projectDirectory+"/resources/stopword.txt";
        long startTime,endTime;
        try {
            startTime = System.currentTimeMillis();
            Files.newBufferedReader(Paths.get(negationDirectory)).lines().forEach(item->{
                wordsTobeIgnored.put(item,0);
            });
            endTime = System.currentTimeMillis();
            System.out.println("Stop Words Loaded Successfully.");
            System.out.println("Elapsed time to load stop words "+(endTime-startTime)+" milliseconds.");
        } catch (IOException e) {
            System.out.println("Error Reading Stop Words From File.");
        }
        System.out.println("Total Number Of Stop Words In The File "+wordsTobeIgnored.size());
    }
}
