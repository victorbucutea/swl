define(
		[ 'jquery', 'common/utils' ],
		function() {
			var FusedMunicipalityDetailController = function($scope, $http, municipalityService) {
				"use strict";
				
				$scope.backup = {};
				
				$scope.isVisible = false;
				$scope.fusedMunicipalityName = null;
				$scope.fusedMunicipalityId = null;
				$scope.errorMessage = "";
				$scope.infoMessage ="";
				
				$scope.model = {
						primaryLanguage : null,
						secondaryLanguage : null,
						languageFacility: null, 
						synonymsArray :{},
						submunicipalities: {},
						streets: {}
				};
				
				var resetModel = function() {
					$scope.model.primaryLanguage = null;
					$scope.model.secondaryLanguage = null;
					$scope.model.languageFacility = null;
					$scope.model.synonymsArray = {};
					$scope.model.submunicipalities = {};
					$scope.model.streets = {};
					$scope.fusedMunicipalityName = null;
					$scope.fusedMunicipality = null;
					$scope.fusedMunicipalityId = null;
					$scope.errorMessage = "";
					$scope.infoMessage ="";
					$scope.isVisible = false;
				};

				var onGetFusedMunicipalityCallback = function(result){
					console.log("Fused Municipality: "+JSON.stringify(result));
					$scope.backup = angular.copy(result);
					resetModel();
					$scope.fusedMunicipalityName = result.postalCode + " " + determineMunicipalityName(result);
					$scope.fusedMunicipality = result;
					$scope.model.primaryLanguage = formatLanguage(result.primaryLanguage);
					$scope.model.secondaryLanguage = formatLanguage(result.secondaryLanguage);
					$scope.model.languageFacility = formatLanguage(result.languageFacility);
					$scope.model.synonymsArray = transformToArray($scope.fusedMunicipality.synonyms);
					$scope.fusedMunicipalityId = result.reference;
					$scope.isVisible = true;
					$scope.romaZoom.isModified = false;
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

				var refreshFusedMunicipalityDetails = function(item) {
					municipalityService.getFusedMunicipality(item.reference, onGetFusedMunicipalityCallback);
				};

				var formatLanguage = function(item) {
					if (item == undefined || "UNKNOWN" == item.toUpperCase()) {
						return "-";
					} else {
						return item.charAt(0).toUpperCase() + item.toLowerCase().slice(1);
					}
				};

				var determineMunicipalityName = function(fusedMunicipality) {
					var firstLanguage = fusedMunicipality.primaryLanguage;
					for (name in fusedMunicipality.name) {
						if (firstLanguage.toLowerCase() == fusedMunicipality.name[name][0].language.toLowerCase()) {
							return fusedMunicipality.name[name][0].text;
						}
					}
				};

				$scope.$watch("romaZoom.activeItem", function(selectedFusedMunicipality, lastSelectedFusedMunicipality){
					if (selectedFusedMunicipality.municipality != undefined) {
						refreshFusedMunicipalityDetails(selectedFusedMunicipality.municipality);
					}
				});
				
				$scope.save = function() {
					$("#saveBut").trigger('blurGrid');
					addSynonymsCollectionsToScope($scope.fusedMunicipality, $scope.model.synonymsArray);

					if (!$scope.$$phase) {
						$scope.$digest();
					}

					console.log("Saving:" + JSON.stringify($scope.fusedMunicipality));
					municipalityService.saveFusedMunicipality($scope.fusedMunicipality,
							onSaveSuccessFusedMunicipalityCallback,
							onCRUDErrorFusedMunicipalityCallback);
				}

				$scope.cancel = function() {
					resetModel();
					onGetFusedMunicipalityCallback(angular.copy($scope.backup));
				}
				
				var onSaveSuccessFusedMunicipalityCallback = function(result) {
					onGetFusedMunicipalityCallback(result);
					$scope.fusedMunicipalityId = result.reference;
					$scope.infoMessage ="Fused municipality successfully saved.";
				}

				var onCRUDErrorFusedMunicipalityCallback = function(result) {
					console.log("fused municipality crud error: "+JSON.stringify(result));
					$scope.errorMessage = createErrorMessage(result.data);
				}
				
				$scope.triggerLoadSubMunicipalities = function() {
					console.log("Triggering the loading of sub-municipalities");
					municipalityService.getSubMunicipalities($scope.fusedMunicipalityId, onGetSubMunicipalitiesCallback);
				}

				var onGetSubMunicipalitiesCallback = function(result){
					console.log("Sub Municipalities: "+JSON.stringify(result));
					$scope.model.submunicipalities = transformToSubMunicipalityArray(result.items);
					if (!$scope.$$phase) {
						$scope.$digest();
					}
				};
				
				
				var transformToSubMunicipalityArray = function(submunicipalities) {
					var itemsDataArray = [];
					for (var i = 0; i < submunicipalities.length; i++) {
						var item = new Object();
						item.fusedMunicipality = determineMunicipalityName($scope.fusedMunicipality);
						item.fusedMunicipalityReference = $scope.fusedMunicipalityId;
						item.subMunicipality = determineMunicipalityName(submunicipalities[i].submunicipality);
						item.subMunicipalityReference = submunicipalities[i].submunicipality.reference;
						
						itemsDataArray.push(item);
					}

					return itemsDataArray;
				};
				
				$scope.triggerLoadStreets = function() {
					console.log("Triggering the loading of streets");
					$http.get('scripts/geographical/features/fusedMunicipalitiesStreetsMock.json').success(
							function(data) {
								onGetFusedMunicipalitiesStreetsCallback(data);
							});
				}

				var onGetFusedMunicipalitiesStreetsCallback = function(result){
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
					module.controller('FusedMunicipalityDetailController', [ '$scope',
							'$http', 'municipalityService', FusedMunicipalityDetailController ]);
				}
			};
		});