/* Functions */
var submit = {
	objectify: function(t1,t2,t3,t4){
		return {
			name: t1,
			text: t2,
			language: t3,
			privacy: t4,
			SID : getCookie()
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
        $.ajax('/data', {
            type: 'POST',
            data: JSON.stringify(objekt), // pack the bid object into json string
            success: function(objekt) {
                // server returns the bid with its new generated id
                // syncing js&dom is a pain. angularjs may help
				pump.loadSubmitted(objekt);
            },
            error: function(req, text) {
				var id = $('#resp');
				id.html("Uploading failed. Failed to connect to server");
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



