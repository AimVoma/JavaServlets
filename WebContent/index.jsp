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
<body>


<h1>Retrieve data from database in jsp</h1>
<table border="1">
<tr>
<td>ID</td>
<td>First Name</td>
<td>Last Name</td>
<td>Email</td>
<td>Street</td>
<td>Product</td>
<td>Date</td>
</tr>
<%
try{
	H2Utils._initialize();
	connection = H2Utils.getConnection();
	statement = connection.createStatement();
	resultSet = statement.executeQuery("select * from transactions");
	while(resultSet.next()){
	%>
		<tr>
		<td><%=resultSet.getString("id") %></td>
		<td><%=resultSet.getString("name") %></td>
		<td><%=resultSet.getString("sname") %></td>
		<td><%=resultSet.getString("email") %></td>
		<td><%=resultSet.getString("street") %></td>
		<td><%=resultSet.getString("product") %></td>
		<td><%=resultSet.getString("date") %></td>
		<td><a href="updateIndex.jsp?id=<%=resultSet.getString("id")%>">update</a></td>
		</tr>
	<%
	}
} catch (SQLException e) {
	H2Utils.printSQLException(e);
} finally{
	connection.close();
}
%>
</table>
</body>
</html> 