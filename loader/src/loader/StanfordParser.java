package loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Shared.SerializedTree;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

public class StanfordParser extends Thread{

	LexicalizedParser lp;
	
	public StanfordParser(){
	}
	
	public void run(){

		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		System.out.println("Parser loaded ... \n");

		
		int servPort = 29998;
		Socket clntSock = null;


		try{
	      // Create a server socket to accept client connection requests
			ServerSocket servSock = new ServerSocket(servPort);
	
			while(true) 
			{ // Run forever, accepting and servicing connections
				clntSock = servSock.accept();     // Get client connection
		
//				System.out.println("Handling client of parser at " + 
//						clntSock.getInetAddress().getHostAddress() + " on port " +
//						clntSock.getPort());
		
				InputStream in = clntSock.getInputStream();
				OutputStream out = clntSock.getOutputStream();
				String sentence = "";

/*
				// Receive until client closes connection, indicated by -1 return
				while ((size = in.read(inputByteBuffer)) != -1){
					String s = new String(inputByteBuffer);
					System.out.print("\n"+ s + "\t" + size);
					sentence = sentence + s.substring(0, size);
					if(size<BUFSIZE)
						break;
				}
*/
				
				ObjectInputStream ois = new ObjectInputStream(in);
				Object o = ois.readObject();
				sentence = (String)o;
				
				System.out.println("\n Parsing "+ sentence);
				
			    String[] sent = sentence.split("[ ]+");
			   
			    ArrayList<StanfordWord> wordList = new ArrayList<StanfordWord>();
			    for(String s : sent){
//			    	System.out.print(s + " ");
			    	StanfordWord word = new StanfordWord();
			    	word.setWord(s);
			    	wordList.add(word);
			    }

//			    System.out.println("\n Before parsing : " + new Date());
			    //lp.apply(wordList);
//			    lp.parse(sentence);
			    Tree parse = lp.apply(wordList);
//			    Tree parse = (Tree) lp.apply(Arrays.asList(sent));
//			    System.out.println("Sentence parsed : " + new Date());

//			    System.out.println(parse.pennString());
//			    System.out.println(parse.dependencies());

			    out.flush();
			    ObjectOutputStream oos = new ObjectOutputStream(out);
				oos.writeObject(new SerializedTree(parse));
				oos.flush();
				// close streams and connections
				oos.close();
			    
				clntSock.close();  // Close the socket.  We are done with this client!
			}
	  }catch(Exception e){
		  try {
			  if(clntSock!=null && !clntSock.isClosed())
				  clntSock.close();
		  } catch (IOException e1) {
			  // TODO Auto-generated catch block
			  e1.printStackTrace();
		  }
		  e.printStackTrace();
	  }
	  finally{
//		  this.destroy();
		  
	  }

		
	}
}
