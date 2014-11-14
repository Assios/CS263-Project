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

// The Worker servlet should be mapped to the "/worker" URL.
public class Worker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String json_data = request.getParameter("json");
        
        //TODO Convert json_data to GSON object

        Key movieKey = KeyFactory.createKey("Movie", json_data);
        Entity task = new Entity("MovieInfo", movieKey);
        task.setProperty("json", json_data);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(task);
    }
}
