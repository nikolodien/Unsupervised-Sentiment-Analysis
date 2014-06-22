package com.iitb.sa.semantics.unl.svm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;

public class SvmPredict {

	private static double atof(String s) {
		return Double.valueOf(s).doubleValue();
	}

	private static int atoi(String s) {
		return Integer.parseInt(s);
	}

	public void predict(String feature_file, String output_file,
			String model_file, int predict_probability) throws IOException {
		int correct = 0;
		int total = 0;
		double error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;

		BufferedReader input = new BufferedReader(new FileReader(feature_file));
		DataOutputStream output = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(output_file)));
		svm_model model = svm.svm_load_model(model_file);

		int svm_type = svm.svm_get_svm_type(model);
		int nr_class = svm.svm_get_nr_class(model);
		double[] prob_estimates = null;

		while (true) {
			String line = input.readLine();
			if (line == null)
				break;
			String[] split = line.split("//");
			line = split[0];
			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			double target = atof(st.nextToken());
			int m = st.countTokens() / 2;
			svm_node[] x = new svm_node[m];
			for (int j = 0; j < m; j++) {
				x[j] = new svm_node();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}

			double v;
			if (predict_probability == 1
					&& (svm_type == svm_parameter.C_SVC || svm_type == svm_parameter.NU_SVC)) {
				v = svm.svm_predict_probability(model, x, prob_estimates);
				output.writeBytes(v + " ");
				for (int j = 0; j < nr_class; j++)
					output.writeBytes(prob_estimates[j] + " ");
				output.writeBytes("\n");
			} else {
				v = svm.svm_predict(model, x);
				output.writeBytes(v + "\n");
			}

			if (v == target)
				++correct;
			error += (v - target) * (v - target);
			sumv += v;
			sumy += target;
			sumvv += v * v;
			sumyy += target * target;
			sumvy += v * target;
			++total;
		}
		if (svm_type == svm_parameter.EPSILON_SVR
				|| svm_type == svm_parameter.NU_SVR) {
			System.out.print("Mean squared error = " + error / total
					+ " (regression)\n");
			System.out.print("Squared correlation coefficient = "
					+ ((total * sumvy - sumv * sumy) * (total * sumvy - sumv
							* sumy))
					/ ((total * sumvv - sumv * sumv) * (total * sumyy - sumy
							* sumy)) + " (regression)\n");
		} else
			System.out.print("Accuracy = " + (double) correct / total * 100
					+ "% (" + correct + "/" + total + ") (classification)\n");
	}

	public static void main(String[] args) throws IOException {
		SvmPredict sp = new SvmPredict();
		sp.predict(
				"/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Tourism/svm/Testing/feature_file",
				"/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Tourism/svm/Testing/output_file",
				"/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Tourism/svm/Training/model_file",
				0);
	}
}
