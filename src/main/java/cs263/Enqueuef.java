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

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class Enqueuef extends HttpServlet {	
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String min_year = request.getParameter("minyear");
        String max_year = request.getParameter("maxyear");
        String genre = request.getParameter("genre");
        String director = request.getParameter("director");
        
        System.out.println(min_year);
        System.out.println(max_year);
        System.out.println(genre);
        System.out.println(director);
          	        
        // Add the task to the default queue.
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        
        Query q = new Query("Top250");
        PreparedQuery pq = ds.prepare(q);
      
        for (Entity result : pq.asIterable()) {
            Key key = result.getKey();
            String year = (String) result.getProperty("year");
            String q_genre = (String) result.getProperty("genre");
            String q_director = (String) result.getProperty("director");

            if (q_director.toLowerCase().contains(director.toLowerCase())) {
            	System.out.println(q_director + " MATCHES " + director);

            }
        }
        
    }
    
}

