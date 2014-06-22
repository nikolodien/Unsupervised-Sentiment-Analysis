package com.iitb.sa.semantics.unl.batchunl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

	public static void convertFile(String infilename, String ofilename) {
		try {
			FileReader fr = new FileReader(new File(infilename));
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(new File(ofilename));
			BufferedWriter bw = new BufferedWriter(fw);
			String line = null;
			StringBuffer text = new StringBuffer();
			while ((line = br.readLine()) != null) {
				line = process(line);
				text.append(line);
			}
			bw.write(text.toString().trim());
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void convertDir(String idir, String odir) {
		File dir = new File(idir);
		for (File file : dir.listFiles()) {
			String filename = file.getName();
			String ofilename = odir + "/" + filename;
			convertFile(file.getAbsolutePath(), ofilename);
		}
	}

	private static String process(String line) {
		StringBuffer sb = new StringBuffer();
		line = line.trim();
		Pattern p = Pattern.compile("([0-9]+)(.+)");
		for (String string : line.split(" ")) {
			string = string.trim();
			if (string.startsWith(":") || string.equals(""))
				continue;
			if (string.contains("_")) {
				for (String substring : string.split("_")) {
					if (substring.equals(""))
						continue;
					else if (substring.matches("[0-9]+")) {
						continue;
					} else if (substring.matches("[0-9]+.+")) {
						Matcher matcher = p.matcher(substring);
						if (matcher.find() && substring.length()>0) {
							substring = substring.substring(matcher.end()-1,
									substring.length());
						}
					}
					if (substring.startsWith("#"))
						substring = substring.substring(1,
								substring.length());
					if (substring.endsWith("#"))
						substring = substring.substring(0,
								substring.length()-1);
					sb.append(substring + " ");
				}
			} else {
				if (string.startsWith("#"))
					string = string.substring(1, string.length());
				if (string.endsWith("#"))
					string = string.substring(0, string.length()-1);
				sb.append(string + " ");
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		convertDir(
				"/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Product_SenseTagged/pos",
				"/home/nikhilkumar/Documents/RnD/ACL Experiments/Corpus/English_Product/pos");
	}
}
