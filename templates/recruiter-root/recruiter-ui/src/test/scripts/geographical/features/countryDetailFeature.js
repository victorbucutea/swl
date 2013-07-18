define(
		[ 'jquery', 'common/utils' ],
		function() {
			var CountryDetailController = function($scope, $http, $resource,
					countryService, postalLocationService) {
				"use strict";
				$scope.isVisible = false;
				$scope.validateChildren = false;
				$scope.countryName = null;
				$scope.countryId = null;
				$scope.errorMessage = "";
				$scope.infoMessage ="";
				
				$scope.country = {
					borderCountry : false,
					convention : "NONE"
				};
				 var model={
						selectedSortingCenter:{}
						
				}
				$scope.model = model; 

				var resetModel = function() {
					$scope.validateChildren = false;
					$scope.countryName = null;
					$scope.countryId = null;
					$scope.errorMessage = "";
					$scope.infoMessage ="";
					// set default values
					$scope.country.borderCountry = false;
					$scope.country.convention = "NONE";
					$scope.country.prefix = "";
					$scope.country.namesArray = [];
					$scope.country.synonymsArray = [];
				};

				var onSaveSuccessCountryCallback = function(result) {
					onGetCountryCallback(result);
					$scope.countryId = result.reference;
					$scope.infoMessage ="Country successfully saved.";
				}

				var onCRUDErrorCountryCallback = function(result) {
					console.log("country crud error: "+JSON.stringify(result));
					$scope.errorMessage = createErrorMessage(result.data);
				}
				
				var onDeleteSuccessCountryCallback = function(result) {
					$scope.infoMessage = "Country successfully deleted.";
					$scope.isVisible = true;
					$scope.country = countryService.createCountry();
					resetModel();
				}

				var checkMandatoryFields =  function() {
					$scope.errorMessage = "";
					$scope.infoMessage = "";
					var country = $scope.country;
					if (country != undefined || country != null) {
						/*if (country.names == undefined || country.names == null) {
							$scope.errorMessage = "Please add a name to the country.";
							return false;
						}*/
						if (country.iso == undefined
								|| country.iso.numeric3 == undefined) {
							$scope.errorMessage = "Please specify the ISO number for the country.";
							return false;
						}
						if (country.iso.alpha2 == undefined) {
							$scope.errorMessage = "Please specify the prefix ISO2 for the country.";
							return false;
						}
						if (country.borderCountry == undefined
								|| country.borderCountry == null) {
							country.borderCountry = false;
						}
						if (country.convention == undefined
								|| country.convention == null) {
							country.convention = "NONE";
						}
						/*if (country.sortingCenter == undefined
								|| country.sortingCenter.reference == undefined) {
							$scope.errorMessage = "Please select a sorting center for the country.";
							return false;
						}*/
					}
					return true;	
				};

				$scope.save = function() {
					$("#saveBut").trigger('blurGrid');
					if(!checkMandatoryFields()){
							return;
					}
					if ($scope.country.reference == undefined) {
						// create
						createNamesToScope($scope.country, $scope.country.namesArray);
						createSynonymsToScope($scope.country, $scope.country.synonymsArray);
					} else {
						// update
						addNamesCollectionsToScope($scope.country, $scope.country.namesArray);
						addSynonymsCollectionsToScope($scope.country, $scope.country.synonymsArray);
					}

					$scope.validateChildren = true;
					if (!$scope.$$phase) {
						$scope.$digest();
					}

					console.log("Saving:" + JSON.stringify($scope.country));
					countryService.saveCountry($scope.country,
							onSaveSuccessCountryCallback,
							onCRUDErrorCountryCallback);

				};
				
				var onGetCountryCallback = function(result) {
					$scope.country = result;
					console.log('$scope.country = ' + $scope.country);
					$scope.countryName = determineName($scope.country.name);
					$scope.countryId = result.reference;
					$scope.isVisible = true;
					$scope.country.namesArray = transformToArray($scope.country.name);
					$scope.country.synonymsArray = transformToArray($scope.country.synonyms);
					$scope.errorMessage = "";
					$scope.infoMessage = "";
					//console.log("Getting:" + JSON.stringify($scope.country));
				};

				var refreshCountryDetails = function(reference) {
					countryService.getCountry(reference, onGetCountryCallback);
				};
				
				$scope.remove = function(){
					$("#deleteBut").trigger('blurGrid');
					if(confirm("Are you shure you want to delete this country?")){
						countryService.deleteCountry($scope.country, onDeleteSuccessCountryCallback, onCRUDErrorCountryCallback);
					}
					
				}
				$scope
						.$watch(
								"romaZoom.activeItem",
								function(selectedCountry, lastSelectedCountry) {
									if (selectedCountry.country != undefined) {
										refreshCountryDetails(selectedCountry.country.reference);
									} else {
										$scope.isVisible = true;
										$scope.country = countryService.createCountry();
										resetModel();
									}
									postalLocationService.getSortingCenters(function(data){
										$scope.sortingCenters = data.items;
										if (!$scope.$$phase) {
											$scope.$digest();
										}
									});
								});


			};

			return {
				isFeatureOf : function(module) {
					module.controller('CountryDetailController', [ '$scope',
							'$http', '$resource', 'countryService', 'postalLocationService',
							CountryDetailController ]);
				}
			};
		});