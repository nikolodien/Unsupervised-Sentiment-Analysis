package twitterdiscourse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import scorer.BingLiu;

/**
 *
 * Find polarity of a string based on Bing Liu lexicon
 * @author subhabrata mukherjee
 */

public class FindOpinion {

    BingLiu bl = new BingLiu();
    SmileyHandler sh = new SmileyHandler();

    public String findOpinionDis(String str) {

        String extract;
        str = str.replaceAll("[,.;:-@#$%^&*|~/]", "");
        extract = str;
        String tok[] = extract.split(" ");
        int posWord = 0, negWord = 0;
        for (int i = 0; i < tok.length; i++) {
            String op = bl.getPrediction(tok[i]);
            if (op.contains("pos")) {
                if (tok[i].contains("#")) {
                    negWord++;
                } else {
                    posWord++;
                }
            } else if (op.contains("neg")) {
                if (tok[i].contains("#")) {
                    posWord++;
                } else {
                    negWord++;
                }
            }
        }
        if (posWord >= negWord && posWord != 0) {
            return "pos";
        } else if (negWord > posWord && negWord != 0) {
            return "neg";
        } else {
            return "obj";
        }
    }

    String findOpinionBase(String str) {

        String extract;
        str = str.toLowerCase().replaceAll("[,.;:-@#$%^&*|~/]", "");
        extract = str;
        String tok[] = extract.split(" ");
        int posWord = 0, negWord = 0;
        Boolean flag = false;
        int window = 0;
        for (int i = 0; i < tok.length; i++) {
            if (tok[i].equals("no") || tok[i].equals("not") || tok[i].equals("neither") || tok[i].equals("nor")) {
                flag = true;
            }
            if (flag == true) {
                window++;
            }
            if (window >= 5) {
                window = 0;
                flag = false;
            }

            String op = bl.getPrediction(tok[i]);
            System.out.println(op + " " + tok[i]);
            if (op.contains("pos")) {
                if (flag == true) {
                    negWord++;
                } else {
                    posWord++;
                }
            } else if (op.contains("neg")) {
                if (flag == true) {
                    posWord++;
                } else {
                    negWord++;
                }
            }
        }
        if (negWord > posWord) {
            return "neg";
        } else if (posWord >= negWord && posWord != 0) {
            return "pos";
        } else {
            return "obj";
        }
    }

    public static void main(String[] args) throws IOException {
        FindOpinion fp = new FindOpinion();
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        System.out.print(">>");
        while((line=bf.readLine())!=null){
        	System.out.println("Sentiment:"+fp.findOpinionDis(line));
        	System.out.print(">>");
        }
    }
}
