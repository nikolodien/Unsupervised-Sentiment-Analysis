/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterdiscourse;

import scorer.BingLiu;
import java.io.*;
import java.util.*;

/**
 *
 * Incorporate discourse rules in a bag-of-words model ; Main Contribution of this project.
 * @author subhabrata mukherjee
 */
public class DiscourseRules {

    BingLiu bl = new BingLiu();
    int printLine = 0;

    /**
     *
     * Extract number from an alphanumeric string
     */
    public static String getOnlyNumerics(String str) {

        if (str == null) {
            return null;
        }

        StringBuffer strBuff = new StringBuffer();
        char c;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (Character.isDigit(c)) {
                strBuff.append(c);
            }
        }
        return strBuff.toString();
    }

    /**
     *
     * Ignore strong modals and conditionals
     */
    public static boolean ignoreAll(String str) {

        Vector modal = new Vector();
        modal.add(" if ");
        modal.add(" might ");
        modal.add(" could ");
        modal.add(" can ");
        modal.add(" would ");
        modal.add(" may ");
        str = str.toLowerCase();
        String tok[] = str.split("[ , ; . ! : \" ' ]");
        for (int i = 0; i < modal.size(); i++) {
            if (str.contains(modal.get(i).toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Give more weightage to words before certain discourse elements
     */
    public static boolean weightBefore(String str) {

        Vector v = new Vector();
        str = str.toLowerCase();
        v.add(" till ");
        v.add(" until ");
        v.add(" unless ");
        v.add(" despite ");
        v.add(" inspite ");
        v.add("though");
        v.add("although");
        v.add("spite");
        for (int i = 0; i < v.size(); i++) {
            if (str.contains(v.get(i).toString()) || str.contains(v.get(i).toString().trim())) {
                //System.out.println(str+" detected");
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Find if the string is a negation operator
     */
    public static boolean findNegation(String str) {

        str = str.toLowerCase();
        if (str.equals("not") || str.equals("neither") || str.equals("nor") || str.equals("no") || str.equals("never")) {
            return true;
        } else if (str.contains("not_") || str.contains("neither_") || str.contains("nor_") || str.contains("no_") || str.contains("never_")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * Find if the string is a connective
     */
    public static boolean isConnective(String str) {
        Vector conn = new Vector();
        conn.add("but");
        conn.add("still");
        conn.add("however");
        conn.add("yet");
        conn.add("otherwise");
        conn.add("nevertheless");
        conn.add("nonetheless");
        conn.add("nevertheless");
        conn.add("till");
        conn.add("until");
        conn.add("unless");
        conn.add("despite");
        conn.add("inspite");
        conn.add("though");
        conn.add("although");

        str = str.toLowerCase();
        for (int i = 0; i < conn.size(); i++) {
            String s = conn.get(i).toString() + "_";
            if (str.equals(conn.get(i).toString()) || str.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * Given a string return the discourse weighted string
     * @param strLine the given sentence
     * @return the discourse weighted sentence with words repeated once or twice times according to their position before or after the discourse markers
     */
    public String discourse(String strLine) {
        Vector concl = new Vector();
        concl.add("but");
        concl.add("still");
        concl.add("however");
        concl.add("yet");
        concl.add("however");
        concl.add("result");
        concl.add("therefore");
        concl.add("thus");
        concl.add("moreover");
        concl.add("nevertheless");
        concl.add("nonetheless");
        concl.add("otherwise");
        concl.add("consequently");
        concl.add("furthermore");
        concl.add("so");
        concl.add("so_3147781");
        concl.add("subsequently");
        concl.add("eventually");
        concl.add("hence");


        int wtConcl = 0;
        int ignoreAfter = 0;
        int wtBefore = 0;
        int isNegation = 0;
        int countNeg = 0;
        int window = 5;

        String strRet = "";

        wtConcl = 0;
        ignoreAfter = 0;
        wtBefore = 0;
        isNegation = 0;
        countNeg = 0;


        //give more weightage to words before
        if (weightBefore(strLine)) {
            wtBefore = 1;
        }

        String bigtok[] = strLine.split("[ , ) . ( ; :   -  @ # $ % ^ & * | ~  /]");

        for (int j = 0; j < bigtok.length; j++) {

            //stop negation
            //isNegation=0;

            bigtok[j] = bigtok[j].trim().toLowerCase();
            //stop negation if another connective is found
            if (isNegation == 1 && isConnective(bigtok[j])) {
                isNegation = 0;
            }
            //count window for negation
            if (isNegation == 1) {
                countNeg++;
            }
            if (countNeg > window) {
                isNegation = 0;
                countNeg = 0;
            }

            //give more wightage to words occurring after

            for (int k = 0; k < concl.size(); k++) {
                String s = concl.get(k).toString() + "_";
                if (concl.get(k).toString().equals(bigtok[j].toLowerCase()) || bigtok[j].contains(s)) {
                    wtConcl = 1;
                    break;
                }
            }
            //ignore words after
//                            if (bigtok[j].contains("unless") || bigtok[j].contains("until"))
//                                ignoreAfter=1;

            //give more weightage to words before
            if (weightBefore(bigtok[j])) {
                wtBefore = 0;
            }

            //find if there is negation
            if (findNegation(bigtok[j])) {
                //System.out.println("Isnegation ="+isNegation);
                //System.out.println("Negation detected with "+bigtok[j]);
                isNegation = 1;
                countNeg = 0;
            }



            if (bigtok[j].length() > 0 && ignoreAfter != 1) {
                if (countNeg <= window && isNegation == 1) {
                    if (printLine == 1) {
                        System.out.print("Neg ");
                    }
                    strRet += "#";
                }
                if (printLine == 1) {
                    System.out.println(bigtok[j]);
                }
                strRet += bigtok[j] + " ";
                if (wtConcl == 1) {
                    if (countNeg <= window && isNegation == 1) {
                        if (printLine == 1) {
                            System.out.print("Neg ");
                        }
                        strRet += "#";
                    }
                    if (printLine == 1) {
                        System.out.println(bigtok[j]);
                    }
                    strRet += bigtok[j] + " ";
                }
                if (wtBefore == 1) {
                    if (countNeg <= window && isNegation == 1) {
                        if (printLine == 1) {
                            System.out.print("Neg ");
                        }
                        strRet += "#";
                    }
                    if (printLine == 1) {
                        System.out.println(bigtok[j]);
                    }
                    strRet += bigtok[j] + " ";
                }
            }


            if (bigtok[j].contains(".") || bigtok[j].contains("!") || bigtok[j].contains("?")) {
                if (printLine == 1) {
                    System.out.println("$");
                }
                wtConcl = 0;
                ignoreAfter = 0;
                wtBefore = 0;
                isNegation = 0;
                countNeg = 0;
            }
        }
        return strRet;
    }

    public static void main(String[] args) throws Exception {

        DiscourseRules pl = new DiscourseRules();
        String str = "I love you but do not want to stay with you.";

        String opinion = pl.discourse(str);
        System.out.println(opinion);
    }
}
