package assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class searcher {
	public static void searcher(String path, String query) throws ClassNotFoundException, IOException, ParserConfigurationException, SAXException {
		File file = new File("./collection.xml");
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		Document doc = docBuild.parse(file);
		doc.getDocumentElement().normalize();
		NodeList docList = doc.getElementsByTagName("doc");
		String[] title_list = new String[docList.getLength()];
		for(int i =0; i < docList.getLength(); i++) {
			Node docNode = docList.item(i);
			Element docElmnt = (Element) docNode;
			NodeList titleList= docElmnt.getElementsByTagName("title");
			Element titleElmnt = (Element) titleList.item(0);
			Node title = titleElmnt.getFirstChild();
			title_list[i] = title.getNodeValue();
		}
		Double[] weigh_list = CalcSim(query,path,title_list.length);	
		int[] Rank_list = new int[weigh_list.length];
		for(int i =0; i < Rank_list.length; i ++) {
			Rank_list[i] = 0;
		}
		for(int i =0; i < weigh_list.length; i++) {
			for(int j = 0; j < weigh_list.length; j++) {
				if(weigh_list[i] <= weigh_list[j]) {
					Rank_list[i]++;
				}
			}
		}
		int Rank = 1;
		for(int i = 0; i < Rank_list.length; i++) {
			if(Rank == 4)
				break;
			if(Rank_list[i] == 1) {
				System.out.println("Rank " + Rank +"위는 "+ title_list[i] +"입니다.! 유사도는 " + weigh_list[i]+ "입니다.!");
				Rank++;
			}
		}
		for(int i = 0; i < Rank_list.length; i++) {
			if(Rank == 4)
				break;
			if(Rank_list[i] == 2) {
				System.out.println("Rank " + Rank +"위는 "+ title_list[i] +"입니다.! 유사도는 " + weigh_list[i]+ "입니다.!");
				Rank++;
			}
		}
		for(int i = 0; i < Rank_list.length; i++) {
			if(Rank == 4)
				break;
			if(Rank_list[i] == 3) {
				System.out.println("Rank " + Rank +"위는 "+ title_list[i] +"입니다.! 유사도는 " + weigh_list[i]+ "입니다.!");
				Rank++;
			}
		}
		for(int i = 0; i < Rank_list.length; i++) {
			if(Rank == 4)
				break;
			if(Rank_list[i] == 4) {
				System.out.println("Rank " + Rank +"위는 "+ title_list[i] +"입니다.! 유사도는 " + weigh_list[i]+ "입니다.!");
				Rank++;
			}
		}
		for(int i = 0; i < Rank_list.length; i++) {
			if(Rank == 4)
				break;
			if(Rank_list[i] == 5) {
				System.out.println("Rank " + Rank +"위는 "+ title_list[i] +"입니다.! 유사도는 " + weigh_list[i]+ "입니다.!");
				Rank++;
			}
		}
	}
	
	public static Double[] InnerProduct(String query, String path,int doc_size) throws IOException, ClassNotFoundException {
		FileInputStream fileStream = new FileInputStream(path);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		HashMap h_index = (HashMap)object;
		
		Double[] InnerProduct_list = new Double[doc_size];
		for(int i =0; i < InnerProduct_list.length; i++) {
			InnerProduct_list[i] = 0.0;
		}
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		for( int i = 0 ; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			if(h_index.containsKey(kwrd.getString())){
				Object temp = h_index.get(kwrd.getString());
				double[] w_arr = (double[])temp;
				for(int j = 0; j < w_arr.length/2; j++) {
					int index = (int) Math.round(w_arr[2*j]);
					InnerProduct_list[index] += w_arr[2*j +1];
				}
			}
		}
		for(int i = 0; i < InnerProduct_list.length; i++) {
			InnerProduct_list[i] =  Double.parseDouble(String.format("%.3f",InnerProduct_list[i]));
		}
		return InnerProduct_list;
	}
}
