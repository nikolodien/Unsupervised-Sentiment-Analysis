package Shared;

import java.io.Serializable;

import edu.stanford.nlp.trees.Tree;

public class SerializedTree implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	public Tree parse;

	public SerializedTree(Tree p){
		parse = p;
	}

}
