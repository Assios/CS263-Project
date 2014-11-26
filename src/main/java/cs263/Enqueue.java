package cs263;

// The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


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
    String imdbID = null;
    boolean duplicate = false;
    String user = null;
    	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String movie = URLFetch.swap(request.getParameter("movie"));
        String json_data = null;
        
        URLFetch.addToDatastore();
        
        UserService userService = UserServiceFactory.getUserService();
        User current_user = userService.getCurrentUser();
        
        if (current_user != null) {
        	user = current_user.getNickname();
        }
        else
        	user = "Anonymous";
        
        //Fetch data from website
        try {
			json_data = URLFetch.readUrl("http://www.omdbapi.com/?t=" + movie);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        //CONVERT TO GSON
    	Gson gson = new Gson();     	
    	HashMap<String,String> movie_info = new Gson().fromJson(json_data, new TypeToken<HashMap<String, String>>(){}.getType());
    	        
        // Add the task to the default queue.
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        
        Query q = new Query("Top250");
        PreparedQuery pq = ds.prepare(q);
        
        //Checks if the movie exists AND that it is a movie (we don't want TV series)
        if ((movie_info.get("Response").equals("True")) && (movie_info.get("Type").equals("movie"))) {
        	duplicate = false;
        	getMovieInfo(movie_info);
        	
            for (Entity result : pq.asIterable()) {
                Key key = result.getKey();
                String id = (String) result.getProperty("imdbID");
                if (id.equals(imdbID)) {
                	duplicate = true;
                }
            }
            
            if (!duplicate) {
		        Queue queue = QueueFactory.getDefaultQueue();     
		        queue.add(withUrl("/worker").param("title", title).param("year", year).param("director", director).param("genre", genre).param("plot", plot).param("rating", rating).param("poster", poster).param("imdbID",  imdbID).param("user",  user));
            }
            
            response.sendRedirect("/list.jsp");
        }
        
        else {
        	response.sendRedirect("/404.html");
        }
        
    }
    
    private void getMovieInfo(HashMap<String,String> map) {
    	this.title = map.get("Title");
    	this.year = map.get("Year");
    	this.director = map.get("Director");
    	this.genre = map.get("Genre");
    	this.plot = map.get("Plot");
    	this.rating = map.get("imdbRating");
    	this.poster = map.get("Poster");
    	this.imdbID = map.get("imdbID");
    }
    
}

