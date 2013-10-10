<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
	<jsp:attribute name="active">Visualize</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<h1>Visualize</h1>
			<ul class="nav nav-tabs" data-tabs="tabs">
		    <li class="active"><a data-toggle="tab" href="#growth">Growth Chart</a></li>
		    <li><a data-toggle="tab" href="#plot2">Plot 2</a></li>
		    <li><a data-toggle="tab" href="#custom">Custom Plot</a></li>
		</ul>
		<div class="tab-content">
		    <div class="tab-pane active" id="growth">
		        <h1>Growth Chart</h1>
		        <p>Our plots could come from a jquery based plotter, jqplot, or from an underlying tool, python/perl/R 
		        server calls.</p>
        			<img src="<c:url value="/resources/images/plantgrowth.png"/>"></img>
		    </div>
		    <div class="tab-pane" id="plot2">
		        <h1>Plot 2</h1>
		        <p>A second plot belongs here.</p>
		    </div>
		    <div class="tab-pane" id="custom">
		        <h1>Custom</h1>
		        <p>Ability to make a custom plot belongs here!</p>
		    </div>
      		</div>
			<h3>Current Jobs</h3>
			<li>Arabidopsis Jun-13-2013</li>
			<!--  Modal button -->
			<a data-toggle="modal" href="#myModal" class="btn btn-default btn-lg"
						style="width: auto;">Select Data</a>
			  <!-- Modal -->
			  <div class="modal fade large" id="myModal" tabindex="-1"
						role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			    <div class="modal-dialog large">
			      <div class="modal-content modal-fit">
			        <div class="modal-header">
			          <button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">&times;</button>
			          <h4 class="modal-title">Select Data</h4>
			        </div>
			        <div class="modal-body">
			          <img src="<c:url value="/resources/images/spreadsheet.png"/>"></img>
			        </div>
			        <div class="modal-footer">
			          <button type="button" class="btn btn-default"
										data-dismiss="modal">Close</button>
			          <button type="button" class="btn btn-default">Save changes</button>
			        </div>
			      </div>
							<!-- /.modal-content -->
			    </div>
						<!-- /.modal-dialog -->
			  </div>
					<!-- /.modal -->
		</div>
	</div>
	</jsp:body>
</tags:userarea-template>
</body>
</html>
