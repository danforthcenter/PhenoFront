<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
	<jsp:attribute name="active">Batch Download</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron"
				style="padding-left: 30px; padding-right: 30px;">				
				<p style="min-height:00px">
					<!-- I suck -->
					 
				<h2>Image Batch Download</h2>
				<p>
					 Enter information in the fields to refine your snapshots. All fields are optional.
					 </p>
					 <span class="help-block">
					 	If you are downloading to a server with wget, use --output-document FILENAME.zip to save it to give it a reasonable name. Results are 
					 	in an uncompressed ZIP archive.
					 </span>
					 <form role="form" id ="query-builder" class="select-data" method="GET" action="<c:url context="/phenofront" value="/massdownload" />">
					  	<div class="form-group">
					  		<input type="hidden" value="${activeExperiment}" name="activeExperiment"/> 
					  		
					    	<label for="plantBarcode">Barcode Regex search (DB.+, D[A]{3,}, .+AA, etc.):</label>
							<input type="text" id="plantBarcode" name="plantBarcode"
							class="form-control" placeholder="DBAA"
							title="Find Snapshots where the barcode matches the input string, or Regex. Supports POSIX Extended Regex." />
							
							<a id ="performBarcodeMatch" class="btn btn-default btn-block btn-sm">Preview Regex (Not exhaustive)</a>
							<textarea class="form-control" rows="3" id="matchedBarcodes"></textarea>
							
							<label for="measurementLabel">Measurement Label Regex Search</label>
							<input type="text" id="measurementLabel" name="measurementLabel"
							class="form-control" placeholder=""
							title="Find Snapshots where the measurement(experiment) label matches the input string. Supports POSIX Extended Regex." />
							
							<a id ="performMeasurementLabelMatch" class="btn btn-default btn-block btn-sm">Preview Regex (Not exhaustive)</a>
							<textarea class="form-control" rows="3" id="matchedMeasurementLabels"></textarea>
													  
							<label for="startTime">Snapshots After:</label>	
							<input type="text" class="form-control" name="startTime" id="startTime"
							title="Click the box to choose a date." />
										  
							<label for="endTime">Snapshots Before:</label>
							<input type="text" class="form-control" name="endTime" id="endTime"
							title="Click the box to choose a date." />
						</div>
						<div class="form-group">
							<label>Image types to download</label>
							
							<div class="checkbox">
							  <label>
							  	<input type="checkbox" name="fluo" value="true" checked>
								Fluorescent Images
							  </label>
							</div>
							
							<div class="checkbox">
							  <label>
							  	<input type="checkbox" name="nir" value="true" checked>
								Near Infrared Images
							  </label>
							</div>
							
							<div class="checkbox">
							  <label>
							  	<input type="checkbox" name="vis" value="true" checked>
								Visible Light Images
							  </label>
							</div>
							
							<div class="checkbox">
							  <label>
							  	<input type="checkbox" name="watering" value="false" checked>
								Include Watering Snapshots
							  </label>
							</div>
							
							<input type="hidden" name="downloadKey" value="${ downloadKey }">
					  	</div>
					  	<div class="result hidden"></div>
					    <a id ="submit"
							class="btn btn-default btn-block btn-large">Submit</a>
					 </form>
		</div>
	</div>
	</jsp:body>
</tags:userarea-template>
</body>
<script type="text/javascript" src="<c:url value="/resources/js/computeRegex.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/convertToList.js"/>"></script>
<script>
$(document).ready(function(){
	$('#startTime').datetimepicker();
	$('#endTime').datetimepicker();
	
	$('#headers').children().each(function(){
		$(this).tooltip({content: "Click to sort by column."});
	});
	
    $( '#measurementLabel' ).tooltip();
    $( '#plantBarcode' ).tooltip();
    $( '#startTime' ).tooltip();
	$( '#endTime' ).tooltip();
    $( '#max' ).tooltip();
    
    $("#submit").submit(function(){
        $(this).find('input[type=submit]').attr('disabled', 'disabled');
    });
    $( "#submit" ).click(function(){
    	$(".result").removeClass("hidden").show().addClass("alert alert-success");
    	var serialized = '<c:url context="/phenofront" value="/massdownload" />' +"?"+ $("#query-builder").serialize();
    	$(".result").html("<a href='"+ serialized + "'>Your Download Link </a>This link may only be used once.");
    });
    
    var exampleBarcodes = convertToList("${exampleBarcodes}");
    $("#performBarcodeMatch").click(function() {
    	$("#matchedBarcodes").val(computeRegex($("#plantBarcode").val(), exampleBarcodes));
    });
    
    
    var exampleMeasurementLabels = convertToList("${exampleMeasurementLabels}");
    $("#performMeasurementLabelMatch").click(function() {
    	$("#matchedMeasurementLabels").val(computeRegex($("#measurementLabel").val(), exampleMeasurementLabels));
    });
});
</script>
</html>
