var source = {
	objectify: function(t1){
		return{
			id : t1
		};
	}
};

function loadFile(){
	var objekt = source.objectify(gup('id'));
	$.ajax('/data', {
		dataType: 'json',
		data: objekt,
		success: function (item) {
			var name = $('h3#name');
			var tag = $('pre');
			name.html(item.name);
			tag.attr("class", "brush: " + item.language);
			tag.html(item.text);
			SyntaxHighlighter.highlight();
		},
		error: function (req, text) {
			console.error('failed to load item: ' + text);
		}
	});
};

$(function() {
	loadFile();
});