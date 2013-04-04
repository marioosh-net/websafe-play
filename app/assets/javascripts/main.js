$(document).ready(function(){
	$('#url').select();
	$('#url').focus();
	
	$('#okAsync').click(function(){
		$('body').append('<iframe src="/asyncPost?'+$('#form').serialize()+'"></iframe>');
		/*
		$.ajax({
			url: '/asyncPost',
			method: 'post',
			data: $('#form').serialize()
		});
		*/
	});
	
	$('#comet').click(function(){
		$.ajax({
			url: '/comet',
			method: 'get'
		});		
	});
	
	$('#open').click(function(){
		$('body').append('<iframe src="/comet1"></iframe>');
		/*
		$.ajax({
			url: '/comet1',
			method: 'get',
			success: function(data) {
				alert(data);
				// $(document).append(data);
			}
		});
		*/		
	});
	
	$('#open1').click(function(){
		$.ajax({
			url: '/chunks1',
			method: 'get',
			success: function(data) {
			   eval(data);
			}
		});		
	});	
	
});

window['log'] = log
function log(m) {
	$('#log').append(m+'<br\>');
	console.log(m);
	
	var objDiv = document.getElementById("log");
	objDiv.scrollTop = objDiv.scrollHeight;
	
	if(m == 'DONE') {
		window.location.reload();
		// lepiej byloby przeladowywac tylko liste 
	}
}
