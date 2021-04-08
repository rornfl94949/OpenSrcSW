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
				System.out.println("Rank " + Rank +"���� "+ title_list[i] +"�Դϴ�.! ���絵�� " + weigh_list[i]+ "�Դϴ�.!");
				Rank++;
			}
		}
		for(int i = 0; i < Rank_list.length; i++) {
			if(Rank == 4)
				break;
			if(Rank_list[i] == 2) {
				System.out.println("Rank " + Rank +"���� "+ title_list[i] +"�Դϴ�.! ���絵�� " + weigh_list[i]+ "�Դϴ�.!");
				Rank++;
			}
		}
		for(int i = 0; i < Rank_list.length; i++) {
			if(Rank == 4)
				break;
			if(Rank_list[i] == 3) {
				System.out.println("Rank " + Rank +"���� "+ title_list[i] +"�Դϴ�.! ���絵�� " + weigh_list[i]+ "�Դϴ�.!");
				Rank++;
			}
		}
		for(int i = 0; i < Rank_list.length; i++) {
			if(Rank == 4)
				break;
			if(Rank_list[i] == 4) {
				System.out.println("Rank " + Rank +"���� "+ title_list[i] +"�Դϴ�.! ���絵�� " + weigh_list[i]+ "�Դϴ�.!");
				Rank++;
			}
		}
		for(int i = 0; i < Rank_list.length; i++) {
			if(Rank == 4)
				break;
			if(Rank_list[i] == 5) {
				System.out.println("Rank " + Rank +"���� "+ title_list[i] +"�Դϴ�.! ���絵�� " + weigh_list[i]+ "�Դϴ�.!");
				Rank++;
			}
		}
	}
	
	public static Double[] CalcSim(String query, String path,int doc_size) throws IOException, ClassNotFoundException {
		FileInputStream fileStream = new FileInputStream(path);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		HashMap h_index = (HashMap)object;
		
		double [] inner_pro = new double[doc_size];
		for(int i =0; i < doc_size; i++) {
			inner_pro[i] = 0.0;
		}
		double [][] Denomi = new double[2][doc_size]; //�и��� ��Ʈ ���� 2���� �и� ��Ų��
		for(int i =0; i < doc_size; i++) {
			Denomi[0][i] = 0.0;
			Denomi[1][i] = 0.0;
		}
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		for( int i = 0 ; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			if(h_index.containsKey(kwrd.getString())){
				for(int j = 0; j < doc_size ; j++) {
					Denomi[0][j] += (1*1);
				} //Ű���尡 ������ �� Ű���忡 ���� ����ġ 1�� �������� �ϴ� �� ��
				Object temp = h_index.get(kwrd.getString());
				double[] w_arr = (double[])temp;
				for(int j = 0; j < w_arr.length/2; j++) {
					int index = (int) Math.round(w_arr[2*j]);
					inner_pro[index] += w_arr[2*j +1];
					Denomi[1][index] += Math.pow(w_arr[2*j +1],2); 
				}
			}else {
				for(int j = 0; j < doc_size ; j++) {
					Denomi[0][j] += (1*1); //Ű���尡 hashmap�� �������� �ʴ´ٸ� ��� ������ �� Ű���忡 ���� ����ġ�� 0������ �и𿡴� �� Ű���忡 ���� ����ġ�� 1*1�� ��
				}
			}
		}
		Double[] weigh_list = new Double[doc_size];
		for(int i =0; i < weigh_list.length; i++) {
			weigh_list[i] = (inner_pro[i]) / (Math.sqrt(Denomi[0][i])*Math.sqrt(Denomi[1][i]));
		}
		for(int i = 0; i < weigh_list.length; i++) {
			weigh_list[i] =  Double.parseDouble(String.format("%.3f",weigh_list[i])); // ���� weigh_list�� �ݿø�����.
		}
		return weigh_list;
	}
}