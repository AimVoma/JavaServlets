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
	<table>
		<tr>
			<th>name</th>
			<th>sname</th>
			<th>email<th>
			<th>street<th>
		</tr>
		<c:forEach items="${keyList}" var="usr">
        <tr>
				<td><c:out value="${usr.name}" /></td>
				<td><c:out value="${usr.sname}" /></td>
		</tr>
		</c:forEach>
	</table>
    <form method="post" action="ManageDBServlet"
       enctype="multipart/form-data">
       Select file to upload: <input type="file" name="file" size="60" /><br />
       <br /> <input type="submit" value="Upload" />
    </form>
<center/>
</body>
</html>