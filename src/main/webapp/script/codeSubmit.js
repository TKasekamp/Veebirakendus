/* Functions */
var submit = {
	objectify: function(t1,t2,t3,t4){
		return {
			name: t1,
			text: t2,
			language: t3,
			privacy: t4
		};
	}
};

var pump = {
    loadSubmitted: function(objekt) {
		window.location.href = "source.html?id=" + objekt.id;
    },

    newCode: function() {
        var name = $('#codename').val();
		if(!name){
			name = "Untitled";
		}
		var text = $('#codearea').val();
		var language = $('#language').find(':selected').text();
		var privacy = $('#privacy').find(':selected').text();
		var objekt = submit.objectify(name,text,language,privacy);
        $.ajax('/data/ajax', {
            type: 'POST',
            data: JSON.stringify(objekt), 
            success: function(objekt) {
            	clearHeaders();
				pump.loadSubmitted(objekt);
            },
            error: function(req, text) {
				window.alert("Failed to connect to the server.\nPlease check your internet connection and try again later.");
            }
        });
    }
};

$(function() {
    $('#submit1').click(function() {
		pump.newCode();
    });
	
	enableTab("codearea");
});

function clearHeaders(){
	window.localStorage.clear();
	$('#codename').val('');
	$('#codearea').val('');
}



