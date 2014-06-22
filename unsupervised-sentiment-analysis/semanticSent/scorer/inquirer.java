package scorer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * Use Inquirer for Polarity Detection
 * @author subhabrata mukherjee
 */
public class inquirer {

    public HashMap<String, String> Inqr;

    public inquirer() {
        // TODO Auto-generated constructor stub
        this.Inqr = new HashMap<String, String>();
        populateInqr(ApplicationProperty.projectpath + "inquirer");
    }

    /**
     *
     * Use the method for predicting the polarity of a word
     * @param word the given word
     * @return the polarity of the word as <CODE>pos</CODE>, <CODE>neg</CODE> or <CODE>obj</CODE>
     */
    public String getPrediction(String word) {
        String result = "", tag = "", polarity = "";
        int poscount = 0, negcount = 0, objcount = 0;
        //float posscore=0.0f,negscore=0.0f;
        String strength = "";
        StringTokenizer st1 = null, st2 = null;
        result = getScoreFromInqr(word.toUpperCase());

        if (result == null) {
            return "nan";
        }
        st1 = new StringTokenizer(result, "#");
        while (st1.hasMoreTokens()) {
            tag = st1.nextToken();

            if (tag.equals("pos")) {
                poscount++;
            } else if (tag.equals("neg")) {
                negcount++;
            } else if (tag.equals("obj")) {
                objcount++;
            }


        }

        if (poscount > negcount && poscount > objcount) {
            return "pos";
        } else if (negcount > poscount && negcount > objcount) {
            return "neg";
        } else if (objcount > poscount && objcount > negcount) {
            return "obj";
        } else {
            return "obj";
        }


    }

    /**
     *
     * Populates inquirer in a hashmap
     * @param sourceFile path of the lexicon
     */
    public void populateInqr(String sourceFile) {
        String line = "";
        try {
            LovinsStemmer ls = new LovinsStemmer();
            System.out.println("POPULATING INQUIRER");
            StringTokenizer st1 = null, st2 = null;
            BufferedReader in = new BufferedReader(new FileReader(sourceFile));
            String word = "", polarity = "", polarityval = "";
            while ((line = in.readLine()) != null && line.contains("\t")) {
                word = line.substring(0, line.indexOf("\t"));
                if (word.contains("#")) {
                    word = word.substring(0, word.indexOf("#"));
                }

                word = ls.getStem(word);  // ADDING THE STEM TO THE RESOURCE
                word = word.toUpperCase();
                if (line.contains("Positiv")) {
                    polarity = "pos";
                } else if (line.contains("Negativ")) {
                    polarity = "neg";
                } else {
                    polarity = "obj";
                }

                if (this.Inqr.containsKey(word)) {
                    this.Inqr.put(word, this.Inqr.remove(word) + "#" + polarity);
                } else {
                    this.Inqr.put(word, polarity);
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(line + " " + e);
            //e.printStackTrace();
        }

    }

    public String getScoreFromInqr(String token) {
        if (this.Inqr.containsKey(token)) {
            return this.Inqr.get(token);
        } else {
            return null;
        }
    }

    public HashMap<String, String> getInqrHashMap() {
        return this.Inqr;
    }
}
