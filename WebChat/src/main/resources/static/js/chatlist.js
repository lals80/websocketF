var form = document.getElementById('Chatlist');

function registerChatroom() {
	form.action="/chattings/form"
	form.setAttribute('method', 'GET');
	form.submit();
}

function registerProcess() {
	form.action="/chattings"
	form.setAttribute('method', 'POST');
	form.submit();
}