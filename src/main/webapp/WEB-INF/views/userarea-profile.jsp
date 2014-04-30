<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
	<jsp:attribute name="active">Profile</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<h1>Profile</h1>
			<table class="table table-striped">
					
					<tr>
						<td>Username:</td><td>${user.username }</td><td></td>
					</tr>
					<tr>
						<td>Password:</td><td>*******</td>
						<td>
							<a data-toggle="modal" class=""
							href='#${ user.username }'>Change Password</a>
							<tags:modal-template>
								<jsp:attribute name="id">${ user.username }</jsp:attribute>
								<jsp:attribute name="title">Change ${ user.username }'s Password</jsp:attribute>
								<jsp:body>
								  <form role="form" class="changepass">
				        		    <input type=hidden class="userid" name="userid"
											value="${user.userId }" />
									  <div class="form-group">
									    <label for="oldpass">Current Password</label>
									    <input type="password" class="form-control" id="oldpass"
									    		placeholder="Current Password" name="oldpass">
									  </div>
									  <div class="form-group">
									    <label for="newpass">New Password</label>
										<input type="password" class="form-control" id="newpass"
												placeholder="New Password" name="newpass">
									  </div>
									  <div class="form-group">
									    <label for="validate">Re-enter New Password</label>
										<input type="password" class="form-control" id="validate"
												placeholder="New Password" name="validate">
									  </div>
									  <div class="result hidden"></div>
									  <button type="submit"
											class="btn btn-default btn-block btn-large">Submit</button>
								  </form>
								</jsp:body>
							</tags:modal-template>
						</td>
					</tr>
					<tr>
						<td>
						Group Name:
						</td>
						<td>
						${ user.group.groupName }
						</td>
						<td>
						</td>
					</tr>
					<tr>
						<td>Authority:</td>
						<td>
						${ user.authority }
						</td>
						<td>
						</td>
					</tr>
					<tr>
						<td>Active Jobs:</td>
						<td></td>
						<td></td>
					</tr>
			</table>
		</div>
	</div>
	</jsp:body>
</tags:userarea-template>
</body>
<!-- page specific JS -->
<script type="text/javascript" src="<c:url value="/resources/js/jquery.validate.min.js"/>"></script>
<script type="text/javascript">
	$().ready(function() {
		$('.changepass').validate({
							errorClass : 'error',
							validClass : 'success',
							errorElement : 'span',
							highlight : function(element, errorClass,
									validClass) {
								$(element).parents(
										"div[class='clearfix']")
										.addClass(errorClass)
										.removeClass(validClass);
							},
							unhighlight : function(element, errorClass,
									validClass) {
								$(element).parents(".error")
										.removeClass(errorClass)
										.addClass(validClass);
							},
							rules : {
								newpass : {
									required : true,
									minlength : 5
								},
								validate : {
									required : true,
									equalTo : "#newpass",
									minlength : 5
								}
							}
						});
		$('.changepass').submit(function() {
				var form = $(this);
				$.ajax({
					type : "POST",
					url : '<c:url context="/phenofront/userarea/profile" value="/changepass" />',
					data : $(this).serialize(),
					success : function(data) {
						form.find('.result').removeClass('hidden alert alert-danger');
						form.find('.result').addClass("alert alert-success");
						form.find('.result').text("Password changed successfully.");
					},
					error : function(xhr,status, error) {
						form.find('.result').removeClass('hidden alert alert-success');
						form.parent().find('.result').addClass("alert alert-danger");
						form.parent().find('.result').text(xhr.responseText);
					}
				});
		return false;
	});

});
</script>
</html>
