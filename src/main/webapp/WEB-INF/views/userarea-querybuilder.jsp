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
					 <form role="form" id ="query-builder" class="select-data" method="GET"
					action="<c:url context="/phenofront" value="/massdownload" />">
					  	<div class="form-group">
					  		<input type="hidden" value="${activeExperiment}" name="activeExperiment"/>
					    	<label for="plantBarcode">Barcode search (DBAA, DAAA, *AA..):</label>
							<input type="text" id="plantBarcode" name="plantBarcode"
							class="form-control" placeholder="DBAA"
							title="Only snapshots which plant barcode begin with your input will be return. This can be used to specify accession and plant types. (DBAA etc)" />						  
							<label for="measurementLabel">Measurement Label (Accepts regular expressions)</label>
							<input type="text" id="measurementLabel" name="measurementLabel"
							class="form-control" placeholder=""
							title="Find Snapshots which have the measurement(experiment) label that contains the input string. Also supports regular expressions." />						  
							<label for="after">Snapshots After:</label>	
							<input type="text" class="form-control" name="after" id="after"
							title="Click the box to choose a date." />			  
							<label for="before">Snapshots Before:</label>
							<input type="text" class="form-control" name="before"
							title="Click the box to choose a date." id="before" />
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
<script>
$(document).ready(function(){
	$('#before').datetimepicker();
	$('#after').datetimepicker();
	$('#headers').children().each(function(){
		$(this).tooltip({content: "Click to sort by column."});
	});
    $( '#measurementLabel' ).tooltip();
    $( '#plantBarcode' ).tooltip();
    $( '#before' ).tooltip();
    $( '#after' ).tooltip();
    $( '#max' ).tooltip();
    $("#submit").submit(function(){
        $(this).find('input[type=submit]').attr('disabled', 'disabled');
    });
    $( "#submit" ).click(function(){
    	$(".result").removeClass("hidden").show().addClass("alert alert-success");
    	var serialized = '<c:url context="/phenofront" value="/massdownload" />' +"?"+ $("#query-builder").serialize();
    	$(".result").html("<a href='"+ serialized + "'>Your Download Link </a>This link may only be used once.");
    });
});
</script>
</html>
