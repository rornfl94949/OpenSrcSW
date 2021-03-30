package assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {

	public static void indexer(String path) throws IOException, ParserConfigurationException, SAXException {
		FileOutputStream O_fileStream = new FileOutputStream("./index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(O_fileStream);
		HashMap<String, double[]> h_index = new HashMap();
		
		File file = new File(path);
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		Document doc = docBuild.parse(file);
		doc.getDocumentElement().normalize();
		NodeList docList = doc.getElementsByTagName("doc");
		
		String[] id_list = new String[docList.getLength()];
		String[] body_list = new String[docList.getLength()];
		
		for (int i = 0; i < docList.getLength(); i++) {
			Node docNode = docList.item(i);
			if (docNode.getNodeType() == Node.ELEMENT_NODE) {
				// doc 엘리먼트 
				Element docElmnt = (Element) docNode;
				id_list[i] = docElmnt.getAttribute("id");
				
				// body 태그
				NodeList bodyList= docElmnt.getElementsByTagName("body");
				Element bodyElmnt = (Element) bodyList.item(0);
				Node body = bodyElmnt.getFirstChild();
				body_list[i] = body.getNodeValue();
			}
		}
		String [][][] arr = new String[id_list.length][2][];
		int[] arr_index = new int[id_list.length];
		for (int i = 0; i < id_list.length; i++) {
			int check = 0;
			int index_1 = 0;
			int index_2 = 0;
			while((index_1 - 1) != body_list[i].lastIndexOf('#')) {
				index_2 =  body_list[i].indexOf(':',index_1);
				index_1 = body_list[i].indexOf('#',index_2) + 1;
				check++;
			}
			arr[i][0] = new String[check];
			arr[i][1] = new String[check];
		}	
		for (int i = 0; i < id_list.length; i++) {
			int check = 0;
			int index_1 = 0;
			int index_2 = 0;
			int index_3 = 0;
			while((index_1 - 1) != body_list[i].lastIndexOf('#')) {
				index_2 = body_list[i].indexOf(':',index_1);
				index_3 = body_list[i].indexOf('#',index_2);
				String key = body_list[i].substring(index_1,index_2);
				String value = body_list[i].substring(index_2+1,index_3);
				arr[i][0][check] = key;
				arr[i][1][check] = value;
				index_1 = body_list[i].indexOf('#',index_2) + 1;
				check++;
			}
		}	
		for (int i = 0; i < id_list.length; i++) {
			for(int j = 0; j < arr[i][0].length; j++) {
				if(h_index.containsKey(arr[i][0][j]))continue;
				else if(arr[i][0][j] != null){
					double[] arr2 = new double[2*id_list.length];
					arr2[0] = i;
					arr2[1] = Double.parseDouble(arr[i][1][j]);
					int index = 1;
					for(int k = i+1; k < id_list.length; k++) {
						for(int l = 0; l < arr[k][0].length; l++) {
							if(arr[i][0][j].equals(arr[k][0][l])) {
								arr2[2*index] = k;
								arr2[(2*index) + 1] = Double.parseDouble(arr[k][1][l]);
								index++;
								break;
							}
						}
					}
					double[] arr3 = new double[2*index];
					for(int k = 0; k < 2*index; k++) {
						arr3[k] = arr2[k];
					}
					double w = (double)(id_list.length)/(double)(arr3.length/2);
					for(int k = 0; k < arr3.length/2; k++) {
						arr3[2*k+1] = Double.parseDouble(String.format("%.2f", arr3[2*k+1] * Math.log(w)));
					}
					h_index.put(arr[i][0][j], arr3);
				}
			}
		}
		objectOutputStream.writeObject(h_index);
		objectOutputStream.close();
	}
}
