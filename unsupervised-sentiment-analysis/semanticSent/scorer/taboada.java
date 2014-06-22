package scorer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * Use Taboada for Polarity Detection
 * @author subhabrata mukherjee
 */
public class taboada {

    public HashMap<String, String> Taboada;

    public taboada() {
        // TODO Auto-generated constructor stub
        this.Taboada = new HashMap<String, String>();
        populateTaboada(ApplicationProperty.projectpath + "taboada");
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
        float posscore = 0.0f, negscore = 0.0f;
        StringTokenizer st1 = null, st2 = null;
        result = getScoreFromTaboada(word.toUpperCase());
        if (result == null) {
            return "obj";
        } else {
            return result;
        }


    }

    /**
     *
     * Populates Taboada in a hashmap
     * @param sourceFile path of the lexicon
     */
    public void populateTaboada(String sourceFile) {
        try {
            LovinsStemmer ls = new LovinsStemmer();
            float poshits = 0, neghits = 0, tothits = 0;
            float posscore = 0.0f, negscore = 0.0f, objscore = 0.0f;
            String word = "", polarity = "";
            System.out.println("POPULATING TABOADA");
            BufferedReader in = new BufferedReader(new FileReader(sourceFile));
            String line = "";
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                StringTokenizer tk = new StringTokenizer(line, "\t");
                word = tk.nextToken();
                word = ls.getStem(word);   // THIS IS STEMMER HERE

                word = word.toUpperCase();
                neghits = Long.parseLong(tk.nextToken());
                poshits = Long.parseLong(tk.nextToken());
                tothits = Long.parseLong(tk.nextToken());
                //	if(word.toUpperCase().equals("HAPPY"))
                //		System.out.println(neghits+" "+poshits+" "+tothits);
                posscore = poshits / tothits;
                negscore = neghits / tothits;
                objscore = 1 - posscore - negscore;
                if (posscore > negscore) {
                    polarity = "pos";
                } else if (negscore > posscore) {
                    polarity = "neg";
                } else {
                    polarity = "obj";
                }
                this.Taboada.put(word.toUpperCase(), polarity);
            }



        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public String getScoreFromTaboada(String token) {
        if (this.Taboada.containsKey(token.toUpperCase())) {
            return this.Taboada.get(token.toUpperCase());
        } else {
            return null;
        }
    }

    public HashMap<String, String> getTaboadaHashMap() {
        return this.Taboada;
    }
}
