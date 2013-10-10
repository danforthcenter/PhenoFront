<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:base-template>
	<jsp:attribute name="active">Home</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<img src ="resources/images/placeholder.png"></img>
			<h1>Hello world! :3</h1>

			<P>The time on the server is ${serverTime}.</P>
			

		</div>
	</div>
	</jsp:body>
</tags:base-template>
</body>
</html>
