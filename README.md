## _CS263 Project._

This website displays movie information based on the user's preferences. You can search for movies and add them to a public list to get information about the movies. You could then filter movies (on genre, director or year) from either the public user list or the official IMDB's Top 250 list.

You could for example get a list of all the thriller movies on IMDB's Top 250 list released between 1950 and 1960 that are directed by Alfred Hitchcock (there are 6).

### TODO

1. Add ability to provide an imdb.com url to automatically add all movies on the page to the user list. There are many <a href="http://www.imdb.com/features/anniversary/2010/lists/">lists</a> on imdb.com, and the user should have the ability to add movies from whichever list he wants by just providing the url.
2. Let the user upload pictures through blobstore, and use the <a href="https://cloud.google.com/appengine/docs/java/images/">Images API</a>.
3. Memcache.
