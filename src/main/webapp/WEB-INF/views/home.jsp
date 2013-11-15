<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:base-template>
	<jsp:attribute name="active">Home</jsp:attribute>
	<jsp:body>
	<div class="container">
	<script type="text/javascript" src="<c:url value="/resources/js/tablesorter.js"/>"></script>
		<div class="jumbotron">
			<img src ="resources/images/placeholder.png"></img>
			<h1>Hello world! :3</h1>

			<P>The time on the server is ${serverTime}.</P>
			<table id="labrat">
				<tr><th>header1<span direction="true" col="0" class="sorter glyphicon glyphicon-sort"></span></th>
					<th>header2<span direction="true" col="1" class="sorter glyphicon glyphicon-sort"></span></th></tr>
				<tr><td>val1</td><td>val2</td></tr>
				<tr><td>val5</td><td>vala</td></tr>
				<tr><td>val17</td><td>eenam</td></tr>
				
			</table>

		</div>
	</div>
	</jsp:body>
</tags:base-template>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$(".sorter").each(function(){
		var dir = $(this).attr("direction");
		var col = $(this).attr("col");
		$(this).click(function(){
			$("#labrat").sortColumn(col, dir);
			dir = !dir;
		});
	});
});

</script>
</html>
