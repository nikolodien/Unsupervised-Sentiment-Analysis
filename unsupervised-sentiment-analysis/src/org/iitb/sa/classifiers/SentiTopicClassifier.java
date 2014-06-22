package org.iitb.sa.classifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import cc.mallet.pipe.Pipe;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class SentiTopicClassifier {

	private static TopicInferencer inferencer = null;
	private static Pipe pipe = null;

	static {
		loadClassifier("resources/sentitopic/topic-inferencer");
		initPipe("resources/sentitopic/text_300000_fortopicmodeling.mallet");
	}

	private static void loadClassifier(String filename) {
		ObjectInputStream ois;
		try {
			System.out.println(SentiTopicClassifier.class.getClassLoader()
					.getResource(filename));
			inferencer = TopicInferencer.read(new File(
					SentiTopicClassifier.class.getClassLoader()
							.getResource(filename).getPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void initPipe(String inputFile) {
		// TODO Auto-generated method stub
		pipe = InstanceList.load(
				new File(SentiTopicClassifier.class.getClassLoader()
						.getResource(inputFile).getPath())).getPipe();
	}

	public static int classify(String text) {
		if (inferencer != null) {
			Instance instance = new Instance(text, null, null, null);
			instance = pipe.instanceFrom(instance);
			double[] distribution = inferencer.getSampledDistribution(instance,
					100, 10, 10);
			System.out.println(distribution[0]);
			System.out.println(distribution[1]);
			if (distribution[0] >= distribution[1]) {
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
