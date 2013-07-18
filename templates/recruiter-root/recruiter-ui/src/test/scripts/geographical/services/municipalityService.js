define([],
	    function() {
	        return function($rootScope, $resource) {
	            "use strict";

	            var self = this;
	            var fusedMunicipality = undefined;
	            
	           var fusedMunicipalityRest = $resource("/rom-geographical-api/geo/fusedmunicipalities/:id",
           			{
   						id: "@reference"
        			},
			           {
		      				getAll: {method:'GET', isArray:false}
           			});
	            
	           var subMunicipalityRest = $resource("/rom-geographical-api/geo/submunicipalities/:id",
	           			{
	   						id: "@reference"
	           			});
	            
	           var subMunicipalitiesRest = $resource("/rom-geographical-api/geo/municipalities/:id/submunicipalities",
	           			{
	   						id: "@reference"
	           			});
		            
	            self.getFusedMunicipality = function(fusedMunicipalityId, callbackFunc){
	            	fusedMunicipalityRest.get({id:fusedMunicipalityId}, function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.getSubMunicipality = function(subMunicipalityId, callbackFunc){
	            	subMunicipalityRest.get({id:subMunicipalityId}, function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.getFusedMunicipalities = function(callbackFunc){
	            	fusedMunicipalityRest.getAll(function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.getSubMunicipalities = function(fusedMunicipalityId, callbackFunc){
	            	subMunicipalitiesRest.get({id:fusedMunicipalityId}, function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.saveFusedMunicipality = function(fusedMunicipality, successCallbackFunc, errorCallbackFunc){
	            	fusedMunicipality.$save(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            };
	            
	            self.saveSubMunicipality = function(subMunicipality, successCallbackFunc, errorCallbackFunc){
	            	subMunicipality.$save(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            };

	            return self;
	        };
	    });