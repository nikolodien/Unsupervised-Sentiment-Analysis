package twitterdiscourse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import scorer.ApplicationProperty;

/**
 *
 * Removes the stop word passed as a token
 * @author subhabrata mukherjee
 */
public class StopWordRemover {

    public String stopwords;

    public StopWordRemover() {
        this.stopwords = new String();
        populateStopWords(ApplicationProperty.projectpath + "stopwords");
    }

    /**
     *@param word the given token
     *@return if the word is a stopword, an empty string will be returned; or else the word itself will be returned
     */
    public String removeIfStopWord(String word) {
        if (this.stopwords.contains(word + " ")) {
            return "";
        } else {
            return word;
        }


    }

    /**
     *
     * Populate the stop words
     * @param sourceFile file containing stop words
     */
    public void populateStopWords(String sourceFile) {
        try {

            System.out.println("POPULATING STOP WORD REMOVER");
            BufferedReader in = new BufferedReader(new FileReader(sourceFile));
            String line = "";
            while ((line = in.readLine()) != null) {
                this.stopwords += line + " ";


            }
            in.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}
