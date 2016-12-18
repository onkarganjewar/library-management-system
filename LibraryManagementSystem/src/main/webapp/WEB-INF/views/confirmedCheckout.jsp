<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	
	var val = $('#val1').val();
	var book=$('#bookTitle').val();
	console.log(val);
	if (val == "BookCopyNotAvailable"){
	alert(book+" is not available.You would be added to the waiting list.");
	var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/viewCart?name="+ $('#userid').val();
	window.location.replace(url);
	}
	else if (val == "Failure"){
		alert("Something went wrong");
		var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/viewCart?name="+ $('#userid').val();
		window.location.replace(url);
		}
	else if (val == "Duplicate"){
		alert(book +" already checked by you.");
		var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/viewCart?name="+ $('#userid').val();
		window.location.replace(url);
		}
	else if (val == "OverallCheckoutLimitReached"){
		alert("You can checkout only 10 books at any time of the year.Please return a few books to checkout.");
		var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/viewCart?name="+ $('#userid').val();
		window.location.replace(url);
		}
	else if (val == "DayCheckoutLimitReached"){
		alert("You can checkout only 5 books in a day.Please try again tomorrow.");
		var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/viewCart?name="+ $('#userid').val();
		window.location.replace(url);
		}
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
		<input type="hidden" id="useremail" value="${useremail }">
				<input type="hidden" id="val1" value="${val1 }">
		<input type="hidden" id="bookTitle" value="${bookTitle }">
		<div class="panel-heading"><span class="lead">Check Out Details</span></div>
		<div class="row" style="display: none;">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="userid">UserId</label>
                    <div class="col-md-7">
                        <input type="text" path="userid" id="userid" class="form-control input-sm" readonly="true" value="${userid}"/>
                    </div>
                </div>
            </div>
            
		<!-- Default Panel Contents -->
            <hr>
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="dueDate">Due Date</label>
                    <div class="col-md-7">
                    	<fmt:parseDate value="${widget}" pattern="yyyy-MM-dd" 
                          var="parsedDate" type="date" />
						<fmt:formatDate value="${parsedDate}" var="widget" 
                        	   type="date" pattern="mm/dd/yyyy" />
                        <input type="text" id="dueDate" value="${due }" class="form-control input-sm" readonly="true"/>
                    </div>
                </div>
            </div>
            
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
						</tr>
					</c:forEach>
			</tbody>
		</table>	</div>
	<sec:authorize access="hasRole('USER')">
		<div class="well">
			<a href="<c:url value='/patron/home' />" class="btn btn-primary btn-lg" >Back to Home</a>
		</div>
	</sec:authorize>
	
</body>
</html>