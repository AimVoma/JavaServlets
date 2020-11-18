<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Upload Service</title> 
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
		margin-right:5em;
	    padding:5px 15px; 
	    background:#ccc; 
	    border:0 none;
	    cursor:pointer;
	    -webkit-border-radius: 10px;
	    border-radius: 10px; 
	}
	 </style> 
</head>
<body>
    <h1>File Upload</h1>
    <form method="post" action="UploadServlet"
        enctype="multipart/form-data">
        Select file to upload: <input type="file" name="file" size="60" /><br />
        <br /> <input type="submit" value="Upload" />
    </form>
</body>
</html>