package twitterdiscourse;

import java.io.*;

import com.iitb.sa.semantics.unl.SentimentAnalyser;

import scorer.ApplicationProperty;

/**
 *
 * Find accuracy of the system
 * @author subhabrata mukherjee
 */
public class FindAccuracy {

    /**
     *
     * Find accuracy of the system on gold standard set of tweets
     * @param path Path of the file containing tagged tweets ; 
     * the tweet and corresponding polarities <CODE>positive, negative or objective </CODE> are separated by <CODE>$$$</CODE>
     * @return accuracy figure (double)
     */
    public static double Accuracy(String path) {
        int PosT = 0, NegT = 0, ObjT = 0, Pos = 0, Neg = 0, Obj = 0;
        double acc = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String read;
            while ((read = br.readLine()) != null) {
                String tok[] = read.split("[$$$]+");
                String pol = tok[1];
                try {
                	if(tok[0].contains("\\((.*?)\\)")){
                		continue;
                	}
                	int sentiment = Integer.parseInt(SentimentAnalyser.analyseSentenceSemanticallyEnhanced(tok[0],pol,false).split("::::")[1]);
                	if(sentiment>0 && pol.contains("pos"))Pos++;
                	else if(sentiment<0 && pol.contains("neg"))Neg++;
                	else if(sentiment==0 && pol.contains("obj"))Obj++;
                    if (pol.contains("pos")) {
                        PosT++;
                    } else if (pol.contains("neg")) {
                        NegT++;
                    } else {
                        ObjT++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int total = PosT + NegT + ObjT;
                int correct = Pos + Neg + Obj;
                acc = (double) correct / (double) total;

            }
            System.out.println("PosTotal = " + PosT + " NegTotal = " + NegT + " ObjTotal = " + ObjT);
            System.out.println("PosCorrect = " + Pos + " NegCorrect = " + Neg + " ObjCorrect = " + Obj);
            br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

    public static void main(String[] args) {
        String str = ApplicationProperty.projectpath + "/output.txt";
        try {
            System.out.println("Final Accuracy = " + Accuracy(str));
            SentimentAnalyser.writeToFile();
        } catch (Exception e) {
        }
    }
}
