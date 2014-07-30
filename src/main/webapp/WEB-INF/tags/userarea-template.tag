<%@ attribute name="active" required="true" rtexprvalue="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>${ active }</title>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/bootstrap.min.css"/>"></link>
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/jquery-ui-1.10.3.custom.min.css"/>"></link>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/custom.css"/>"></link>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.10.2.min.js"/>"></script>
<script type="text/javascript" src='<c:url value="/resources/js/jquery-ui-1.10.3.custom.min.js"/>'></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui-timepicker-addon.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
</head>
<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<c:url context="/phenofront/userarea" value="/querybuilder"/>">PhenoFront</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
			
				<li <c:if test = "${ active == 'Home'}">class = "active"</c:if>>
					<a href="<c:url context="/phenofront/userarea" value="/querybuilder"/>">Home</a>
				</li>
				
				<li <c:if test = "${ active == 'Batch Download'}">class = "active"</c:if>>
					<a href="<c:url context="/phenofront/userarea" value="/querybuilder"/>">Query Builder</a>
				</li>
				
				<li <c:if test = "${ active == 'Select Experiment'}">class = "active"</c:if>>
					<a href="<c:url context= "/phenofront" value="/selectexperiment"/>">Select Experiment</a>
				</li>
				
				<li <c:if test = "${ active == 'Profile'}">class = "active"</c:if>>
					<a href="<c:url context= "/phenofront/userarea" value="/profile"/>">Profile</a>
				</li>

				<li>
					<a href="<c:url value="/j_spring_security_logout"/>">Log-out</a>
				</li>
				
			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
</div>
<jsp:doBody/>
</html>
