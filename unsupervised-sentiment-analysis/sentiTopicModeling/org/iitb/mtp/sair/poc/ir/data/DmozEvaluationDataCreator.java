package org.iitb.mtp.sair.poc.ir.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.iitb.mtp.sair.poc.mysql.Connector;

public class DmozEvaluationDataCreator {
	String dir = "resources/evaluation/dmoz/files/";
	Connection connection = null;

	public void createFiles() throws ClassNotFoundException, SQLException,
			IOException {
		ResultSet rs = getSites();
		File directory = new File(dir);
		if(!directory.exists()){
			directory.mkdir();
		}
		while (rs.next()) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(dir
					+ rs.getString(1) + ".txt")));
			StringBuffer buffer = new StringBuffer(rs.getString(4));
			for (int i = 0; i < 4; i++) {
				buffer.append(buffer.toString());
			}
			bw.write(buffer.toString());
			bw.close();
		}
	}

	public void create5folds() throws ClassNotFoundException, SQLException,
			IOException {
		// Each fold should have training and testing files
		ResultSet rs = getSites();
		String fold = "resources/evaluation/dmoz/fold";
		int max = 1273;
		int limit = max / 5;
		for (int i = 1, foldi = 1; i <= max; i += limit + 1, foldi++) {
			File dir = new File(fold + foldi);
			if (!dir.exists())
				dir.mkdir();
			dir = new File(fold + foldi + "/training/");
			if (!dir.exists())
				dir.mkdir();
			dir = new File(fold + foldi + "/testing/");
			if (!dir.exists())
				dir.mkdir();
			List<Integer> fileIDs = new ArrayList<Integer>();
			for (int j = i; j <= i + limit && j <= max; j++) {
				fileIDs.add(j);
				if (rs.next()) {
					BufferedWriter bw = new BufferedWriter(new FileWriter(
							new File(fold + foldi + "/testing/" + rs.getString(1)
									+ ".txt")));
					StringBuffer buffer = new StringBuffer(rs.getString(4));
					for (int k = 0; k < 4; k++) {
						buffer.append(buffer.toString());
					}
					bw.write(buffer.toString());
					bw.close();
				}
			}
			ResultSet rs2 = getSites();
			while(rs2.next()){
				if(!fileIDs.contains(rs2.getInt(1))){
					BufferedWriter bw = new BufferedWriter(new FileWriter(
							new File(fold + foldi + "/training/" + rs2.getString(1)
									+ ".txt")));
					StringBuffer buffer = new StringBuffer(rs2.getString(4));
					for (int k = 0; k < 4; k++) {
						buffer.append(buffer.toString());
					}
					bw.write(buffer.toString());
					bw.close();
				}
			}
		}
	}

	private ResultSet getSites() throws ClassNotFoundException, SQLException {
		String query = "select * from links";
		connection = Connector.getConnection("dmoz", "root", "niks");
		ResultSet resultSet = connection.createStatement().executeQuery(query);
		return resultSet;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		connection.close();
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException, IOException {
		new DmozEvaluationDataCreator().create5folds();
	}
}
