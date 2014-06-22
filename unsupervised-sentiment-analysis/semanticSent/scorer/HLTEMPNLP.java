package scorer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * Use the Subjectivity Lexicon for Polarity Detection
 * @author subhabrata mukherjee
 */
public class HLTEMPNLP {

    public HashMap<String, String> HLT;

    public HLTEMPNLP() {
        // TODO Auto-generated constructor stub
        this.HLT = new HashMap<String, String>();
        populateHLT(ApplicationProperty.projectpath + "subjclueslen1-HLTEMNLP05.tff");
    }

    /**
     *
     * Use the method for predicting the polarity of a word
     * @param word the given word
     * @return the polarity of the word as <CODE>pos</CODE>, <CODE>neg</CODE>, <CODE>obj</CODE> or <CODE>objcd</CODE>
     */
    public String getPrediction(String word) {
        String result = "", tag = "", polarity = "";
        int poscount = 0, negcount = 0, objcount = 0;
        //float posscore=0.0f,negscore=0.0f;
        String strength = "";
        StringTokenizer st1 = null, st2 = null;
        result = getScoreFromHLT(word.toUpperCase());
        if (result == null) {
            return "nan";
        }
        st1 = new StringTokenizer(result, "#");
        while (st1.hasMoreTokens()) {
            tag = st1.nextToken();
            st2 = new StringTokenizer(tag, "$");
            while (st2.hasMoreTokens()) {
                //	posscore=Integer.parseInt(st2.nextToken());
                //	posscore=Float.parseFloat(st2.nextToken());
                //	negscore=Float.parseFloat(st2.nextToken());
                strength = st2.nextToken();
                polarity = st2.nextToken();
                if (polarity.equals("positive")) {
                    poscount++;
                } else if (polarity.equals("negative")) {
                    negcount++;
                } else {
                    objcount++;
                }

            }


        }

        if (poscount > negcount && poscount > objcount) {
            return "pos";
        } else if (negcount > poscount && negcount > objcount) {
            return "neg";
        } else if (objcount > poscount && objcount > negcount) {
            return "obj";
        } else {
            return "objcd";
        }


    }

    /**
     *
     * Populates subjectivity lexicon in a hashmap
     * @param sourceFile path of the lexicon
     */
    public void populateHLT(String sourceFile) {
        try {
            LovinsStemmer ls = new LovinsStemmer();
            System.out.println("POPULATING HLTEMPNLP");
            StringTokenizer st1 = null, st2 = null;
            BufferedReader in = new BufferedReader(new FileReader(sourceFile));
            String line = "", word = "", polarity = "", polarityval = "";
            while ((line = in.readLine()) != null) {
                //		System.out.println("here");
                st1 = new StringTokenizer(line, " ");
                polarity = st1.nextToken();
                polarity = polarity.substring(polarity.indexOf("=") + 1);
                word = st1.nextToken();
                word = st1.nextToken();

                word = word.substring(word.indexOf("=") + 1);
                word = ls.getStem(word);   		// THIS IS THE STEMMER HERE
                polarityval = st1.nextToken();
                polarityval = st1.nextToken();
                polarityval = st1.nextToken();
                polarityval = polarityval.substring(polarityval.indexOf("=") + 1);
                polarity = polarity + "$" + polarityval;
                //		System.out.println(word+" "+polarity);
                if (this.HLT.containsKey(word)) {
                    this.HLT.put(word.toUpperCase(), this.HLT.remove(word.toUpperCase()) + "#" + polarity);
                } else {
                    this.HLT.put(word.toUpperCase(), polarity);
                }

            }
            //	count++;
            //	}
            //}
            //	in.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public String getScoreFromHLT(String token) {
        if (this.HLT.containsKey(token)) {
            return this.HLT.get(token);
        } else {
            return null;
        }
    }

    public HashMap<String, String> getHLTHashMap() {
        return this.HLT;
    }
}
