package scorer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * Use the SentiWordNet for Polarity Detection
 * @author subhabrata mukherjee
 */
public class SWNinterface {

    public HashMap<String, String> SWN;

    public SWNinterface() {
        // TODO Auto-generated constructor stub
        this.SWN = new HashMap<String, String>();
        populateSWN(ApplicationProperty.projectpath + "Sentiwordnet.txt");
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
        result = getScoreFromSWN(word.toUpperCase());
        if (result == null) {
            return "nan";
        }
        st1 = new StringTokenizer(result, "#");

        while (st1.hasMoreTokens()) {
            tag = st1.nextToken();
            st2 = new StringTokenizer(tag, "$");
            while (st2.hasMoreTokens()) {
                posscore = Integer.parseInt(st2.nextToken());
                posscore = Float.parseFloat(st2.nextToken());
                negscore = Float.parseFloat(st2.nextToken());

                if (posscore > negscore) {
                    poscount++;
                } else if (posscore < negscore) {
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
            return "obj";
        }


    }

    /**
     *
     * Populates SWN in a hashmap
     * @param sourceFile path of the lexicon
     */
    public void populateSWN(String sourceFile) {
        try {
            LovinsStemmer ls = new LovinsStemmer();
            System.out.println("POPULATING SWN");
            BufferedReader in = new BufferedReader(new FileReader(sourceFile));
            String line = "";
            StringTokenizer st = null;
            String synsetId = "";
            String posScr = "";
            String negScr = "";
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                StringTokenizer tk = new StringTokenizer(line);


                int count = 1;

                while (tk.hasMoreTokens()) {
                    String data = tk.nextToken();
                    //System.out.println(data + "     "+ count);
                    if (count == 2) {
                        synsetId = data;
                    }
                    if (count == 3) {
                        posScr = data;
                    }
                    if (count == 4) {
                        negScr = data;
                    }
                    if (count >= 5) {
                        st = new StringTokenizer(data);
                        while (st.hasMoreTokens()) {
                            String[] termData = st.nextToken().split("#");
                            //System.out.println(termData[0]);
                            if (this.SWN.containsKey(ls.getStem(termData[0]))) {
                                this.SWN.put(ls.getStem(termData[0]).toUpperCase(), this.SWN.remove(ls.getStem(termData[0]).toUpperCase())
                                        + "#" + synsetId + "$" + posScr + "$" + negScr);
                            } else {
                                this.SWN.put(ls.getStem(termData[0]).toUpperCase(), synsetId + "$" + posScr + "$" + negScr);
                            }
                        }

                    }
                    count++;
                }
            }
            in.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public String getScoreFromSWN(String token) {
        if (this.SWN.containsKey(token)) {
            return this.SWN.get(token);
        } else {
            return null;
        }
    }

    public HashMap<String, String> getSWNHashMap() {
        return this.SWN;
    }
}
