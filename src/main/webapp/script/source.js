var editing = false;

var code = {
	id : "",
	text : "",
	lang : ""
};

var source = {
	objectify: function(t1){
		return{
			id : t1
		};
	},
	objectify2: function(t1,t2){
		return{
			id : t1,
			text : t2
		};
	}
};

function loadFile(){
	code.id = gup('id');
	var objekt = source.objectify(code.id);
	$.ajax('/data', {
		dataType: 'json',
		data: objekt,
		success: function (item) {
			var name = $('h3#name');
			var tag = $('pre');
			name.html(item.name);
			tag.attr("class", "brush: " + item.language.toLowerCase());
			tag.html(item.text);
			SyntaxHighlighter.highlight();
			code.text = item.text;
			code.lang = item.language.toLowerCase();
		},
		error: function (req, text) {
			console.error('failed to load item: ' + text);
		}
	});
};

function edit(){
	var button = $("#edit");
	if(editing){
		button.html("Edit");
		var position = $(".content");
		code.text = $('#codearea').val();
		position.hide();
		position.empty();
		position.append("<pre class=\"brush: " + code.lang + "\">" + code.text + "</pre>");
		SyntaxHighlighter.highlight();
		position.fadeToggle();
		editing = false;
		var objekt = source.objectify2(code.id,code.text);
		$.ajax('/data', {
            type: 'POST',
            data: JSON.stringify(objekt), // pack the bid object into json string
            success: function(objekt) {
                //comment
            },
            error: function(req, text) {
				console.log(text);
            }
        });
	}
	else{
		button.html("Save");
		var position = $(".content");
		position.hide();
		position.empty();
		position.append("<textarea id=\"codearea\"></textarea>");
		$("#codearea").val(code.text);
		position.fadeToggle();
		editing = true;
	}
};

$(function() {
	loadFile();
	
	$("#edit").click(function(){
		edit();
	});
});