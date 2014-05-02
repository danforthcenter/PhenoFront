<%@ attribute name="active" required="true" rtexprvalue="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
<title>${ active }</title>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/bootstrap.min.css"/>"></link>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/custom.css"/>"></link>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.10.2.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.validate.min.js"/>"></script>
</head>
<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">PhenoFront</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li 
					<c:if test = "${ active == 'User Management'}">class = "active"</c:if>
					><a href="<c:url context="/phenofront/admin" value="/users"/>">User Management</a></li>
				<li 
					<c:if test = "${ active == 'User Area'}">class = "active"</c:if>
					><a href="<c:url context="/phenofront/userarea" value="/"/>">User Area</a></li>
					
				<li><a href="<c:url value="/j_spring_security_logout"/>">Log-out</a></li>
			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
</div>
<jsp:doBody/>
</html>
