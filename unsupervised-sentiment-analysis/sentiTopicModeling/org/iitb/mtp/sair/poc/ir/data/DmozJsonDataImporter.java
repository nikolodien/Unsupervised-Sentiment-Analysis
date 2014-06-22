package org.iitb.mtp.sair.poc.ir.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.iitb.mtp.sair.poc.mysql.Connector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class DmozJsonDataImporter {
	List<JSONObject> jsondata = null;
	Connection connection = null;
	Statement statement = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultData = null;

	public DmozJsonDataImporter() {
		jsondata = new ArrayList<JSONObject>();
	}

	public void importdata(String filename) throws ClassNotFoundException,
			SQLException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				new File(filename)));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("["))
				line = line.substring(1);
			if (line.endsWith(","))
				line = line.substring(0, line.length() - 1);
			if (line.endsWith("]"))
				line = line.substring(0, line.length() - 1);
			JSONObject obj = (JSONObject) JSONValue.parse(line);
			if (obj != null) {
				jsondata.add(obj);
			} else {
				System.out.println(line);
			}
		}
		br.close();
	}

	public void mysqlupdater() throws ClassNotFoundException {
		try {
			connection = Connector.getConnection("dmoz", "root", "niks");
			preparedStatement = connection
					.prepareStatement("insert into links(url,name,description,category) values(?,?,?,?)");
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		}
		for (JSONObject jsonObject : jsondata) {
			// System.out.println(((JSONArray)jsonObject.get("url")).get(0));
			try {
				preparedStatement.setString(1,
						((JSONArray) jsonObject.get("url")).get(0).toString().trim());
				preparedStatement.setString(2,
						((JSONArray) jsonObject.get("name")).get(0).toString().trim());
				preparedStatement.setString(3, ((JSONArray) jsonObject
						.get("description")).get(0).toString().trim());
				preparedStatement.setString(4, jsonObject.get("category")
						.toString().trim());
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			try {
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		connection.close();
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException, IOException {
		DmozJsonDataImporter importer = new DmozJsonDataImporter();
		importer.importdata("resources/evaluation/dmoz/items.json");
		importer.mysqlupdater();
	}
}
