<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<html>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

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
		<input type="hidden" id="userid" value="${userid }">
		<div class="panel-heading"><span class="lead">Please Verify Checkout Details</span></div>
		<div class="row" style="display: none;">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="userid">UserId</label>
                    <div class="col-md-7">
                        <input type="text" path="userid" id="userid" class="form-control input-sm" readonly="true" value="${userid}"/>
                    </div>
                </div>
            </div>
            
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
				<c:url var="fooUrl" value="/patron/checkout?name=${userid }" />
				<form:form id="frmCheckout" action="${fooUrl }" method="POST"
					modelAttribute="bookListWrapper">

					<c:forEach items="${bookListWrapper.booksList}" varStatus="i"
						var="book">
					<form:input type="hidden" path="booksList[${i.index}].id"
									value="${book.id}" />
							
						<tr>
							<td><form:input type="hidden" path="booksList[${i.index}].publicationYear"
									value="${book.publicationYear}"/>${book.publicationYear}</td>
							<td><form:input type="hidden" path="booksList[${i.index}].libraryLocation"
									value="${book.libraryLocation}"/>${book.libraryLocation}</td>
							<td><form:input type="hidden" path="booksList[${i.index}].author"
									value="${book.author}"/>${book.author}</td>
							<td><form:input type="hidden" path="booksList[${i.index}].title"
									value="${book.title}"/>${book.title}</td>
							<td><form:input type="hidden" path="booksList[${i.index}].publisher"
									value="${book.publisher}"/>${book.publisher}</td>
						</tr>
					</c:forEach>
				</form:form>
			</tbody>
		</table>
		<div class="row">
                <div class="form-actions floatRight">
                  	 <input class="btn btn-success custom-width" type="submit" value="Checkout" id="Checkout" name="Checkout" form="frmCheckout"> <a href="<c:url value='/patron/home' />" class="btn btn-danger custom-width">Cancel</a>
                </div>
            </div>
	</div>
</body>
</html>