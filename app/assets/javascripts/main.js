$(document).ready(function(){
	/*
	$('#url').select();
	$('#url').focus();
	*/
	$('#search-string').select();
	$('#search-string').focus();
	
	/*
	if($('#list').is(':empty')) {
		$('#list').load('/list');
	}
	*/
	
	$('#form input').keydown(function(e) {
	    if (e.keyCode == 13) {
	    	$('#okAsync').trigger('click');
	    }
	});
	
	$('#okAsync').click(function(){
		if(!$('#log').is(":visible")) {
			$('#log').slideToggle();
		}
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
	
	$('.log-header').click(function() {
		$('#log').slideToggle();
	});
	
	$('#search-start').click(function(){
		if($('#search-string').val().trim() != '') {
			window.location = '/search1/'+$('#search-string').val().trim();
		}
	});
	$('#search-string').keydown(function(e) {
	    if (e.keyCode == 13) {
	    	$('#search-start').trigger('click');
	    }
	});
	$('.opendeps').click(depsclick);
	
});

window['depsclick'] = depsclick;
var depsclick = function(){
	var id = $(this).attr('parentid');
	if($('.deps_'+id).is(":visible")) {
		$('.deps_'+id).toggle();
	} else {
		$('#deps_img_'+id).addClass('loading');
		$('.deps_'+id).load('/deps/'+id, function(){
			$('.deps_'+id).toggle();
			$('#deps_img_'+id).removeClass('loading');
		});
	}
	return false;
};

window['log'] = log
function log(m) {
	$('#log').append(m+'<br\>');
	console.log(m);
	
	var objDiv = document.getElementById("log");
	objDiv.scrollTop = objDiv.scrollHeight;
	
	if(m == 'DONE') {
		/* window.location.reload();*/
		$('#list').load('/list', function(){
			$('.opendeps').click(depsclick);					
		});
		$('#clicks1').load('/clicks');
		$('#tags1').load('/tags');		
		if($('#log').is(":visible")) {
			$('#log').slideToggle();
			$('#new').slideToggle();
		}
		// $('#new').slideToggle();
	}
}

window['openLink'] = openLink;
function openLink(id) {
	jQuery.get('/click/'+id, function(data) {
		jQuery('div.count_'+id).html(data);
		$('#clicks1').load('/clicks');
	}, 'text');
	window.open('/open/'+id);
}
