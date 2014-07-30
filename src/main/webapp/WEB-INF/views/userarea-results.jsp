<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
	<jsp:attribute name="active">Results</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron"
			style="padding-left:30px; padding-right:30px;">
		
			<h2 class="align-center">Query Results</h2>
			<br />
			
			<div class="table-responsive">
				<table class="table table-striped table-condensed table-bordered" id="results-table">
					<tr id="headers" title="Click to sort by a column">
						<th direction="true" col="0" class="sorter">Time Stamp</th>
						<th direction="true" col="0" class="sorter">Plant Barcode</th>
						<th direction="true" col="0" class="sorter">Measurement Label</th>
						<th direction="true" col="0" class="sorter">Car Tag</th>
						<th direction="true" col="0" class="sorter">Completed</th>
						<th direction="true" col="0" class="sorter">Users</th>
						<th>Images</th>
					</tr>
					
					<c:forEach var="snapshot" items="${ snapshots }">
						<tr>
							<td>${ snapshot.timeStamp }</td>
							<td>${ snapshot.plantBarcode }</td>
							<td>${ snapshot.measurementLabel }</td>
							<td>${ snapshot.carTag }</td>
							<td>${ snapshot.completed }</td>
							<td>~~~~~NOT IMPLEMENTED YET~~~~~</td>
							<td><a class="single-set" href = "<c:url context="/phenofront/userarea" value="/stream" />/${ snapshot.id }" download>Download</a></td>
						</tr>
					</c:forEach>
				</table>
			</div>
			
		</div>
	</div>
	</jsp:body>
</tags:userarea-template>
</body>
<script type="text/javascript" src="<c:url value="/resources/js/tablesorter.js"/>"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	
	
	$("#headers").children().each(function(){
		$(this).tooltip({content: "Click to sort by column."});
	});
    
    $(".sorter").each(function(){
		var dir = $(this).attr("direction");
		var col = $(this).attr("col");
		$(this).click(function(){
			$("#results-table").sortColumn(col, dir);
			dir = !dir;
		});
	});
    
    
});
</script>
</html>
