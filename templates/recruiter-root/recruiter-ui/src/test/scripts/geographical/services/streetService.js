define([],
	    function() {
	        return function($rootScope, $resource) {
	            "use strict";

	            var self = this;
	            
	           var streetRest = $resource("/rom-geographical-api/geo/streets/:id",
            			{
    						id: "@reference"
               			});
	           
	            self.getStreet = function(streetId, callbackFunc){
	            	streetRest.get({id:streetId}, function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.createStreet = function(){
	            	return new streetRest();
	            }
	            
	            self.saveStreet = function(street, successCallbackFunc, errorCallbackFunc){
	            	street.$save(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            }
	            self.deleteStreet = function(street, successCallbackFunc, errorCallbackFunc){
	            	street.$remove(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            }

	            return self;
	        };
	    });