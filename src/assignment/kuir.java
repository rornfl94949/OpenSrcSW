package assignment;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws DOMException, ParserConfigurationException, IOException, TransformerException, SAXException, ClassNotFoundException {
		// TODO Auto-generated method stub
		if (args[0].equals("-c")) {
			String directory = args[1];
			makeCollection.makeCollection(directory);
		}else if (args[0].equals("-k")) {
			String path = args[1];
			makeKeyWord.makeKeyWord(path);
		}else if (args[0].equals("-i")) {
			String path = args[1];
			indexer.indexer(path);
		}
	}

}