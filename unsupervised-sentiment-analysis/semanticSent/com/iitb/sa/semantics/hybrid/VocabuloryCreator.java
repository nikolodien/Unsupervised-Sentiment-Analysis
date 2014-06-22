package com.iitb.sa.semantics.hybrid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VocabuloryCreator {
	private static Map<String, Integer> dictionary = new HashMap<String, Integer>();

	static int getSize() {
		return dictionary.size();
	}

	static {
//		try {
//			int start = 0;
//			String file = "/home/nikhilkumar/Documents/RnD/Workspace/SemanticSent/resources/words.txt";
//			// For positive words
//			BufferedReader br = new BufferedReader(new FileReader(
//					new File(file)));
//			String line = null;
//			while ((line = br.readLine()) != null)
//				dictionary.put(line.trim(), start++);
//			br.close();
//		} catch (FileNotFoundException e) {
//			System.err.println("Error opening file");
//		} catch (IOException e) {
//			System.err.println("Error reading line");
//		}
	}

	static int getIndex(String word) {
		if (dictionary.containsKey(word)) {
			return dictionary.get(word);
		} else {
			return -1;
		}
	}

	public static void createFromCorpus(String idir)
			throws IOException {
		Set<String> words = new HashSet<String>();
		File dir = new File(idir);
		for (File file : dir.listFiles()) {
			words.addAll(getWords(file));
		}
		List<String> wordList = new ArrayList<String>();
		wordList.addAll(words);
		Collections.sort(wordList);
		System.out.println(wordList);
		int index = 0;
		for (String word : wordList) {
			dictionary.put(word, index++);
		}
	}

	private static Set<String> getWords(File file) {
		Set<String> words = new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				for (String word : line.split(" ")) {
					words.add(getStemmedWord(word));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}

	private static String getStemmedWord(String word) {
		Stemmer st = new Stemmer();
		for (int i = 0; i < word.length(); i++) {
			st.add(word.charAt(i));
		}
		st.stem();
		return st.toString();
	}
}