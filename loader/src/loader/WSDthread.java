package loader;

import in.ac.iitb.cfilt.common.StopWords;
import in.ac.iitb.cfilt.common.config.AppProperties;
import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.interfaces.SynsetFetcher;
import in.ac.iitb.cfilt.wsd.sup.data.Algorithms;
import in.ac.iitb.cfilt.wsd.sup.engine.SupervisedWSD;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class WSDthread extends Thread {
	SupervisedWSD supWSD ; 

	public WSDthread(){
	}
	
	public void run(){

		PrintStream os = System.out;
		try{
			AppProperties.load("properties/Common.properties");
			AppProperties.load("properties/SupWSD.properties");
			SynsetFetcher.initialize();
			StopWords.load(AppProperties.getProperty("stop.words.base.dir"));
			String sourceLanguage = AppProperties.getProperty("source.language"); 
			supWSD = new SupervisedWSD(Language.getLanguage(sourceLanguage));
			supWSD.load(1);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.setOut(os);
		System.out.println("WSD loaded ... \n");

		
		int servPort = 29997;


		try{
	      // Create a server socket to accept client connection requests
			ServerSocket servSock = new ServerSocket(servPort);
	
	
			while(true) 
			{ // Run forever, accepting and servicing connections
				Socket clntSock = servSock.accept();     // Get client connection
		
//				System.out.println("Handling client of wsd at " + 
//						clntSock.getInetAddress().getHostAddress() + " on port " +
//						clntSock.getPort());
		
				InputStream in = clntSock.getInputStream();
				OutputStream out = clntSock.getOutputStream();

				
/**
 * Performing WSD
 */
				Vector<in.ac.iitb.cfilt.as.data.TaggedWord> taggedSentence = new Vector<in.ac.iitb.cfilt.as.data.TaggedWord>();
				
				ObjectInputStream ois = new ObjectInputStream(in);
				taggedSentence = (Vector<in.ac.iitb.cfilt.as.data.TaggedWord>) ois.readObject();

			    System.out.println("\n WSD tagging : " + taggedSentence);
				try 
				{
					supWSD.tagSentence(taggedSentence, Algorithms.WN_PLUS_PLUS_ITERATIVE_WSD);
					for(int i=0;i<taggedSentence.size();i++){
						in.ac.iitb.cfilt.as.data.TaggedWord w = taggedSentence.get(i);
					}
				} 
				catch (Exception e){
					e.printStackTrace();
				}
//			    System.out.println("After WSD  tagging : " + new Date());

			    out.flush();
			    ObjectOutputStream oos = new ObjectOutputStream(out);
			    oos.writeObject(taggedSentence);
				oos.flush();
				// close streams and connections

				oos.close();
				ois.close();
				clntSock.close();  // Close the socket.  We are done with this client!
			}
	  }catch(Exception e){
		  e.printStackTrace();
	  }

	}
}
