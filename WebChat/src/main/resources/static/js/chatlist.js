var form = document.getElementById('Chatlist');

function registerChatroom() {
	form.action="/chattings/form"
	form.setAttribute('method', 'get');
	form.submit();
}