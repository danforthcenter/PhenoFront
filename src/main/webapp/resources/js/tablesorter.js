/**
 * tablesorter.js
 * @author Steven Hill - Donald Danforth Plant Science Center 2013
 */
(function( $ ){
	//alpha numeric starts here
	var alpha_num_sort = function(col, desc){
		return function(a, b){
			function chunkify(t) {
			    var tz = new Array();
			    var x = 0, y = -1, n = 0, i, j;

			    while (i = (j = t.charAt(x++)).charCodeAt(0)) {
			      var m = (i == 46 || (i >=48 && i <= 57));
			      if (m !== n) {
			        tz[++y] = "";
			        n = m;
			      }
			      tz[y] += j;
			    }
			    return tz;
			  }

			  var aa = chunkify(a[col]);
			  var bb = chunkify(b[col]);

			  for (var x = 0; aa[x] && bb[x]; x++) {
			    if (aa[x] !== bb[x]) {
			      var c = Number(aa[x]), d = Number(bb[x]);
			      if (c == aa[x] && d == bb[x]) {
			    	if(desc)
			    		return c - d;
			    	else
			    		return d-c;
			      } else{
			    	  if (desc)
			    		  return (aa[x] > bb[x]) ? 1 : -1;
			    	  else
			    		  return (aa[x] > bb[x]) ? -1 : 1;
			      }
			    }
			  }
			  return aa.length - bb.length; //???? this is 0
		};
	};
	
	/**
	 * $(table).sortColumn
	 * Sorts the table based on a passed column of a table, either in ascending or descending manner.
	 * 
	 * @arg sort_by - column to sort by
	 * @arg desc - boolean representing if the table should be sorted in descending order.
	 */
   $.fn.sortColumn = function(sort_by, desc) {
	   if(!$(this).is("table")){
		   return false;
	   }
	   var rows = $("tr:gt(0)"); // skip the header row, get all rows
	   var sortable_rows = [];
	   //get just values
	   rows.each(function(index) {
		   var current_row = [];
	       $(this).children("td").each(function() { // calculate amount, vat, subtotal for row
	    	   current_row.push($(this).html()); //sorts on tds so the preceeding html is irrelevant (except with numbers)
		   });
	       sortable_rows.push(current_row);
	   });
	   
	   if (sort_by >= sortable_rows.length){
		   return false;
	   }
	   //sort_by_col
	   var sorted_rows = sortable_rows.sort(alpha_num_sort(sort_by, desc));
	   //annotate the table
	   var i = 0; var j = 0;
	   rows.each(function(index) {
		   j = 0;
	       $(this).children("td").each(function() { // calculate amount, vat, subtotal for row
	    	   $(this).html(sorted_rows[i][j]);
	    	   j++;
		   });
	       i++;
	   });	   
   }; 
})( jQuery );