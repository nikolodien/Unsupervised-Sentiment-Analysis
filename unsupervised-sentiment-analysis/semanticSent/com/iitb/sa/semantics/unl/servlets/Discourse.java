package com.iitb.sa.semantics.unl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitterdiscourse.FindOpinion;

import com.iitb.sa.semantics.unl.SentimentAnalyser;

/**
 * Servlet implementation class Discourse
 */
public class Discourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static FindOpinion fp = new FindOpinion();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Discourse() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sentiment = fp.findOpinionDis(request.getParameter("input"));
		response.getWriter().println((sentiment.equals("pos")?1:(sentiment.equals("neg")?-1:0)));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
