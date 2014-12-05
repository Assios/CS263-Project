package cs263;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class Serve extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
    		String key = req.getParameter("blob-key");
    		String name = req.getParameter("name");
            BlobKey blobKey = new BlobKey(key);
            blobstoreService.serve(blobKey, res);
            
            //Add blob to datastore
    		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
            Query q = new Query("Blobs");
    		PreparedQuery pq = ds.prepare(q);
    		
    		Key datastoreKey = KeyFactory.createKey("Blobs", key);
    		Entity photo = new Entity("Blobs", datastoreKey);
    		
    		photo.setProperty("key", blobKey.getKeyString());
    		photo.setProperty("name", name);

    		if (name!=null) {
    		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    		datastore.put(photo);
    		}
    		
        }
}
