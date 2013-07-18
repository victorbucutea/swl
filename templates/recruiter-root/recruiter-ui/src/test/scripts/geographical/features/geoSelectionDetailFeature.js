define([], function() {
	var GeoSelectionController = function($scope, geoselectionService) {
		"use strict";



		$scope.save = function() {

			var types = this.modifications;

			var deletedTypes = types.deletedItems;
			var updatedTypes = types.updatedItems;
			var createdTypes = types.createdItems;


			/*
			 * chain operations. If create succeeds, then update, if update
			 * succeeds then delete.
			 */
			createNewTypes(createdTypes, function() {
				saveUpdatedTypes(updatedTypes, function() {
					removeDeletedTypes(deletedTypes);
				});
			});

		};



		function createNewTypes(createdTypes, saveCallback) {
			if (createdTypes.isEmpty()) {
				saveCallback();
				return;
			}
			geoselectionService.createGeoselectionTypes(createdTypes,saveCallback,init);
		}

		
		function saveUpdatedTypes(updatedTypes, removeCallback) {
			if (updatedTypes.isEmpty()) {
				removeCallback();
				return;
			}
			geoselectionService.saveGeoselectionTypes(updatedTypes,removeCallback, init);
		}



		function removeDeletedTypes(deletedTypes) {
			if (deletedTypes.isEmpty()) {
				init();
				return;
			}
			geoselectionService.deleteGeoselectionTypes(deletedTypes,init);
		}


		$scope.cancel = function() {
			console.log("cancelling .");
		}

		init();

		function init() {
			geoselectionService.getAllGeoselectionTypes(function(data) {
				$scope.geoselectiontypes = data;
			});

		}

	};

	return {
		isFeatureOf : function(module) {
			module.controller('GeoSelectionController', [ '$scope', 'geoselectionService',
					GeoSelectionController ])
		}
	};
});