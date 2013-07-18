define(
		[ 'jquery', 'common/utils' ],
		function() {
			var SubMunicipalityDetailController = function($scope, $http, municipalityService) {
				"use strict";
				
				$scope.backup = {};
				
				$scope.isVisible = false;
				$scope.subMunicipalityName = null;
				$scope.subMunicipalityId = null;
				$scope.errorMessage = "";
				$scope.infoMessage ="";
				
				$scope.model = {
						primaryLanguage : null,
						secondaryLanguage : null,
						languageFacility: null, 
						synonymsArray :{},
						streets: {}
				};
				
				var resetModel = function() {
					$scope.model.primaryLanguage = null;
					$scope.model.secondaryLanguage = null;
					$scope.model.languageFacility = null;
					$scope.model.synonymsArray = {};
					$scope.streets = {};
					$scope.subMunicipalityName = null;
					$scope.subMunicipality = null;
					$scope.subMunicipalityId = null;
					$scope.errorMessage = "";
					$scope.infoMessage ="";
					$scope.isVisible = false;
				};

				var onGetSubMunicipalityCallback = function(result){
					console.log("Sub Municipality: "+JSON.stringify(result));
					$scope.backup = angular.copy(result);
					resetModel();
					$scope.subMunicipalityName = result.postalCode + " " + determineMunicipalityName(result);
					$scope.subMunicipality = result;
					$scope.model.primaryLanguage = formatLanguage(result.primaryLanguage);
					$scope.model.secondaryLanguage = formatLanguage(result.secondaryLanguage);
					$scope.model.languageFacility = formatLanguage(result.languageFacility);
					$scope.model.synonymsArray = transformToArray($scope.subMunicipality.synonyms);
					$scope.subMunicipalityId = result.reference;
					$scope.isVisible = true;
					$scope.romaZoom.isModified = false;
					$scope.errorMessage = "";
					$scope.infoMessage = "";
					if (!$scope.$$phase) {
						$scope.$digest();
					}
				};
				
				var transformToArray = function(syns) {
					var itemsDataArray = [];
					for (name in syns.items) {
						itemsDataArray.push(syns.items[name]);
					}

					return itemsDataArray;
				};

				var refreshSubMunicipalityDetails = function(item) {
					municipalityService.getSubMunicipality(item.reference, onGetSubMunicipalityCallback);
				};

				var formatLanguage = function(item) {
					if (item == undefined || "UNKNOWN" == item.toUpperCase()) {
						return "-";
					} else {
						return item.charAt(0).toUpperCase() + item.toLowerCase().slice(1);
					}
				};

				var determineMunicipalityName = function(subMunicipality) {
					var firstLanguage = subMunicipality.primaryLanguage;
					for (name in subMunicipality.name) {
						if (firstLanguage.toLowerCase() == subMunicipality.name[name][0].language.toLowerCase()) {
							return subMunicipality.name[name][0].text;
						}
					}
				};

				$scope.$watch("romaZoom.activeItem", function(selectedSubMunicipality, lastSelectedSubMunicipality){
					if (selectedSubMunicipality.submunicipality != undefined) {
						refreshSubMunicipalityDetails(selectedSubMunicipality.submunicipality);
					}
				});
				
				$scope.save = function() {
					$("#saveBut").trigger('blurGrid');
					addSynonymsCollectionsToScope($scope.subMunicipality, $scope.model.synonymsArray);

					if (!$scope.$$phase) {
						$scope.$digest();
					}

					console.log("Saving:" + JSON.stringify($scope.subMunicipality));
					municipalityService.saveSubMunicipality($scope.subMunicipality,
							onSaveSuccessSubMunicipalityCallback,
							onCRUDErrorSubMunicipalityCallback);
				}

				var onSaveSuccessSubMunicipalityCallback = function(result) {
					onGetSubMunicipalityCallback(result);
					$scope.subMunicipalityId = result.reference;
					$scope.infoMessage ="Sub municipality successfully saved.";
				}

				var onCRUDErrorSubMunicipalityCallback = function(result) {
					console.log("seb municipality crud error: "+JSON.stringify(result));
					$scope.errorMessage = createErrorMessage(result.data);
				}
				
				$scope.cancel = function() {
					resetModel();
					onGetSubMunicipalityCallback(angular.copy($scope.backup));
				}
				
				$scope.triggerLoadStreets = function() {
					console.log("Triggering the loading of streets");
					$http.get('scripts/geographical/features/fusedMunicipalitiesStreetsMock.json').success(
							function(data) {
								onGetSubMunicipalitiesStreetsCallback(data);
							});
				}

				var onGetSubMunicipalitiesStreetsCallback = function(result){
					console.log("Streets: "+JSON.stringify(result));
					$scope.model.streets = transformToFusedMunicipalitiesStreetsArray(result.streets);
					if (!$scope.$$phase) {
						$scope.$digest();
					}
				};
				
				
				var transformToFusedMunicipalitiesStreetsArray = function(streets) {
					var itemsDataArray = [];
					for (var i = 0; i < streets.length; i++) {
						var item = new Object();
						item.fusedMunicipality = determineMunicipalityName(streets[i].municipality);
						item.fusedMunicipalityReference = streets[i].municipality.municipalityRef;
						if (streets[i].submunicipality != undefined) {
							item.subMunicipality = determineMunicipalityName(streets[i].submunicipality);
							item.subMunicipalityReference = streets[i].submunicipality.fusedMunicipalityRef;
						}
						if (streets[i].name != undefined) {
							if (streets[i].name.fr != undefined) {
								item.frenchName = streets[i].name.fr.text;
								item.frenchNameReference = streets[i].name.fr.reference;
							}
							if (streets[i].name.nl != undefined) {
								item.dutchName = streets[i].name.nl.text;
								item.dutchNameReference = streets[i].name.nl.reference;
							}
						}
						
						itemsDataArray.push(item);
					}

					return itemsDataArray;
				};

			}
			
			return {
				isFeatureOf : function(module) {
					module.controller('SubMunicipalityDetailController', [ '$scope',
							'$http', 'municipalityService', SubMunicipalityDetailController ]);
				}
			};
		});