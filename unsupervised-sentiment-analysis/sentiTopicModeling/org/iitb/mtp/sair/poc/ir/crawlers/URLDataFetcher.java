package org.iitb.mtp.sair.poc.ir.crawlers;

import java.io.IOException;

import org.json.simple.JSONObject;

public interface URLDataFetcher extends DataFetcher {
	@Override
	JSONObject fetch(String entity) throws IOException;
}
