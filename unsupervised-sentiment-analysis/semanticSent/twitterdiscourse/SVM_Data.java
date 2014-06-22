package twitterdiscourse;

import scorer.BingLiu;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import scorer.LovinsStemmer;

/**
 *
 * Supervised Classification System of the Weighted Discourse Sentences
 * @author subhabrata mukherjee
 */
public class SVM_Data {

    static BingLiu bl = new BingLiu();

    /**
     *
     * Process a string to remove all unwanted symbols
     */
    public static String process(String sen) {

        sen = sen.replaceAll("\\.", "");
        String sen1 = sen.replaceAll("[,$:!();\"~`^*?/]", "");
        sen1 = sen1.replaceAll("['-]", "");
        //sen1=sen1.replaceAll("\\.","");
        sen1 = sen1.replaceAll("( )+", " ");
        sen1 = sen1.replaceAll("@user", "");
        sen1 = sen1.replaceAll("@link", "");
        return sen1;
    }

    /**
     *
     * Generate Feature Vectors to be used by Support Vector Machines for classification
     * @param filename path of the file containing sentences ; each line of the file contains a sentence and the corresponding polarity <CODE>positive, negative or objective</CODE>
     * separated by <CODE>$$$</CODE>;
     * The SVM uses a bag-of-words model with unigrams as features, stemming and ignoring stop words ; positive emoticons are replace with <CODE>happy</CODE> and negative emoticons are replaced with <CODE>sad</CODE> ;
     *The output file is <CODE>output.txt</CODE> ; Each line of the file contains the feature vector in the following format: <CODE> class_label feature_number1:feature_frequency1 feature_number2:feature_frequency2 ... </CODE> ;
     * The format is the same as that of LIBSVM and SVMLIGHT;
     * The positive samples are labeled <CODE>1</CODE> and the negative sample are labeled <CODE>2</CODE>;
     *
     */
    public static void createFeatureVector(String filename, StopWordRemover stop, LovinsStemmer ls) {

        DiscourseRules pl = new DiscourseRules();
        SmileyHandler sh = new SmileyHandler();
        Vector v = new Vector();
        try {
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            FileWriter fw = new FileWriter(filename + "out.txt");
            BufferedWriter out = new BufferedWriter(fw);
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String tok[] = strLine.split("[$$$]+");
                if (tok[1].contains("obj")) {
                    continue;
                }
                strLine = tok[0];
                if (sh.getSmileyPrediction(strLine).contains("pos")) {
                    strLine += " happy";
                }
                if (sh.getSmileyPrediction(strLine).contains("neg")) {
                    strLine += " sad";
                }
                String sen = process(strLine);
                String ret = pl.discourse(sen);

                out.write(ret);
                out.write("$$$");
                out.write(tok[1]);
                out.write("\n");
                String word[] = ret.toLowerCase().split(" ");
                for (int i = 0; i < word.length; i++) {
                    String st = stop.removeIfStopWord(word[i]);
                    if (st.length() < 2) {
                        continue;
                    }
                    String w = ls.getStem(word[i]);
                    if (!v.contains(w)) {
                        v.add(w);
                    }
                }
            }
            out.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fstream = new FileInputStream(filename + "out.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String newfile = filename + "result.txt";
            FileWriter fw = new FileWriter(newfile);
            BufferedWriter out = new BufferedWriter(fw);
            String strLine;
            while ((strLine = br.readLine()) != null) {
                HashMap hm = new HashMap();
                String delims = "[$$$]+";
                String tok[] = strLine.split(delims);
                String word[] = tok[0].toLowerCase().split(" ");
                for (int i = 0; i < word.length; i++) {
                    String st = stop.removeIfStopWord(word[i]);
                    if (st.length() < 2) {
                        continue;
                    }
                    String w = ls.getStem(word[i]);
                    if (v.contains(w)) {
                        if (!hm.containsKey(w)) {
                            hm.put(w, 1);
                        } else {
                            int c = Integer.parseInt(hm.get(w).toString());
                            c++;
                            hm.put(w, c);
                        }
                    }
                }
                int label;
                if (tok[1].contains("pos")) {
                    label = 1;
                } else if (tok[1].contains("neg")) {
                    label = 2;
                } else {
                    label = 2;
                }
                String output = label + "";
                for (int i = 0; i < v.size(); i++) {
                    String w = v.get(i).toString();
                    if (hm.containsKey(w)) {
                        int c = Integer.parseInt(hm.get(w).toString());
                        int d = i + 1;
                        output += " " + d + ":" + c;
                    }
                }
                System.out.println("output=" + output);
                out.write(output);
                out.write("\n");
            }
            out.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void prepare(String filename) {

        LovinsStemmer ls = new LovinsStemmer();
        StopWordRemover stop = new StopWordRemover();
        createFeatureVector(filename, stop, ls);
    }
    
    public static void main(String[] args) {
		prepare("tdata/out.txt");
	}
}
