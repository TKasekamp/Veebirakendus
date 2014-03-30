var editing = false;

var code = {
	id : "",
	text : ""
};

var source = {
	objectify: function(t1,t2){
		return{
			id : t1,
			text : t2,
			SID : getCookie()
		};
	}
};

function evaluate() {
	code.text = $("#codearea").val();
	code.id = gup("id");
}

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
		var objekt = source.objectify(code.id,code.text);
		$.ajax('/edit', {
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
		enableTab("codearea");
		position.fadeToggle();
		editing = true;
	}
};

$(function() {
	evaluate();
	SyntaxHighlighter.highlight();
	$("#edit").click(function(){
		edit();
	});
});