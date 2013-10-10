$().ready(function(){
	$('form').validate({
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
	$('form').submit(function(){
		var form = $(this)
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

});