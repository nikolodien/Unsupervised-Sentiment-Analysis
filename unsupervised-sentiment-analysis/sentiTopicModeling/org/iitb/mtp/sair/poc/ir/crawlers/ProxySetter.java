package org.iitb.mtp.sair.poc.ir.crawlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

public class ProxySetter {

	private final static String host = "netmon.iitb.ac.in";
	private final static Integer port = 80;
	private static String username;
	private static String password;

	static {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					"resources/proxy.txt")));
			username = reader.readLine().split("=")[1];
			password = reader.readLine().split("=")[1];
		} catch (FileNotFoundException e) {
			// TODO replace with logging
			System.out.println("Error opening file");
		} catch (IOException e) {
			// TODO replace with logging
			System.out.println("Error reading from file");
		}
	}

	static Proxy getProxy() {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host,
				port));
		Authenticator authenticator = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication(username,
						password.toCharArray()));
			}
		};
		Authenticator.setDefault(authenticator);
		return proxy;
	}
}
