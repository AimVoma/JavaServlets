<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.logging.Level" %>
<%@ page import="java.util.logging.Logger" %>
<%@ page import="com.wtc.H2Utils" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Logger logger = Logger.getLogger("ServletLogger");

String first_name=request.getParameter("first_name");
String last_name=request.getParameter("last_name");
String email=request.getParameter("email");
String street=request.getParameter("street");
String product=request.getParameter("product");
Date date = null;
/*
* Parse SDF object as SQL.DATE
*/
try{
	date=new java.sql.Date(sdf.parse(request.getParameter("date")).getTime());
}catch (NullPointerException nlp){
	logger.log(Level.WARNING, "Date object parse from request failed!");
}

Connection connection = null;
PreparedStatement statement = null;
ResultSet resultSet = null;

// Table Transactions Visibility column
boolean visible = true;

try{
	connection = H2Utils.getConnection();
	
	String sqlQuery = "insert into transactions (name, sname, email, street, product, date, visible) values (?, ?, ?, ?, ?, ?, ?);";
	
	statement=connection.prepareStatement(sqlQuery);
	statement.setString(	1, first_name);
	statement.setString(	2, last_name);
	statement.setString(	3, email);
	statement.setString(	4, street);
	statement.setString(	5, product);
	statement.setDate(		6, date);
	statement.setBoolean(	7, visible);
	
	int execVerification = statement.executeUpdate();
	
	if(execVerification == 1)
		logger.log(Level.INFO, String.format("Record %s Created Succesfully!", "Transactions"));
	else
		logger.log(Level.WARNING, String.format("There was a problem creating record %s", "Transactions"));
}
catch(SQLException sql)
{
	H2Utils.printSQLException(sql);
}
finally
{
	if (connection != null)
		connection.close();
	request.getRequestDispatcher("/index.jsp").forward(request, response);
}

%> 