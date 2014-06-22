package loader;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StanfordNER ner = new StanfordNER();
		StanfordParser parser = new StanfordParser();
		WSDthread wsd = new WSDthread();
		
		ner.start();
		parser.start();
		wsd.start();
		
		try{
			while(true){
				if(!ner.isAlive()){
					ner = new StanfordNER();
					ner.start();
				}
				if(!parser.isAlive()){
					parser = new StanfordParser();
					parser.start();
				}
				if(!wsd.isAlive()){
					wsd = new WSDthread();
					wsd.start();
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
