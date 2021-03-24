package assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {
	public static void makeCollection(String directory) throws ParserConfigurationException, DOMException, IOException, TransformerException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        Document docu = docBuilder.newDocument();
        docu.setXmlStandalone(true); 
        Element docs = docu.createElement("docs");
        docu.appendChild(docs);
        
		String path = directory;
		File dir = new File(path);
		File []fileList = dir.listFiles();
		
		int tag_index, count = 0;
		
		for(File file : fileList) {
			if(file.isFile()) {
				
				String fileName = file.getName();
				
				FileInputStream input=new FileInputStream(path + "\\" + fileName);
				InputStreamReader reader=new InputStreamReader(input,"UTF-8");
				BufferedReader br  =  new BufferedReader(reader);
				String line = null;
				String docs_body = "";
				
	            Element doc = docu.createElement("doc");					
	            docs.appendChild(doc);
	            doc.setAttribute("id",  Integer.toString(count)); 			
	 
				while((line=br.readLine())!=null){
					
					tag_index = line.indexOf("<");
					
					if(line.substring(tag_index,tag_index+2).equals("<t")) {
						String textWithoutTag = line.substring(tag_index+7, line.indexOf("</"));
			            Element title = docu.createElement("title");				
			            title.appendChild(docu.createTextNode(textWithoutTag));
			            doc.appendChild(title);
			           
					}else if(line.substring(tag_index,tag_index+2).equals("<p")){
						String textWithoutTag = line.substring(tag_index+3, line.indexOf("</"));
						docs_body += textWithoutTag + " ";
					}
				}
	            Element body = docu.createElement("body");					
	            body.appendChild(docu.createTextNode(docs_body));
	            doc.appendChild(body);
	            
				count++;
			}
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		 
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); 
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); 

        DOMSource source = new DOMSource(docu);
        StreamResult result = new StreamResult(new FileOutputStream(new File("./collection.xml")));

        transformer.transform(source, result);	
	}
}
