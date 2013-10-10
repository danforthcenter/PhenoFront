<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
	<jsp:attribute name="active">Schedule</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<h1>Schedule a Job</h1>
			<p>
			Need some display about the current schedule, maybe something from status even.
			</p>
			
			<form id="scheudule-form" method="post" action="">
			 <div class="form-group">
			    <label for="experimentName">Experiment Name</label>
			    <input type ="text" class="form-control" placeholder="Experiment Name"></input>
			 </div>
			 <div class="form-group">
			    <label for="startDate">Start Date</label>
			    <input class="form-control" type ="date"></input>
			 </div>
			 <div class="form-group">
			    <label for="endDate">End Date</label>
			    <input class="form-control" type ="date"></input>
			 </div>
			 <input type="submit" class="btn">button</input>
			</form>
		</div>
	</div>
	</jsp:body>
</tags:userarea-template>
</body>
</html>
