package org.iitb.sa.classifiers;

import cc.mallet.topics.TopicalNGramInferencer;

public class SentiTopicalBigramClassifier {
	public static int classify(String text) {
		double[] distribution = TopicalNGramInferencer
				.findTopicDistribution(text);
		System.out.println(distribution[0]);
		System.out.println(distribution[1]);
		if (distribution[0] >= distribution[1]) {
			return 1;
		} else {
			return -1;
		}
	}
}
