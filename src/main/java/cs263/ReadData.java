package cs263;

// The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class ReadData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	response.setContentType("text/html");
    	PrintWriter write = response.getWriter();
    	
    	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    	
    	Query q = new Query("Movie");
    	PreparedQuery pq = ds.prepare(q);
    	
    	write.print("<p>List of movies:</p>");
    	
    	for (Entity result : pq.asIterable()) {
    		Key key = result.getKey();
    		String title = (String) result.getProperty("title");
    		String rating = (String) result.getProperty("rating");
    		String year = (String) result.getProperty("year");
    		write.print("<p>" + title + " from " + year + " has a score of " + rating + " on IMDB.</p>");

    	}
    }
}
