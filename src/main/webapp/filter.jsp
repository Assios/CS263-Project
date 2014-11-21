<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.taskqueue.Queue" %>
<%@ page import="com.google.appengine.api.taskqueue.QueueFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="favicon.ico">

    <title>Movies</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/main.css" rel="stylesheet">

  </head>

  <body>

    <div class="container">
      <div class="header">
        <ul class="nav nav-pills pull-right">
          <li class="active"><a href="/">Home</a></li>
          <li class="active"><a href="/list.jsp">Movie list</a></li>
          <li class="active"><a href="/filter.jsp">Filter movies</a></li>
          <li class="active"><a href="/about.html">About</a></li>
        </ul>
        <h3 class="text-muted">Movies</h3>
      </div>

      <h3>Filter movies below.<br>You will get a list of movies from the IMDB Top 250 list that matches your criteria.</h3>

    <form action="/enqueuef" class="navbar-form" role="search" method="post">
      <div class="input-group">
        <span class="input-group-addon">Min year</span>
        <input type="text" name="minyear" class="form-control" placeholder="0">
      </div>
      <br><br>
      <div class="input-group">
        <span class="input-group-addon">Max year</span>
        <input type="text" name="maxyear"  class="form-control" placeholder="9999">
      </div>
      <br><br>
      <div class="input-group">
        <span class="input-group-addon">Genre</span>
        <input type="text" name="genre" class="form-control">
      </div>
      <br><br>
      <div class="input-group">
        <span class="input-group-addon">Directed by</span>
        <input type="text" name="director" class="form-control">
      </div>
      <br><br>
      <button type="submit" class="btn btn-default">Submit</button>
    </form>



      <div class="footer">
        <p>&copy; Movies</p>
      </div>

    </div>

  </body>
</html>
