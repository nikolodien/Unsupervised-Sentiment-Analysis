package org.iitb.sa.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iitb.sa.classifiers.BagOfWords;
import org.iitb.sa.classifiers.SentiTopicClassifier;
import org.iitb.sa.classifiers.SentiTopicalBigramClassifier;
import org.json.simple.JSONObject;

import cc.mallet.topics.TopicalNGramInferencer;

import com.iitb.sa.semantics.unl.Constants;
import com.iitb.sa.semantics.unl.SentimentAnalyser;

import edu.stanford.nlp.io.EncodingPrintWriter.out;

/**
 * Servlet implementation class SentiServer
 */
@WebServlet("/sentiserver")
public class SentiServer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public SentiServer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String input = request.getParameter("input").toString();
		System.out.println(input);
		int bowClassification = BagOfWords.classify(input);
		int sentiTopicClassification = SentiTopicClassifier.classify(input);
		int sentiTopicalBigramClassification = SentiTopicalBigramClassifier
				.classify(input);
		int unlClassification = Integer.parseInt(SentimentAnalyser
				.analyseSentenceSemanticallyEnhanced(input).split(
						Constants.SEPARATOR)[1]);
		int consensus = getConsensus(bowClassification,
				sentiTopicalBigramClassification, unlClassification);
		response.setContentType("application/json");
		JSONObject output = new JSONObject();
		System.out.println("bow:" + bowClassification);
		output.put("bow", bowClassification);
		System.out.println("topic:" + sentiTopicClassification);
		output.put("topic", sentiTopicClassification);
		System.out.println("topical:" + sentiTopicalBigramClassification);
		output.put("topical", sentiTopicalBigramClassification);
		System.out.println("unl:" + unlClassification);
		output.put("unl", unlClassification);
		System.out.println("consensus:" + consensus);
		output.put("consensus", consensus);
		System.out.println(output.toJSONString());
		response.getWriter().print(output);
	}

	private int getConsensus(int bowClassification,
			int sentiTopicalBigramClassification, int unlClassification) {
		int sum = bowClassification + sentiTopicalBigramClassification
				+ 3*unlClassification;
		return sum >= 0 ? 1 : -1;
	}

}
