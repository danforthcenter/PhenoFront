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
				<input type="hidden" value="${activeExperiment}" name="activeExperiment"/>
	 			
	 			
				<label for="plantBarcode">
					Barcode Regex Search (DB.+, D[A]{3,}, .+AA, etc.)
				</label>
				<input type="text" name="plantBarcode" id="plantBarcode"
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
				
				<!--  
				<label for="userRestriction">
					Restrict By User:
				</label>
				
				<input type="text" name="userRestriction" id="userRestriction"
					class="form-control" placeholder="~~~~~NOT IMPLEMENTED YET~~~~~username1, usernam2, username3, etc."
					title="A comma separated list of users. Removes any snapshots downloaded by a listed user.~~~~~NOT IMPLEMENTED YET~~~~~" />
				-->
			</div>
			
			<div class="form-group">
				<label>Image Download Options</label>
			
				<div class="checkbox">
					<label>
						<input type="checkbox" name="fluo" value="true" checked />
						Include Fluorescent Images
					</label>
				</div>
				
				<div class="checkbox">
					<label>
						<input type="checkbox" name="nir" value="true" checked />
						Include Near Infrared Images
					</label>
				</div>
				
				<div class="checkbox">
					<label>
						<input type="checkbox" name="vis" value="true" checked />
						Include Visible Light Images
					</label>
				</div>
				
				<div class="checkbox">
					<label>
						<input type="checkbox" name="watering" value="false" checked />
						Include Watering Snapshots
					</label>
				</div>
				
				<input type="hidden" name="downloadKey" value="${ downloadKey }">
			</div>
			
			<div class="downloadLink hidden"></div>
			<a id ="generateDownloadLink" class="btn btn-default btn-block btn-large">
				Generate Download Link
			</a>
			<br />
			
			<a id ="previewQuery" class="btn btn-default btn-block btn-large">
				View Full Query Results
			</a>
			<div class="queryPreview hidden"></div>
			
		</form>
	</div>
</div>
</jsp:body>
</tags:userarea-template>
</body>
<script type="text/javascript" src="<c:url value="/resources/js/computeRegex.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/convertToList.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/displayHTML.js"/>"></script>
<script>
$(document).ready(function(){
	
    $("#plantBarcode").tooltip();
    $("#measurementLabel").tooltip();
    $("#startTime").tooltip();
	$("#endTime").tooltip();
	$("#userRestriction").tooltip();
	
	$("#startTime").datetimepicker();
	$("#endTime").datetimepicker();
	
    // Generate download link
    $( "#generateDownloadLink" ).click(function() {
        $(".downloadLink").removeClass("hidden").show().addClass("alert alert-success");
        var downloadURL = '<c:url context="/phenofront" value="/massdownload" />' + "?" + $("#query-builder").serialize();
        $(".downloadLink").html("<a href='"+ downloadURL + "'>Your Download Link </a>This link may only be used once.");
    });
    
 	// Query preview
    $('#previewQuery').click(function() {
    	
    	var form = $(this);
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront" value="/userarea/querypreview" />',
			data: $("#query-builder").serialize(),
			success: function(csv) {
				$(".queryPreview").removeClass("hidden").show().addClass("alert alert-success");
				var html = displayHTML(csv);
				$(".queryPreview").html(html);
			},
			error: function(xhr, status, error) {
				form.find(".queryPreview").removeClass("hidden alert alert-success");
				form.parent().find(".result").addClass("alert alert-danger");
				form.parent().find(".result").text(xhr.responseText);
			}
		});
    });
});
</script>
</html>
