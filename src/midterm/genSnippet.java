package midterm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class genSnippet {
	public static void genSnippet(String directory, String text) throws IOException {
		FileInputStream input=new FileInputStream("./" +directory);
		InputStreamReader reader=new InputStreamReader(input,"UTF-8");
		BufferedReader br  =  new BufferedReader(reader);
		String line = null;
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(text, true);
		String[] arr =new String[kl.size()];
		for( int j = 0 ; j < kl.size(); j++) {
			Keyword kwrd = kl.get(j);
			arr[j] = kwrd.getString();
		}
		int best = 0;
		int index = 0;
		int max = 0;
		while((line=br.readLine())!=null) {
			index ++;
			KeywordList kl2 = ke.extractKeyword(line, true);
			String[] arr2 =new String[kl.size()];
			int ch = 0;
			for( int j = 0 ; j < kl2.size(); j++) {
				Keyword kwrd2 = kl2.get(j);
				for(int i =0; i < arr.length; i++) {
					if(kwrd2.getString().equals(arr[i]))
						ch++;
				}
			}
			if(ch > max) {
				max = ch;
				best = index;
			}
		}
		FileInputStream input2=new FileInputStream("./" + directory);
		InputStreamReader reader2=new InputStreamReader(input,"UTF-8");
		BufferedReader br2  =  new BufferedReader(reader);
		String line2 = null;
		index = 0;
		while((line=br.readLine())!=null) {
			index ++;
			if(index == best) {
				System.out.println(line);
			}
		}
	}
}
