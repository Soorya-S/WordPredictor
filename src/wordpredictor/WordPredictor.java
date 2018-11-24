package wordpredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Soorya
 */
public class WordPredictor {

    //root path for the files to be read and write
    public static String DIR_PATH = "./src/wordpredictor/data/";
    
    //change this valuse to increase/decrease suggesion count
    public static int MAX_SUGGESTION_RANGE = 5;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WordPredictor wp = new WordPredictor();
        
        //begining of Task-1
        wp.readFiles();
        
        //begining of Task-2
        //read the 'word_frequency.txt' file from the disc
        List<String> wordList = new ArrayList<String>();
        BufferedReader br = null;
        File wordFile = new File(DIR_PATH + "word_frequency.txt");  
        try {
            br = new BufferedReader(new FileReader(wordFile));
            String words;
            while ((words = br.readLine()) != null) 
            {
                wordList.add(words);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(WordPredictor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        System.out.println("Enter input or type 'exit' to close");
        Scanner sc = new Scanner(System.in);
        String input = "";
        
        while(!input.equalsIgnoreCase("exit"))
        {
            input = sc.nextLine();
            int i = 0;
            for(String s : wordList)
            {
                String freq = s.substring(0, s.indexOf('\t'));
                String suggestion = s.substring(freq.length()+1, s.length());
                if(suggestion.startsWith(input))
                {
                    System.out.println(suggestion + "\t(" + freq + ")");
                    i++;
                }
                
                if(i>=MAX_SUGGESTION_RANGE)
                {
                    break;
                }
            }
        };        
        sc.close();
    }
    
    
    
    //read the input files from the data dir.
    private void readFiles()
    {
        BufferedReader br = null;
        try {
            
            /*
             * reading the 'word_corpus.txt' file and store it in an ArrayList<String>
            */
            File wordFile = new File(DIR_PATH+ "word_corpus.txt");  
            br = new BufferedReader(new FileReader(wordFile));
            String words;
            List<String> wordList = new ArrayList<String>();
            while ((words = br.readLine()) != null) 
            {
                wordList.add(words);
            }
            
            
            /*
             * reading the 'character_mapping.txt' and store it in a HashMap<String,Sting>
            */
            File mapFile = new File(DIR_PATH+ "character_mapping.txt");
            br = new BufferedReader(new FileReader(mapFile));
            String mapings;
            Map<String,String> charMap = new HashMap<String, String>();
            while ((mapings = br.readLine()) != null) 
            {
                String key = mapings.substring(0, 1);
                String val = mapings.substring(2, mapings.length());
                charMap.put(val,key);
            }
   
                
            /*
             * sorting is done on readed character maps to avoid conflicts 
            */
            Map<String, String> treeMap = new TreeMap<String, String>(new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    if (s1.length() > s2.length()) {
                        return -1;
                    }
                    else if (s1.length() < s2.length()) {
                        return 1;
                    }
                    else {
                        return s1.compareTo(s2);
                    }
                }
            });
            treeMap.putAll(charMap);

            writeFile(wordList,treeMap);
            
        } catch (IOException ex) {
            Logger.getLogger(WordPredictor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(WordPredictor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    /*
     * This function will get the wordList and charMap and replace the original characters and then the result will be
     * written into file "word_frequency.txt"
    */
    private void writeFile(List<String> wordList, Map<String,String> charMap)
    {
        int size = wordList.size();
        int mapSize = charMap.size();
        StringBuffer output= new StringBuffer();
        for(int i=0;i<size;i++)
        {
            String curWord = wordList.get(i);
            for (Map.Entry<String,String> entry : charMap.entrySet())
            {
                if(curWord.contains(entry.getKey()))
                {
                    curWord = curWord.replace(entry.getKey(), entry.getValue());
                }
            }
            output.append(curWord+"\n");
        }
        
        
        /*
         * write the final results to the file 'word_frequency.txt'
        */
        File file = new File(DIR_PATH + "word_frequency.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(output.toString());
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(WordPredictor.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}