define(['jquery'], function() {
	"use strict";

	return function($scope,$http) {
		
		var rootDiv = null;
	
		
		$scope.focus = function () { 
			rootDiv.addClass("open");
		};
		
		
		this.init  = function (element ) {
			rootDiv = $(element);
			
			$(document).click(function(event){
				var clickTarget = $(event.target);
				if (!clickTarget.hasParent(rootDiv)) {
					//clicked outside parent div
					rootDiv.removeClass("open");
				}
			});
		};
	};
});