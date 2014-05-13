function renderPage() {
	var path = location.pathname;
	var bodyHtml = localStorage.getItem(path);
	var bodyElement = document.createElement("body");
	if (!bodyHtml) {
		bodyElement.innerHTML = "I'm sorry, but the page you're trying to view is not accessible at the moment.<br>";
		bodyElement.innerHTML += "Please check your internet connection or try again later.";
	} else {
		bodyElement.innerHTML = bodyHtml;
	}
	document.body = bodyElement;
}

renderPage();