package cs263;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class Workerf extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String genre = request.getParameter("genre");
        String plot = request.getParameter("plot");
        String rating = request.getParameter("rating");
        String poster_url = request.getParameter("poster");
        String imdbID = request.getParameter("imdbID");

        Date date = new Date();

        Key movieKey = KeyFactory.createKey("Movie", title);
        Entity movie = new Entity("Movie", movieKey);
        movie.setProperty("date", date);
        movie.setProperty("title", title);
        movie.setProperty("year", year);
        movie.setProperty("director", director);
        movie.setProperty("genre", genre);
        movie.setProperty("plot", plot);
        movie.setProperty("rating", rating);
        movie.setProperty("poster", poster_url);
        movie.setProperty("imdbID", imdbID);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        datastore.put(movie);
    }
}
