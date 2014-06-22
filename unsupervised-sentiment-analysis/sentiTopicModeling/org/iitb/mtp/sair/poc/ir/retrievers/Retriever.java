package org.iitb.mtp.sair.poc.ir.retrievers;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public interface Retriever {
	public void doPagingSearch(BufferedReader in,
			IndexSearcher searcher, Query query, int hitsPerPage, boolean raw,
			boolean interactive, int sentiment) throws IOException;
}
