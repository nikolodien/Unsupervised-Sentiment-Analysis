package com.iitb.sa.semantics.unl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Client.ApplicationProperty;

public class Polarities {
	private final static Map<String, Integer> polarities = new HashMap<String, Integer>();

	static {
		try {
			String prefix = Polarities.class.getClassLoader().getResource("").getPath()+"resources/word_lists/";
			String positiveFile = prefix + "positive-words.txt";
			String negativeFile = prefix + "negative-words.txt";
			// For positive words
			BufferedReader br = new BufferedReader(new FileReader(new File(
					positiveFile)));
			String line = null;
			while ((line = br.readLine()) != null)
				polarities.put(line.trim(), 1);
			br.close();
			// For negative words
			br = new BufferedReader(new FileReader(new File(negativeFile)));
			while ((line = br.readLine()) != null)
				polarities.put(line.trim(), -1);
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error opening file");
		} catch (IOException e) {
			System.err.println("Error reading line");
		}
	}

	public static Integer getPolarity(String word) {
		if (polarities.containsKey(word))
			return polarities.get(word);
		else
			return 0;
	}
}
