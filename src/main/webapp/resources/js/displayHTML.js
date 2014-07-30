/**
 * Converts CSV into an HTML table, and expects the following tags in the CSV:
 * 		timestamp
 * 		plant barcode
 * 		measurement label
 * 		car tag
 * 		completed
 * 
 * @author cjmcentee
 */

function displayHTML(csv)
{
	var table = convertCSVToTable(csv);
	
	var html =
	'<div class="table-responsive">\
	<table class="table table-striped table-condensed table-bordered" id="results-table">\
		<tr id="headers">\
			<th>Time Stamp</th>\
			<th>Plant Barcode</th>\
			<th>Measurement Label</th>\
			<th>Car Tag</th>\
			<th>Completed</th>\
			<th>Users</th>\
			<th>Images</th>\
		</tr>';
	
	table.forEach(function(row) {
		html += '<tr>';
		html += '<td>' + row["timestamp"] + '</td>';
		html += '<td>' + row["plant barcode"] + '</td>';
		html += '<td>' + row["measurement label"] + '</td>';
		html += '<td>' + row["car tag"] + '</td>';
		html += '<td>' + row["completed"] + '</td>';
		html += '<td>' + "NOT IMPLEMENTED" + '</td>';
		html += '<td><a class="single-set" download="" href="/phenofront/userarea/stream/' + row["id"] + '"> Download </a></td>';
		html += '</tr>'
	});
	
	html +=		
	'</table>\
	</div> ';
	
	return html;
}

function convertCSVToTable(csv)
{
	var lines = csv.split("\n");
	var headerLabels = lines[0].split(",");
	lines.shift(); // The first line is the headers
	lines.pop();   // The last line is blank
	
	var rows = [];
	lines.forEach(function(line) {
		var lineValues = line.split(",");
		
		var row = { };
		for (var i = 0; i < lineValues.length; i++) {
			var valueName = headerLabels[i];
			var value = lineValues[i];
			
			row[valueName] = value;
		}
		
		rows.push(row);
	});
	
	return rows;
}