define([], function() {
	"use strict";
	return function() {
		return function(data,input) {
			var results = new Array();
			
			if (!data)
				return false;
			
			for (var i = 0; i< data.length; i++ ){
				var item = data[i];
				var idContainsInput = item.id.indexOf(input) != -1;
				var nameContainsInput = item.name.indexOf(input) != -1;
				if (idContainsInput || nameContainsInput ) {
					results.push(item);
				}
			}
			return results;
		};
	};
});