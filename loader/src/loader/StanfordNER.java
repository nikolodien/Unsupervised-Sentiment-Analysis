package loader;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;


public class StanfordNER extends Thread{

	AbstractSequenceClassifier classifier;
	
	public StanfordNER(){
	}
	
	public void run(){

		String loadPath = "classifiers/english.all.3class.distsim.crf.ser.gz";
		classifier = CRFClassifier.getClassifierNoExceptions(loadPath);
		System.out.println("NER loaded ... \n");

		
		int servPort = 29999;


		try{
	      // Create a server socket to accept client connection requests
			ServerSocket servSock = new ServerSocket(servPort);
	
			while(true) 
			{ // Run forever, accepting and servicing connections
				Socket clntSock = servSock.accept();     // Get client connection
		
//				System.out.println("Handling client of ner at " + 
//						clntSock.getInetAddress().getHostAddress() + " on port " +
//						clntSock.getPort());
		
				InputStream in = clntSock.getInputStream();
				OutputStream out = clntSock.getOutputStream();
				String sentence = "";

				// Receive until client closes connection, indicated by -1 return
				ObjectInputStream ois = new ObjectInputStream(in);
				Object o = ois.readObject();
				sentence = (String)o;
				
				System.out.println("\n NE tagging : "+ sentence);
				sentence = sentence.replace("  ", " ");
//				System.out.println("\n\n"+ sentence);
				String tagged = classifier.classifyWithInlineXML(sentence);
//				System.out.println("\n\n"+ tagged);

				out.flush();
			    ObjectOutputStream oos = new ObjectOutputStream(out);
				oos.writeObject(tagged);
				oos.flush();
				
				
				clntSock.close();  // Close the socket.  We are done with this client!
			}
	  }catch(Exception e){
		  e.printStackTrace();
	  }

	}
}
