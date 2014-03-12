function loadCode(){
	$.ajax('/data', {
		dataType: 'json',
		success: function (itemsJson) {
			var tag = $('.content');
			for (var i = 0; i < itemsJson.length; i++) {
				tag.append("<a href=\"source.html?id=1\">" + itemsJson[i].name + "</a>");
			}
		},
		error: function (req, text) {
			console.error('failed to load items: ' + text);
		}
	});
};



$(function() {
	loadCode();
});
