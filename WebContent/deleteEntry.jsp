 <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.logging.*" %>
<%@ page import="com.wtc.H2Utils" %>


<%
Logger logger = Logger.getLogger("ServletLogger");
String id = request.getParameter("id");
Connection connection = null;
Statement statement = null;
ResultSet resultSet = null;
%>

<%
try{
	if(!H2Utils.getStatus())
		H2Utils._initialize();
	
	connection = H2Utils.getConnection();
	statement = connection.createStatement();
	int verification = statement.executeUpdate(String.format("delete from transactions where id=%s", id));
	if (verification == 1)
		logger.log(Level.INFO, String.format("Deletion of Transaction ID entry %s was done succesfully", id));
	else
		logger.log(Level.WARNING, String.format("Deletion of Transaction ID entry %s was NOT succesfull!", id));
} catch (SQLException e) {
	H2Utils.printSQLException(e);
} finally{
	connection.close();
	request.getRequestDispatcher("/index.jsp").forward(request, response);
}
%>
