package twitterdiscourse;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Properties;


import scorer.HLTEMPNLP;
import scorer.SWNinterface;
import scorer.inquirer;
import scorer.taboada;

import java.io.*;
import scorer.LovinsStemmer;

/**
 *
 * Use C-Feel-It for Polarity Detection of Tweets
 * @author subhabrata mukherjee
 */

public class CFeelIt {

    public SWNinterface resSi;
    public taboada resTb;
    public inquirer resInq;
    public HLTEMPNLP resHlt;
    public StopWordRemover swr;
    public SmileyHandler sh;
    public static Vector v;
    public Vector adj;
    public Vector adj_indx;
    int printLine = 0;
    //public Vector adj_indx;

    public CFeelIt() {
        resHlt = new HLTEMPNLP();
        resSi = new SWNinterface();
        resTb = new taboada();
        resInq = new inquirer();
        swr = new StopWordRemover();
        sh = new SmileyHandler();
        printLine = 0;
        v = new Vector();
        adj = new Vector();
        adj_indx = new Vector();
        v.add("no");
        v.add("not");
        v.add("never");
        v.add("neither");
        v.add("nor");

    }

    /**
     *
     * Find if the word is a negation operator
     * @param word THe given word
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public static boolean isNegation(String word) {
        //    v=new Vector();

        if (v.contains(word.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * Reverse the polarity if it is negated
     * @param polarity
     * @return negated polarity
     */
    public static String reverseIfNegation(String polarity, boolean isNegation) {
        String result = "", next = "";
        StringTokenizer st = new StringTokenizer(polarity, " ");
        //System.out.println ("st = "+polarity);
        if (!isNegation) {
            return polarity;
        } else {
            //System.out.println("Negation located with string as "+polarity);
            while (st.hasMoreTokens()) {
                next = st.nextToken();
                if (next.startsWith("pos")) {
                    result += "neg";
                } else if (next.startsWith("neg")) {
                    result += "pos";
                } else {
                    result += next;
                }

            }
            //System.out.println("returning "+result);
            return result;
        }

    }

    /**
     *
     * Calculate the sentiment of a tweet
     * @param cleanTweet the given tweet
     * @return the prediction by four sentiment lexicons separated by space, polarity elements as  <CODE>positive</CODE>, <CODE>negative</CODE> and <CODE>objective</CODE>
     */
    public String calculateSentimentOfATweet(String cleanTweet) {
        //System.out.println("HERE");

        String word = null, polarity = null;
        StringTokenizer st1 = null;
        int penaltyCountForNegation = 0;
        boolean isNegation = false;
        //int poscount[]=,negcount[]=null,objcount[]=null,ambcount[]=null;
        int[] poscount = new int[4];
        int[] negcount = new int[4];
        int[] objcount = new int[4];
        int[] ambcount = new int[4];
        //Override emoticons

        polarity = sh.getSmileyPrediction(cleanTweet);
        //System.out.println("Smiley handler returned :"+polarity);
        if (polarity != null && !polarity.equals("none")) {
            return polarity + " " + polarity + " " + polarity + " " + polarity;
        }

        //System.out.println ("cleanTweet="+cleanTweet);
        //String str=ss.Sentence(cleanTweet);
        //System.out.println ("str="+str);

        LovinsStemmer ls = new LovinsStemmer();

        //st1=new StringTokenizer(str," ");
        st1 = new StringTokenizer(cleanTweet, " ");

        while (st1.hasMoreTokens()) {
            word = st1.nextToken();
            if (isNegation(word)) {
                isNegation = true;
                penaltyCountForNegation = 0;
            } else if (isNegation) {

                penaltyCountForNegation++;
                if (penaltyCountForNegation == 4) {
                    isNegation = false;
                    penaltyCountForNegation = 0;

                }
            }

            word = swr.removeIfStopWord(word);
            word = ls.getStem(word);

            if (!word.equals("")) {
                polarity = resSi.getPrediction(word);
                if (printLine == 1) {
                    System.out.println("b4   sw " + word + " " + polarity);
                }
                polarity = reverseIfNegation(polarity, isNegation);
                if (printLine == 1) {
                    System.out.println("aftr sw " + word + " " + polarity);
                }
                if (polarity.equals("pos")) {
                    poscount[0]++;
                } else if (polarity.equals("neg")) {
                    negcount[0]++;
                } else if (polarity.equals("obj")) {
                    objcount[0]++;
                } else {
                    ambcount[0]++;
                }

                polarity = resHlt.getPrediction(word);
                polarity = reverseIfNegation(polarity, isNegation);
                //System.out.println("Hlt "+word+" "+polarity);
                if (polarity.equals("pos")) {
                    poscount[1]++;
                } else if (polarity.equals("neg")) {
                    negcount[1]++;
                } else if (polarity.equals("obj")) {
                    objcount[1]++;
                } else {
                    ambcount[1]++;
                }

                polarity = resInq.getPrediction(word);
                polarity = reverseIfNegation(polarity, isNegation);
                //System.out.println("Inq "+word+" "+polarity);
                if (polarity.equals("pos")) {
                    poscount[2]++;
                } else if (polarity.equals("neg")) {
                    negcount[2]++;
                } else if (polarity.equals("obj")) {
                    objcount[2]++;
                } else {
                    ambcount[2]++;
                }

                polarity = resTb.getPrediction(word);
                polarity = reverseIfNegation(polarity, isNegation);
                //System.out.println("Tb "+word+" "+polarity);
                if (polarity.equals("pos")) {
                    poscount[3]++;
                } else if (polarity.equals("neg")) {
                    negcount[3]++;
                } else if (polarity.equals("obj")) {
                    objcount[3]++;
                } else {
                    ambcount[3]++;
                }
            }
        }
        String output = "";
        //System.out.println("posword="+poscount.length+" & negword="+negcount.length+" & objword="+objcount.length+" & ambword="+ambcount.length);
        for (int i = 0; i <= 3; i++) {
            ambcount[i] = 0;
            objcount[i] = 0;
            //System.out.println(poscount[i]+" "+negcount[i]);
            if (poscount[i] != 0 && poscount[i] > negcount[i]) {
                output += "positive ";
            } else if (negcount[i] != 0 && negcount[i] > poscount[i]) {
                output += "negative ";
            } else {
                output += "objective ";
            }


        }
        // System.out.println("here out="+output);
        return output;
    }

    /**
     *
     * Combine the prediction of four sentiment lexicons and give a single polarity
     * @param results Prediction of four sentiment lexicons
     * @return a combined polarity
     */
    public String giveOnePrediction(String results) {
        String line = "";
        String tag = "", tagSeq = "";
        String res1 = "", res2 = "", res3 = "", res4 = "";
        float posScore = 0.0f, negScore = 0.0f, objScore = 0.0f;
        float pos[] = new float[4];
        float neg[] = new float[4];
        float obj[] = new float[4];
        float posweight[] = {0.71f, 0.739f, 0.786f, 0.996f};
        float negweight[] = {0.21f, 0.49f, 0.239f, 0.013f};
        StringTokenizer st2 = null;
        float total = 0.0f;
        StringTokenizer st = null;
        //   System.out.println("in give one prediction");
        String tok[] = results.split(" ");
        for (int i = 0; i < 4; i++) {
            res1 = tok[i];
            if (res1.equals("positive")) {
                pos[i]++;
            } else if (res1.equals("negative")) {
                neg[i]++;
            } else {
                obj[i]++;
            }
        }

        for (int i = 0; i < 4; i++) {
            posScore = pos[i] * posweight[i];
            negScore = neg[i] * negweight[i];
            objScore = obj[i] * 1.0f;
        }

        float totalTotal = 0.0f;//for normalizing
        totalTotal = posScore + negScore + objScore;
        posScore = posScore / totalTotal;
        negScore = negScore / totalTotal;
        objScore = objScore / totalTotal;
        System.out.println("Returning for Subho " + posScore + " " + negScore + " " + objScore);
        //return (posScore + " " + negScore + " " + objScore);

        if (negScore > posScore) {
            return "neg";
        } else {
            return "pos";
        }
    }

    public static void main(String[] args) {


        CFeelIt scv3 = new CFeelIt();
        String result = scv3.calculateSentimentOfATweet("This is completely wrong. I don't know what is going on");
        System.out.println(scv3.giveOnePrediction(result));
    }
}
