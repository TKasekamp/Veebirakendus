var signup = {
	objectify: function(t1,t2,t3){
		return {
			username: t1,
			email: t2,
			password: t3
		};
	}
};

$(function() {
	function newUser(){
		var newuser = $("#signup-username").val();
		var newemail = $("#signup-email").val();
		var pass1 = $("#signup-password").val();
		var pass2 = $("#signup-password2").val();
		var objekt = signup.objectify(newuser,newemail,pass1);
		if(pass1 == pass2){
			$.ajax('/signup', {
            type: 'POST',
            data: JSON.stringify(objekt), // pack the bid object into json string
            success: function(objekt) {
				console.log("Fuck off");
            },
            error: function(req, text) {
				objekt.name = "Uploading failed. Failed to connect to server";
                loadSubmitted(objekt);
            }
        });
		}
		else{
			console.log("passwords do not match");
		}
	}

    $('#signup-submit').click(function() {
		newUser();
    });	
});
