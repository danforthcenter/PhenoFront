<%@ attribute name="id" required="true" rtexprvalue="true"%>
<%@ attribute name="title" required="true" rtexprvalue="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="modal fade large" id="${ id }" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modal-fit">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title">${ title }</h4>
			</div>
			<div class="modal-body">
				<jsp:doBody/>
			</div>
		</div>
	</div>
</div>