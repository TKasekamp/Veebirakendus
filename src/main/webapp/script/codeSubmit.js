
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
    loadSubmitted: function(objekt,text) {
        var header = $('#resp-name');
		var body = $('#resp-body');
		header.hide();
		body.hide();
		header.html(objekt.name);
		body.html(text);
		header.fadeToggle();
		body.fadeToggle();
    },

    newCode: function() {
        var name = $('#codename').val();
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
				pump.loadSubmitted(objekt,text);
            },
            error: function(req, text) {
				objekt.name = "Uploading failed";
                loadSubmitted(objekt.name,"we are sorry but we seemed to have misplaced your code, please try again in a minute");
            }
        });
    }
};

$(function() {
    $('#submit1').click(function() {
		pump.newCode();
        //vldemo2.makeBid(viewingList, bidBuilder);
    });
	
	
});



