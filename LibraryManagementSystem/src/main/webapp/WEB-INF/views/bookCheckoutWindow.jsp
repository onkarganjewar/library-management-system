<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#Checkout').click( function() {
			$.ajax({
				url : 'http://localhost:8080/Cmpe275-Library-Management-System/patron/confirm-checkout-book-'+ $('#id').val()+'?name='+$('#userid').val(),
				type : 'GET',
				success: function (data) {
					console.log(data);
					if (data == "Success") {
						alert("Book Checked out successfully.Email regarding the book details will be sent to you shortly.");
						var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/confirmedCheckout?bookId="+ $('#id').val() +"&&userId=" +$('#userid').val();
						window.location.replace(url);
					} else if (data == "Duplicate") {
						alert("The book is already issued by you. Please checkout a different book.");
						var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/home";
						window.location.replace(url);
					} else if (data == "Failure") {
						alert("Requested book cannot be issued right now. You will be notified as soon as the book is available.");
						var url = "http://localhost:8080/Cmpe275-Library-Management-System/patron/home";
						window.location.replace(url);
					} else if (data == "Exception") {
						alert("Cannot issue book. Please try again.")
					} else if (data == "DayCheckoutLimit") {
						alert("You cannot issue more than 5 books in a day. Please try again tomorrow.")
					} else if (data == "TotalCheckoutLimit") {
						alert("You cannot issue more than 10 books at any time. Please return some books and try again.")
					}
	            },
	            error: function (textStatus, errorThrown) {
	                alert("Error getting the data");
	            }
			});
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
		<input type="hidden" id="useremail" value="${useremail }">
		<div class="panel-heading"><span class="lead">Please Verify Checkout Details</span></div>
		<!-- Default Panel Contents -->
		<form:form method="POST" modelAttribute="book" class="form-horizontal">
            <form:input type="hidden" path="id" id="id"/>
            
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="publicationYear">Publication Year</label>
                    <div class="col-md-7">
                        <form:input type="text" path="publicationYear" id="publicationYear" class="form-control input-sm" readonly="true"/>
                        <div class="has-error">
                            <form:errors path="publicationYear" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
         
             
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="author">Author</label>
                    <div class="col-md-7">
                        <form:input type="text" path="author" id="author" class="form-control input-sm" readonly="true"/>
                        <div class="has-error">
                            <form:errors path="author" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="title">Title</label>
                    <div class="col-md-7">
                        <form:input type="text" path="title" id="title" class="form-control input-sm" readonly="true"/>
                        <div class="has-error">
                            <form:errors path="title" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="callNumber">Call Number</label>
                    <div class="col-md-7">
                        <form:input type="text" path="callNumber" id="callNumber" class="form-control input-sm"  readonly="true"/>
                        <div class="has-error">
                            <form:errors path="callNumber" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="publisher">Publisher</label>
                    <div class="col-md-7">
                        <form:input type="text" path="publisher" id="publisher" class="form-control input-sm" readonly="true"/>
                        <div class="has-error">
                            <form:errors path="publisher" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
            
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
            
            <div class="row" style="display: none;">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="userid">UserId</label>
                    <div class="col-md-7">
                        <input type="text" path="userid" id="userid" class="form-control input-sm" readonly="true" value="${userid}"/>
                    </div>
                </div>
            </div>
            
            
            
            <div class="row">
                <div class="form-actions floatRight">
                  	 <input type="button" class="btn btn-success custom-width" value="Checkout" id="Checkout" name="Checkout"> <a href="<c:url value='/patron/home' />" class="btn btn-danger custom-width">Cancel</a>
                </div>
            </div>
        </form:form>
	</div>
</body>
</html>