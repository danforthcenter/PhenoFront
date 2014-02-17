<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:admin-template>
	<jsp:attribute name="active">User Management</jsp:attribute>
	<jsp:body>
	<div class="container">
		<div class="jumbotron">
			<h1>User Management</h1>
			<table class="table table-striped">
					<tr>
						<th>Username</th>
						<th>Group</th>
						<th>Authority</th>
						<th>Change Password</th>
					</tr>
				<c:forEach var='user' items='${ users }'>
					<tr>
						<td>
							<a data-toggle="modal" href="#username${user.userId}">${user.username }</a>
							<tags:modal-template title="Change ${user.username }'s username" id="username${user.userId }">
							<jsp:body>
						      <form role="form" class="username">
				        	    <input type=hidden class = "userid" name="userid" value="${user.userId }" />
								  <div class="form-group">
								    <label for="username">Username</label>
									<input type="text" name="username" class="form-control" placeholder="${user.username}"/>									  
								  </div>
								  <div class="result hidden"></div>
								    <button type="submit" class="btn btn-default btn-block btn-large">Submit</button>
								  </form>
								</jsp:body>
							</tags:modal-template>
						</td>
						<td>
							<a data-toggle="modal" href="#group${user.userId}" style="width: auto;">${ user.group.groupName }</a>
							<tags:modal-template title="Change ${user.username}'s group" id="group${user.userId }">
								<jsp:body>
						          <form role="form" class="group">
				        		    <input type=hidden class = "userid" name="userid" value="${user.userId}" />
								    <div class="form-group">
									  <label for="group">User Group</label>
									    <select class="form-control" name="groupname" id="groupname">
									      <c:forEach var='group' items='${groups}'>
									      	<option>${ group.groupName }</option>
									      </c:forEach>
										</select>
									</div>
									<div class="result hidden"></div>
								    <button type="submit" class="btn btn-default btn-block btn-large">Submit</button>
								  </form>
								</jsp:body>
							</tags:modal-template>
						</td>
						<td> 
							<a data-toggle="modal" href="#authority${user.userId }" style="width: auto;">${ user.authority }</a>
							<tags:modal-template>
								<jsp:attribute name="id">authority${user.userId }</jsp:attribute>
								<jsp:attribute name="title">Change ${ user.username }'s Password</jsp:attribute>
								<jsp:body>
								<form role="form" class="authority">
				        		  <input type=hidden class = "userid" name="userid" value="${user.userId }" />
								    <div class="form-group">
									  <label for="authority">User Authority</label>
									    <select class="form-control" name="authority" id="authority">
										  <option>ROLE_ADMIN</option>
										  <option>ROLE_USER</option>
										</select>
									</div>
									<div class="result hidden"></div>
								    <button type="submit" class="btn btn-default btn-block btn-large">Submit</button>
								  </form>
								</jsp:body>
							</tags:modal-template>
						</td>
						<td>
							<a data-toggle="modal" class="btn btn-default" href='#${ user.username }'>Change Password</a>
							<tags:modal-template>
								<jsp:attribute name="id">${ user.username }</jsp:attribute>
								<jsp:attribute name="title">Change ${ user.username }'s Password</jsp:attribute>
								<jsp:body>
								  <form role="form" class="changepass">
				        		    <input type=hidden class = "userid" name="userid" value="${user.userId }" />
									  <div class="form-group">
									    <label for="oldpass">New Password</label>
										<input type="password" class="form-control" id="newpass" placeholder="New Password" name="newpass">
									  </div>
									  <div class="form-group">
									    <label for="validate">Re-enter New Password</label>
										<input type="password" class="form-control" id="validate" placeholder="New Password" name="validate">
									  </div>
									  <div class="result hidden"></div>
									  <button type="submit"	class="btn btn-default btn-block btn-large">Submit</button>
								  </form>
								</jsp:body>
							</tags:modal-template>
							<a data-toggle="modal" href="#remove${user.userId }" class="pull-right"><span class ='glyphicon glyphicon-remove'></span></a>
							<tags:modal-template title="Delete User?" id="remove${user.userId }">
								<jsp:body>
									<form role="form" class="removeuser">
										<h2>Are you sure you want to remove ${user.username}?</h2>
										<div class="result hidden"></div>
										<input type="hidden" name="userid" value="${user.userId }"/>
										<button type="submit" class="btn btn-default btn-block btn-large">Yes</button>
										<button type="button" class="btn btn-default btn-block btn-large" data-dismiss="modal">No</button>
									</form>
								</jsp:body>
							</tags:modal-template>
						</td>
					</tr>
				</c:forEach>
					<tr>
						<td><small>New User</small></td>
						<td><small>New User</small></td>
						<td><small>New User</small></td>
						<td><small>New User</small>
							<a data-toggle="modal" href="#newuser"><span class="glyphicon glyphicon-plus pull-right"></span></a>
							<tags:modal-template title="New User" id="newuser">
								<jsp:body>
									<form role="form" class="newuser">
									  <div class="form-group">
									    <label for="username">Username</label>
										<input type="text" class="form-control" id="username" placeholder="Username" name="username">
									  </div>
									  <div class="form-group">
									    <label for="password">Password</label>
										<input type="password" class="form-control" id="password" placeholder="Password" name="password">
									  </div>
									  <div class="form-group">
									    <label for="validate">Re-enter Password</label>
										<input type="password" class="form-control" id="validate" placeholder="Confirm Password" name="validate">
									  </div>
									  <div class="form-group">
									    <label for="authority">User Authority</label>
									      <select class="form-control" name="authority" id="authority">
										    <option>ROLE_ADMIN</option>
										    <option>ROLE_USER</option>
										  </select>
									  </div>
									  <div class="form-group">
									    <label for="group" >User Group</label>
									      <select id="grouptip" data-toggle="tooltip" title="If this user needs a new group, choose empty, create the group, and then update the new user." class="form-control" name="groupname" id="groupname">
									        <c:forEach var='group' items='${groups}'>
									      	  <option>${ group.groupName }</option>
									        </c:forEach>
									          <option style="font-style:italic;">empty</option>
										  </select>
									  </div>
									  <div class="result hidden"></div>
									  <button type="submit"	class="btn btn-default btn-block btn-large">Submit</button>
									</form>
								</jsp:body>
							</tags:modal-template>
						</td>
					</tr>
			</table>
		</div>
	</div>
	</jsp:body>
</tags:admin-template>
</body>
<!-- page specific JS -->
<script type="text/javascript">
$().ready(function(){
	$('#grouptip').tooltip();
	$('.changepass').validate({
	    errorClass:'error',
	    validClass:'success',
	    errorElement:'span',
	    highlight: function (element, errorClass, validClass) { 
	        $(element).parents("div[class='clearfix']").addClass(errorClass).removeClass(validClass); 
	    }, 
	    unhighlight: function (element, errorClass, validClass) { 
	        $(element).parents(".error").removeClass(errorClass).addClass(validClass); 
	    },
	    rules:  {
	          newpass: { 
	                required: true, minlength: 5
	          }, 
	          validate: { 
	                required: true, equalTo: "#newpass", minlength: 5
	          }
	    }
	});
	$('.changepass').submit(function(){
		var form = $(this);
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront/admin" value="/changepass" />',
			data: $(this).serialize(),
			success: function(data){
				form.find('.result').removeClass('hidden alert alert-danger');
				form.find('.result').addClass("alert alert-success");
				form.find('.result').text("Password changed successfully.");
			},
			error: function(xhr, status, error) {
				form.find('.result').removeClass('hidden alert alert-success');
				form.parent().find('.result').addClass("alert alert-danger");
				form.parent().find('.result').text(xhr.responseText);
			}
		});
		return false;
	});
	$('.authority').submit(function(){
		var form = $(this);
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront/admin" value="/changeauthority" />',
			data: $(this).serialize(),
			success: function(data){
				form.find('.result').removeClass('hidden alert alert-danger');
				form.find('.result').addClass("alert alert-success");
				form.find('.result').text("Authority changed successfully. Refresh your page to see the changes.");
			},
			error: function(xhr, status, error) {
				form.find('.result').removeClass('hidden alert alert-success');
				form.parent().find('.result').addClass("alert alert-danger");
				form.parent().find('.result').text(xhr.responseText);
			}
		});
		
		return false;
	});
	$('.group').submit(function(){
		var form = $(this);
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront/admin" value="/changegroup" />',
			data: $(this).serialize(),
			success: function(data){
				form.find('.result').removeClass('hidden alert alert-danger');
				form.find('.result').addClass("alert alert-success");
				form.find('.result').text("Group changed successfully. Refresh your page to see changes.");
			},
			error: function(xhr, status, error) {
				form.find('.result').removeClass('hidden alert alert-success');
				form.parent().find('.result').addClass("alert alert-danger");
				form.parent().find('.result').text(xhr.responseText);
			}
		});
		return false;
	});
	$('.username').submit(function(){
		var form = $(this);
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront/admin" value="/changeusername" />',
			data: $(this).serialize(),
			success: function(data){
				form.find('.result').removeClass('hidden alert alert-danger');
				form.find('.result').addClass("alert alert-success");
				form.find('.result').text("Username changed successfully. Refresh your page to see changes.");
			},
			error: function(xhr, status, error) {
				form.find('.result').removeClass('hidden alert alert-success');
				form.parent().find('.result').addClass("alert alert-danger");
				form.parent().find('.result').text(xhr.responseText);
			}
		});
		return false;
	});
	$('.removeuser').submit(function(){
		var form = $(this);
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront/admin" value="/removeuser" />',
			data: $(this).serialize(),
			success: function(data){
				location.reload();
				form.find('.result').removeClass('hidden alert alert-danger');
				form.find('.result').addClass("alert alert-success");
				form.find('.result').text("User removed successfully. Refresh your page to see changes.");
			},
			error: function(xhr, status, error) {
				form.find('.result').removeClass('hidden alert alert-success');
				form.parent().find('.result').addClass("alert alert-danger");
				form.parent().find('.result').text(xhr.responseText);
			}
		});
		return false;
	});
	$('.newuser').submit(function(){
		var form = $(this);
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront/admin" value="/newuser" />',
			data: $(this).serialize(),
			success: function(data){
				location.reload();
				form.find('.result').removeClass('hidden alert alert-danger');
				form.find('.result').addClass("alert alert-success");
				form.find('.result').text("New user added! Refresh your page to see changes.");
			},
			error: function(xhr, status, error) {
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
