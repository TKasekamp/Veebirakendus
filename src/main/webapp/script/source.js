var current_hash = window.location.hash;

function check_hash() {
    if ( window.location.hash != current_hash ) {
        current_hash = window.location.hash;
		console.log(current_hash);
		if(current_hash == "#edit"){
			edit();
		}
		else{
			save();
		}
    }
}

function set_hash( new_hash ) {
    window.location.hash = new_hash;
}

hashCheck = setInterval( "check_hash()", 50 );

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
	code.text = $("#content").html();
	code.id = gup("id");
	code.lang = $("#content").attr("class");
	if(window.location.hash){
		if(window.location.hash == "#edit"){
			edit();
		}
		else{
			save();
		}
	}
}

function save(){
		var button = $("#edit");
		button.html("Edit");
		code.text = $("#codearea").val();
		var position = $(".content");
		position.hide();
		position.empty();
		position.append("<pre class=\"" + code.lang + "\" id=\"content\">" + code.text + "</pre>");
		SyntaxHighlighter.highlight();
		position.fadeToggle();
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

function edit(){
		var button = $("#edit");
		button.html("Save");
		var position = $(".content");
		position.hide();
		position.empty();
		position.append("<textarea id=\"codearea\"></textarea>");
		$("#codearea").val(code.text);
		enableTab("codearea");
		position.fadeToggle();
};

$(function() {
	evaluate();
	SyntaxHighlighter.highlight();
	$("#edit").click(function(){
		if($("#edit").html() == "Edit"){
			set_hash("edit");
		}
		else{
			set_hash("save");
		}
	});
});