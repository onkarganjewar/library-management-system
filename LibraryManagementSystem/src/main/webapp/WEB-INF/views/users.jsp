<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$('#btnSearch').click( function() {
		console.log("button clicked");
		var bookId = $('#txtSearch').val();
		var url = "http://localhost:8080/Cmpe275-Library-Management-System/user/search-book-"+bookId;
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
    		 	<input class="form-control" type="text" id="txtSearch" placeholder="Search Book Name">
				<input type="button" class="btn btn-primary" value="Search" id="btnSearch" name="btnSearch" style="margin:10px 0px 0px 0px;" />  			
			</div>
		</div>
		<div class="panel-heading"><span class="lead">List of Books</span></div>
		<!-- Default Panel Contents -->
		<table class="table table-hover">
			<thead>
				<tr>
					<th>Publication Year</th>
					<th>Location</th>
					<th>Availability</th>
					<th>Author</th>
					<th>Title</th>
					<th>Publisher</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${books}" var="book">
					<tr>
						<td>${book.publicationYear}</td>
						<td>${book.libraryLocation}</td>
						<td>${book.availability}</td>
						<td>${book.author}</td>
						<td>${book.title}</td>
						<td>${book.publisher}</td>
						<sec:authorize access="hasRole('USER')">
							<td><a href="<c:url value='/user/checkout-book-${book.id}'/>" class="btn btn-success custom-width">Checkout</a></td>
						</sec:authorize>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>