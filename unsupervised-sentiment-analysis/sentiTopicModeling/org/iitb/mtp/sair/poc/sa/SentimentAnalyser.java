package org.iitb.mtp.sair.poc.sa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iitb.mtp.sair.poc.sa.pos.PosTagger;

public class SentimentAnalyser {
	private final static SentiWordNet swna = new SentiWordNet();
	private final static List<String> sentiPosTags = new ArrayList<String>();

	static {
		sentiPosTags.add("JJ");
		sentiPosTags.add("JJR");
		sentiPosTags.add("JJS");
		sentiPosTags.add("VB");
		sentiPosTags.add("VBD");
		sentiPosTags.add("VBG");
		sentiPosTags.add("VBN");
		sentiPosTags.add("VBP");
		sentiPosTags.add("VBZ");
	}

	public static int analyseSentence(String line) {
		float sum = 0;
		Map<String, String> posTags = PosTagger.tag(line);
		for (String word : posTags.keySet()) {
			if (sentiPosTags.contains(posTags.get(word))) {
				sum += swna.sentiScore(word);
			}
		}
		return (sum > 0) ? 1 : (sum < 0) ? -1 : 0;
	}
}
