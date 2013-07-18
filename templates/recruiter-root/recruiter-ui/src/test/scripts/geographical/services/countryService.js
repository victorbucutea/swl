define([],
	    function() {
				/*return $resource("/rom-geographical-api/geo/countries/:id",
            			{id: "@reference"}, {
					        update: {method:'POST'}
				});*/
	
	        return function($rootScope, $resource) {
	            "use strict";

	            var self = this;
	            var country = undefined;
	            
	           var countryRest = $resource("/rom-geographical-api/geo/countries/:id",
            			{
    						id: "@reference"
            			},
			           {
		      				getAll: {method:'GET', isArray:false}
		      			}
			           );

	            self.getCountry = function(countryId, callbackFunc){
	            	countryRest.get({id:countryId}, function(data){
	        			callbackFunc(data);
	            	});
	            };
	            
	            self.get = function(){
	            	return countryRest.get();
	            };
	            
	            self.saveCountry = function(country, successCallbackFunc, errorCallbackFunc){
	            	country.$save(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            }
	            
	            self.deleteCountry = function(country, successCallbackFunc, errorCallbackFunc){
	            	country.$remove(function(data){
	            		successCallbackFunc(data);
	            	}, function(data){
	            		errorCallbackFunc(data);
	            	});
	            }
	            
	            self.createCountry = function(){
	            	return new countryRest();
	            }
	            
	            self.getCountries = function(callbackFunc){
	            	countryRest.getAll(function(data){
	        			callbackFunc(data);
	            	});
	            }
	            
	            return self;
	        };
	    });