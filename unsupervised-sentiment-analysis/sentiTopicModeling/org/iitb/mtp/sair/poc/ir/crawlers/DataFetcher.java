package org.iitb.mtp.sair.poc.ir.crawlers;

import java.io.IOException;

public interface DataFetcher {
	Object fetch(String entity) throws IOException;
}
