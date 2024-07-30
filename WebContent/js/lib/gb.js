$(function() {
	$("#i").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : "wordschecker",
				type : "GET",
				dataType : "json",
				data : {
					word : request.term
				},
				success : function(data) {
					response($.map(data, function(item) {
						return {
							value : item
						};
					}));
				},
				error : function(error) {
					alert('error: ' + error);
				}
			});
		},
		minLength : 2
	});
	
//	$("#i").autocomplete({
//		source : data
//	});
});

$(function() {
	$("#search").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : "wordschecker",
				type : "GET",
				dataType : "json",
				data : {
					word : request.term
				},
				success : function(data) {
					response($.map(data, function(item) {
						return {
							value : item
						};
					}));
				},
				error : function(error) {
					alert('error: ' + error);
				}
			});
		},
		minLength : 2
	});
	
//	$("#i").autocomplete({
//		source : data
//	});
});