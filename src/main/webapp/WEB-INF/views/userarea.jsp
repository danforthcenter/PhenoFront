<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
	<jsp:attribute name="active">Home</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<h1>Welcome back ${ username }!</h1>
			<img src="<c:url value="/resources/images/placeholder.png"/>"></img>
			<div class="pull-right">
			<table class="jumbotron">
				<tr><td>Last logged on</td><td>Jun-1-2013</td></tr>
				<tr><td>Group</td><td>Bioinformatics Core</td></tr>
				<tr><td>Data usage</td><td>121.55GB/250GB</td></tr>
			</table>
			<h3>Current Jobs</h3>
			<li>Arabidopsis Jun-13-2013</li>
			</div>
		</div>
	</div>
	</jsp:body>
</tags:userarea-template>
</body>
</html>
