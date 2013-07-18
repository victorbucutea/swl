define([],
	    function() {
	        return function($rootScope, $resource) {
	            "use strict";

	            var self = this;
	            
	           var suffixTypeRest = $resource("/rom-geographical-api/geo/suffixtypes/:id",
            			{
    						id: "@reference"
            			},
               			{
               				getAll: {method:'GET', isArray:false}
               			});
	           
	            self.getSuffixTypes = function(callbackFunc){
	            	suffixTypeRest.getAll(function(data){
	        			callbackFunc(data);
	            	});
	            }
	            
	            self.getSuffixType = function(suffixTypeId, callbackFunc){
	            	suffixTypeRest.get({id:suffixTypeId}, function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.createSuffixType = function(){
	            	return new suffixTypeRest();
	            }
	            
	            self.saveSuffixType = function(suffixType, successCallbackFunc, errorCallbackFunc){
	            	suffixType.$save(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            }
	            self.deleteSuffixType = function(suffixType, successCallbackFunc, errorCallbackFunc){
	            	suffixType.$remove(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            }

	            return self;
	        };
	    });