package scorer;

import java.io.*;
import java.util.HashMap;
import scorer.ApplicationProperty;

/**
 *
 * Use the Bing Liu sentiment lexicon for Polarity Detection
 * @author subhabrata mukherjee
 */
public class BingLiu {

    HashMap hm;
    LovinsStemmer ls;

    public BingLiu() {
        hm = new HashMap();
        ls = new LovinsStemmer();
        LoadData();
    }

    /**
     *
     * Loads Bing Liu lexicon in a hashmap
     */
    public void LoadData() {
        try {
            FileInputStream fstream = new FileInputStream(ApplicationProperty.projectpath + "positive-words.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                //System.out.println (strLine);
                if (!hm.containsKey(strLine)) {
                    hm.put(strLine.trim(), "pos");
                }
                //String word=ls.getStem(strLine);
                //if (!hm.containsKey(word))
                //  hm.put(word,"pos");

            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fstream = new FileInputStream(ApplicationProperty.projectpath + "negative-words.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                //System.out.println (strLine);
                if (!hm.containsKey(strLine)) {
                    //System.out.println (strLine);
                    hm.put(strLine, "neg");
                }
                //String word=ls.getStem(strLine);
                //if (!hm.containsKey(word))
                //  hm.put(word,"neg");

            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Use the method for predicting the polarity of a word
     * @param word the given word
     * @return the polarity of the word as <CODE>pos</CODE>, <CODE>neg</CODE> or <CODE>obj</CODE>
     */
    public String getPrediction(String word) {
        String stem = ls.getStem(word);
        String val;
        if (hm.containsKey(word)) {
            val = hm.get(word).toString();
        } else if (hm.containsKey(stem)) {
            val = hm.get(stem).toString();
        } else {
            return "nan";
        }

        if (val.equals("pos")) {
            return "pos";
        } else if (val.equals("neg")) {
            return "neg";
        } else {
            return "obj";
        }

    }

    public static void main(String[] args) {

        BingLiu bl = new BingLiu();
        System.out.println(bl.getPrediction("impress"));
    }
}
