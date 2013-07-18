define([],
	    function() {
	        return function($rootScope, $resource) {
	            "use strict";

	            var self = this;
	            
	           var postalLocationRest = $resource("/rom-postal-api/postal/sortingcenters");
	            
	            self.getSortingCenters = function(callbackFunc){
	            	postalLocationRest.get(function(data){
	        			callbackFunc(data);
	            	});
	            }
	            
	            return self;
	        };
	    });