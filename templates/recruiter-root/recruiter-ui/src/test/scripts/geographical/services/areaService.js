define([],
	    function() {
	        return function($rootScope, $resource) {
	            "use strict";

	            var self = this;
	            
	           var areaTypeRest = $resource("/rom-geographical-api/geo/areatypes/:id",
            			{
    						id: "@reference"
            			},
               			{
               				getAll: {method:'GET', isArray:false}
               			});
	           
	           var areaRest = $resource("/rom-geographical-api/geo/areas/:id",
           			{
   						id: "@reference"
           			});

	            self.getAreaType = function(areaTypeId, callbackFunc){
	            	areaTypeRest.get({id:areaTypeId}, function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.getAreaTypes = function(callbackFunc){
	            	areaTypeRest.getAll(function(data){
	        			callbackFunc(data);
	            	});
	            }
	            
	            self.getArea = function(areaId, callbackFunc){
	            	areaRest.get({id:areaId}, function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.saveAreaType = function(areaType, successCallbackFunc, errorCallbackFunc){
	            	areaType.$save(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            };
	            
	            self.saveArea = function(area, successCallbackFunc, errorCallbackFunc){
	            	area.$save(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            };
	            
	            self.deleteAreaType = function(areaType, successCallbackFunc, errorCallbackFunc){
	            	areaType.$remove(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            };
	            
	            self.deleteArea = function(area, successCallbackFunc, errorCallbackFunc){
	            	area.$remove(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            };
	            
	            self.createAreaType = function(){
	            	return new areaTypeRest();
	            };
	            
	            self.createArea = function(){
	            	return new areaRest();
	            };
	            
	            return self;
	        };
	    });