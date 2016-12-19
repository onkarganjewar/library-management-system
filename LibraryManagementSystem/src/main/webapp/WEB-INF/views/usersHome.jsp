<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$('#btnSearch').click( function() {
		console.log("button clicked");
		var bookId = $('#txtSearch').val();
		var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/search-book-"+bookId;
		window.location.replace(url);
	});
});
</script>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Users page</title>
	<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>
<body>
	<div class="col-md-12">
	<div class="col-md-8">
	<h3>Welcome <strong>${user}</strong></h3>
	</div>
	
	<div class="col-md-4">
	<a href="<c:url value="/logout" />" class="btn btn-default" style="margin: 20px 0px 0px 300px;">Logout</a>
	</div>
	</div>
	<div class="panel panel-default">
		<div class="form-group row">
 			 <div class="col-xs-6">
    		 	<input class="form-control" type="text" id="txtSearch" placeholder="Search Book Name" style="margin:0px 0px 0px 20px;">
				<input type="button" class="btn btn-primary" value="Search" id="btnSearch" name="btnSearch" style="margin:10px 0px 0px 20px;" />  
				<input type="hidden" name ="useremail" id="useremail" value="${useremail }">	
				<input type="hidden" name ="userid" id="userid" value="${userid }">	
				
			</div>
		</div>
	</div>
	<sec:authorize access="hasRole('USER')">
		<div class="well">
			<a href="<c:url value='/patron/viewCart?name=${userid}' />" class="btn btn-info btn-lg" style="width:400px; margin:0px 0px 0px 20px;" >View Cart</a>
		    <a href="<c:url value='/patron/viewCheckedOutBooks?name=${userid}' />" class="btn btn-primary btn-lg" style="width:400px; margin:0px 0px 0px 300px;" >View Checked Out Books</a>
			
		</div>
	</sec:authorize>
	<div class="well"><strong>
	<span style="width:500px;font-size:24;">Account Charges due:</span><div class="input-group"></strong>
  <span class="input-group-addon" style="width:30px;height:60px;">$</span>
  <strong><input type="text" class="form-control" style="width:60px;height:60px;" value="${fine }" readonly></strong>
  <span class="input-group-addon" style="width:30px;height:60px;">.00</span>
</div>
	</div>
</body>
</html>