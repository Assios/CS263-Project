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
          <li class="active">
            <!-- user login information -->
<%

    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

if (user != null) {
        pageContext.setAttribute("user", user);

%>

<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign out</a>
<%
} else {
%>
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
<%
    }
%>
          <!-- user login end -->           
          </li>
          <li class="active"><a href="/index.jsp">Home</a></li>
          <li class="active"><a href="/list.jsp">Movie list</a></li>
          <li class="active"><a href="/filter.jsp">Filter movies</a></li>
          <li class="active"><a href="/upload.jsp">Upload poster</a></li>
          <li class="active"><a href="/photos">Posters</a></li>
          <li class="active"><a href="/about.jsp">About</a></li>
        </ul>
        <h3 class="text-muted">Movies</h3>
      </div>

<%
if (user != null) {
        pageContext.setAttribute("user", user);
%>
<p>Logged in as ${fn:escapeXml(user.nickname)}.</p>
<%
}
%>

      <div class="jumbotron">
        <h1>About</h1>
        <p class="lead">
          This website allows the user to get information about movies, using the <a href="http://www.omdbapi.com/">Open Movie Database API</a>. The user could search for a movie that gets added to the <a href="/list.jsp">public list</a> of movies, or go to the <a href="/filter.jsp">filter page</a> to filter movies on either the user list or the <a href="http://www.imdb.com/chart/top">IMDB Top 250 list</a>.

        </p>
      </div>

      <div class="footer">
        <p>&copy; Movies</p>
      </div>

    </div>

  </body>
</html>
