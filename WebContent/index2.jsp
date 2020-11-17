<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style>
th, tr, td, table {
	border: 1px solid red;
}
</style>
</head>
<body>
<center> 
	<h1> Transaction Table JOIN Results: </h1>
	<table>
		<tr>
			<th>ID</th>
			<th>name</th>
			<th>sname</th>
			<th>email</th>
			<th>street</th>
			<th>product</th>
			<th>date</th>
		</tr>
		<c:forEach items="${keyList}" var="usr">
        <tr>
				<td><c:out value="${usr.id}" /></td>
				<td><c:out value="${usr.name}" /></td>
				<td><c:out value="${usr.sname}" /></td>
				<td><c:out value="${usr.email}" /></td>
				<td><c:out value="${usr.street}" /></td>
				<td><c:out value="${usr.product}" /></td>
				<td><c:out value="${usr.date}" /></td>
		</tr>
		</c:forEach>
	</table>
	<br>
	<br>
	<form method="post" action="UploadServlet">
	  <label for="fname">Select ID to remove:</label><br>
	  <input type="text" id="fname" name="fname" value="John">
	  <input type="submit" value="DELETE" />
	</form>
<center/>
</body>
</html>