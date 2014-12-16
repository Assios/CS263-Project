package cs263;

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

//Only used for testing, now using list.jsp file instead

public class ListMovies extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter write = response.getWriter();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("Movie");
		PreparedQuery pq = ds.prepare(q);

		write.print("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><meta name=\"description\" content=\"\"><meta name=\"author\" content=\"\"><link rel=\"icon\" href=\"favicon.ico\"><title>Movies</title><link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"><link href=\"css/main.css\" rel=\"stylesheet\"></head><body><div class=\"container\"><div class=\"header\"><ul class=\"nav nav-pills pull-right\"><li class=\"active\"><a href=\"/\">Home</a></li><li class=\"active\"><a href=\"/filter\">Filter movies</a></li><li class=\"active\"><li class=\"active\"><a href=\"/upload.jsp\">Upload poster</a></li><li class=\"active\"><a href=\"/photos\">Posters</a></li><a href=\"#\">About</a></li></ul><h3 class=\"text-muted\">Movies</h3></div>");

		write.print("<p>List of movies:</p>");

		for (Entity result: pq.asIterable()) {
			Key key = result.getKey();
			String title = (String) result.getProperty("title");
			String rating = (String) result.getProperty("rating");
			String year = (String) result.getProperty("year");
			String poster = (String) result.getProperty("poster");
			write.print("<p><a href=\"" + poster + "\">" + title + "</a> from " + year + " has a score of " + rating + " on IMDB.</p>");

		}

		write.print("<div class=\"footer\"><p>&copy; Movies</p> </div></div></body></html>");
	}
}