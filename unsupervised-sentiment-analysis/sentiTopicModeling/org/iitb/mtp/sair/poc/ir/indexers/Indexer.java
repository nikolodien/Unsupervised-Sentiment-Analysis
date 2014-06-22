package org.iitb.mtp.sair.poc.ir.indexers;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.IndexWriter;

public interface Indexer {
	void indexDocs(IndexWriter writer, File file) throws IOException;
}
