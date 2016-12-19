<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page  isELIgnored="false" %>
 <%-- 
 <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
 --%>
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link href="https://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css" rel="stylesheet" />
<script src="https://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	
		var val = $('#val1').val();
		console.log(val);
		if (val == "failure")
			alert("Book Cannot be deleted. It is checked out by a patron.");
		else if (val == "exception")
			alert("Something went wrong. Please try again.");
		else if(val=="Success")
			alert("Book Deleted successfully.")
		
	
	
	$('#btnSearch').click( function() {
		console.log("button clicked");
		var bookId = $('#txtSearch').val();
		console.log(bookId);
		var url = "http://localhost:8080/Cmpe275-Library-Management-System/librarian/search-book-"+bookId;
				window.location.replace(url);
	});
		
		$('#btnAddByISBN').click( function() {
			console.log("button clicked");
			var isbn = $('#txtISBN').val();
			console.log(isbn);
			var url = "http://localhost:8080/Cmpe275-Library-Management-System/librarian/bookInfo-"+isbn;
					window.location.replace(url);
		});
		
		$('#datetimepicker').on('changeDate', function(e) {
			  console.log(e.date.toString());
			  alert("Custom Date and Time is now set to "+e.date.toString());
			  var timeDate=e.date.toString();
			  var url="http://localhost:8080/Cmpe275-Library-Management-System/librarian/custom-time-"+timeDate;
			  window.location.replace(url);
			});
		
});


</script>
<script type="text/javascript">

$(function() {
    $('#datetimepicker').datetimepicker({
      language: 'en',
      pick12HourFormat: true
    });
  });
 

</script>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Library Management System - Librarian</title>
	<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/static/css/bootstrap-datetimepicker.min.css'/>" rel="stylesheet"></link>
    <script src="<c:url value='/static/css/bootstrap-datetimepicker.min.js'/>" ></script>
     <link href="<c:url value='/static/css/bootstrap-datetimepicker.css'/>" rel="stylesheet"></link>
    <script src="<c:url value='/static/css/bootstrap-datetimepicker.js'/>" ></script>
        
</head>

<body>
	<div class="col-md-12">
	<div class="col-md-8">
	<h3>Welcome <strong>${user}</strong></h3>
	</div>
	
	<div class="col-md-4">
	<a href="<c:url value="/logout" />" class="btn btn-default" style="margin: 20px 0px 0px 300px;">Logout</a>
	
	<div class="well">
	
			    	<div id="datetimepicker" class="input-append date form_datetime">
	    				<input size="16" type="text" value="${dateTime}" style="width:230px;"  readonly>
	    				<span class="add-on"><i class="icon-th" ></i></span>
					</div>
		 
	</div>
	
	
	</div>
	</div>
	<div class="panel panel-default">
		<div class="form-group row">
 			 <div class="col-xs-6">
 			 <input type="hidden" id="val1" value="${val1 }">
    		 	<input class="form-control" type="text" id="txtSearch" placeholder="Search Book Name" name="txtSearch" style="margin:0px 0px 0px 20px; height:30px;">
    		 	<input type="button" class="btn btn-primary" value="Search" id="btnSearch" name="btnSearch" style="margin:10px 0px 0px 20px;">
  			</div>
		</div>
		<div class="panel-heading"><span class="lead">List of Books</span></div>
		<!-- Default Panel Contents -->
		<table class="table table-hover">
			<thead>
				<tr>
					<th>Publication Year</th>
					<th>Location</th>
					<th>Status</th>
					<th>Author</th>
					<th>Title</th>
					<th>Publisher</th>
					<sec:authorize access="hasRole('ADMIN')">
						<th width="100"></th>
					</sec:authorize>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${books}" var="book">
					<tr>
						<td id="idField" style="display:none;">${book.id}</td>
						<td>${book.publicationYear}</td>
						<td>${book.libraryLocation}</td>
						<td>${book.availability}</td>
						<td>${book.author}</td>
						<td>${book.title}</td>
						<td>${book.publisher}</td>
							<td><a href="<c:url value='/librarian/edit-book-${book.id}'/>" class="btn btn-success custom-width">Edit</a></td>
							<td><a href="<c:url value='/librarian/delete-book-${book.id}'/>" class="btn btn-danger custom-width">Delete</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<sec:authorize access="hasRole('ADMIN')">
		<div class="well">
			<a href="<c:url value='/librarian/new-book' />" class="btn btn-primary" >Add New Book</a>
		</div>
		<div class="row well" style="margin:0px 0px 10px 0px;">
  			<div class="col-lg-6">
   				 <div class="input-group">
     			 <span class="input-group-btn">
       			 <button class="btn btn-primary" type="button" id="btnAddByISBN" name="btnAddByISBN">Add New Book using ISBN</button>
      			</span>
      				<input type="text" placeholder="Add ISBN" name="txtISBN" id="txtISBN" style="height:35px; width:150px; margin:1px 0px 0px 0px;">
    			</div>
 			 </div>
		</div>
	</sec:authorize>
</body>
</html>