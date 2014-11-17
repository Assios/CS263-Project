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
    	
    	HashMap<String,String> map = new Gson().fromJson(json_data, new TypeToken<HashMap<String, String>>(){}.getType());

        System.out.println(map.get("Response"));
		
        System.out.println("STRING");
        System.out.println(json_data);

        
        // Add the task to the default queue.
        Queue queue = QueueFactory.getDefaultQueue();
        
        queue.add(withUrl("/worker").param("json", json_data));

        response.sendRedirect("/");

    }
    
}

