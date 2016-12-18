<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<html>
 <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script>
function checkValid(){
	var inputCopies=$('#copies').val();

	if(isNaN(inputCopies))
	alert("Enter valid number of copies. Input value is an invalid number.");
	}
</script>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Book Registration Form</title>
    <link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>
 
<body>
    <div class="generic-container">
       <div class="well lead">Book Registration Form</div>
        <form:form method="POST" modelAttribute="book" class="form-horizontal">
            <form:input type="hidden" path="id" id="id"/>
             
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="publicationYear">Publication Year</label>
                    <div class="col-md-7">
                        <form:input type="text" path="publicationYear" id="publicationYear" class="form-control input-sm" autofocus="true"/>
                        <div class="has-error">
                            <form:errors path="publicationYear" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
     
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="libraryLocation">Location</label>
                    <div class="col-md-7">
                        <form:input type="text" path="libraryLocation" id="libraryLocation" class="form-control input-sm" />
                        <div class="has-error">
                            <form:errors path="libraryLocation" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
          
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="copies">Copies</label>
                    <div class="col-md-7">
                        <input type="text" id="copies" class="form-control input-sm" name=copies onblur="checkValid()" value = "${copies }" />
                        <div class="has-error">
                            <form:errors path="copies" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
     
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="availability">Availability</label>
                    <div class="col-md-7">
                        <form:input type="text" path="availability" id="availability" class="form-control input-sm"  value="Available" readonly="true"/>
                        <div class="has-error">
                            <form:errors path="availability" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
     
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="keywords">Keywords</label>
                    <div class="col-md-7">
                        <form:input type="text" path="keywords" id="keywords" class="form-control input-sm" />
                        <div class="has-error">
                            <form:errors path="keywords" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="author">Author</label>
                    <div class="col-md-7">
                        <form:input type="text" path="author" id="author" class="form-control input-sm" />
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
                        <form:input type="text" path="title" id="title" class="form-control input-sm" />
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
                        <form:input type="text" path="callNumber" id="callNumber" class="form-control input-sm" />
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
                        <form:input type="text" path="publisher" id="publisher" class="form-control input-sm" />
                        <div class="has-error">
                            <form:errors path="publisher" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>
     
            <div class="row">
                <div class="form-actions floatRight">
                    <c:choose>
                        <c:when test="${edit}">
                            <input type="submit" value="Update" class="btn btn-primary btn-sm"/>  <a href="<c:url value='/librarian/home'  />" class="btn btn-danger btn-sm">Cancel</a>
                        </c:when>
                        <c:otherwise>
                            <input type="submit" value="Register" class="btn btn-primary btn-sm"/> <a href="<c:url value='/librarian/home'/>" class="btn btn-danger btn-sm">Cancel</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
    </div>
</body>
</html>