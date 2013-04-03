$(document).ready(function(){
	$('#url').select();
	$('#url').focus();
	
	$('#okAsync').click(function(){
		$.ajax({
			url: '/asyncPost',
			method: 'post',
			data: $('#form').serialize()
		});
	});
	
	$('#comet').click(function(){
		$.ajax({
			url: '/comet',
			method: 'get'
		});		
	});
});

window['log'] = log
function log(m) {
	$('#log').html(m+'<br\>');
	console.log(m);
}
