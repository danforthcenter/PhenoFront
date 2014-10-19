<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
<jsp:attribute name="active">Batch Download</jsp:attribute>
<jsp:body>
<div class="container">
	<div class="jumbotron"
		style="padding-left: 30px; padding-right: 30px;">				
	 
		<h2>Image Batch Download</h2>
		<p>
			Enter information in the fields to refine your snapshots. All fields are optional.
		</p>
		<span class="help-block">
			If you are downloading to a server with wget, use --output-document FILENAME.zip to save it to give it a reasonable name. Results are 
			in an uncompressed ZIP archive.
		</span>
		
		<form role="form" id ="query-builder" class="select-data" method="GET" action="<c:url context="/phenofront" value="/massdownload" />" >
			<div class="form-group">
				
				<input type="hidden" name="downloadKey" value="${downloadKey}" id="downloadKey" />
				<input type="hidden" name="experiment" value="${experiment}" />
	 			<input type="hidden" name="logQuery" value="true" />
	 			
				<label for="barcode">
					Barcode Regex Search (DB.+, D[A]{3,}, .+AA, etc.)
				</label>
				<input type="text" name="barcode" id="barcode"
					class="form-control" placeholder="DBAA"
					title="Find Snapshots where the barcode matches the input string, or Regex. Supports POSIX Extended Regex." />
				
				
				<label for="measurementLabel">
					Measurement Label Regex Search
				</label>
				<input type="text" name="measurementLabel" id="measurementLabel"
					class="form-control" placeholder=""
					title="Find Snapshots where the measurement(experiment) label matches the input string. Supports POSIX Extended Regex." />
				
				
				<label for="startTime">
					Snapshots Start Time:
				</label>	
				<input type="text" name="startTime" id="startTime"
					class="form-control" placeholder=""
					title="The earliest a snapshot can occur. Click the box to choose a date." />
				
				  
				<label for="endTime">
					Snapshots End Time:
				</label>
				<input type="text" name="endTime" id="endTime"
					class="form-control" placeholder=""
					title="The latest a snapshot can occur. Click the box to choose a date." />
			</div>
			
			<div class="form-group">
				<label>Image Download Options</label>
			
				<div class="checkbox">
					<label>
						<input type="checkbox" name="includeVisible" value="true" checked />
						Include Visible Light Images
					</label>
				</div>
				
				<div class="checkbox">
					<label>
						<input type="checkbox" name="includeFluorescent" value="true" checked />
						Include Fluorescent Images
					</label>
				</div>
				
				<div class="checkbox">
					<label>
						<input type="checkbox" name="includeInfrared" value="true" checked />
						Include Near Infrared Images
					</label>
				</div>
				
				<div class="checkbox">
					<label>
						<input type="checkbox" name="includeWatering" value="true" checked />
						Include Watering Snapshots
					</label>
				</div>
			</div>
			
			<br>
			
			<p>The download link can be found under preview query</p>
			
			<br>
			
			<a id="previewQuery" class="btn btn-default btn-block ladda-button"
				data-size="l"
				data-style="slide-left"
				data-spinner-color="#FF0000">
				<span class="ladda-label">Preview Query</span>
			</a>
			<br>
			<div id="queryPreview" class ="hidden"></div>
			
		</form>
	</div>
</div>
</jsp:body>
</tags:userarea-template>
</body>

<script type="text/javascript" src="<c:url value="/resources/js/queryElement.js"/>"></script>

<script>
$(document).ready(function(){
	
    $("#barcode").tooltip();
    $("#measurementLabel").tooltip();
    $("#startTime").tooltip();
	$("#endTime").tooltip();
	
	$("#startTime").datetimepicker();
	$("#endTime").datetimepicker();
    
    // Helper methods for the preview operations
 	function displayQuery(query, snapshots) {
 		var query_element = queryElement(query, snapshots, $('#downloadKey'));
 		$("#queryPreview").empty();
 		$("#queryPreview").show();
 		
 		$("#queryPreview").removeClass("hidden alert alert-danger");
 		
 		$("#queryPreview").append(query_element);
 		$("#collapseQuery" + query.id).collapse('show'); // Show query preview by default
 	}
    
 	// Query preview
 	Ladda.bind("#previewQuery");
    $("#previewQuery").click(function(e) {
    	e.preventDefault();
    	var laddaEffect = Ladda.create(this);
    	laddaEffect.start();
    	
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront" value="/userarea/querypreview" />',
			data: $("#query-builder").serialize(),
			success: function(queryJSON) {
				displayQuery(queryJSON["query"], queryJSON["snapshots"]);
				laddaEffect.stop();
			},
			error: function(xhr, status, error) {
				$("#queryPreview").empty();
				$("#queryPreview").show();
				
				$("#queryPreview").removeClass("hidden");
				$("#queryPreview").addClass("alert alert-danger");
				
				$("#queryPreview").text(xhr.responseText);
				laddaEffect.stop();
			}
		});
    });
});
</script>
</html>
