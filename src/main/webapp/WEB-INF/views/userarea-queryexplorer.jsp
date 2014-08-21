<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<tags:userarea-template>

<jsp:attribute name="active">Query Explorer</jsp:attribute>

<jsp:body>

<div class="container">
	<div class="jumbotron" style="padding-left: 30px; padding-right: 30px;">				
		
		<h2>Query And Metadata Explorer</h2>
		<p>
			View past queries and downloads and modify their associated metadata.
		</p>
		
		<!-- Download key in the event the user desires to complete an incomplete download -->
		<input type="hidden" name="downloadKey" value="${ downloadKey }" id="downloadKey" />
		
		<div class="panel-default">
			<div class="panel-heading" data-toggle="collapse" data-target="#collapseMassModify">
				<div class="panel-title">
					Modify Metadata Via FileUpload
				</div>
			</div>
			<div class="collapse panel panel-body" id="collapseMassModify">
				<label>
					Select file for large-scale metadata modification
					<input id="fileupload" type="file" name="file" data-url="/phenofront/userarea/changemetadatabyfile" />
				</label>
				
				<a href="/phenofront/userarea/changemetadatainstructions" class="btn btn-default btn-block fileDownloadSimpleRichExperience">Download File Formatting Instructions</a>
			</div>
		</div>
		
		<div class="panel-default">
			<div class="panel-heading" data-toggle="collapse" data-target="#collapseQueryHistory">
				<div class="panel-title">
					Explore Query History
				</div>
			</div>
			<div class="collapse panel panel-body" id="collapseQueryHistory">
				<form role='form' id="queryFilter">
					
					<input type="hidden" value="${experiment}" name="experiment"/>
					<input type="hidden" value="" id="currentQueries" name="currentQueries"/>
					
					<label for="queryUsername">
						Only include queries made by:
					</label>
					<select class="form-control" name="queryUsername" id="queryUsername"
						title="Leaving this field blank means queries from any user will be included.">
						<option value=""></option> <!-- Null option -->
				      <c:forEach var="user" items="${allUsers}">
				      	<option value="${ user.username }">${ user.username }</option>
				      </c:forEach>
					</select>
					
					<div class="checkbox">
						<label>
							<input type="checkbox" id="onlyCommented" name="onlyCommented" value="true"
							title ="Only include queries that have user-annotated comments associated with them." />
							Commented Queries Only
						</label>
					</div>
					
				</form>
				<br />
				
				<a id="getQueries" class="btn btn-default btn-block">Request Queries</a>
				<br />
				<div id="queries" class="hidden">
				</div>
				<button id="loadMoreQueries" class="btn btn-default btn-block hidden">Load More Queries</button>
				<div id="noMoreQueries" class="hidden align-center">
					<h2>
						No More Queries
					</h2>
				</div>
			</div>
		</div>
		
	</div>
</div>
</jsp:body>
</tags:userarea-template>

<script type="text/javascript" src="<c:url value="/resources/js/queryElement.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.fileupload.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.iframe-transport.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/vendor/jquery.ui.widget.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/vendor/jquery.fileDownload.js"/>"></script>


<script type="text/javascript">
// Uploads metadata config file to the server
$(function () {
    $('#fileupload').fileupload({
        dataType: 'json',
        done: function (e, data) {
            $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo(document.body);
            });
        }
    });
});

$(document).ready(function() {
	
	$(function() {
	    $(document).on("click", "a.fileDownloadCustomRichExperience", function() {
	 
	        var $preparingFileModal = $("#preparing-file-modal");
	 
	        $preparingFileModal.dialog({ modal: true });
	 
	        $.fileDownload($(this).attr('href'), {
	            successCallback: function(url) {
	 
	                $preparingFileModal.dialog('close');
	            },
	            failCallback: function(responseHtml, url) {
	 
	                $preparingFileModal.dialog('close');
	                $("#error-modal").dialog({ modal: true });
	            }
	        });
	        return false; //this is critical to stop the click event which will trigger a normal file download!
	    });
	});
	
	$('#queryUsername').tooltip();
	$('#onlyCommented').tooltip();
	
	$('#getInstructions').click(function () {
		$.ajax({
			url: '<c:url context="/phenofront/userarea" value="/changemetadatainstructions" />',
		});
		var downloadURL = '<c:url context="/phenofront" value="/changemetadatainstructions" />';
        $(".downloadLink").html("<a href='"+ downloadURL + "'>Your Download Link </a>This link may only be used once.");
	});
	
	var currentQueries = [];
	var previousQuery = {};
	$('#loadMoreQueries').click(function () {
		$.ajax({
			type: 'POST',
			url: '<c:url context="/phenofront/userarea" value="/queries" />',
			data: previousQuery,
			success: function(queriesJSON) {
				addDisplayQueries(queriesJSON);
				previousQuery = $('#queryFilter').serialize(); // save the query for the load more queries button
			},
			error: function(xhr, status, error) {
				addQueryFailure(xhr.responseText);
			}
		});
	});
	
	$('#getQueries').click(function() {
		getQueries();
	});
	
	
	function getQueries() {
		var currentQueries = []; // Clear current queries
		$('#currentQueries').val(currentQueries);
		console.log($('#queryFilter').serialize());
		$.ajax({
			type: 'POST',
			url: '<c:url context="/phenofront/userarea" value="/queries" />',
			data: $('#queryFilter').serialize(),
			success: function(queriesJSON) {
				querySuccess();
				displayQueries(queriesJSON);
				previousQuery = $('#queryFilter').serialize(); // save the query for the load more queries button
			},
			error: function(xhr, status, error) {
				queryFailure(xhr.responseText);
			}
		});
	}
	
	function querySuccess() {
		$('#queries').removeClass('hidden');
		$('#queries').removeClass('alert alert-danger');
		
		// empty the current queries
		currentQueries = [];
		$('#currentQueries').val(currentQueries);
	}
	
	function displayQueries(queries) {
		$('#queries').empty();
		
		queries.forEach(function (query) {
			var queryDiv = queryElement(query, '', $('#downloadKey'));
			$('#queries').append(queryDiv);
			
			// repopulate current queries
			currentQueries.push(query.id);
		});
		
		$('#currentQueries').val(currentQueries);
		
		// Only show loadmore button if queries were actually returned
		if (queries.length > 0) {
			$('#loadMoreQueries').removeClass('hidden');
			$('#noMoreQueries').addClass('hidden');
		}
		else {
			$('#loadMoreQueries').addClass('hidden');
			$('#noMoreQueries').removeClass('hidden');
		}
			
	}
	
	function queryFailure(message) {
		$('#queries').removeClass('hidden');
		$('#queries').addClass('alert alert-danger');
		$('#queries').text(message);
		
		$('#loadMoreQueries').addClass('hidden');
		$('#noMoreQueries').addClass('hidden');
		
		currentQueries = [];
		$('#currentQueries').val(currentQueries); // empty the current queries
	}
	
	function addDisplayQueries(queries) {
		
		queries.forEach(function (query) {
			var queryDiv = queryElement(query, '', $('#downloadKey'));
			$('#queries').append(queryDiv);
			
			// repopulate current queries
			currentQueries.push(query.id);
		});
		
		$('#currentQueries').val(currentQueries);
		
		// Only show loadmore button if queries were actually returned
		if (queries.length > 0) {
			$('#loadMoreQueries').removeClass('hidden');
			$('#noMoreQueries').addClass('hidden');
		}
		else {
			$('#loadMoreQueries').addClass('hidden');
			$('#noMoreQueries').removeClass('hidden');
		}
	}
	
	function addQueryFailure(message) {
		var queryFailure = $('<div />', {
			'class': 'alert alert-danger'
		});
		
		var failureMessage = $('<h3 />', {
			'text': message
		});
		
		queryFailure.append(failureMessage);
		
		$('#queries').append($('<br />'))
					 .append(queryFailure);
		
		$('#loadMoreQueries').addClass('hidden');
		$('#noMoreQueries').addClass('hidden');
	}
});
</script>