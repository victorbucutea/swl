define([],
	    function() {
	        return function($rootScope, $resource) {
	            "use strict";

	           var self = this;
	            
	           var physicalPointTypeRest = $resource("/rom-geographical-api/geo/physicalpointtypes/:id",
            			{
    						id: "@reference"
            			},
               			{
               				getAll: {method:'GET', isArray:false}
               			});

	           
	           self.getPhysicalPointType = function(ppTypeId, callbackFunc){
	        	   physicalPointTypeRest.get({id:ppTypeId}, function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.getPhysicalPointTypes = function(callbackFunc){
	            	physicalPointTypeRest.getAll(function(data){
	        			callbackFunc(data);
	            	});
	            }
	            
	            self.savePhysicalPointType = function(ppType, successCallbackFunc, errorCallbackFunc){
	            	ppType.$save(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            };
	            
	            self.deletePhysicalPointType = function(ppType, successCallbackFunc, errorCallbackFunc){
	            	ppType.$remove(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            };
	            
	            self.createPhysicalPointType = function(){
	            	return new physicalPointTypeRest();
	            };
	            
	            return self;
	        };
	    });