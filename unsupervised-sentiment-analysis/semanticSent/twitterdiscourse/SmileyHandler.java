package twitterdiscourse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.iitb.sa.semantics.unl.Preprocessor;

import scorer.ApplicationProperty;

/**
 *
 * Returns the polarity of an emoticon
 * @author subhabrata mukherjee
 */

public class SmileyHandler {

    public HashMap smileys;

    public SmileyHandler() {
        // TODO Auto-generated constructor stub
        this.smileys = new HashMap();
        populateSmileys("resources/word_lists/smileys.txt");
    }

/**
 *
 *Returns the polarity of the emoticon (if any) present in the tweet
 * @param tweet the given tweet
 * @return the polarity of any emoticon present in the tweet
 */


    public String getSmileyPrediction(String tweet) {
        String tweetHere = null;
        tweetHere = tweet;
        Collection c = smileys.keySet();
        String key = "";
        Iterator it = c.iterator();
        if (tweetHere != null) {
            tweetHere = tweetHere.replaceAll(" ", "");
            tweetHere = tweetHere.toUpperCase();

            while (it.hasNext()) {
                key = (String) it.next();
                if (tweetHere.contains(key)) {
                    return (String) smileys.get(key);
                }

            }



        }


        return "none";
    }
/**
 *
 * Populate the emoticons in a hashmap
 * @param sourceFile file containing emoticons and corresponding polarity separated by <CODE>tab</CODE>
 */

    public void populateSmileys(String sourceFile) {
        try {

            System.out.println("POPULATING SMILEY HANDLER");
            BufferedReader in = new BufferedReader(new FileReader(Preprocessor.class.getClassLoader().getResource(sourceFile).getPath()));
            String smiley = "", tag = "";
            StringTokenizer st = null;
            String line = null;
            while ((line = in.readLine()) != null) {
                st = new StringTokenizer(line, "\t");
                smiley = st.nextToken();
                smiley = smiley.toUpperCase();
                tag = st.nextToken();

                smileys.put(smiley, tag);

                //this.stopwords+=lne+" ";


            }
            in.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}
