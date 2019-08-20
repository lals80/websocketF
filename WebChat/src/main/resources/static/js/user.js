$(document).ready(function() {
    $('#btnUpdateUser').click(function(){
    	$('#userUpdateForm').append("<input type='hidden' id='putOrDelete' name='_method' value='PUT'/>");
    });

    $('#btnDeleteUser').click(function(){
    	$('#userUpdateForm').append("<input type='hidden' id='putOrDelete' name='_method' value='DELETE'/>");
    });
})

function logout() {
	location.href = "/logout.do";
}

function chatlist() {
	location.href = "/chattings"
}