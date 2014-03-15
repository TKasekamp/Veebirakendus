var login = {
	objectify : function(t1,t2){
		return{
			username : t1,
			password : t2
		};
	}
};

$(document).ready(function () {
    $("a#login").click(function () {
        var temp = $("div.login");
        if (temp.css("visibility") == "hidden") {
            temp.css("visibility", "visible");
        }
        else {
            temp.css("visibility", "hidden");
        }
    });
	
	$("#loginarea").click(function (){
		var user = $("#username").val();
		var pass = $("#password").val();
		var objekt = login.objectify(user,pass);
		$.ajax('/login', {
            type: 'POST',
            data: JSON.stringify(objekt), // pack the bid object into json string
            success: function(objekt) {
                console.log("Login success");
            },
            error: function(req, text) {
				console.log("Login failed");
            }
        });
	});
});

