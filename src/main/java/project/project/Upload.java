package project.project;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;


public class Upload extends HttpServlet {
	
	private BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
		
	 Map<String, BlobKey> blobs = blobstore.getUploadedBlobs(request);
	 
     BlobKey blobKey = blobs.get("myFile");
     
     if (blobKey == null) {
    	 response.sendRedirect("/");
     }
     else {
    	 response.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
     }
 }
}
