package project.project;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;


public class Serve extends HttpServlet {
	
	private BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
     BlobKey blobKey = new BlobKey(request.getParameter("blob-key"));
     blobstore.serve(blobKey, response);
	}
}
