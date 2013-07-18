define(
		[ 'jquery', 'common/utils' ],
		function() {
			var StreetDetailController = function($scope, $http, streetService, municipalityService) {
				"use strict";
				
				$scope.backup = {};

				$scope.isVisible = false;
				$scope.dutchNameRequired = false;
				$scope.frenchNameRequired = false;
//				$scope.validateChildren = false;
				$scope.streetName = null;
				$scope.streetId = null;
				$scope.fusedMunicipalityName = null;
				$scope.submunicipalityrequired = false;
				$scope.subMunicipalityLabel = "Sub Municipality";
				$scope.subMunicipalityReference = null;
				$scope.errorMessage = "";
				$scope.infoMessage ="";
				$scope.street = {};
				
				var model={
						frenchName : {},
						dutchName : {},
						frenchKeepAsSyn : true,
						dutchKeepAsSyn : true
				}
				
				$scope.model = model; 

				var resetModel = function() {
//					$scope.validateChildren = false;
					$scope.streetName = null;
					$scope.streetId = null;
					$scope.subMunicipalityReference = null;
					$scope.errorMessage = "";
					$scope.infoMessage ="";
					// set default values
					$scope.street.synonymsArray = [];
					$scope.model.frenchName = {language: "FRENCH"};
					$scope.model.dutchName = {language: "DUTCH"};
					$scope.model.frenchKeepAsSyn = true;
					$scope.model.dutchKeepAsSyn = true;
					$scope.dutchNameRequired = false;
					$scope.frenchNameRequired = false;
					$scope.submunicipalityrequired = false;
				};

				var refreshStreetDetails = function(item) {
					streetService.getStreet(item.reference, onGetStreetCallback);
				};

				var onGetStreetCallback = function(result){
					console.log("Street: "+JSON.stringify(result));
					$scope.backup = angular.copy(result);
					resetModel();

					$scope.street = result;
					$scope.streetName = determineName($scope.street.names);
					retrieveParentFusedMunicipality($scope.street.fusedmunicipality.reference);
					$scope.street.synonymsArray = transformToArray($scope.street.synonyms);
					setRequiredField($scope.street.fusedmunicipality.primaryLanguage);
					setRequiredField($scope.street.fusedmunicipality.secondaryLanguage);
					if ($scope.street.submunicipality != undefined) {
						$scope.subMunicipalityReference = $scope.street.submunicipality.reference;
						$scope.submunicipalityrequired = true;
					}
					
					$scope.streetId = result.reference;
					$scope.isVisible = true;
					$scope.romaZoom.isModified = false;
					$scope.editMode = false;
					if (!$scope.$$phase) {
						$scope.$digest();
					}
				};
				
				var setRequiredField = function(fusedMunicipalityLanguage) {
					if (fusedMunicipalityLanguage != undefined && fusedMunicipalityLanguage != null) {
						if ("french" == fusedMunicipalityLanguage.toLowerCase()) {
							$scope.frenchNameRequired = true;
							if ($scope.street.names != undefined) {
								$scope.model.frenchName =  angular.copy($scope.street.names.fr[0]);
							}
						} else if ("dutch" == fusedMunicipalityLanguage.toLowerCase()){
							$scope.dutchNameRequired = true;
							if ($scope.street.names != undefined) {
								$scope.model.dutchName =  angular.copy($scope.street.names.nl[0]);
							}
						}
					}
				};
				
				$scope.$watch("romaZoom.activeItem", function(selectedStreet, lastSelectedStreet) {
					if (selectedStreet.street != undefined) {
						refreshStreetDetails(selectedStreet.street);
					} else {
						newStreetMode();
					}
				});

				var retrieveParentFusedMunicipality = function(fusedMunicipalityReference) {
					municipalityService.getFusedMunicipality(fusedMunicipalityReference, function(data){
						console.log("getFusedMunicipality: "+JSON.stringify(data));
						$scope.fusedMunicipalityName = data.postalCode + " " + determineMunicipalityName(data);
						setRequiredField(data.primaryLanguage);
						setRequiredField(data.secondaryLanguage);
						retrieveSubMunicipalities(fusedMunicipalityReference);
						
						if (!$scope.$$phase) {
							$scope.$digest();
						}
					});
				};
				
				var retrieveSubMunicipalities = function(fusedMunicipalityReference) {
					municipalityService.getSubMunicipalities(fusedMunicipalityReference, function(data){
						console.log("getSubMunicipalities: "+JSON.stringify(data));
						$scope.submunicipalities = data.items;
						if (!$scope.$$phase) {
							$scope.$digest();
						}
					});
				};
				
				var determineMunicipalityName = function(fusedMunicipality) {
					var firstLanguage = fusedMunicipality.primaryLanguage;
					for (name in fusedMunicipality.name) {
						if (firstLanguage.toLowerCase() == fusedMunicipality.name[name][0].language.toLowerCase()) {
							return fusedMunicipality.name[name][0].text;
						}
					}
				};

			var newStreetMode = function() {
				resetModel();
				$scope.street = streetService.createStreet();
				$scope.backup = {};
				$scope.editMode = true;
				$scope.isVisible = true;
				var fusedMunicipalityReference = $scope.romaZoom.contextData["Municipalities"];
				retrieveParentFusedMunicipality(fusedMunicipalityReference);
				$scope.street.fusedmunicipality_ref = fusedMunicipalityReference;
			};
			
			$scope.cancel = function() {
				resetModel();
				if (angular.equals({}, $scope.backup) == false) {
					onGetStreetCallback(angular.copy($scope.backup));
				}
			};

			var checkMandatoryFields =  function() {
				$scope.errorMessage = "";
				$scope.infoMessage = "";
//				if ($scope.street == undefined || $scope.street.fusedmunicipality.reference == undefined 
//						|| $scope.street.fusedmunicipality.reference == null) {
//					$scope.errorMessage = "Please select the Fused municipality.";
//					return false;
//				}
				if ($scope.dutchNameRequired && $scope.model.dutchName.text == null) {
					$scope.errorMessage = "Please specify the Dutch name.";
					return false;
				}
				if ($scope.frenchNameRequired && $scope.model.frenchName.text == null) {
					$scope.errorMessage = "Please specify the French name.";
					return false;
				}
				return true;
			};
			
			$scope.save = function() {
				$("#saveBut").trigger('blurGrid');
				if(!checkMandatoryFields()){
						return;
				}
				if ($scope.street.reference == undefined) {
					// create
					$scope.street.names = [];
					$scope.street.names.push($scope.model.dutchName);
					$scope.street.names.push($scope.model.frenchName);
					createSynonymsToScope($scope.street, $scope.street.synonymsArray);
				} else {
					// update
					addSynonymsCollectionsToScope($scope.street, $scope.street.synonymsArray);
					$scope.street.updated_names = [];
					$scope.street.names_to_synonyms = [];
					if ($scope.dutchNameRequired && $scope.model.dutchName.text != $scope.street.names.nl[0].text) {
						$scope.street.updated_names.push($scope.model.dutchName);
						if ($scope.model.dutchKeepAsSyn) {
							addSynonymToStreet($scope.street.names.nl[0]);
						}
					}
					if ($scope.frenchNameRequired && $scope.model.frenchName.text != $scope.street.names.fr[0].text) {
						$scope.street.updated_names.push($scope.model.frenchName);
						if ($scope.model.frenchKeepAsSyn) {
							addSynonymToStreet($scope.street.names.fr[0]);
						}
					}
				}

				$scope.validateChildren = true;
				if (!$scope.$$phase) {
					$scope.$digest();
				}

				console.log("Saving:" + JSON.stringify($scope.street));
				streetService.saveStreet($scope.street,
						onSaveSuccessStreetCallback,
						onCRUDErrorStreetCallback);
			};
			
			var addSynonymToStreet = function(translation) {
				if ($scope.street.names_to_synonyms ==  undefined) {
					$scope.street.names_to_synonyms = [];
				}
				translation.reference = "";
				$scope.street.names_to_synonyms.push(translation);
				
			};
			
			var onSaveSuccessStreetCallback = function(result) {
				onGetStreetCallback(result);
				$scope.streetId = result.reference;
				$scope.infoMessage ="Street successfully saved.";
			};

			var onCRUDErrorStreetCallback = function(result) {
				console.log("street crud error: "+JSON.stringify(result));
				$scope.errorMessage = createErrorMessage(result.data);
			};
			
			$scope.remove = function(){
				$("#deleteBut").trigger('blurGrid');
				if(confirm("Are you sure you want to delete this street?")){
					streetService.deleteStreet($scope.street, onDeleteSuccessStreetCallback, onCRUDErrorStreetCallback);
				}
			};

			var onDeleteSuccessStreetCallback = function(result) {
				newStreetMode();
				$scope.infoMessage = "Street successfully deleted.";
			};
		};

		return {
			isFeatureOf : function(module) {
				module.controller('StreetDetailController', [ '$scope',
						'$http', 'streetService', 'municipalityService', StreetDetailController ]);
			}
		};
});