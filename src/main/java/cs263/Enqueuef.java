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

import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
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
        int min_year = Integer.parseInt(request.getParameter("minyear"));
        int max_year = Integer.parseInt(request.getParameter("maxyear"));
        String genre = request.getParameter("genre");
        String director = request.getParameter("director");
        
        System.out.println(min_year);
        System.out.println(max_year);
        System.out.println(genre);
        System.out.println(director);
          	        
        // Add the task to the default queue.
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        
        Filter yearMinFilter =
        		  new FilterPredicate("year",
        		                      FilterOperator.GREATER_THAN_OR_EQUAL,
        		                      min_year);
        
        Filter yearMaxFilter =
        		  new FilterPredicate("year",
        		                      FilterOperator.LESS_THAN_OR_EQUAL,
        		                      max_year);
        Filter yearRangeFilters =
        		  CompositeFilterOperator.and(yearMinFilter, yearMaxFilter);
        
        Query q = new Query("Movie").setFilter(yearRangeFilters);
        PreparedQuery pq = ds.prepare(q);
        
        for (Entity result : pq.asIterable()) {
            Key key = result.getKey();
            String title = (String) result.getProperty("title");
            String year = (String) String.valueOf(result.getProperty("year"));
            String q_genre = (String) result.getProperty("genre");
            String q_director = (String) result.getProperty("director");

            System.out.println(title + " from " + year);
            }
        }
        
    } 	 	
    

