package midterm;

import java.io.IOException;

public class midterm {

	public static void main(String[] args) throws IOException  {
		// TODO Auto-generated method stub
		if (args[0].equals("-f")) {
			String directory = args[1];
			String text = args[3];
			genSnippet.genSnippet(directory, text);
			
		}
	}

}
