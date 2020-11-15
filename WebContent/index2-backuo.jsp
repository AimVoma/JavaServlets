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
	<table>
		<tr>
			<th>name</th>
			<th>sname</th>
			<th>email<th>
			<th>street<th>
		</tr>

		<c:forEach items="${keyList}" var="usr">
        <tr>
				<td>${usr.name}</td>
				<td>${usr.sname}</td>
				<td>${usr.email}</td> 
				<td>${usr.street}</td> 
		</tr>
		</c:forEach>
	</table>

</body>
</html>