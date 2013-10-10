<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:base-template>
	<jsp:attribute name="active">Log-in</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<div class="alert alert-danger ${hidden}">${error}</div>
			
			<form class="form-signin" method="POST"  action="<c:url context="/phenofront" value="/j_spring_security_check"/>">
	        	<h2 class="form-signin-heading">Please sign in</h2>
	        	<input name="j_username" id="j_password" type="text" class="form-control"	placeholder="Username" autofocus=""/>
	        	<input name = "j_password" id = "j_password" type="password" class="form-control"	placeholder="Password"/>
	        	<label class="checkbox">
	          		<input type="checkbox" value="remember-me"> Remember me
	        	</label>
	        	<button class="btn btn-lg btn-default btn-block" type="submit">Sign in</button>
	      	</form>
		
			
		</div>
	</div>
	</jsp:body>
</tags:base-template>
</body>
</html>
