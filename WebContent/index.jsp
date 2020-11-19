 <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.logging.*" %>
<%@ page import="com.wtc.H2Utils" %>


<%
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
    table{
      	text-align:center;
    }
   	tr td{
  		padding-top:3px;
	} 
   	h1 {
   		margin-top:5em; 
        color:green; 
      }
	a {
		color: hotpink;
		border:0 none;
		-webkit-border-radius: 8px;
	    border-radius: 8px; 
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

<h1>Retrieve Table Transactions from H2 database</h1>
<div align="center">
<table border="1">
<tr>
<td>ID</td>
<td>First Name</td>
<td>Last Name</td>
<td>Email</td>
<td>Street</td>
<td>Product</td>
<td>Date</td>
<td><b>Update</b></td>
<td><b>Delete</b></td>
</tr>
<%
try{
	if (!H2Utils.getStatus())
		H2Utils._initialize();
	
	connection = H2Utils.getConnection();
	statement = connection.createStatement();
	resultSet = statement.executeQuery("select * from transactions");
	while(resultSet.next()){
	%>
		<% if (!resultSet.getBoolean("visible")) continue;%>
		<tr>
		<td><%=resultSet.getString("id") %></td>
		<td><%=resultSet.getString("name") %></td>
		<td><%=resultSet.getString("sname") %></td>
		<td><%=resultSet.getString("email") %></td>
		<td><%=resultSet.getString("street") %></td>
		<td><%=resultSet.getString("product") %></td>
		<td><%=resultSet.getString("date") %></td>
		<td><a href="updateEntry.jsp?id=<%=resultSet.getString("id")%>">update</a></td>
		<td><a href="deleteEntry.jsp?id=<%=resultSet.getString("id")%>">delete</a></td>
		</tr>
	<%
	}
} catch (SQLException e) {
	H2Utils.printSQLException(e);
} finally{
	if (connection != null)
		connection.close();
}
%>
</table>

<form method="post" action="createEntry.jsp">
    <label>Create new Transaction Entry </label>
    <input type="submit" value="Create">
</form>
&nbsp;&nbsp;
</div>
</body>
</html> 