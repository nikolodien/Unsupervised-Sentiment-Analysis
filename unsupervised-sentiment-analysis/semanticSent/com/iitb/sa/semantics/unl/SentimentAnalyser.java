package com.iitb.sa.semantics.unl;

import iitb.unlenco.unl.UnlConverter;
import iitb.unlenco.unl.SimpleEnconverter.UNLExpression;
import iitb.unlenco.unl.SimpleEnconverter.UNLRelation;
import iitb.unlenco.unl.SimpleEnconverter.UniversalWord;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import twitterdiscourse.SmileyHandler;

import com.iitb.sa.semantics.unl.svm.FeatureVector;

public class SentimentAnalyser {
	private final static SentiWordNet swna = SentiWordNet.instance();
	private final static UnlConverter unc = new UnlConverter();
	private static DocumentBuilderFactory docFactory;
	private static DocumentBuilder docBuilder;
	private static Document doc;
	private static final String prefix = "";
	private static final String PATH = prefix + "result.xml";
	private static Element tests;
	private static int id = 1;
	private static SmileyHandler sh = new SmileyHandler();
	private static final int SMILEYWEIGHT = 3;

	static {
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			tests = doc.createElement("tests");
			doc.appendChild(tests);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static int analyseSentence(String line) throws IOException {
		float sum = 0;
		String[] split = line.split(" ");
		for (int i = 0; i < split.length; i++) {
			int score = swna.sentiScore(split[i]);
			sum += swna.sentiScore(split[i]);
		}
		return (sum > 0) ? 1 : (sum < 0) ? -1 : 0;
	}

	public static void writeToFile() {
		// write the content into xml file
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(PATH));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);
			System.out.println("File saved!");
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static int analyseSentenceSemantically(String line) {
		return analyseSentenceSemantically(line, "");
	}

	public static int analyseSentenceSemantically(String line, String expected) {
		float sum = 0;
		UNLExpression unl = null;
		Element test = doc.createElement("test");
		try {

			Element sentence = doc.createElement("sentence");
			sentence.appendChild(doc.createTextNode(line));
			test.appendChild(sentence);

			Element expectedElement = doc.createElement("expected");
			expectedElement.appendChild(doc.createTextNode(expected));
			test.appendChild(expectedElement);

			unl = UnlConverter.convert(line);

			Element unlelement = doc.createElement("unl");
			Element relations = doc.createElement("relations");
			for (UNLRelation relation : unl.getRelationList()) {
				Element relationElement = doc.createElement("relation");
				relationElement.appendChild(doc.createTextNode(relation
						.toString()));
				relations.appendChild(relationElement);
			}
			unlelement.appendChild(relations);
			Element uws = doc.createElement("words");
			for (UniversalWord uw : unl.getUwList()) {
				Element wordElement = doc.createElement("word");
				wordElement.appendChild(doc.createTextNode(uw.toString()));
				uws.appendChild(wordElement);
			}
			unlelement.appendChild(uws);
			test.appendChild(unlelement);

			Element words = doc.createElement("words");

			for (UNLRelation relation : unl.getRelationList()) {

				if (relation.toString().startsWith("agt")) {
					for (UniversalWord word : unl.getUwList()) {
						if (relation.toString().contains(word.toString())
						/* && word.getAttributes().contains("@entry") */) {
							boolean invert = word.getAttributes().contains(
									"@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(word.getWord());
							sum = sum + wordpolarity;
							Element word_relation = doc
									.createElement("word-relation");

							Element relationElement = doc
									.createElement("relation");
							relationElement.appendChild(doc
									.createTextNode(relation.toString()));
							word_relation.appendChild(relationElement);

							Element wordelement = doc.createElement("word");
							wordelement.appendChild(doc.createTextNode(word
									+ "::" + wordpolarity));
							word_relation.appendChild(wordelement);

							words.appendChild(word_relation);
							System.out.println(sum + " " + word.getWord());
						}
					}
				}
				if (relation.toString().startsWith("man")) {
					for (UniversalWord word : unl.getUwList()) {
						if (relation.toString().contains(word.toString())
								&& word.getAttributes().contains("@entry")) {
							boolean invert = word.getAttributes().contains(
									"@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(word.getWord());
							sum = sum + wordpolarity;
							Element word_relation = doc
									.createElement("word-relation");

							Element relationElement = doc
									.createElement("relation");
							relationElement.appendChild(doc
									.createTextNode(relation.toString()));
							word_relation.appendChild(relationElement);

							Element wordelement = doc.createElement("word");
							wordelement.appendChild(doc.createTextNode(word
									+ "::" + wordpolarity));
							word_relation.appendChild(wordelement);

							words.appendChild(word_relation);
							System.out.println(sum + " " + word.getWord());
						}
					}
				}
				if (relation.toString().startsWith("mod")) {
					for (UniversalWord word : unl.getUwList()) {
						String relString = relation.toString();
						if (relation.toString()
								.substring(0, relString.length() - 2)
								.endsWith(word.toString())
						&& word.getAttributes().contains("@entry")) {
							boolean invert = word.getAttributes().contains(
									"@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(word.getWord());
							sum = sum + wordpolarity;
							Element word_relation = doc
									.createElement("word-relation");

							Element relationElement = doc
									.createElement("relation");
							relationElement.appendChild(doc
									.createTextNode(relation.toString()));
							word_relation.appendChild(relationElement);

							Element wordelement = doc.createElement("word");
							wordelement.appendChild(doc.createTextNode(word
									+ "::" + wordpolarity));
							word_relation.appendChild(wordelement);

							words.appendChild(word_relation);
							System.out.println(sum + " " + word.getWord());
						}
					}
				}
				if (relation.toString().startsWith("obj")) {
					for (UniversalWord word : unl.getUwList()) {
						if (relation.toString().contains(word.toString())
								&& word.getAttributes().contains("@entry")) {
							boolean invert = word.getAttributes().contains(
									"@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(word.getWord());
							sum = sum + wordpolarity;
							Element word_relation = doc
									.createElement("word-relation");

							Element relationElement = doc
									.createElement("relation");
							relationElement.appendChild(doc
									.createTextNode(relation.toString()));
							word_relation.appendChild(relationElement);

							Element wordelement = doc.createElement("word");
							wordelement.appendChild(doc.createTextNode(word
									+ "::" + wordpolarity));
							word_relation.appendChild(wordelement);

							words.appendChild(word_relation);
							System.out.println(sum + " " + word.getWord());
						}
					}
				}
				if (relation.toString().startsWith("aoj")) {
					for (UniversalWord word : unl.getUwList()) {
						String relString = relation.toString();
						if (relation.toString()
								.substring(6, relString.length() - 2)
								.startsWith(word.toString())
								&& word.getAttributes().contains("@entry")) {
							boolean invert = word.getAttributes().contains(
									"@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(word.getWord());
							sum = sum + wordpolarity;
							Element word_relation = doc
									.createElement("word-relation");

							Element relationElement = doc
									.createElement("relation");
							relationElement.appendChild(doc
									.createTextNode(relation.toString()));
							word_relation.appendChild(relationElement);

							Element wordelement = doc.createElement("word");
							wordelement.appendChild(doc.createTextNode(word
									+ "::" + wordpolarity));
							word_relation.appendChild(wordelement);

							words.appendChild(word_relation);
							System.out.println(sum + " " + word.getWord());
						}
					}
				}
			}
			test.appendChild(words);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Element sentiment = doc.createElement("sentiment");
			sentiment.appendChild(doc.createTextNode(""
					+ ((sum > 0) ? "POS" : (sum < 0) ? "NEG" : "OBJ")));

			test.appendChild(sentiment);

			Attr attr = doc.createAttribute("id");
			attr.setValue("" + id);
			test.setAttributeNode(attr);

			tests.appendChild(test);
			id++;
		}

		return (sum > 0) ? 1 : (sum < 0) ? -1 : 0;
	}

	public static String analyseSentenceSemanticallyEnhanced(String line) {
		return analyseSentenceSemanticallyEnhanced(line, null, false);
	}

	public static String analyseSentenceSemanticallyEnhanced(String l,
			String expected, boolean debug) {
		float sum = getSmileyFactor(l) * SMILEYWEIGHT;
		l = Preprocessor.preprocess(l);
		ArrayList<String> allines = new ArrayList<String>();
		String[] lines = l.split(" \\. ");
		for (String line : lines) {
			String[] newlines = line.split("!");
			for (String newline : newlines) {
				newline = newline.trim();
				if (newline.equals("") || newline.equals("!"))
					continue;
				else
					allines.add(newline);
			}
		}
		UNLExpression unl = null;
		StringBuffer buffer = new StringBuffer("");
		for (String line : allines) {
			Element test = null;
			if (debug) {
				test = doc.createElement("test");
			}
			try {
				if (debug) {
					Element sentence = doc.createElement("sentence");
					sentence.appendChild(doc.createTextNode(line));
					test.appendChild(sentence);

					Element expectedElement = doc.createElement("expected");
					expectedElement.appendChild(doc.createTextNode(expected));
					test.appendChild(expectedElement);
				}
				try {
					unl = UnlConverter.convert(line);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Element words = null;
				if (debug) {
					Element unlelement = doc.createElement("unl");
					Element relations = doc.createElement("relations");
					for (UNLRelation relation : unl.getRelationList()) {
						Element relationElement = doc.createElement("relation");
						relationElement.appendChild(doc.createTextNode(relation
								.toString()));
						relations.appendChild(relationElement);
					}
					unlelement.appendChild(relations);
					Element uws = doc.createElement("words");
					for (UniversalWord uw : unl.getUwList()) {
						Element wordElement = doc.createElement("word");
						wordElement.appendChild(doc.createTextNode(uw
								.toString()));
						uws.appendChild(wordElement);
					}
					unlelement.appendChild(uws);
					test.appendChild(unlelement);

					words = doc.createElement("words");
				}

				for (UNLRelation relation : unl.getRelationList()) {
					// first processing the agt relation
					if (relation.getRelation().startsWith("agt")) {
						// Process for source only
						if (/* relation.getScope() <= 1 */relation.getUw1().getAttributes().contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW1()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (debug) {
								Element word_relation = doc
										.createElement("word-relation");

								Element relationElement = doc
										.createElement("relation");
								relationElement.appendChild(doc
										.createTextNode(relation.toString()));
								word_relation.appendChild(relationElement);

								Element wordelement = doc.createElement("word");
								wordelement.appendChild(doc
										.createTextNode(relation.getW1()
												.getWord()
												+ "::"
												+ wordpolarity));
								word_relation.appendChild(wordelement);

								words.appendChild(word_relation);
							}
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW1().getCmWord()
													.getRoots()[0]);
						}
					}

					// Next we look for the obj relation
					else if (relation.getRelation().startsWith("obj")
					/* && relation.getScope() <= 1 */) {
						// Process for both source and target

						// Process for source first
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW1()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (debug) {
								Element word_relation = doc
										.createElement("word-relation");

								Element relationElement = doc
										.createElement("relation");
								relationElement.appendChild(doc
										.createTextNode(relation.toString()));
								word_relation.appendChild(relationElement);

								Element wordelement = doc.createElement("word");
								wordelement.appendChild(doc
										.createTextNode(relation.getW1()
												.getWord()
												+ "::"
												+ wordpolarity));
								word_relation.appendChild(wordelement);

								words.appendChild(word_relation);
							}
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW1().getCmWord()
													.getRoots()[0]);
						}
						// Target next
						if (relation.getUw2().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (debug) {
								Element word_relation = doc
										.createElement("word-relation");

								Element relationElement = doc
										.createElement("relation");
								relationElement.appendChild(doc
										.createTextNode(relation.toString()));
								word_relation.appendChild(relationElement);

								Element wordelement = doc.createElement("word");
								wordelement.appendChild(doc
										.createTextNode(relation.getW2()
												.getWord()
												+ "::"
												+ wordpolarity));
								word_relation.appendChild(wordelement);

								words.appendChild(word_relation);
							}
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}
					}

					// After that we look for aoj relation
					else if (relation.getRelation().startsWith("aoj")
					/* && relation.getScope() <= 2 */) {
						// Process for both source and target

						// Process for source first
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW1()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (debug) {
								Element word_relation = doc
										.createElement("word-relation");
								//
								Element relationElement = doc
										.createElement("relation");
								relationElement.appendChild(doc
										.createTextNode(relation.toString()));
								word_relation.appendChild(relationElement);
								//
								Element wordelement = doc.createElement("word");
								wordelement.appendChild(doc
										.createTextNode(relation.getW1()
												.getWord()
												+ "::"
												+ wordpolarity));
								word_relation.appendChild(wordelement);
								//

								words.appendChild(word_relation);
							}
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW1().getCmWord()
													.getRoots()[0]);
						}
						// Target UW
						if (relation.getUw2().getAttributes()
								.contains("@entry")) {

							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (debug) {
								Element word_relation = doc
										.createElement("word-relation");
								//
								Element relationElement = doc
										.createElement("relation");
								relationElement.appendChild(doc
										.createTextNode(relation.toString()));
								word_relation.appendChild(relationElement);
								//
								Element wordelement = doc.createElement("word");
								wordelement.appendChild(doc
										.createTextNode(relation.getW1()
												.getWord()
												+ "::"
												+ wordpolarity));
								word_relation.appendChild(wordelement);
								//
								words.appendChild(word_relation);
							}
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}

					}
					// After that we look for mod relation
					else if (relation.getRelation().startsWith("mod")
					/* && relation.getScope() <= 2 */) {
						// Process for both source and target

						// Consider target if source has attribute @entry
						if (relation.getUw1().getAttributes()
							 .contains("@entry") /*||
							 relation.getUw1().getAttributes()
							 .contains("@indef")*/) {
							// Source first
							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (debug) {
								Element word_relation = doc
										.createElement("word-relation");
								//
								Element relationElement = doc
										.createElement("relation");
								relationElement.appendChild(doc
										.createTextNode(relation.toString()));
								word_relation.appendChild(relationElement);

								Element wordelement = doc.createElement("word");
								wordelement.appendChild(doc
										.createTextNode(relation.getW2()
												.getWord()
												+ "::"
												+ wordpolarity));
								word_relation.appendChild(wordelement);
								//
								words.appendChild(word_relation);
							}
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}
					}
					// After that we look for man relation
					else if (relation.getRelation().startsWith("man")
					/* && relation.getScope() <= 2 */) {
						// Process for both source and target

						// Consider target if source has attribute @entry
						if (relation.getUw1().getAttributes()
								.contains("@entry")
								|| relation.getUw2().getAttributes()
										.contains("@entry")) {
							// Source first
							boolean invert = (relation.getUw2().getAttributes()
									.contains("@not") || relation.getUw1().getAttributes()
									.contains("@not")) ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (debug) {
								Element word_relation = doc
										.createElement("word-relation");
								//
								Element relationElement = doc
										.createElement("relation");
								relationElement.appendChild(doc
										.createTextNode(relation.toString()));
								word_relation.appendChild(relationElement);
								//
								Element wordelement = doc.createElement("word");
								wordelement.appendChild(doc
										.createTextNode(relation.getW2()
												.getWord()
												+ "::"
												+ wordpolarity));
								word_relation.appendChild(wordelement);
								//
								words.appendChild(word_relation);
							}
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}
					} else if (relation.getRelation().startsWith("and")
					/* && relation.getScope() <= 2 */) {
						// Process for both source and target

						// Consider target if source has attribute @entry
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW1()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (debug) {
								Element word_relation = doc
										.createElement("word-relation");
								//
								Element relationElement = doc
										.createElement("relation");
								relationElement.appendChild(doc
										.createTextNode(relation.toString()));
								word_relation.appendChild(relationElement);
								//
								Element wordelement = doc.createElement("word");
								wordelement.appendChild(doc
										.createTextNode(relation.getW2()
												.getWord()
												+ "::"
												+ wordpolarity));
								word_relation.appendChild(wordelement);
								//
								words.appendChild(word_relation);
							}
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW1().getCmWord()
													.getRoots()[0]);
						}

						// Consider Target UW if Source UW has attribute @entry
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {

							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (debug) {
								Element word_relation = doc
										.createElement("word-relation");
								//
								Element relationElement = doc
										.createElement("relation");
								relationElement.appendChild(doc
										.createTextNode(relation.toString()));
								word_relation.appendChild(relationElement);
								//
								Element wordelement = doc.createElement("word");
								wordelement.appendChild(doc
										.createTextNode(relation.getW1()
												.getWord()
												+ "::"
												+ wordpolarity));
								word_relation.appendChild(wordelement);
								//
								words.appendChild(word_relation);
							}
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}
					}
				}
				// test.appendChild(words);
			} catch (Exception e) {
				// Do nothing
			} finally {
				if (debug) {
					Element sentiment = doc.createElement("sentiment");
					sentiment.appendChild(doc.createTextNode(""
							+ ((sum > 0) ? "POS" : (sum < 0) ? "NEG" : "OBJ")));
					//
					test.appendChild(sentiment);
					//
					Attr attr = doc.createAttribute("id");
					attr.setValue("" + id);
					test.setAttributeNode(attr);
					//
					tests.appendChild(test);
					id++;
				}
			}
			if (unl != null) {
				for (UNLRelation relation : unl.getRelationList()) {
					buffer.append(relation.toString() + "<br/>");
				}
			}
		}
		System.out.println(unl);
		return buffer.toString() + Constants.SEPARATOR
				+ ((sum > 0) ? 1 : (sum < 0) ? -1 : 0);
	}

	private static float getSmileyFactor(String line) {
		String smileyPrediction = sh.getSmileyPrediction(line);
		if (smileyPrediction.equals("objective")) {
			return 0;
		} else if (smileyPrediction.equals("positive")) {
			return 1;
		} else if (smileyPrediction.equals("negative")) {
			return -1;
		} else {
			return 0;
		}
	}

	public static FeatureVector createFeatureVector(String l) {

		FeatureVector fv = new FeatureVector();

		float sum = getSmileyFactor(l) * SMILEYWEIGHT;
		fv.setSmileyprediction(sum);

		l = Preprocessor.preprocess(l);
		ArrayList<String> alllines = new ArrayList<String>();
		String[] lines = l.split(" \\. ");
		for (String line : lines) {
			String[] newlines = line.split("!");
			for (String newline : newlines) {
				newline = newline.trim();
				if (newline.equals("") || newline.equals("!"))
					continue;
				else
					alllines.add(newline);
			}
		}
		StringBuffer buffer = new StringBuffer("");
		for (String line : alllines) {
			UNLExpression unl = null;
			try {
				unl = UnlConverter.convert(line);

				for (UNLRelation relation : unl.getRelationList()) {
					// first processing the agt relation
					if (relation.getRelation().startsWith("agt")) {
						// Process for source only
						if (true) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW1()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (wordpolarity > 0)
								fv.setPos(fv.getPos() + 1);
							else if (wordpolarity < 0)
								fv.setNeg(fv.getNeg() + 1);
							else
								fv.setObj(fv.getObj() + 1);
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW1().getCmWord()
													.getRoots()[0]);
						}
					}

					// Next we look for the obj relation
					else if (relation.getRelation().startsWith("obj")) {
						// Process for both source and target

						// Process for source first
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW1()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (wordpolarity > 0)
								fv.setPos(fv.getPos() + 1);
							else if (wordpolarity < 0)
								fv.setNeg(fv.getNeg() + 1);
							else
								fv.setObj(fv.getObj() + 1);
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW1().getCmWord()
													.getRoots()[0]);
						}
						// Target next
						if (relation.getUw2().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (wordpolarity > 0)
								fv.setPos(fv.getPos() + 1);
							else if (wordpolarity < 0)
								fv.setNeg(fv.getNeg() + 1);
							else
								fv.setObj(fv.getObj() + 1);
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}
					}

					// After that we look for aoj relation
					else if (relation.getRelation().startsWith("aoj")) {
						// Process for both source and target

						// Process for source first
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW1()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (wordpolarity > 0)
								fv.setPos(fv.getPos() + 1);
							else if (wordpolarity < 0)
								fv.setNeg(fv.getNeg() + 1);
							else
								fv.setObj(fv.getObj() + 1);
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW1().getCmWord()
													.getRoots()[0]);
						}
						// Target UW
						if (relation.getUw2().getAttributes()
								.contains("@entry")) {

							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (wordpolarity > 0)
								fv.setPos(fv.getPos() + 1);
							else if (wordpolarity < 0)
								fv.setNeg(fv.getNeg() + 1);
							else
								fv.setObj(fv.getObj() + 1);
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}

					}
					// After that we look for mod relation
					else if (relation.getRelation().startsWith("mod")) {
						// Process for both source and target

						// Consider target if source has attribute @entry
						if (true) {
							// Source first
							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (wordpolarity > 0)
								fv.setPos(fv.getPos() + 1);
							else if (wordpolarity < 0)
								fv.setNeg(fv.getNeg() + 1);
							else
								fv.setObj(fv.getObj() + 1);
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}
					}
					// After that we look for man relation
					else if (relation.getRelation().startsWith("man")) {
						// Process for both source and target

						// Consider target if source has attribute @entry
						if (relation.getUw1().getAttributes()
								.contains("@entry")
								|| relation.getUw2().getAttributes()
										.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (wordpolarity > 0)
								fv.setPos(fv.getPos() + 1);
							else if (wordpolarity < 0)
								fv.setNeg(fv.getNeg() + 1);
							else
								fv.setObj(fv.getObj() + 1);
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}
					} else if (relation.getRelation().startsWith("and")) {
						// Process for both source and target
						// Consider target if source has attribute @entry
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW1()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (wordpolarity > 0)
								fv.setPos(fv.getPos() + 1);
							else if (wordpolarity < 0)
								fv.setNeg(fv.getNeg() + 1);
							else
								fv.setObj(fv.getObj() + 1);
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW1().getCmWord()
													.getRoots()[0]);
						}

						// Consider Target UW if Source UW has attribute @entry
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {

							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							int wordpolarity = (invert ? -1 : 1)
									* Polarities.getPolarity(relation.getW2()
											.getCmWord().getRoots()[0]
											.toLowerCase());
							sum = sum + wordpolarity;
							if (wordpolarity > 0)
								fv.setPos(fv.getPos() + 1);
							else if (wordpolarity < 0)
								fv.setNeg(fv.getNeg() + 1);
							else
								fv.setObj(fv.getObj() + 1);
							System.out
									.println(sum
											+ " "
											+ wordpolarity
											+ " "
											+ relation.getW2().getCmWord()
													.getRoots()[0]);
						}
					}
				}
			} catch (Exception e) {
			}
			if (unl != null) {
				for (UNLRelation relation : unl.getRelationList()) {
					buffer.append(relation.toString() + "<br/>");
				}
			}
		}
		fv.setUnlprediction(((sum > 0) ? 1 : (sum < 0) ? -1 : 0));
		return fv;
	}

	public static Map<String, Integer> getPolarityMap(String l) {

		int factor = 5;

		Map<String, Integer> polarityMap = new HashMap<String, Integer>();

		l = Preprocessor.preprocess(l);
		ArrayList<String> alllines = new ArrayList<String>();
		String[] lines = l.split(" \\. ");
		for (String line : lines) {
			String[] newlines = line.split("!");
			for (String newline : newlines) {
				newline = newline.trim();
				if (newline.equals("") || newline.equals("!"))
					continue;
				else
					alllines.add(newline);
			}
		}
		StringBuffer buffer = new StringBuffer("");
		for (String line : alllines) {
			UNLExpression unl = null;
			try {
				unl = UnlConverter.convert(line);

				for (UNLRelation relation : unl.getRelationList()) {
					// first processing the agt relation
					if (relation.getRelation().startsWith("agt")) {
						// Process for source only
						if (true) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							String word = relation.getW1().getCmWord()
									.getRoots()[0].toLowerCase();
							int wordpolarity = factor * (invert ? -1 : 1)
									* Polarities.getPolarity(word);
							if (polarityMap.containsKey(word))
								polarityMap.put(word, polarityMap.get(word)
										+ wordpolarity);
							else
								polarityMap.put(word, wordpolarity);
						}
					}

					// Next we look for the obj relation
					else if (relation.getRelation().startsWith("obj")) {
						// Process for both source and target

						// Process for source first
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							String word = relation.getW1().getCmWord()
									.getRoots()[0].toLowerCase();
							int wordpolarity = factor * (invert ? -1 : 1)
									* Polarities.getPolarity(word);
							if (polarityMap.containsKey(word))
								polarityMap.put(word, polarityMap.get(word)
										+ wordpolarity);
							else
								polarityMap.put(word, wordpolarity);
						}
						// Target next
						if (relation.getUw2().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							String word = relation.getW2().getCmWord()
									.getRoots()[0].toLowerCase();
							int wordpolarity = factor * (invert ? -1 : 1)
									* Polarities.getPolarity(word);
							if (polarityMap.containsKey(word))
								polarityMap.put(word, polarityMap.get(word)
										+ wordpolarity);
							else
								polarityMap.put(word, wordpolarity);
						}
					}

					// After that we look for aoj relation
					else if (relation.getRelation().startsWith("aoj")) {
						// Process for both source and target

						// Process for source first
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							String word = relation.getW1().getCmWord()
									.getRoots()[0].toLowerCase();
							int wordpolarity = factor * (invert ? -1 : 1)
									* Polarities.getPolarity(word);
							if (polarityMap.containsKey(word))
								polarityMap.put(word, polarityMap.get(word)
										+ wordpolarity);
							else
								polarityMap.put(word, wordpolarity);
						}
						// Target UW
						if (relation.getUw2().getAttributes()
								.contains("@entry")) {

							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							String word = relation.getW2().getCmWord()
									.getRoots()[0].toLowerCase();
							int wordpolarity = factor * (invert ? -1 : 1)
									* Polarities.getPolarity(word);
							if (polarityMap.containsKey(word))
								polarityMap.put(word, polarityMap.get(word)
										+ wordpolarity);
							else
								polarityMap.put(word, wordpolarity);
						}

					}
					// After that we look for mod relation
					else if (relation.getRelation().startsWith("mod")) {
						// Process for both source and target

						// Consider target if source has attribute @entry
						if (true) {
							// Source first
							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							String word = relation.getW2().getCmWord()
									.getRoots()[0].toLowerCase();
							int wordpolarity = factor * (invert ? -1 : 1)
									* Polarities.getPolarity(word);
							if (polarityMap.containsKey(word))
								polarityMap.put(word, polarityMap.get(word)
										+ wordpolarity);
							else
								polarityMap.put(word, wordpolarity);
						}
					}
					// After that we look for man relation
					else if (relation.getRelation().startsWith("man")) {
						// Process for both source and target

						// Consider target if source has attribute @entry
						if (relation.getUw1().getAttributes()
								.contains("@entry")
								|| relation.getUw2().getAttributes()
										.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							String word = relation.getW2().getCmWord()
									.getRoots()[0].toLowerCase();
							int wordpolarity = factor * (invert ? -1 : 1)
									* Polarities.getPolarity(word);
							if (polarityMap.containsKey(word))
								polarityMap.put(word, polarityMap.get(word)
										+ wordpolarity);
							else
								polarityMap.put(word, wordpolarity);
						}
					} else if (relation.getRelation().startsWith("and")) {
						// Process for both source and target
						// Consider target if source has attribute @entry
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {
							// Source first
							boolean invert = relation.getUw1().getAttributes()
									.contains("@not") ? true : false;
							String word = relation.getW1().getCmWord()
									.getRoots()[0].toLowerCase();
							int wordpolarity = factor * (invert ? -1 : 1)
									* Polarities.getPolarity(word);
							if (polarityMap.containsKey(word))
								polarityMap.put(word, polarityMap.get(word)
										+ wordpolarity);
							else
								polarityMap.put(word, wordpolarity);
						}

						// Consider Target UW if Source UW has attribute @entry
						if (relation.getUw1().getAttributes()
								.contains("@entry")) {

							boolean invert = relation.getUw2().getAttributes()
									.contains("@not") ? true : false;
							String word = relation.getW2().getCmWord()
									.getRoots()[0].toLowerCase();
							int wordpolarity = factor * (invert ? -1 : 1)
									* Polarities.getPolarity(word);
							if (polarityMap.containsKey(word))
								polarityMap.put(word, polarityMap.get(word)
										+ wordpolarity);
							else
								polarityMap.put(word, wordpolarity);
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return polarityMap;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		System.out.print(">");
		while (!(line = br.readLine()).equals("quit")) {
			System.out.println(SentimentAnalyser
					.analyseSentenceSemanticallyEnhanced(line));
			System.out.print(">");
		}
	}
}
