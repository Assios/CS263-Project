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
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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

<%

    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

if (user != null) {
        pageContext.setAttribute("user", user);
        
%>

<p>Logged in as ${fn:escapeXml(user.nickname)}.</p>
<%
} else {
%>
<p>
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>.</p>
<%
    }
%>



      <h2>List of movies searched for by users:</h2>
<%      
      DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
      
      Query q = new Query("Movie");
      PreparedQuery pq = ds.prepare(q);
      
      for (Entity result : pq.asIterable()) {
        Key key = result.getKey();
        String title = (String) result.getProperty("title");
        String rating = (String) result.getProperty("rating");
        String year = (String) String.valueOf(result.getProperty("year"));
        String poster = (String) result.getProperty("poster");
        String user = (String) result.getProperty("user");

%>      <p><a href="<%= poster %>"><%= title %></a> from <%= year %> has the score <%= rating %> on IMDB.</p>
<%
      }
%>

      <div class="footer">
        <p>&copy; Movies</p>
      </div>

    </div>

  </body>
</html>
