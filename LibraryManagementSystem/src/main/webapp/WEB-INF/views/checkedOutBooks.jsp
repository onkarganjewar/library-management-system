<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	
		var val = $('#val1').val();
		var renewVal= $('#val2').val();
		if (val == "success")
			alert("Book returned succesfully.");
		if(renewVal=="waitListExists")
			alert("Book cannot be renewed as other patrons are on the waiting list for this book. Please return the book on or before the due date.");
		else if(renewVal=="exceeded")
			alert("Book cannot be renewed more than 2 times. Please return the book on or before the due date.");
		else if(renewVal=="RenewalSuccess")
			alert("Book renewed for a month successfully.");
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
		<div class="panel-heading"><span class="lead">List of Checked Out Books</span></div>
		<!-- Default Panel Contents -->
		<input type="hidden" id="val1" value="${val1 }">
		<input type="hidden" id="val2" value="${val2 }">
		<input type="hidden" id="useremail" value="${useremail }">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>Publication Year</th>
					<th>Author</th>
					<th>Title</th>
					<th>Publisher</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${books}" var="book">
					<tr>
						<td>${book.publicationYear}</td>
						<td>${book.author}</td>
						<td>${book.title}</td>
						<td>${book.publisher}</td>
						<td><a href="<c:url value='/patron/return-book-${book.id}?name=${useremail}'/>" class="btn btn-danger custom-width">Return</a></td>
						<td><a href="<c:url value='/patron/renew-book-${book.id}?name=${useremail}'/>" class="btn btn-info custom-width">Renew</a></td>
						
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<sec:authorize access="hasRole('USER')">
		<div class="well">
			<a href="<c:url value='/patron/home' />" class="btn btn-primary btn-lg" >Back to Home</a>
		</div>
	</sec:authorize>
</body>
</html>