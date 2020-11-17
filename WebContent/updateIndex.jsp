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

<%
try{
	connection = H2Utils.getConnection();
	statement=connection.createStatement();
	String sqlQuery = String.format("select * from transactions where id=%s", id);
	resultSet = statement.executeQuery(sqlQuery);
	while(resultSet.next()){
	%>
	<html>
	<body>
	<h1>Update data from database in jsp</h1>
	<form method="post" action="updateDB.jsp">
		<input type="hidden" name="id" value="<%=resultSet.getString("id") %>">
		H2 Index:<br>
		<input type="text" name="id" value="<%=resultSet.getString("id") %>" readonly>
		<br>
		First name:<br>
		<input type="text" name="first_name" value="<%=resultSet.getString("name") %>">
		<br>
		Last name:<br>
		<input type="text" name="last_name" value="<%=resultSet.getString("sname") %>">
		<br>
		Email:<br>
		<input type="email" name="email" value="<%=resultSet.getString("email") %>">
		<br>
		Street:<br>
		<input type="text" name="street" value="<%=resultSet.getString("street") %>">
		<br>
		Product:<br>
		<input type="text" name="product" value="<%=resultSet.getString("product") %>">
		<br>
		Date:<br>
		<input type="date" name="date" value="<%=resultSet.getDate("date") %>">
		<br><br>
		<input type="submit" value="submit">
	</form>
	<%
	}
	} catch (SQLException sqlE) {
		H2Utils.printSQLException(sqlE);
	} finally{
		connection.close();
	}
%>
</body>
</html>