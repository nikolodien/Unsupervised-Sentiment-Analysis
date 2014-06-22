package org.iitb.mtp.sair.poc.ir.crawlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class ImdbMovieDataFetcher implements URLDataFetcher {

	private final String baseURL = "http://www.omdbapi.com/";
	private final String tomatoesOn = "true";

	@Override
	public JSONObject fetch(String movie) throws IOException {
		String url = baseURL + "?t=" + movie + "&plot=full&tomatoes="
				+ tomatoesOn;
		System.out.println(url);
		URL server = new URL(url);
		URLConnection connection = server
				.openConnection(ProxySetter.getProxy());
		connection.connect();
		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer sb = new StringBuffer();
		String inputLine = null;
		while ((inputLine = reader.readLine()) != null)
			sb.append(inputLine);
		in.close();
		return (JSONObject) JSONValue.parse(sb.toString());
	}

	public static void main(String[] args) throws IOException {
		ImdbMovieDataFetcher mdf = new ImdbMovieDataFetcher();
		JSONObject json = mdf.fetch("Stone");
		System.out.println(json.get("Title"));
		System.out.println(json.get("Plot"));
		System.out.println(json);
	}

}
