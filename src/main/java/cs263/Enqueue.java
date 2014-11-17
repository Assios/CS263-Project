package cs263;

// The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class Enqueue extends HttpServlet {
    //Fields for movie info
    String title = null;
    String year = null;
    String genre = null;
    String director = null;
    String plot = null;
    String rating = null;
    String poster = null;
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String movie = JsonReader.swap(request.getParameter("movie"));
        String json_data = null;
        
        //Fetch data from website
        try {
			json_data = JsonReader.readUrl("http://www.omdbapi.com/?t=" + movie);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        //CONVERT TO GSON
    	Gson gson = new Gson();     	
    	HashMap<String,String> movie_info = new Gson().fromJson(json_data, new TypeToken<HashMap<String, String>>(){}.getType());
    	
        System.out.println(movie_info.get("Title"));
        
        // Add the task to the default queue.        
        if (movie_info.get("Response").equals("True")) {
        	getMovieInfo(movie_info);
	        Queue queue = QueueFactory.getDefaultQueue();     
	        queue.add(withUrl("/worker").param("title", title).param("year", year).param("director", director).param("genre", genre).param("plot", plot).param("rating", rating).param("poster", poster));
	        response.sendRedirect("/");
        }
        else
        	response.sendRedirect("/");
    }
    
    private void getMovieInfo(HashMap<String,String> map) {
    	this.title = map.get("Title");
    	this.year = map.get("Year");
    	this.director = map.get("Director");
    	this.genre = map.get("Genre");
    	this.plot = map.get("Plot");
    	this.rating = map.get("imdbRating");
    	this.poster = map.get("Poster");
    }
    
}

