define([ 'jquery' ], function() {

	Array.prototype.remove = function(element) {
		var idxOfValue = this.indexOf(element);
		this.splice(idxOfValue, 1);
	};

	Array.prototype.add = function(element) {
		this.push(element);
	};

	
	$.fn.hasParent = function(a) {
		return $(a).find(this).length;	
	};
	
	
});
