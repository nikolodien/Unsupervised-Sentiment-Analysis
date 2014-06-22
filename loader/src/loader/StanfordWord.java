package loader;

import edu.stanford.nlp.ling.HasWord;

public class StanfordWord implements HasWord{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String word;
	
	@Override
	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public String word() {
		return word;
	}
	
}
