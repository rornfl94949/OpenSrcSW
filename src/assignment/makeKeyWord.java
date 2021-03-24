package assignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class makeKeyWord {
		public static void makeKeyWord(String path) throws ParserConfigurationException, SAXException, IOException, TransformerException {
				File file = new File(path);
				DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
				
				KeywordExtractor ke = new KeywordExtractor();
				
				Document n_docu = docBuild.newDocument();
		        n_docu.setXmlStandalone(true); 
		        Element n_docs = n_docu.createElement("docs");
		        n_docu.appendChild(n_docs);
		        
				Document doc = docBuild.parse(file);
				
				doc.getDocumentElement().normalize();
	 
				// docs 엘리먼트 리스트
				NodeList docList = doc.getElementsByTagName("doc");
				
				for (int i = 0; i < docList.getLength(); i++) {
					Element n_doc = n_docu.createElement("doc");
					n_docs.appendChild(n_doc);
					n_doc.setAttribute("id", Integer.toString(i));
					
					Node docNode = docList.item(i);
					
					if (docNode.getNodeType() == Node.ELEMENT_NODE) {
						// doc 엘리먼트 
						Element docElmnt = (Element) docNode;
	 
						// title 태그
						NodeList titleList= docElmnt.getElementsByTagName("title");
						Element titleElmnt = (Element) titleList.item(0);
						Node title = titleElmnt.getFirstChild();
						
						Element n_title = n_docu.createElement("title");
						n_title.appendChild(n_docu.createTextNode(title.getNodeValue()));
						n_doc.appendChild(n_title);

						// body 태그
						NodeList bodyList= docElmnt.getElementsByTagName("body");
						Element bodyElmnt = (Element) bodyList.item(0);
						Node body = bodyElmnt.getFirstChild();
						
						String kkma_body = "";
						KeywordList kl = ke.extractKeyword(body.getNodeValue(), true);
						for( int j = 0 ; j < kl.size(); j++) {
							Keyword kwrd = kl.get(j);
							kkma_body += kwrd.getString()+":"+kwrd.getCnt()+"#";
						}
						
						Element n_body = n_docu.createElement("body");
						n_body.appendChild(n_docu.createTextNode(kkma_body));
						n_doc.appendChild(n_body);
					}
				}
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
				
				DOMSource source = new DOMSource(n_docu);
				StreamResult result = new StreamResult(new FileOutputStream(new File( "./index.xml")));
				
				transformer.transform(source, result);
		}
}