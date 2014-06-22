package org.iitb.mtp.sair.poc.sa.pos;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosTagger {
	private static final String TAG_SEPARATOR = "_";
	private static final MaxentTagger tagg = null;

	public static Map<String, String> tag(String sentence) {
		Map<String, String> tags = new HashMap<String, String>();
		for(String wordTag:tagg.tagString(sentence).split(" ")){
			String[] split = wordTag.split(TAG_SEPARATOR);
			tags.put(split[0], split[1]);
		}
		return tags;
	}
}
