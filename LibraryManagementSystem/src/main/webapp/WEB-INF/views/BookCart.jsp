<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	
	$('input[type=checkbox]').prop('checked', false);	
	
	
	
	
		$('input[type=checkbox]').change(function(e){
			
			if ($('input[type=checkbox]:checked').length==0) 
				
			     $('input[type="submit"]').prop('disabled', true);
			else
				$('input[type="submit"]').prop('disabled', false);
			
				   if ($('input[type=checkbox]:checked').length > 5) {
			        $(this).prop('checked', false)
			        alert("You can checkout only 5 books in a day");
			   }
			})
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
		<div class="panel-heading"><span class="lead">Book Cart</span></div>
		<!-- Default Panel Contents -->
		<input type="hidden" id="val1" value="${val1 }">
				<input type="hidden" id="userid" value="${userid }">
		
		<table class="table table-hover">
			<thead>
				<tr>
					<th>Check Out</th>
					<th>Publication Year</th>
					<th>Author</th>
					<th>Title</th>
					<th>Publisher</th>
				</tr>
			</thead>
			<tbody><c:url var="CheckoutUrl" value="/patron/confirm-checkout?name=${userid} " />
 				<form:form id="frmCheckout" action="${CheckoutUrl }" method="POST" modelAttribute="bookListWrapper">
 					<c:forEach items="${bookListWrapper.booksList}" varStatus="i"
 						var="book">
				
					<tr>
					<td><form:checkbox path="booksList[${i.index}].id"
									value="${book.id}" checked="false"/>
						</td>
						<td>${book.publicationYear}</td>
						<td>${book.author}</td>
						<td>${book.title}</td>
						<td>${book.publisher}</td>
						<td><a href="<c:url value='/patron/remove-from-cart-${book.id}?name=${userid}'/>" class="btn btn-danger custom-width">Remove</a></td>
					</tr>
				</c:forEach>
 				</form:form>
			</tbody>
		</table>
	</div>
	<div>
 		<input type="submit" value="Check Out" class="btn btn-success" form="frmCheckout" style="margin:0px 0px 0px 20px;" disabled="true" id="btnCheckout">
		</div>
	<sec:authorize access="hasRole('USER')">
		<div class="well">
			<a href="<c:url value='/patron/home' />" class="btn btn-primary btn-lg" >Back to Home</a>
		</div>
	</sec:authorize>
</body>
</html>