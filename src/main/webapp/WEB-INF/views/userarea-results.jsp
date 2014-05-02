<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
	<jsp:attribute name="active">Results</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron" style="padding-left:30px; padding-right:30px;">
			<h1>Recent Entries</h1>
			<h3>${ date }</h3>
			<div class="table-responsive">
				<table class="table table-striped table-condensed table-bordered" id="results-table">
					<tr id="headers" title="Click to sort by a column">
						<th direction="true" col="0" class="sorter">Plant Barcode</th>
						<th direction="true" col="0" class="sorter">Time Stamp</th>
						<th direction="true" col="0" class="sorter">Measurement Label</th>
						<th direction="true" col="0" class="sorter">Car Tag</th>
						<th direction="true" col="0" class="sorter">Completed</th>
						<th>Images</th>
						<th>Select</th>
					</tr>
					
					<c:forEach var='snapshot' items='${ snapshots }'>
					<!-- plantBarcode=DAAA000626, carTag=, timeStamp=2013-09-25 09:23:40.764, weightBefore=842.0, weightAfter=881.0, waterAmount=40.0, completed=true, id=32034, tiles -->
						<tr>
							<td>${ snapshot.plantBarcode }</td>
							<td>${ snapshot.timeStamp }</td>
							<td>${ snapshot.measurementLabel }</td>
							<td>${ snapshot.carTag }</td>
							<td>${ snapshot.completed }</td>
							<td><a class="single-set" href = "<c:url context="/phenofront/userarea" value="/stream" />/${ snapshot.id }" download>Download</a></td>
						</tr>
					</c:forEach>	
				</table>
			</div>
			<!--  Modal button -->
			<div class="col-lg-12 btn-nopad">
				<a data-toggle="modal" href="#select-data" class="btn btn-default btn-block btn-tile btn-secondary">
					Select Data				
				</a>
			</div>
			<!--
			<div class="col-lg-4 btn-nopad">
				<a href="#" class="btn btn-default btn-block btn-tile btn-tertiary">
					Download Selected Data
				</a>
			</div>
			-->
			<!-- 
			<div class="col-lg-4 btn-nopad">
				<a href="#" class="btn btn-default btn-block btn-tile btn-quartenary">
					Download All Data
				</a>
			</div>
			-->
			<tags:modal-template title="Select Data" id="select-data">
				<jsp:body>
					 <p>
					 Enter information in the fields to refine your snapshots. All fields are optional. The default query will return the 20 most recent snapshots.
					 </p>
					 <form role="form" class="select-data" method="GET" action="<c:url context="/phenofront/userarea" value="/filterresults" />">
	        	    	<input type=hidden class = "userid" name="userid" value="${ user.userId }" />
					  	<div class="form-group">
					  		<!--  
					    	<label for="barcode">Barcode search (DBAA, DAAA, *AA..):</label>
					    	
							<input type="text" id ="barcode" name="barcode" class="form-control" placeholder="DBAA" title="Only snapshots which contain your input will be return. This can be used to specify conditions and plant types. (DBAA etc)"/>						  
							-->
							<label for="after">Snapshots After:</label>	
							<input type="text" class="form-control" name="after" id="after" title= "Click the box to choose a date."/>			  
							<label for="before">Snapshots Before:</label>
							<input type="text" class="form-control" name="before" title="Click the box to choose a date." id="before"/>
							<!--   This label seems like a bad choice
							<label for="max">Maximum Number of Results:</label>
							<input type="text" class="form-control" name="max" id="max" title="Enter the maximum amount of snapshots to return. Larger numbers will take longer to load." placeholder="20"/>
					  		-->
					  	</div>
					  	<div class="result hidden"></div>
					    <button type="submit" class="btn btn-default btn-block btn-large">Submit</button>
					 </form>
				</jsp:body>
			</tags:modal-template>
			
		</div>
	</div>
	</jsp:body>
</tags:userarea-template>
</body>
<script type="text/javascript" src="<c:url value="/resources/js/tablesorter.js"/>"></script>
<script type="text/javascript">
$(document).ready(function(){
	$('#before').datetimepicker();
	$('#after').datetimepicker();
	$('#headers').children().each(function(){
		$(this).tooltip({content: "Click to sort by column."});
	});
    $( '#barcode' ).tooltip();
    $( '#before' ).tooltip();
    $( '#after' ).tooltip();
    $( '#max' ).tooltip();
    $(".sorter").each(function(){
		var dir = $(this).attr("direction");
		var col = $(this).attr("col");
		$(this).click(function(){
			$("#results-table").sortColumn(col, dir);
			dir = !dir;
		});
	});
    
    $(".ssingle-set").click(function(event){
    	//do some ajax to get the image url (should process image too) then perform download action
    	event.preventDefault();
    	$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront/userarea" value="/fetchimage" />',
			data:"id=" + $(this).attr("id"),
			success: function(data){
	//			$(this).preventDefault();  //stop the browser from following
	    		window.location.href = '<c:url context="/phenofront/resources" value="/image_sets" />/'+data;
			},
			error: function(xhr, status, error) {
				console.log(xhr.responseText);
			}
		});
    		
    });
    
});
</script>
</html>
