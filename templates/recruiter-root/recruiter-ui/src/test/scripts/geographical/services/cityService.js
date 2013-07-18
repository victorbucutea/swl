define([], function() {
	return function($rootScope, $resource) {
		"use strict";

		var cityRest = $resource("/rom-geographical-api/geo/cities/:id", {
			id : "@reference"
		});

		self.getCity = function(cityId, callbackFunc) {
			cityRest.get({
				id : cityId
			}, function(data) {
				callbackFunc(data);
			});
		};

		self.saveCity = function(city, successCallbackFunc,
				errorCallbackFunc) {
			city.$save(function(data) {
				successCallbackFunc(data);
			}, function(data) {
				errorCallbackFunc(data);
			});
		};
		
		 self.deleteCity = function(city, successCallbackFunc, errorCallbackFunc){
         	city.$remove(function(data){
         		successCallbackFunc(data);
         	}, function(data){
         		errorCallbackFunc(data);
         	});
         };
         
         self.createCity = function(){
         	return new cityRest();
         };

		return self;
	};
});