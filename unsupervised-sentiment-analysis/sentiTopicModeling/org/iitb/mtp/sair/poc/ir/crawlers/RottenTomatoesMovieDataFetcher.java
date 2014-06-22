package org.iitb.mtp.sair.poc.ir.crawlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class RottenTomatoesMovieDataFetcher implements URLDataFetcher {

	@Override
	public JSONObject fetch(String movie) throws IOException {
		String url = RottenTomatoesConstants.BASE_URL
				+ RottenTomatoesConstants.MOVIES_SUB_URL + "?"
				+ RottenTomatoesConstants.API_KEY_PARAMETER + "="
				+ RottenTomatoesConstants.API_KEY + "&"
				+ RottenTomatoesConstants.QUERY + "=" + movie;
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

	public HashMap<String, String> parseMovieInfo(JSONObject jsonObj) {
		HashMap<String, String> movieInfo = new LinkedHashMap<String, String>();
		JSONArray array = (JSONArray) jsonObj
				.get(RottenTomatoesConstants.MOVIES);
		JSONObject movie = (JSONObject) array.get(0);
		movieInfo.put(RottenTomatoesConstants.TITLE,
				(String) movie.get(RottenTomatoesConstants.TITLE));
		movieInfo.put(RottenTomatoesConstants.ID,
				(String) movie.get(RottenTomatoesConstants.ID));
		movieInfo.put(RottenTomatoesConstants.YEAR,
				((Long) movie.get(RottenTomatoesConstants.YEAR)).toString());
		movieInfo.put(RottenTomatoesConstants.MPAA_RATING,
				(String) movie.get(RottenTomatoesConstants.MPAA_RATING));
		movieInfo.put(RottenTomatoesConstants.RUNTIME, ((Long) movie
				.get(RottenTomatoesConstants.RUNTIME)).toString());
		movieInfo.put(RottenTomatoesConstants.SYNOPSIS,
				(String) movie.get(RottenTomatoesConstants.SYNOPSIS));
		movieInfo.put(RottenTomatoesConstants.CRITICS_CONSENSUS,
				(String) movie.get(RottenTomatoesConstants.CRITICS_CONSENSUS));
		return movieInfo;
	}

	public JSONObject fetchReviews(String movieId) throws IOException {
		String url = RottenTomatoesConstants.BASE_URL + movieId + "/"
				+ RottenTomatoesConstants.REVIEWS_SUB_URL + "?"
				+ RottenTomatoesConstants.API_KEY_PARAMETER + "="
				+ RottenTomatoesConstants.API_KEY;
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
		RottenTomatoesMovieDataFetcher mdf = new RottenTomatoesMovieDataFetcher();
		JSONObject json = mdf.fetch("The+Dark+Knight");
		HashMap<String, String> movieInfo = mdf.parseMovieInfo(json);
		for (String string : movieInfo.keySet()) {
			System.out.println(string + ":" + movieInfo.get(string));
		}
	}

}
