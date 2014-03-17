<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:base-template>
	<jsp:attribute name="active">Select Experiment</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<h1>Select Experiment</h1>
			<p>
			Choose then experimental data you would like to view. If the experiment you are looking for is not listed, contact
			the PI or the system administrator.
			</p>
			<form id = "selection">
				<select class="form-control" name="experimentName" id="experimentName">
			      <c:forEach var='experiment' items='${allowed}'>
			      	<option value="${experiment.experimentName }">${ experiment.experimentName }</option>
			      </c:forEach>
				</select>
				<div class="result hidden"></div>
				<button type="submit" class="btn btn-default btn-block btn-large">Submit</button>
			</form>
		</div>
	</div>
	</jsp:body>
</tags:base-template>
<script type="text/javascript">
$(document).ready(function(){
	$('#selection').submit(function(){
		var form = $(this);
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront" value="/selection" />',
			data: $(this).serialize(),
			success: function(data){
				window.location.href = '<c:url context="/phenofront" value="/userarea/results"/>';
			},
			error: function(xhr, status, error) {
				form.find('.result').removeClass('hidden alert alert-success');
				form.parent().find('.result').addClass("alert alert-danger");
				form.parent().find('.result').text(xhr.responseText);
			}
		});
		return false;
	});
});
</script>
</body>
</html>
