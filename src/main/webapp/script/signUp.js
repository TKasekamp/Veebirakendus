var signup = {
	objectify: function(t1,t2,t3,t4){
		return {
			user: t1,
			email: t2,
			passwd: t3
		};
	}
};

$(function() {
	function newUser(){
		var newuser = $("#signup-username").val();
		var newemail = $("#signup-email").val();
		var pass1 = $("#signup-password").val();
		var pass2 = $("#signup-password2").val();
		if(pass1 == pass2){
			console.log(signup.objectify(newuser,newemail,pass1));
		}
		else{
			console.log("passwords do not match");
		}
	}

    $('#signup-submit').click(function() {
		newUser();
    });	
});
