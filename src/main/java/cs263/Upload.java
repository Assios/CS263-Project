package cs263;


import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class Upload extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("myFile");
        String name = req.getParameter("name");
        
        //Add blob to datastore
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("Blobs");
		PreparedQuery pq = ds.prepare(q);
		String key = blobKey.getKeyString();
		Key datastoreKey = KeyFactory.createKey("Blobs", key);
		Entity photo = new Entity("Blobs", datastoreKey);
		
		photo.setProperty("key", blobKey.getKeyString());
		photo.setProperty("name", name);

		if (name!=null) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(photo);
		}
        
        if (blobKey == null) {
            res.sendRedirect("/");
        } else {
            res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString() + "&name=" + name);
        }
    }
}
