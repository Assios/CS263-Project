package cs263;

// The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.gson.Gson;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class Enqueue extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String movie = JsonReader.swap(request.getParameter("movie"));
        String json_data = null;
        
        //Fetch data from website
        try {
			json_data = JsonReader.readUrl("http://www.omdbapi.com/?s=" + movie);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        //CONVERT TO GSON
    	Gson gson = new Gson();

 
		//convert the json string back to object
		Gson obj = gson.fromJson(json_data, Gson.class);
		System.out.println("OBJECT");
  		System.out.println(obj);
        System.out.println("STRING");
        System.out.println(json_data);

        
        // Add the task to the default queue.
        Queue queue = QueueFactory.getDefaultQueue();
        
        queue.add(withUrl("/worker").param("json", json_data));

        response.sendRedirect("/");

    }
    
}

