package cs263;

// The Enqueue servlet should be mapped to the "/enqueue" URL.
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import java.awt.*;

public class Enqueue extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String movie = request.getParameter("movie");
        
        String url_open ="http://javadl.sun.com/webapps/download/AutoDL?BundleId=76860";
        java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
        System.out.println("URL: " + url_open);
        System.out.println("halla");
        System.out.println(movie);
        // Add the task to the default queue.
        Queue queue = QueueFactory.getDefaultQueue();
        
        
    }
}
