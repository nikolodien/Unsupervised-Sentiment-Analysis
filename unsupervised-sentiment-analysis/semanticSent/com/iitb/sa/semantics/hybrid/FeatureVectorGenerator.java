package com.iitb.sa.semantics.hybrid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.iitb.sa.semantics.unl.Polarities;
import com.iitb.sa.semantics.unl.SentimentAnalyser;

class FeatureVector {
	int[] features = new int[VocabuloryCreator.getSize()];
}

public class FeatureVectorGenerator {

	public void createFeatureFile(String idir, String outputFile)
			throws IOException {
		File dir = new File(idir);
		BufferedWriter bf = new BufferedWriter(new FileWriter(new File(
				outputFile)));
		int count = 1;
		for (File file : dir.listFiles()) {
			System.out.println(file.getName());
			if(file.getName().equals("pos-87") || count>1)count++;
			if(count<4)continue;
			FeatureVector fv = getFeatureVector(file);
			StringBuffer sb = new StringBuffer();
			if (fv != null) {
				if (file.getName().startsWith("pos"))
					sb.append("1");
				else
					sb.append("-1");
				for (int i = 0; i < fv.features.length; i++) {
					sb.append(" " + (i + 1) + ":" + fv.features[i]);
				}
			}
			if (sb.length() > 0) {
				sb.append(" // " + file.getName());
				bf.write(sb.toString());
				bf.write("\n");
			}
			System.out.println(file.getName() + ":" + sb.toString());
		}
		bf.close();
	}

	private FeatureVector getFeatureVector(File file) {
		FeatureVector fv = new FeatureVector();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			int index;
			while ((line = br.readLine()) != null) {
				Map<String, Integer> polarityMap = SentimentAnalyser
						.getPolarityMap(line);
				for (String word : polarityMap.keySet()) {
					index = VocabuloryCreator.getIndex(getStemmedWord(word));
					if (index > -1) {
						fv.features[index] += Polarities.getPolarity(word);
					}
				}
				for (String word : line.split(" ")) {
					if (!polarityMap.containsKey(word)) {
						index = VocabuloryCreator
								.getIndex(getStemmedWord(word));
						if (index > -1) {
							fv.features[index] += Polarities.getPolarity(word);
						}
					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fv;
	}

	private String getStemmedWord(String word) {
		Stemmer st = new Stemmer();
		for (int i = 0; i < word.length(); i++) {
			st.add(word.charAt(i));
		}
		st.stem();
		return st.toString();
	}

	public static void main(String[] args) throws IOException {
		VocabuloryCreator
				.createFromCorpus("/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Product/svm/crossvalidation");
		FeatureVectorGenerator fvg = new FeatureVectorGenerator();
		fvg.createFeatureFile(
				"/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Product/svm/crossvalidation",
				"/home/nikhilkumar/Documents/RnD/Workspace/SemanticSent/resources/feature_file_unl");
		System.out.println("Feature file created");
	}
}
