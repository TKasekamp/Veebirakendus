$(document).ready(
		function() {
				$("#mobile").click(
					function() {
						var button = $(".mobile-menu");
						if(button.attr("id") == "open"){
							button.attr("id","closed");
						}
						else{
							button.attr("id","open");
						}
					}
				);
		});