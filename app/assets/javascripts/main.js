$(document).ready(function(){
	$('#url').select();
	$('#url').focus();
	
	$('#okAsync').click(function(){
		$.ajax({
			url: 'http://localhost:9000/asyncPost',
			method: 'post',
			data: $('#form').serialize(),
			success: function(data) {
				console.log(data);
			}
		});
	});
});