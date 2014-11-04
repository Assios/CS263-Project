package cs263;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class JsonReader {
	
	static String swap(String url) {
		//Swap whitespace with "%20" so that the URL works
		String[] array = url.split("");
		
		String returnString = "";
		
		for (int i = 0; i < array.length; i++) {
			if (array[i].contains(" ")) {
				array[i] = "%20";
			}
			returnString += array[i];
		}
		
		return returnString;
	}
	
    static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read); 

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

}
