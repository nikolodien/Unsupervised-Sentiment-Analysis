package com.iitb.sa.semantics.unl.svm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import twitterdiscourse.FindOpinion;

import com.iitb.sa.semantics.unl.SentimentAnalyser;

public class FeatureVectorGenerator {

	public void createFeatureFile(String idir, String outputFile)
			throws IOException {
		File dir = new File(idir);
		BufferedWriter bf = new BufferedWriter(new FileWriter(new File(
				outputFile)));
		int counter=0;
		for (File file : dir.listFiles()) {
			counter++;
			if(counter<733)continue;
			FeatureVector fv = getFeatureVector(file);
			StringBuffer sb = new StringBuffer();
			if (fv != null) {
				if (file.getName().startsWith("pos"))
					sb.append("1");
				else
					sb.append("-1");
				sb.append(" 1:" + fv.getNeg());
				sb.append(" 2:" + fv.getObj());
				sb.append(" 3:" + fv.getPos());
				sb.append(" 4:" + fv.getSmileyprediction());
				sb.append(" 5:" + fv.getUnlprediction());
			}
			if (sb.length() > 0) {
				sb.append(" // " + file.getName());
				bf.write(sb.toString());
				bf.write("\n");
			}
		}
		bf.close();
	}

	private FeatureVector getFeatureVector(File file) {
		FeatureVector fv = new FeatureVector();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				FeatureVector fv1 = null;
				try{
					 fv1 = SentimentAnalyser.createFeatureVector(line);
				}catch(OutOfMemoryError oome){
					return null;
				}
				fv.setNeg(fv1.getNeg() + fv.getNeg());
				fv.setObj(fv1.getObj() + fv.getObj());
				fv.setPos(fv1.getPos() + fv.getPos());
				fv.setSmileyprediction(fv1.getSmileyprediction()
						+ fv.getSmileyprediction());
				fv.setUnlprediction(fv1.getUnlprediction()
						+ fv.getUnlprediction());
			}
			float temp = 0;
			temp = fv.getSmileyprediction();
			fv.setSmileyprediction(((temp > 0) ? 1 : (temp < 0) ? -1 : 0));
			temp = fv.getUnlprediction();
			fv.setUnlprediction(((temp > 0) ? 1 : (temp < 0) ? -1 : 0));
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fv;
	}

	public static void main(String[] args) throws IOException {
		FeatureVectorGenerator fvg = new FeatureVectorGenerator();
		fvg.createFeatureFile(
				"/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Product/svm/crossvalidation",
				"/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Product/svm/feature_file");
		System.out.println("Training Feature file created");
	}
}
