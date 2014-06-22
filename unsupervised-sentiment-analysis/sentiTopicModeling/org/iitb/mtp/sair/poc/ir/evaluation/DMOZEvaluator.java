package org.iitb.mtp.sair.poc.ir.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.iitb.mtp.sair.poc.mysql.Connector;

public class DMOZEvaluator {
	/**
	 * This will create a reference database for
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void tagDocuments() throws ClassNotFoundException, SQLException {
		// As per training, we can label the topics as follows
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("computers", 0);
		map.put("films", 1);
		map.put("sports", 2);
		map.put("realestate", 3);
		map.put("cooking", 4);
		Connection connection = Connector.getConnection("dmoz", "root", "niks");
		PreparedStatement preparedStatement = connection
				.prepareStatement("insert into reference values(?,?)");
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("select id,category from links");
		while (rs.next()) {
			preparedStatement.setInt(1, rs.getInt(1));
			preparedStatement.setInt(2, map.get(rs.getString(2)));
			preparedStatement.execute();
		}
		connection.close();
	}

	private void evaluateFolds() throws IOException, ClassNotFoundException,
			SQLException {
		Connection connection = Connector.getConnection("dmoz", "root", "niks");
		Statement statement = connection.createStatement();
		String fold = "resources/evaluation/dmoz/fold";
		String line = null, query = null;
		ResultSet rs = null;
		double avgAccuracy = 0.0;
		for (int foldi = 1; foldi <= 5; foldi++) {
			double accuracy = 0.0;
			double count = 0.0;
			double match = 0.0;
			BufferedReader br = new BufferedReader(new FileReader(new File(fold
					+ foldi + "/inferedtopics")));
			// dummy read the first line as it contains info about the file
			br.readLine();
			while ((line = br.readLine()) != null) {
				count++;
				String[] split = line.split(" ");
				int id = Integer.parseInt(split[1].substring(
						split[1].lastIndexOf("/") + 1, split[1].indexOf(".")));
				int topic = Integer.parseInt(split[2]);
				int secondtopic = Integer.parseInt(split[4]);
				int thirdtopic = Integer.parseInt(split[6]);
				int fourthtopic = Integer.parseInt(split[8]);
				query = "select topic from reference where id=" + id;
				rs = statement.executeQuery(query);
				rs.last();
				if (rs.getInt(1) == topic) {
					match++;
				} else if (rs.getInt(1) == secondtopic) {
					match += 0.8;
				} else if (rs.getInt(1) == thirdtopic) {
					match += 0.6;
				} else if (rs.getInt(1) == fourthtopic) {
					match += 0.4;
				} /* else {
					match += 0.2;
				}*/
			}
			System.out.println("For Fold " + foldi + ", matches=" + match
					+ " and count=" + count);
			accuracy = match / count;
			System.out
					.println("Accuracy for Fold " + foldi + " is " + accuracy);
			System.out.println();
			avgAccuracy += accuracy;
		}
		avgAccuracy /= 5;
		System.out.println();
		System.out.println("Average accuracy of the system is " + avgAccuracy);
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException, IOException {
		new DMOZEvaluator().evaluateFolds();
	}

}
