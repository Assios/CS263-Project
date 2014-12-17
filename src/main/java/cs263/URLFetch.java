package cs263;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class URLFetch {
	//Fetches data from URL
	static String swap(String url) {
		//Swap whitespace with "%20" so that the URL works
		String[] array = url.split("");

		String returnString = "";

		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(" ")) {
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
			if (reader != null) reader.close();
		}
	}

	/**
	 * @throws IOException
	 */
	
	static void addTo(String datastore, String url) throws IOException {
		//Adds all movies from an URL to Datastore
		//First generate a String[] with all the ids on an URL
		
		HttpServletResponse resp = null;
		
		boolean duplicate = false;
		String out;
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        out = response.toString();
                
        List<String> matches = new ArrayList<String>();
		
		Pattern p = Pattern.compile("/title/(.+?)/?ref");
		Matcher m = p.matcher(out);

		while(m.find()) {
		    matches.add(m.group(1));
		}
		
		//Remove duplicates
		HashSet hs = new HashSet();
		hs.addAll(matches);
		matches.clear();
		matches.addAll(hs);
		
		UserService userService = UserServiceFactory.getUserService();
		User current_user = userService.getCurrentUser();
		
		String title = null;
		int year = 0;
		int rank = 0;
		String director = null;
		String genre = null;
		String rating = null;
		String id = null;
		String json_data = null;
		String user = null;
		
		if (current_user != null) {
			user = current_user.getNickname();
		} else user = "Anonymous";
		
		for (String s : matches)  {
			duplicate = false;
			s = s.substring(0, 9);
			
			try {
				json_data = URLFetch.readUrl("http://www.omdbapi.com/?i=" + s);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			Gson gson = new Gson();
			HashMap < String, String > movie_info = new Gson().fromJson(json_data, new TypeToken < HashMap < String, String >> () {}.getType());

			title = movie_info.get("Title");
			year = Integer.valueOf(movie_info.get("Year"));
			director = movie_info.get("Director");
			genre = movie_info.get("Genre");
			rating = movie_info.get("imdbRating");
			id = s;

			Key movieKey = KeyFactory.createKey(datastore, title);
			Entity movie = new Entity(datastore, movieKey);
			movie.setProperty("title", title);
			movie.setProperty("year", year);
			movie.setProperty("director", director);
			movie.setProperty("genre", genre);
			movie.setProperty("imdbID", id);
			movie.setProperty("rating", rating);
			movie.setProperty("user", user);

			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			
			Query q = new Query("Movie");
			PreparedQuery pq = ds.prepare(q);
			
			// All duplicate ID's are not necessarily removed
			// If they appear as different strings
			// So we need to check once more
			for (Entity result: pq.asIterable()) {
				Key key = result.getKey();
				String movie_id = (String) result.getProperty("imdbID");
				if (movie_id.equals(id)) {
					duplicate = true;
				}
			}

			if (!duplicate)
				ds.put(movie);
		}
		
		resp.sendRedirect("/list.jsp");
	}

	static void addTop250ToDatastore() throws IOException {
		// Adds all movies from IMDB's Top 250 list
		// To the datastore

		String[] imdbID = {
			"tt0111161", "tt0068646", "tt0071562", "tt0468569", "tt0110912", "tt0060196", "tt0108052", "tt0050083", "tt0167260", "tt0137523", "tt0120737", "tt0080684", "tt0816692", "tt1375666", "tt0109830", "tt0073486", "tt0167261", "tt0099685", "tt0133093", "tt0076759", "tt0047478", "tt0317248", "tt0114369", "tt0114814", "tt0102926", "tt0038650", "tt0064116", "tt0110413", "tt0118799", "tt0034583", "tt0082971", "tt0120586", "tt0054215", "tt0021749", "tt0120815", "tt0047396", "tt0245429", "tt1675434", "tt0027977", "tt0103064", "tt0209144", "tt0253474", "tt0120689", "tt0043014", "tt0078788", "tt0057012", "tt0407887", "tt0172495", "tt0088763", "tt0078748", "tt0482571", "tt0032553", "tt0405094", "tt1345836", "tt0110357", "tt1853728", "tt0081505", "tt0095765", "tt0050825", "tt0169547", "tt0910970", "tt0053125", "tt0090605", "tt0033467", "tt0211915", "tt0052357", "tt0022100", "tt0435761", "tt0082096", "tt0364569", "tt0095327", "tt0119698", "tt1065073", "tt0086190", "tt0066921", "tt0087843", "tt0075314", "tt0105236", "tt0036775", "tt0180093", "tt0112573", "tt0056592", "tt0056172", "tt0051201", "tt0338013", "tt2267998", "tt0093058", "tt0045152", "tt0040522", "tt0070735", "tt0086879", "tt0071853", "tt0208092", "tt0042876", "tt0042192", "tt0119488", "tt0053604", "tt0059578", "tt0053291", "tt0012349", "tt0062622", "tt0040897", "tt0041959", "tt0361748", "tt0097576", "tt1832382", "tt0055630", "tt0372784", "tt0114709", "tt0017136", "tt0105695", "tt0081398", "tt0086250", "tt0986264", "tt0071315", "tt2015381", "tt1049413", "tt0363163", "tt0057115", "tt0095016", "tt0031679", "tt0047296", "tt0457430", "tt1187043", "tt0113277", "tt0050212", "tt2106476", "tt0119217", "tt0993846", "tt0050976", "tt0096283", "tt0044741", "tt0015864", "tt0080678", "tt0050986", "tt0089881", "tt0017925", "tt0083658", "tt0120735", "tt1305806", "tt1205489", "tt0112641", "tt0118715", "tt0032976", "tt1291584", "tt0434409", "tt0347149", "tt0077416", "tt0405508", "tt0025316", "tt0061512", "tt0892769", "tt0116282", "tt1979320", "tt0117951", "tt0055031", "tt0033870", "tt0031381", "tt0758758", "tt0292490", "tt0046268", "tt0268978", "tt0046912", "tt0395169", "tt0167404", "tt0084787", "tt0266543", "tt0477348", "tt0064115", "tt0266697", "tt0091763", "tt0978762", "tt0079470", "tt2024544", "tt1255953", "tt0074958", "tt0052311", "tt0046911", "tt0075686", "tt0401792", "tt2278388", "tt0093779", "tt0092005", "tt0469494", "tt0052618", "tt1877832", "tt0245712", "tt0053198", "tt0032551", "tt0032138", "tt1028532", "tt0405159", "tt0107207", "tt0848228", "tt0036868", "tt0060827", "tt0440963", "tt0056801", "tt0246578", "tt0083987", "tt0087544", "tt0044079", "tt0338564", "tt0073195", "tt0044706", "tt0114746", "tt0079944", "tt0038787", "tt1504320", "tt0083922", "tt1130884", "tt0169102", "tt0088247", "tt0058946", "tt1201607", "tt0107048", "tt0112471", "tt1220719", "tt0048424", "tt0075148", "tt0072890", "tt0113247", "tt0047528", "tt0198781", "tt0061184", "tt0072684", "tt0353969", "tt0325980", "tt0038355", "tt0058461", "tt0092067", "tt0120382", "tt0061722", "tt1454029", "tt0046250", "tt0367110", "tt0054997", "tt0107290", "tt0154420", "tt1798709", "tt0118694", "tt0046359", "tt0101414", "tt0040746", "tt0114787", "tt0070511", "tt0381681", "tt0085334", "tt0049406", "tt0374546"
		};

		String title = null;
		int year = 0;
		int rank = 0;
		String director = null;
		String genre = null;
		String rating = null;
		String id = null;
		String json_data = null;

		for (int i = 0; i < imdbID.length; i++) {
			try {
				json_data = URLFetch.readUrl("http://www.omdbapi.com/?i=" + imdbID[i]);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			Gson gson = new Gson();
			HashMap < String, String > movie_info = new Gson().fromJson(json_data, new TypeToken < HashMap < String, String >> () {}.getType());

			title = movie_info.get("Title");
			year = Integer.valueOf(movie_info.get("Year"));
			director = movie_info.get("Director");
			genre = movie_info.get("Genre");
			rating = movie_info.get("imdbRating");
			id = imdbID[i];
			rank = i + 1;

			Key movieKey = KeyFactory.createKey("Top250", title);
			Entity movie = new Entity("Top250", movieKey);
			movie.setProperty("title", title);
			movie.setProperty("year", year);
			movie.setProperty("director", director);
			movie.setProperty("genre", genre);
			movie.setProperty("imdbID", id);
			movie.setProperty("rating", rating);
			movie.setProperty("rank", rank);

			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			datastore.put(movie);
		}
	}
}