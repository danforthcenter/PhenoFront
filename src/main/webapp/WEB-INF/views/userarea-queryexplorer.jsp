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
		<input type="hidden" name="downloadKey" value="${downloadKey}" id="downloadKey" />
		
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
	
	$('#queryUsername').tooltip();
	$('#onlyCommented').tooltip();
	
	/************************************
	 *		Tagging File Behavior
	 ************************************/
	// Button click download instructions for tagging file
	$('#getInstructions').click(function () {
		$.ajax({
			url: '<c:url context="/phenofront/userarea" value="/changemetadatainstructions" />',
		});
		var downloadURL = '<c:url context="/phenofront" value="/changemetadatainstructions" />';
        $(".downloadLink").html("<a href='"+ downloadURL + "'>Your Download Link </a>This link may only be used once.");
	});
	
	// Model dialog for uploading a tagging file
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
	
	/************************************
	 *		Query Explorer Behavior
	 ************************************/
	var loadedQueryIDs = [];		// The IDs of the currently displayed queries, sent to the server
									// when more queries are asked to be loaded so the server knows which ones
									// the user already has. This array is kept in sync/stored in with #currentQueries
	//
	// SEARCH:
	// The initial search for queries button behavior
	//
	$('#getQueries').click(function() {
		
		clearCurrentQueries();		// New search clears the screen, success or failure
		
		// The actual request to the server
		$.ajax({
			type: 'POST',
			url: '<c:url context="/phenofront/userarea" value="/queries" />',
			data: $('#queryFilter').serialize(),
			success: function(queriesJSON) {
				notifyAjaxSuccess();
				addQueriesToDocument(queriesJSON);
				handleShowLoadButton();
			},
			error: function(xhr, status, error) {
				notifyAjaxFailure(xhr.responseText);
				clearCurrentQueries();
				hideLoadQueriesElements();
			}
		});
		
		//
		// Helper methods to handle the request & result
		//
		
		// Successful search notification
		function notifyAjaxSuccess() {
			$('#queries').removeClass('hidden');
			$('#queries').removeClass('alert alert-danger');
		}
		
		// Clear the old queries on a successful search
		function clearCurrentQueries() {
			loadedQueryIDs = [];
			$('#currentQueries').val(loadedQueryIDs);
			$('#queries').empty();
		}
		
		// Only show load more queries button if queries were actually returned
		function handleShowLoadButton() {
			if (loadedQueryIDs.length > 0) {
				$('#loadMoreQueries').removeClass('hidden');
				$('#noMoreQueries').addClass('hidden');
			}
			else {
				$('#loadMoreQueries').addClass('hidden');
				$('#noMoreQueries').removeClass('hidden');
			}
		}
		
		// Failed search notification
		function notifyAjaxFailure(errorMessage) {
			$('#queries').removeClass('hidden');
			$('#queries').addClass('alert alert-danger');
			
			$('#queries').text(errorMessage);
		}
		
		function hideLoadQueriesElements() {
			$('#loadMoreQueries').addClass('hidden');
			$('#noMoreQueries').addClass('hidden');
		}
	});
	
	// Adds the JSON queries to the currently displayed queries
	// Returns the queries in an array
	function addQueriesToDocument(queries_JSON) {
		
		queries_JSON.forEach(function (query) {
			var query_element = queryElement(query, '', $('#downloadKey'));
			$('#queries').append(query_element);
			
			// Track which queries have been added to the document
			loadedQueryIDs.push(query.id);
		});
		
		$('#currentQueries').val(loadedQueryIDs);
	}
	
	
	//
	// LOAD:
	// Load more queries button behavior (appears after a non-zero result search or a non-zero load)
	//
	$('#loadMoreQueries').click(function () {
		
		// Server request for more queries
		$.ajax({
			type: 'POST',
			url: '<c:url context="/phenofront/userarea" value="/queries" />',
			data: $('#queryFilter').serialize(),	// Indicates which queries are already displayed
			success: function(queriesJSON) {
				addQueriesToDocument(queriesJSON);
			},
			error: function(xhr, status, error) {
				notifyLoadMoreQueriesAjaxFailure(xhr.responseText);
			}
		});
		
		function parseQueries(queries) {
			
		}
		
		function notifyLoadMoreQueriesAjaxFailure(message) {
			var queryFailureElement = $('<div />', {
				'class': 'alert alert-danger'
			});
			
			var failureMessage = $('<h3 />', {
				'text': message
			});
			
			queryFailureElement.append(failureMessage);
			
			$('#queries').append($('<br />'))
						 .append(queryFailureElement);
			
			$('#loadMoreQueries').addClass('hidden');
			$('#noMoreQueries').addClass('hidden');
		}
	});
	
	
});
</script>