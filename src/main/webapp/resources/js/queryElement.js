/**
 * Nightmarish spagetti code.
 * 
 * Below here you enter into the city of woes
 * Below here you enter into eternal pain
 * Below here you enter the population of loss
 * 
 * Abandon all hope, you who enter here
 */

var  ID				= "id";
var  EXPERIMENT		= "experiment";

var  BARCODE		= "plant barcode";
var  MEASUREMENT	= "measurement label";
var  TIMESTAMP		= "timestamp";

var  CAR_TAG		= "car tag";
var  COMPLETED		= "completed";

var  WEIGHT_BEFORE	= "weight before";
var  WEIGHT_AFTER	= "weight after";
var  WATER_AMOUNT	= "water amount";

var  TILES			= "tiles";
var  TAG			= "tag";

function queryElement(query, snapshots, downloadKey)
{
	var panel = $('<form />', {
		'class': 'panel-default query-panel',
		'role': 'form'
	});
	
	// Form values for AJAX
	var queryLogging = $('<input />', {
		'type': 'hidden',
		'name': 'logQuery',
		'value': false
	});
	
	var queryId = $('<input />', {
		'type': 'hidden',
		'name': 'ids',
		'value': query.id
	});
	
	var queryExperiment = $('<input />', {
		'type': 'hidden',
		'name': 'experiment',
		'value': query.experiment
	});
	
	var queryTypeMetadata = $('<input />', {
		'type': 'hidden',
		'name': 'metadataType',
		'value': 'query'
	});
	
	// Header of the query display, expands the body
	// Contains a quick summary of the query
	var header = $('<div />', {
		'class': 'query-panel-heading',
		'type': 'button',
		'data-toggle': 'collapse',
		'data-target': '#collapseQuery' + query.id
	});
	
	// Expanding body of the query display, expanded by header
	// Contains all information on the query
	var body = $('<div />', {
		'class': 'collapse panel-body container-fluid',
		'id': 'collapseQuery' + query.id
	});
	
	// Generated download link that serializes this form
	var url = '/phenofront/massdownload' + '?' + form.serialize(); 
	var downloadLinkRow = $('<div />', {'class': 'row' })
		.append($('<div />', {'class': 'col-sm-4', 'text': 'Download Link' }))
		.append($('<a />',   {'class': 'col-sm-8', 'text': 'Full Download', 'href': url }));
	
	header.append(queryTitle(query));
	console.log("change applied");
	var table = queryTable(query);
	var metadataTable = metadataTable(query.metadata);
		metadataTable.append(downloadLinkRow);
	
	body.append(queryTable)
		.append($('<hr />'))
		.append(metadataTable)
		.append(queryComment(query, panel))
		.append($('<br />'))
		.append(snapshotsButton(query, snapshots));
	
	panel.append(header)
		 .append(body)
		 .append(downloadKey)
		 .append(queryLogging)
		 .append(queryId)
		 .append(queryExperiment)
		 .append(queryTypeMetadata);
	
	return panel;
}

function queryTitle(query)
{
	var title = $('<div />', {
		'class': 'panel-title'
	});
	
	var topRow		= splitRow('Query #' + query.id, query.metadata.dateMade);
	var bottomRow	= splitRow(query.experiment, query.metadata.numberSnapshots + ' snapshots');
	
	title.append(topRow)
		.append(bottomRow);
	
	return title;
}

function queryTable(query)
{
	var barcodeRow		= plainRow('Barcode Regex',				regexVariable(query.barcode));
	var measurementRow	= plainRow('Measurement Label Regex',	regexVariable(query.measurementLabel));
	var timeRangeRow	= plainRow('Time Range',				timeRangeVariable(query.startTime, query.endTime));
	
	var wateringRow		= plainRow('Watering Data',				inclusionVariable(query.includeWatering));
	var visibleRow		= plainRow('Visible Images',			inclusionVariable(query.includeVisible));
	var infraredRow		= plainRow('Near Infrared Images',		inclusionVariable(query.includeInfrared));
	var fluorescentRow	= plainRow('Fluorescent Images',		inclusionVariable(query.includeFluorescent));
	
	var table = $('<div />', {
		'class': 'data-packed'
	})
		.append(barcodeRow)
		.append(measurementRow)
		.append(timeRangeRow)
		.append(wateringRow)
		.append(visibleRow)
		.append(infraredRow)
		.append(fluorescentRow);
	
	return table;
}

function metadataTable(metadata)
{
	var userRow			= plainRow('User',					requiredVariable(metadata.username));
	var dateRow			= plainRow('Date Issued',			requiredVariable(metadata.dateMade));
	
	var sizeRow			= plainRow('Size',					metadata.bytes);
	var snapshotsRow	= plainRow('Number of Snapshots',	metadata.numberSnapshots);
	var tilesRow		= plainRow('Number of Tiles',		metadata.numberTiles);
	var imagesRow		= plainRow('Number of Images',		metadata.numberTiles);
	
	var initializedRow	= plainRow('Download Initialized',	metadata.downloadBegin);
	var completedRow	= plainRow('Download Completed',	metadata.downloadEnd);
	var incompleteRow	= plainRow('Download Status',		'Incomplete');
	var interruptedRow	= plainRow('Download Status',		'Interrupted');
	var neverDownloadRow= plainRow('Download Status',		'Never downloaded');
	
	var table = $('<div />', {
		'class': 'data-packed'
	});
	
	table.append(userRow)
		 .append(dateRow);
	
	if (metadata.bytes != 0)
		table.append(sizeRow);
	
	if (metadata.numberSnapshots != -1)
		table.append(snapshotsRow);
	
	if (metadata.numberTiles != -1)
		table.append(imagesRow);
	
	if (metadata.downloadBegin != null) {
		table.append(initializedRow);
	
		if (metadata.downloadEnd != null)
			table.append(completedRow);
		
		else if (metadata.interrupted == true)
			table.append(interruptedRow);
		
		else
			table.append(incompleteRow);
	}
	else {
		table.append(neverDownloadRow);
	}
	
	return table;
}

function queryComment(query, queryForm)
{
	var metadata = query.metadata;
	
	// Manage the HTML construction
	var commentSystem = $('<div />');
	
	var row = $('<div />', {
		'class': 'row'
	});
	var commentLabel = $('<div />', {
		'class': 'data-packed col-sm-8',
		'text': 'Comments:'
	});
	var updateButton = $('<button />', {
		'type': 'button',
		'class': 'btn btn-default col-sm-3',
		'text': 'Commit Update'
	});
	updateButton.hide();
	
	var textArea = $('<textarea />', {
		'class': 'form-control',
		'rows': '4',
		'name': 'metadata',
		'value': metadata.comment
	});
	textArea.val(metadata.comment); // Because assigning it with 'value' obviously can't work
	
	row.append(commentLabel)
	   .append(updateButton);
	commentSystem.append(row)
				 .append(textArea);
	
	// Give the button its AJAX functionality
	updateButton.click(function () {
		console.log("POST: " + queryForm.serialize());
		$.ajax({
			type: 'POST',
			url: '/phenofront/userarea/changemetadata',
			data: queryForm.serialize(),
			success: function() {
				updateSuccess();
			},
			error: function(xhr, status, error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
				updateError();
			}
		});
	});
	
	function updateSuccess()
	{
		updateButton.html('Update Successful');
		updateButton.fadeOut('slow');
		commentSystem.removeClass('has-warning has-error').addClass('has-success');
		commentSystem.removeClass('has-warning has-error').addClass('has-success');
	}
	
	function updateError()
	{
		updateButton.html('Update Failed');
		commentSystem.removeClass('has-warning has-success').addClass('has-error');
		commentSystem.removeClass('has-warning has-success').addClass('has-error');
	}
	
	textArea.on('input', function(event) {
		updateButton.fadeIn('slow');
		updateButton.html('Commit Update');
		commentSystem.removeClass('has-success has-error').addClass('has-warning');
		commentSystem.removeClass('has-success has-error').addClass('has-warning');
	});
	
	return commentSystem;
}

function snapshotsButton(query, snapshots)
{
	var snapshotContainer = $('<div />', {
		'class': 'no-space'
	});
	
	var displayButton = $('<button />', {
		'type': 'button',
		'class': 'btn btn-default btn-block',
		'text': 'Show Full Results'
	});
	var snapshotList = $('<ul />', {
		'class': 'list-group'
	});
	
	var snapshotTable = snapshotTableHeader();
	snapshotTable.hide();
	
	var snapshotTableBody = $('<tbody />');
	
	snapshotTable.append(snapshotTableBody);
	
	snapshotContainer.append(displayButton)
					 .append($('<br />'))
	 				 .append(snapshotTable);
	
	var resultsDisplayed = false;
	displayButton.click(function() {
		// Toggle show vs hide
		if (resultsDisplayed) {
			displayButton.html('Show Full Results');
			snapshotTable.hide()
			resultsDisplayed = false;
		}
		
		else {
			// Display the snapshots of they've already been retrieved
			if (snapshots != null && snapshots != "")
				displaySnapshots(snapshots);
			
			else {
				$.ajax({
					type: 'POST',
					url: '/phenofront/userarea/querypreview',
					data: query,
					success: function(queryJSON) {
						displaySnapshots(queryJSON.snapshots);
					},
					error: function(xhr, status, error) {
						
					}
				});
			}
		}
	});
	
	function displaySnapshots(snapshotsToDisplay)
	{
		displayButton.html('Hide Results');
		resultsDisplayed = true;
		snapshotTable.show();
		snapshots = snapshotsToDisplay; // So we don't have to retrieve them again
		
		snapshotsToDisplay.forEach(function (snapshot) {
			//var snapshotDiv = snapshotPanel(snapshot);
			//snapshotList.append(snapshotDiv);
			var snapshotRow = snapshotTableRow(snapshot);
			snapshotTableBody.append(snapshotRow);
		});
		
		snapshotList.show();
	}
	
	return snapshotContainer;
}

function snapshotTableHeader()
{
	var table = $('<table />', {
		'class': 'table table-condensed table-striped data-packed'
	});
	var header = $('<thead />');
	
	var firstRow = $('<tr />');
	firstRow.append($('<th>' + ID + '</th>'))
			.append($('<th>' + BARCODE + '</th>'))
			.append($('<th>' + MEASUREMENT + '</th>'))
			.append($('<th>' + CAR_TAG + '</th>'))
			.append($('<th>' + TIMESTAMP + '</th>'))
			.append($('<th>' + WATER_AMOUNT + '</th>'))
			.append($('<th>' + TAG + '</th>'));
	
	header.append(firstRow);
	table.append(header);
	
	return table;
}

function snapshotTableRow(snapshot)
{
	var row = $('<tr />');
	var snapshotTag = changeSnapshotTagButton(snapshot);	
	
	row.append($('<td>' + snapshot.id + '</td>'))
	   .append($('<td>' + snapshot.plantBarcode + '</td>'))
	   .append($('<td>' + snapshot.measurementLabel + '</td>'))
	   .append($('<td>' + snapshot.carTag + '</td>'))
	   .append($('<td>' + snapshot.timestamp + '</td>'))
	   .append($('<td>' + snapshot.waterAmount + '</td>'))
	   .append($('<td />').append(snapshotTag));
	
	return row
}

function changeSnapshotTagButton(snapshot)
{
	// Change tag button
	var tagInputGroup = $('<form />', {
		'role': 'form',
		'class': 'input-group btn-group-xs'
	});
	var tagInput = $('<input />', {
		'type': 'text',
		'name': 'metadata',
		'class': 'form-control input-sm',
		'value': possiblyEmptyVariable(snapshot.tag)
	});
	var tagInputButtonDiv = $('<span />', {
		'class': 'input-group-btn'
	});
	var tagInputButton = $('<button />', {
		'class': 'btn btn-default btn-sm',
		'type': 'button',
		'text': 'Apply'
	});
	
	// Form values for AJAX
	var snapshotId = $('<input />', {
		'type': 'hidden',
		'name': 'ids',
		'value': snapshot.id
	});
	
	var snapshotExperiment = $('<input />', {
		'type': 'hidden',
		'name': 'experiment',
		'value': snapshot.experiment
	});
	
	var snapshotTypeMetadata = $('<input />', {
		'type': 'hidden',
		'name': 'metadataType',
		'value': 'snapshot'
	});
	
	// Give the button its AJAX functionality
	tagInputButton.click(function () {
		console.log("POST: " + tagInputGroup.serialize());
		$.ajax({
			type: 'POST',
			url: '/phenofront/userarea/changemetadata',
			data: tagInputGroup.serialize(),// + '&metadata=' + tagInputType.val(), // for some reason this isn't being serialized, so hack it
			success: function() {
				updateSuccess(tagInput);
			},
			error: function(xhr, status, error) {
				updateError(tagInput);
			}
		});
	});
	
	tagInputButtonDiv.append(tagInputButton);
	tagInputGroup.append(tagInput)
				 .append(tagInputButtonDiv)
				 .append(snapshotId)
				 .append(snapshotExperiment)
				 .append(snapshotTypeMetadata);
	
	return tagInputGroup;
}

function snapshotPanel(snapshot)
{
	var listElement = $('<li />', {
		'class': 'list-group-item'
	})
	
	// Header for the snapshot display, expands the body
	// Contains a quick summary of the snapshot
	var headerForm = $('<form />', {
		'role': 'form'
	})
	var header = $('<div />', {
		'class': 'panel-heading'
	});
	
	// Body of the snapshot display, expanded by the header
	// Contains all information on the snapshot
	var body = $('<div />', {
		'class': 'collapse panel-body data-packed',
		'id': 'collapseSnapshot' + snapshot.id
	});
	
	var tileList = $('<ul />',{
		'class': 'list-group'
	});
	
	headerForm.append(header)
			  .append(snapshotId)
			  .append(snapshotExperiment)
			  .append(snapshotTypeMetadata);
	header.append(snapshotTitle(snapshot, headerForm));
	
	snapshot.tiles.forEach(function (tile) {
		tileList.append(tilePanel(tile, snapshot.experiment));
	});
	
	body.append(snapshotTable(snapshot))
		.append(tileList);
	
	listElement.append(headerForm)
		 	   .append(body);
	
	return listElement;
}

function snapshotTitle(snapshot, snapshotForm)
{
	var tagSystem = $('<div />', {
		'class': 'input-group input-group-sm col-sm-7 snapshot-tag'
	});
	var tagLabel = $('<div />', {
		'class': 'input-group-addon',
		'text': 'Tag'
	});
	var tagInput = $('<input />', {
		'class': 'form-control',
		'id': 'snapshot' + snapshot.id + 'tag',
		'value': snapshot.tag,
		'name': 'metadata',
		'type': 'text'
	});
	var buttonContainer = $('<span />', {
		'class': 'input-group-btn'
	})
	var tagButton = $('<button />', {
		'type': 'button',
		'class': 'btn btn-default',
		'text': 'Commit Update'
	});
	
	// Give the button its AJAX functionality
	tagButton.click(function () {
		console.log("POST: " + snapshotForm.serialize());
		$.ajax({
			type: 'POST',
			url: '/phenofront/userarea/changemetadata',
			data: snapshotForm.serialize(),
			success: function() {
				updateSuccess(tagInput);
			},
			error: function(xhr, status, error) {
				updateError(tagInput);
			}
		});
	});
	
	tagSystem.append(tagLabel)
			 .append(tagInput)
			 .append(buttonContainer.append(tagButton));
	
	var snapshotTitle = $('<div />', {
		'class': 'col-sm-5 snapshot-title',
		'type': 'button',
		'data-toggle': 'collapse',
		'data-target': '#collapseSnapshot' + snapshot.id
	});
	
	var snapshotId = ($('<div />', {
		'class': 'col-sm-5 text-left',
		'text': 'Snapshot #' + snapshot.id
	}));
	var snapshotDate = ($('<div />', {
		'class': 'col-sm-7 text-right',
		'text': snapshot.timestamp
	}));
	
	snapshotTitle.append(snapshotId)
				 .append(snapshotDate);
	
	var title = $('<div />', {
		'class': 'data-packed row'
	});
		
	title.append(snapshotTitle)
		 .append(tagSystem);
	
	return title;
}
function snapshotTable(snapshot)
{
	var barcodeRow		= plainRow('Barcode',				snapshot.plantBarcode);
	var measurementRow	= plainRow('Measurement Label',		snapshot.measurementLabel);
	var timestampRow	= plainRow('Timestamp',				snapshot.timestamp);
	
	var carTagRow		= plainRow('Car Tag',				snapshot.carTag);
	var completedRow	= plainRow('Measurement Completed',	snapshot.completed);
	
	var weightBeforeRow	= plainRow('Weight Before',			snapshot.weightBefore);
	var weightAfterRow	= plainRow('Weight After',			snapshot.weightAfter);
	var waterRow		= plainRow('Water Amount',			snapshot.waterAmount);
	
	var table = $('<div />')
		.append(barcodeRow)
		.append(measurementRow)
		.append(timestampRow)
		.append(carTagRow)
		.append(completedRow)
		.append(weightBeforeRow)
		.append(weightAfterRow)
		.append(waterRow);
	
	return table;
}

function tilePanel(tile, experiment)
{	
	// Tile container
	var panel = $('<li />', {
		'class': 'list-group-item'
	});
	
	// Variables for AJAX
	var tileId = $('<input />', {
		'type': 'hidden',
		'name': 'ids',
		'value': tile.id
	});
	
	var tileExperiment = $('<input />', {
		'type': 'hidden',
		'name': 'experiment',
		'value': experiment
	});
	
	var tileTypeMetadata = $('<input />', {
		'type': 'hidden',
		'name': 'metadataType',
		'value': 'tile'
	});
	
	// Header of tile, expands the body
	// Contains summary of the tile
	var headerForm = $('<form />', {
		'role': 'form'
	});
	
	var header = $('<div />', {
		'class': 'panel-heading data-packed',
		'type': 'button',
		'data-toggle': 'collapse',
		'data-target': '#collapseTile' + tile.id
	});
	
	var title = $('<h3 />', {
		'class': 'panel-title',
		'text': 'Tile #' + tile.id
	});
	
	// Body of the tile, expanded by header
	// Contains all relevant information on the tile
	var body = $('<div />', {
		'class': 'collapse panel-body',
		'id': 'collapseTile' + tile.id
	});
	
	header.append(title);
	
	headerForm.append(header)
			  .append(tileId)
			  .append(tileExperiment)
			  .append(tileTypeMetadata);
	
	body.append(tileTable(tile, headerForm));
	
	panel.append(headerForm)
		 .append(body);
	
	return panel;
}

function tileTable(tile, tileForm)
{
	var tagSystem = $('<div />', {
		'class': 'input-group input-group-sm col-sm-7 snapshot-tag'
	});
	var tagLabel = $('<div />', {
		'class': 'input-group-addon',
		'text': 'Tag'
	});
	var tagInput = $('<input />', {
		'class': 'form-control',
		'id': 'tile' + tile.id + 'tag',
		'value': tile.tag,
		'name': 'metadata',
		'type': 'text'
	});
	var buttonContainer = $('<span />', {
		'class': 'input-group-btn'
	})
	var tagButton = $('<button />', {
		'type': 'button',
		'class': 'btn btn-default',
		'text': 'Commit Update'
	});
	
	// Give the button its AJAX functionality
	tagButton.click(function () {
		$.ajax({
			type: 'POST',
			url: '/phenofront/userarea/changemetadata',
			data: tileForm.serialize() + '&metadata=' + tagInput.val(), // for some reason this isn't being serialized, so hack it
			success: function() {
				updateSuccess(tagInput);
			},
			error: function(xhr, status, error) {
				updateError(tagInput);
			}
		});
	});
	
	tagSystem.append(tagLabel)
			 .append(tagInput)
			 .append(buttonContainer.append(tagButton));
	
	var imageTypeRow	= plainRow('Image Type',	imageType(tile.dataFormat));
	var dimensionsRow	= plainRow('Dimensions',	tile.width + ' x ' + tile.height);
	var perspectiveRow	= plainRow('Perspective',	"UNIMPLEMENTED");
	
	var table = $('<div />')
		.append(tagSystem)
		.append(imageTypeRow)
		.append(dimensionsRow)
		.append(perspectiveRow);

	return table;
}

function imageType(dataFormat)
{
	if (dataFormat == 0)
		return "Near Infrared";
	if (dataFormat == 1)
		return "Visible";
	if (dataFormat == 6)
		return "Fluorescent";
	else
		return "ERROR UNRECOGNIZED FORMAT";
}

function plainRow(rowLabel, rowValue)
{
	var row = $('<div />', {'class': 'row' })
		.append($('<div />', {'class': 'col-sm-4', 'text': rowLabel }))
		.append($('<div />', {'class': 'col-sm-8', 'text': rowValue }));
	
	return row;
}

function splitRow(columnLeft, columnRight)
{
	var row = $('<div />', {'class': 'row' })
		.append($('<div />', {'class': 'col-split col-sm-6 text-left',  'text': columnLeft }))
		.append($('<div />', {'class': 'col-split col-sm-6 text-right', 'text': columnRight }));
	
	return row;
}
function timeRangeVariable(startTime, endTime)
{
	if (startTime == null && endTime == null)
		return "All times";
	else if (startTime == null)
		return "All times before " + endTime;
	else if (endTime == null)
		return startTime + " through to all times after";
	else
		return startTime + " - " + endTime;
}

function inclusionVariable(includeX)
{
	if (includeX)
		return "Included";
	else
		return "Not Included";
}

function requiredVariable(requiredString)
{
	if (requiredString == null || requiredString == "")
		return "ERROR NOT FOUND";
	else
		return requiredString;
}

function regexVariable(regexString)
{
	if (regexString == null || regexString == "")
		return "<ANY>";
	else
		return regexString;
}

function possiblyEmptyVariable(notRequiredString)
{
	if (notRequiredString == null || notRequiredString == "")
		return "";
	else
		return notRequiredString;
}

function updateSuccess(inputBox)
{
	console.log("tag updated successfully.");
}

function updateError(inputBox)
{
	console.log("tag not updated successfully.");
}