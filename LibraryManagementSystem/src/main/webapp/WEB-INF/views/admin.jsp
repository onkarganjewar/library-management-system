<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%-- 
 <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
 --%>
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#btnSearch').click( function() {
		console.log("button clicked");
		var bookId = $('#txtSearch').val();
		console.log(bookId);
		var url = "http://localhost:8080/Cmpe275-Library-Management-System/search-book-"+bookId;
				window.location.replace(url);
	});
	
	$('#datetimepicker2').on('changeDate', function(e) {
		  console.log(e.date.toString());
		  var timeDate=e.date.toString();
		  var url="http://localhost:8080/Cmpe275-Library-Management-System/demo-checkout-"+timeDate;
		  window.location.replace(url);
		});
	
});
</script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script> 
<script type="text/javascript" src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://tarruda.github.com/bootstrap-datetimepicker/assets/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="http://tarruda.github.com/bootstrap-datetimepicker/assets/js/bootstrap-datetimepicker.pt-BR.js"></script>
  
<script type="text/javascript">

$(function() {
    $('#datetimepicker2').datetimepicker({
      language: 'en',
      pick12HourFormat: true
    });
  });
 

</script>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Admin page</title>
	<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/static/css/bootstrap-datetimepicker.min.css'/>" rel="stylesheet"></link>
    <script src="<c:url value='/static/css/bootstrap-datetimepicker.min.js'/>" ></script>
    
    <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" media="screen"
     href="http://tarruda.github.com/bootstrap-datetimepicker/assets/css/bootstrap-datetimepicker.min.css">
</head>

<body>
  
	<div class="col-md-12">
	<div class="col-md-8">
	<h3>Welcome <strong>${user}</strong></h3>
	</div>
	
	<div class="col-md-4">
		
	<a href="<c:url value="/logout" />" class="btn btn-default" style="margin: 20px 0px 10px 300px;">Logout</a>
	<div class="well">
  <div id="datetimepicker2" class="input-append">
    <input data-format="MM/dd/yyyy HH:mm:ss PP" type="text" name="inputTimeDate" id="inputTimeDate"></input>
    <span class="add-on">
      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
      </i>
    </span>
  </div>
</div>
	</div>
	
	
	</div>
	
	<div class="panel panel-default">
		<div class="form-group row">
 			 <div class="col-xs-6">
    		 	<input class="form-control" type="text" id="txtSearch" placeholder="Search Book Name" name="txtSearch" style="margin:0px 0px 10px 20px;">
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
					<th>Availability</th>
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
						<td>${book.publicationYear}</td>
						<td>${book.libraryLocation}</td>
						<td>${book.availability}</td>
						<td>${book.author}</td>
						<td>${book.title}</td>
						<td>${book.publisher}</td>
						<sec:authorize access="hasRole('ADMIN')">
							<td><a href="<c:url value='/edit-book-${book.id}'/>" class="btn btn-success custom-width">Edit</a></td>
						</sec:authorize>
						<sec:authorize access="hasRole('ADMIN')">
							<td><a href="<c:url value='/delete-book-${book.id}'/>" class="btn btn-danger custom-width">Delete</a></td>
						</sec:authorize>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<sec:authorize access="hasRole('ADMIN')">
		<div class="well">
			<a href="<c:url value='/newBook' />" class="btn btn-primary" >Add New Book</a>
		</div>
	</sec:authorize>
</body>
</html>