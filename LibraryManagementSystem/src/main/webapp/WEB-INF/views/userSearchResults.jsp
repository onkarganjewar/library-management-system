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
				} 
				else if (data == "Failure") {
					alert("Cannot add the book. Something went wrong. Try again.");
					var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/cart-search-book-"+ $('#bookName').val()+'?name='+$('#userid').val();
					window.location.replace(url);
				} 
            },
            error: function (textStatus, errorThrown) {
                alert("Error getting the data");
            }
		});
	}
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
					placeholder="Search Book Name" name="txtSearch"> <input
					type="button" class="btn btn-primary" value="Search" id="btnSearch"
					name="btnSearch" style="margin: 10px 0px 0px 0px;">
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
							<td>${book.author}</td>
							<td><input type="hidden" id="bookName" name="bookName" value="${book.title}" /> ${book.title}</td>
							<td>${book.publisher}</td>
							<td><input type="hidden" id="bookId" name="bookId" value="${book.id}" /></td>
					  		<td><button class="btn btn-success" id="btnId" onclick="addToCart()">Add To Cart</button>
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
</body>
</html>