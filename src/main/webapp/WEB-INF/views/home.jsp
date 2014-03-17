<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:base-template>
	<jsp:attribute name="active">Home</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<h1 style="padding-bottom:30px">Welcome to the Phenotyping web portal!</h1>
			
			<img style="width:50%;" class="col-md-6" src="<c:url value="/resources/images/phenotyper3.jpg"/>"></img>
			<div class="pull-right col-md-6">
				Login to begin, if you do not have an account, contact an administrator.
			
			</div>
		</div>
	</div>
	</jsp:body>
</tags:base-template>
</body>


</html>
