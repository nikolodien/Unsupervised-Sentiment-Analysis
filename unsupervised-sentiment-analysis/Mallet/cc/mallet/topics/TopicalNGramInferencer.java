package cc.mallet.topics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class TopicalNGramInferencer {

	private static TopicalNGrams tng;
	private static Pipe pipe;
//	private static String path = TopicalNGramInferencer.class.getClassLoader().getResource("").getPath(); 
	private static String path = "/home/nick/Documents/workspace/mindingit/WebContent/WEB-INF/classes/";

	static{
		String model = path+"resources/bigramsentitopic/for100000/model";
		String input = path+"resources/bigramsentitopic/for100000/input.mallet";
		read(model);
		tng.initializePriors();
		readInput(input);
	}

	private static void readInput(String input) {
		pipe = InstanceList.load(new File(input)).getPipe();	
	}

	public static double[] findTopicDistribution(Instance instance) {
		return tng.getSampledDistribution(instance, 100);
	}

	public static double[] findTopicDistribution(String text) {
		Instance instance = new Instance(text, null, null, null);
		instance = pipe.instanceFrom(instance);
		return findTopicDistribution(instance);
	}

	public static void read(String file) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					new File(file)));
			tng = (TopicalNGrams) ois.readObject();
			ois.close();
		} catch (IOException e) {
			System.err.println("Exception reading file " + file + ": " + e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String line = null;
		System.out.println();
		System.out.print(">>");
		try {
			while ((line = reader.readLine()) != null) {
				double[] findTopicDistribution = TopicalNGramInferencer.findTopicDistribution(line);
				for (int i = 0; i < findTopicDistribution.length; i++) {
					System.out.println("Topic="+i+",probability="+findTopicDistribution[i]);
				}
				System.out.print(">>");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
