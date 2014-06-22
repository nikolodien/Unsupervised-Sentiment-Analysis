package org.iitb.mtp.sair.poc.ir.crawlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class RottenTomatoesConstants {
	static final String BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0/";
	static final String API_KEY_PARAMETER = "apikey";
	static String API_KEY;
	static final String MOVIES_SUB_URL = "movies.json";
	static final String REVIEWS_SUB_URL = "reviews.json";
	static final String QUERY = "q";
	
	static{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("resources/rotten_tomatoes.txt")));
			API_KEY  = reader.readLine().split("=")[1];
		} catch (FileNotFoundException e) {
			// TODO replace with logging
			System.out.println("Error opening file");
		} catch (IOException e) {
			// TODO replace with logging
			System.out.println("Error reading from file");
		}
		
	}
	
	// Constants for parsing
	static final String MOVIES = "movies";
	static final String TITLE = "title";
	static final String ID = "id";
	static final String YEAR = "year";
	static final String MPAA_RATING = "mpaa_rating";
	static final String RUNTIME = "runtime";
	static final String CRITICS_CONSENSUS = "critics_consensus";
	static final String SYNOPSIS = "synopsis";
	
}
