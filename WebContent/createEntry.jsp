 <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.logging.*" %>
<%@ page import="com.wtc.H2Utils" %>

<%
String id = request.getParameter("id");
Connection connection = null;
Statement statement = null;
ResultSet resultSet = null;
%>


<html>	
<head> 
  <title>Java Servlets</title> 
  <style> 
    body { 
		text-align:center; 
    }
   	h1 {
   		margin-top:5em; 
        color:green; 
      }
	form {
		margin-top:2.5em;
	}
	input[type=submit] {
	    padding:5px 15px; 
	    background:#ccc; 
	    border:0 none;
	    cursor:pointer;
	    -webkit-border-radius: 8px;
	    border-radius: 8px; 
	}
  </style> 
</head> 
	<body>
	<h1>Create new Transaction Record in H2 Database</h1>
	<div>
		<form method="post" action="createEntryDB.jsp">
			First name:<br>
			<input type="text" name="first_name" placeholder="Insert Name ...">
			<br>
			Last name:<br>
			<input type="text" name="last_name" placeholder="Insert Last Name ..."">
			<br>
			Email:<br>
			<input type="email" name="email" placeholder="Insert Email ...">
			<br>
			Street:<br>
			<input type="text" name="street" placeholder="Insert Street ...">
			<br>
			Product:<br>
			<input type="text" name="product" placeholder="Insert Product ...">
			<br>
			Date:<br>
			<input type="date" name="date" value="Date">
			<br><br>
			<input type="submit" value="submit">
		</form>
	</div>
	</body>
</html>