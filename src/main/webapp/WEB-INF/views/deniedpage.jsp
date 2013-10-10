<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:base-template>
	<jsp:attribute name="active">Access Denied</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<h1>Access Denied. </h1>
			<p>You do not have permissions to view this page. Contact the administrator for details.</p>
		</div>
	</div>
	</jsp:body>
</tags:base-template>
</body>
</html>
