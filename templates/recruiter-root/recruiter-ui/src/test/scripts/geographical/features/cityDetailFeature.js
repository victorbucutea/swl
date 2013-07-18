define([ 'jquery', 'common/utils' ], function() {
	var CityDetailController = function($scope, $http, cityService, countryService) {
		"use strict";
		$scope.isVisible = false;
		$scope.cityId = null;
		$scope.cityName = null;
		$scope.validateChildren = false;
		$scope.city = {};
		$scope.errorMessage = "";
		$scope.infoMessage = "";
		$scope.postalCodes = {};

		var resetModel = function() {
			$scope.validateChildren = false;
			$scope.cityId = null;
			$scope.cityName = null;
			$scope.errorMessage = "";
			$scope.infoMessage = "";
			$scope.postalCodes = {};
			$scope.city.namesArray = [];
			$scope.city.synonymsArray = [];
		};

		var onSaveSuccessCityCallback = function(result) {
			onGetCityCallback(result);
			$scope.infoMessage = "City successfully saved.";
		}

		var onCRUDErrorCityCallback = function(result) {
			console.log("city crud error: "+JSON.stringify(result));
			$scope.errorMessage = createErrorMessage(result.data);
		}
		
		var onDeleteSuccessCityCallback = function(result) {
			$scope.isVisible = true;
			$scope.city = cityService.createCity();
			resetModel();
			$scope.infoMessage = "City successfully deleted.";
		}
		
		$scope.save = function() {
			$("#saveBut").trigger('blurGrid');
			if ($scope.city.reference == undefined) {
				// create
				createNamesToScope($scope.city, $scope.city.namesArray);
				createSynonymsToScope($scope.city, $scope.city.synonymsArray);
				if($scope.postalCodes.added_items!=undefined){
					$scope.city.postalcodes = $scope.postalCodes.added_items; 
				}
			} else {
				// update
				addNamesCollectionsToScope($scope.city, $scope.city.namesArray);
				addSynonymsCollectionsToScope($scope.city, $scope.city.synonymsArray);
				if($scope.postalCodes.added_items!=undefined){
					$scope.city.added_postalcodes =  $scope.postalCodes.added_items; 
				}
				if($scope.postalCodes.updated_items!=undefined){
					$scope.city.updated_postalcodes = $scope.postalCodes.updated_items; 
				}
				if($scope.postalCodes.removed_items!=undefined){
					$scope.city.removed_postalcodes = $scope.postalCodes.removed_items; 
				}
			}
			if($scope.city.country_ref==undefined){
				$scope.city.country_ref = $scope.romaZoom.contextData["Countries"];
			}
			
			$scope.validateChildren = true;
			if (!$scope.$$phase) {
				$scope.$digest();
			}

			console.log("Saving:" + JSON.stringify($scope.city));
			cityService.saveCity($scope.city,
					onSaveSuccessCityCallback, onCRUDErrorCityCallback);

		};
		
		var onGetCityCallback = function(result) {
			$scope.errorMessage = "";
			$scope.infoMessage = "";
			$scope.city = result;
			console.log('$scope.city = ' + $scope.city);
			// TODO determine by user language
			$scope.cityName = determineName($scope.city.name);
			$scope.cityId = result.reference;
			$scope.isVisible = true;
			$scope.city.namesArray = transformToArray($scope.city.name);
			$scope.city.synonymsArray = transformToArray($scope.city.synonyms);
			$scope.postalCodes= {};
			$scope.postalCodes.data = $scope.city.postalCodes.items;
			$scope.city.country_ref=$scope.city.countryRef;
		};

		var refreshCityDetails = function(reference) {
			cityService.getCity(reference, onGetCityCallback);
		};
		
		$scope.remove = function(){
			$("#deleteBut").trigger('blurGrid');
			if(confirm("Are you shure you want to delete this city?")){
				cityService.deleteCity($scope.city, onDeleteSuccessCityCallback, onCRUDErrorCityCallback);
			}
			
		}

		$scope.$watch("romaZoom.activeItem", function(selectedCity,
				lastSelectedCity) {
			if (selectedCity.city != undefined) {
				refreshCityDetails(selectedCity.city.reference);
			} else {
				$scope.isVisible = true;
				$scope.city = cityService.createCity();
				resetModel();
				$scope.city.country_ref = $scope.romaZoom.contextData["Countries"];
			}
			countryService.getCountries(function(data){
				$scope.countries = data.items;
				if (!$scope.$$phase) {
					$scope.$digest();
				}
			});
		});

	};

	return {
		isFeatureOf : function(module) {
			module.controller('CityDetailController', [ '$scope', '$http',
					'cityService', 'countryService', CityDetailController ])
		}
	};
});