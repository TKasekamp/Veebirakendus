var mystuff = {
	objectify : function(){
		return{
			SID : getCookie()
		};
	}
};

function loadCode(){
	$.ajax('/data', {
		dataType: 'json',
		data: mystuff.objectify(),
		success: function (itemsJson) {
			var tag = $('.down');
			for (var i = 0; i < itemsJson.length; i++) {
				tag.append("<li><a href=\"source.html?id=" + itemsJson[i].id + "\">" + itemsJson[i].name + "</a></li>");
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
