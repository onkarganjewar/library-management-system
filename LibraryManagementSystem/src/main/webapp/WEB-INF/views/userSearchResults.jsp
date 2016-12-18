<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%-- 
 <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
 --%>
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
function addToCart(){
		$.ajax({
			url : 'http://localhost:8080/Cmpe275-Library-Management-System/patron/addToCart-'+ $('#bookId').val()+'?name='+$('#userid').val(),
			type : 'GET',
			async : false,
			success: function (data) {
				console.log(data);
				if (data == "Success") {
					alert("Book added to the cart.");
					var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
					window.location.replace(url);
				} else if (data == "AlreadyCheckedOut") {
					alert("Book cannot be added to cart. It is already checked out by you.");
					var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
					window.location.replace(url);
				} else if (data == "AlreadyInCart") {
					alert("Book is already in the cart.");
					var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
					window.location.replace(url);
				} else if (data == "Unavailable") {
					if(confirm('Book is unavailable at this time. Would like to be added to the waiting list for this book?')){
						console.log("YES Selected");
						var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/add-to-waiting-list-"+ $('#bookId').val()+'?name='+$('#userid').val();
						$.ajax({
			                type: "get",
			                url: url,
			                success: function(data){
			            		console.log("SUCCESS CALLBACK");
			            		console.log(data);
			            		if (data == "Added") {
			    					alert("You are now added to the waiting list for this book. You will receive an email as soon as the book is available.")
			            			var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
			    					window.location.replace(url);
			    				} else if (data == "Failed") {
			    					alert("Cannot be added to the waiting list. You are already added to the waiting list for this book.")
			    					var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
			    					window.location.replace(url);
			    				} 
			                }, error: function (textStatus, errorThrown) {
			                    console.log("Error getting the data");
			                    console.log("Text status"+textStatus);
			                    console.log("Error thrown"+errorThrown);
			                }
						}); 
					} else {
						var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
						window.location.replace(url);
					}
				} else if (data == "Failure") {
					alert("Cannot add the book. Something went wrong. Try again.");
					var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
					window.location.replace(url);
				} else if (data == "OnHold") {
					if(confirm('Book is currently reserved for another patron right now. Would you like to be added to the waiting list for this book?')){
						console.log("ON HOLD CALLBACK");
						var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/add-to-waiting-list-"+ $('#bookId').val()+'?name='+$('#userid').val();
						$.ajax({
			                type: "get",
			                url: url,
			                success: function(data){
			            		console.log("SUCCESS CALLBACK");
			            		console.log(data);
			            		if (data == "Added") {
			    					alert("You are now added to the waiting list for this book. You will receive an email as soon as the book is available.")
			            			var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
			    					window.location.replace(url);
			    				} else if (data == "Failed") {
			    					alert("Cannot be added to the waiting list. You are already added to the waiting list for this book.")
			    					var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
			    					window.location.replace(url);
			    				} 
			                }, error: function (textStatus, errorThrown) {
			                    console.log("Error getting the data");
			                    console.log("Text status");
			                    console.log(textStatus);
			                    console.log("Error thrown");
			                    console.log(errorThrown);
			                }
						}); 
					} else {
						var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
						window.location.replace(url);
					}
				}
            },
            error: function (textStatus, errorThrown) {
                alert("Error getting the data");
            }
		});
	}
</script>
<script type="text/javascript">
$(document).ready(function() {
	$('#btnSearch').click( function() {
		var bookId = $('#txtSearch').val();
		var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/search-book-"+bookId;
		window.location.replace(url);
	});
});
</script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Library Management System - Search Results</title>
<link href="<c:url value='/static/css/bootstrap.css' />"
	rel="stylesheet"></link>
<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>

<body>
	<div class="col-md-12">
		<div class="col-md-8">
			<h3>
				Welcome <strong>${user}</strong>
			</h3>
		</div>

		<div class="col-md-4">
			<a href="<c:url value="/logout" />" class="btn btn-default"
				style="margin: 20px 0px 0px 300px;">Logout</a>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="form-group row">
			<div class="col-xs-6">
				<input type="hidden" id="val1" value="${val1 }"> <input
					class="form-control" type="text" id="txtSearch"
					placeholder="Search Book Name" name="txtSearch" style="margin: 0px 0px 0px 20px;"> <input
					type="button" class="btn btn-primary" value="Search" id="btnSearch"
					name="btnSearch" style="margin: 10px 0px 0px 20px;">
				<input type="hidden" name ="useremail" id="useremail" value="${useremail }">	
				<input type="hidden" name ="userid" id="userid" value="${userid }">	
				
			</div>
		</div>
		<div class="panel-heading">
			<span class="lead">Search Results</span>
		</div>
		<!-- Default Panel Contents -->
		<hr>
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
							<td><input type="hidden" id="bookName" name="bookName" value="${book.title}" /> ${book.title}</td>
							<td>${book.publisher}</td>
							<td><input type="hidden" id="bookId" name="bookId" value="${book.id}" /></td>
					  		<td><button class="btn btn-success" id="btnId" onclick="addToCart()">Add To Cart</button>
<%-- 					  		<td><a href="<c:url value='/patron/checkout-book-${book.id}?name=${useremail}'/>" class="btn btn-info custom-width">Checkout</a></td>
 --%>					  		
						</tr>
					</c:forEach>
			</tbody>
		</table>
		<hr>
	</div>
	<sec:authorize access="hasRole('USER')">
		<div class="well">
			<a href="<c:url value='/patron/viewCart?name=${userid}' />" class="btn btn-primary" >View Cart</a>
		</div>
	</sec:authorize>
	<sec:authorize access="hasRole('USER')">
		<div class="well">
			<a href="<c:url value='/patron/viewCheckedOutBooks?name=${userid}' />" class="btn btn-primary" >View Checked Out Books</a>
		</div>
	</sec:authorize>
	<sec:authorize access="hasRole('USER')">
		<div class="well">
			<a href="<c:url value='/patron/home' />" class="btn btn-primary" >Back to Home</a>
		</div>
	</sec:authorize>
</body>
</html>