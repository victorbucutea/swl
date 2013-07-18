define([], function() {
	return function($rootScope, $resource, $q,$http) {
		"use strict";

		var self = this;
		var geoselection = undefined;
		var geoselectionTypeRest = $resource("/rom-geographical-api/geo/geoselectiontypes/:id", 
				{
					id : "@reference"
				}, 
				{
					getAll : {method : 'GET', isArray : false}
				});


		
		self.createGeoselectionTypes = function(createdTypes,saveCallback,initCallback) {
			console.log("creating ",createdTypes);
			var promises = [];
			for ( var typeId in createdTypes.data) {
				var data = createdTypes.asJSON("names", typeId);
				promises.push($http.post("/rom-geographical-api/geo/geoselectiontypes", data));
			}
			
			createdTypes.clear();
			$q.all(promises).then(function(arg) {console.log('insert finished', arg);saveCallback();}, 
								  function(e)   {console.log('insert finished with error',e);initCallback();});
		}
		
		
		
		self.saveGeoselectionTypes = function (updatedTypes,removeCallback,initCallback){ 
			var promises = [];
			console.log('saving ', updatedTypes);

			var errorEncountered = false;
			for ( var typeId in updatedTypes.data) {
				var data = updatedTypes.asJSON("updated_names", typeId);
				promises.push($http.post("/rom-geographical-api/geo/geoselectiontypes/" + typeId, data));
			}

			updatedTypes.clear();
			$q.all(promises).then(function(arg) {console.log('save finished',arg);removeCallback();}, 
								  function(e)   {console.log('save finished with error', e);initCallback();});
		}

		
		
		self.deleteGeoselectionTypes = function(deletedTypes, initCallback) {
			var promises = [];
			
			for ( var int in deletedTypes.data) {
					promises.push($http['delete']("/rom-geographical-api/geo/geoselectiontypes/" + int));
			}
			
			deletedTypes.clear();
			$q.all(promises).then(function(arg) {console.log('delete finished', arg);initCallback();}, 
								  function(e) {console.log('delete finished with error', e);initCallback();});
		}

		
		
		
		self.getAllGeoselectionTypes = function(callback) {
			geoselectionTypeRest.getAll(function(data) {
				callback(data);
			});
		}

		return self;
	};
});