package com.iitb.sa.semantics.unl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opennlp.tools.util.HashList;

/**
 * @author Nikhilkumar Jadhav
 * 
 *         This class will be used before invoking the UNL generator. It aims to
 *         remove, hashtags to help generate proper url
 * 
 */
public class Preprocessor {

	private static Map<String, String> abbreviations = new HashMap<String, String>();
	private static Map<String, String> smileys = new HashMap<String, String>();

	static {
		String location = "resources/word_lists/Dictionary.txt";
		String slocation = "resources/word_lists/smileys.txt";
		BufferedReader reader = null;
		BufferedReader sreader = null;
		try {
			reader = new BufferedReader(new FileReader(Preprocessor.class.getClassLoader().getResource(location).getPath()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] strings = line.split("\t");
				abbreviations.put(strings[0], strings[1]);
			}
			sreader = new BufferedReader(new FileReader(Preprocessor.class.getClassLoader().getResource(slocation).getPath()));
			while ((line = sreader.readLine()) != null) {
				String[] strings = line.split("\t");
				smileys.put(strings[0], strings[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (sreader != null) {
					sreader.close();
				}
			} catch (IOException e) {
				// Redundant
			}
		}

	}

	public static String preprocess(String line) {
		if (line.length() == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		// Remove Smileys
		line = removeSmileys(line).toString();
		// Change @user to user
		for (String string : line.split(" ")) {
			while (string.startsWith("@")) {
				string = string.substring(1);
			}
			sb.append(string + " ");
		}
		line = sb.toString();
		sb = new StringBuffer();
		// Now remove all the hashTags
		for (String string : line.split(" ")) {
			while (string.startsWith("#")) {
				string = string.substring(1);
			}
			sb.append(string + " ");
		}
		// Now remove redundant special characters
		int count = 0;
		char prev = sb.charAt(0);
		for (int i = 1, j = 1; i < sb.length(); i++) {
			Character ch = sb.charAt(i);
			if (!Character.isLetter(ch) && (prev == ch)) {
				count++;
			} else {
				sb.setCharAt(j, ch);
				j++;
			}
			prev = ch;
		}
		sb = new StringBuffer(sb.substring(0, sb.toString().length() - count));
		// Map the short forms to their correct forms
		sb = expand(sb.toString());
		sb.append(" ");
		return sb.toString();
	}

	private static String removeSmileys(String input) {
		StringBuffer sb = new StringBuffer();
		// First remove all the hashTags
		for (String string : input.split(" ")) {
			if (string.length() > 0) {
				boolean special = false;
				Character ch = string.charAt(string.length() - 1);
				if (smileys.containsKey(string)) {
					string = "";
				} else {
					if (!Character.isLetterOrDigit(ch)) {
						string = string.substring(0, string.length() - 1);
						special = true;
					}
				}
				sb.append(" " + string + ((special) ? "" + ch : ""));
			}
		}
		return sb.toString().trim();
	}

	private static StringBuffer expand(String input) {
		StringBuffer sb = new StringBuffer();
		// First remove all the hashTags
		for (String string : input.split(" ")) {
			Character ch = new Character(' ');
			boolean special = false;
			if (string.length() > 1) {
				ch = string.charAt(string.length() - 1);
				if (!Character.isLetterOrDigit(ch)) {
					string = string.substring(0, string.length() - 1);
					special = true;
				}
			}
			if (abbreviations.containsKey(string)) {
				string = abbreviations.get(string);
			}
			sb.append(string + " " + ((special) ? " " + ch + " ": " "));
		}
		return sb;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		System.out.println(smileys);
		System.out.print(">");
		while (!(line = br.readLine()).equals("quit")) {
			System.out.println(preprocess(line));
			System.out.print(">");
		}
	}
}
