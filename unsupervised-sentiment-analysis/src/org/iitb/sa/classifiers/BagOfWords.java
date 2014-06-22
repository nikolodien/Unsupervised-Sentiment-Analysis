package org.iitb.sa.classifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Random;

import cc.mallet.classify.Classifier;
import cc.mallet.types.Instance;
import cc.mallet.types.Label;
import cc.mallet.types.Labeling;

public class BagOfWords {

	private static Classifier classifier = null;

	static {
		loadClassifier("resources/bow/classifier_300000.mallet");
	}

	private static void loadClassifier(String filename) {
		ObjectInputStream ois;
		try {
			System.out.println(BagOfWords.class.getClassLoader().getResource(
					filename));
			ois = new ObjectInputStream(new FileInputStream(BagOfWords.class
					.getClassLoader().getResource(filename).getPath()));
			classifier = (Classifier) ois.readObject();
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int classify(String text) {
		if (classifier != null) {
			Instance instance = new Instance(text, null, null, null);
			Instance processedInstance = classifier.getInstancePipe()
					.instanceFrom(instance);
			Labeling labeling = classifier.classify(processedInstance)
					.getLabeling();
			Label bestLabel = labeling.getBestLabel();
			if (bestLabel.toString().equals("pos")) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 1;
		}
	}

	public Instance createInstance(String text) {

		return null;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String line = null;
		System.out.println();
		System.out.print(">>");
		while ((line = reader.readLine()) != null) {
			System.out.println(classify(line));
			System.out.print(">>");
		}
	}
}
