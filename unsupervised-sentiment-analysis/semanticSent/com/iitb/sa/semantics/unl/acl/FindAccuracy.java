package com.iitb.sa.semantics.unl.acl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import twitterdiscourse.FindOpinion;

import com.iitb.sa.semantics.unl.SentimentAnalyser;

public class FindAccuracy {
	
	private static FindOpinion fp = new FindOpinion();
	
	public static void findDirAccuracy(String idir, int desired) {
		double files = 0, correct = 0;
		int prediction = 0;
		File dir = new File(idir);
		for (File file : dir.listFiles()) {
			files++;
			prediction = predictDiscourseAccuracy(file);
			// TODO
			if ((desired == -1 && (prediction ==0 || prediction == desired)) || (desired == 1 && prediction == desired)) {
				correct++;
			}
			System.out.println(file.getAbsolutePath());
			System.out.println("Correct=" + correct + ",files=" + files);
		}
		System.out.println("Total no. of files = " + files);
		System.out.println("Correct predictions = " + correct);
		System.out.println("Accuracy = " + (correct / files) * 100);
	}

	private static int predictfile(File file) {
		int sum = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				sum += Integer.parseInt(SentimentAnalyser
						.analyseSentenceSemanticallyEnhanced(line)
						.split("::::")[1]);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ((sum > 0) ? 1 : (sum < 0) ? -1 : 0);
	}

	private static int predictDiscourseAccuracy(File file){
		int sum = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				String sentiment = fp.findOpinionDis(line);
				sum += sentiment.equals("pos")?1:(sentiment.equals("neg")?-1:0);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ((sum > 0) ? 1 : (sum < 0) ? -1 : 0);
	}
	
	public static void findAccfromfeature(String filename){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			double pos=0,neg=0,posc=0,negc=0;
			while((line=br.readLine())!=null){
				String[] split = line.split(" ");
				double prediction = Double.parseDouble(split[5].split(":")[1]);
				if(split[7].startsWith("n")){
					neg++;
					if(prediction<=0){
						negc++;
					}
				}else{
					pos++;
					if(prediction>0){
						posc++;
					}
				}
			}
			System.out.println("No. of positive:"+pos);
			System.out.println("No. of correct positive predictions:"+posc);
			System.out.println("No. of negative:"+neg);
			System.out.println("No. of correct negative predictions"+negc);
			System.out.println("Accuracy of positive"+posc/pos);
			System.out.println("Accuracy of negative:"+negc/neg);
			System.out.println("Total accuracy:"+(posc+negc)/(pos+neg));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		findDirAccuracy("/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Product/neg",-1);
//		findAccfromfeature("/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Product/svm/feature_file");
	}
}
