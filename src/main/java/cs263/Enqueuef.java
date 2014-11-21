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
    	
    	boolean filter_genre = false;
        boolean filter_director = false;
        int min_year;
        int max_year;
        
        //Set lower and higher bound to 0 and 9999 if years not specified
    	if (!isNumeric(request.getParameter("minyear")))
    		min_year = 0;
    	else
            min_year = Integer.parseInt(request.getParameter("minyear"));

    	if (!isNumeric(request.getParameter("maxyear")))
    		max_year = 9999;
    	else
            max_year = Integer.parseInt(request.getParameter("maxyear"));
    		
        String genre = request.getParameter("genre");
        String director = request.getParameter("director");

        if (!genre.isEmpty())
        	filter_genre = true;
        if (!director.isEmpty())
        	filter_director = true;
        
        System.out.println(min_year);
        System.out.println(max_year);
        System.out.println(genre);
        System.out.println(director);

        
    	response.setContentType("text/html");
    	PrintWriter write = response.getWriter();

        
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
        
        write.print("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><meta name=\"description\" content=\"\"><meta name=\"author\" content=\"\"><link rel=\"icon\" href=\"favicon.ico\"><title>Movies</title><link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"><link href=\"css/main.css\" rel=\"stylesheet\"></head><body><div class=\"container\"><div class=\"header\"><ul class=\"nav nav-pills pull-right\"><li class=\"active\"><a href=\"/\">Home</a></li><li class=\"active\"><a href=\"/filter.jsp\">Filter movies</a></li><li class=\"active\"><a href=\"#\">About</a></li></ul><h3 class=\"text-muted\">Movies</h3></div>");
    	
    	write.print("<p>Filtered movies:</p>");

        for (Entity result : pq.asIterable()) {
            Key key = result.getKey();
            String id = (String) result.getProperty("imdbID");
            String rating = (String) result.getProperty("rating");
            String title = (String) result.getProperty("title");
            String year = (String) String.valueOf(result.getProperty("year"));
            String q_genre = (String) result.getProperty("genre");
            String q_director = (String) result.getProperty("director");
                        
            //FILTER GENRE AND DIRECTOR
            if (filter_genre && filter_director) {
            	if (q_genre.toLowerCase().contains(genre.toLowerCase())
            			&& q_director.toLowerCase().contains(director.toLowerCase()))
            		write.print("<p><b>Title:</b> " + title + ". <b>Year:</b> " + year + ". <b>Director:</b> " + q_director + ". <b>Genre:</b> " + q_genre + ". Rating: " + rating + "/10. <a target=\"_blank\" href=\"http://www.imdb.com/title/" + id + "\">OPEN ON IMDB</b></a></p>");
            }
            else if (filter_genre && !(filter_director)) {
            	if (q_genre.toLowerCase().contains(genre.toLowerCase()))
            		write.print("<p><b>Title:</b> " + title + ". <b>Year:</b> " + year + ". <b>Director:</b> " + q_director + ". <b>Genre:</b> " + q_genre + ". Rating: " + rating + "/10. <a target=\"_blank\" href=\"http://www.imdb.com/title/" + id + "\">OPEN ON IMDB</b></a></p>");
            }
            else if (!(filter_genre) && filter_director) {
            	if (q_director.toLowerCase().contains(director.toLowerCase()))
            		write.print("<p><b>Title:</b> " + title + ". <b>Year:</b> " + year + ". <b>Director:</b> " + q_director + ". <b>Genre:</b> " + q_genre + ". Rating: " + rating + "/10. <a target=\"_blank\" href=\"http://www.imdb.com/title/" + id + "\">OPEN ON IMDB</b></a></p>");
            }
            else
        		write.print("<p><b>Title:</b> " + title + ". <b>Year:</b> " + year + ". <b>Director:</b> " + q_director + ". <b>Genre:</b> " + q_genre + ". Rating: " + rating + "/10. <a target=\"_blank\" href=\"http://www.imdb.com/title/" + id + "\">OPEN ON IMDB</b></a></p>");
            
        }
        
    }
    
    public static boolean isNumeric(String str)  
    {  
      try  
      {  
        double d = Double.parseDouble(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }
        
} 	 	
    

