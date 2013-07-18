 define(['jquery', 'common/utils'], function() {
		var SuffixTypeDetailController = function($scope, $http, suffixTypeService, physicalPointTypeService) {
			"use strict";
			
			$scope.backup = {};

			$scope.isVisible = false;
			$scope.validateChildren = false;
			$scope.suffixTypeName = null;
			$scope.suffixTypeId = null;
			$scope.errorMessage = "";
			$scope.infoMessage ="";
			
			$scope.ppTypes = [];
			$scope.suffixTypes = [];
			$scope.ppTypesExclLabel = "Physical point types";
			$scope.showExclusions = false;
			$scope.suffixTypesExclLabel = "Suffix types";
			
			$scope.suffixType = {
					deletable : true
			};
			
			var resetModel = function() {
				$scope.errorMessage = "";
				$scope.infoMessage ="";
				$scope.isVisible = false;
				$scope.editMode = false;
				$scope.validateChildren = false;
				$scope.showExclusions = false;
				
				$scope.suffixTypeName = null;
				$scope.suffixTypeId = null;
				$scope.suffixType.synonymsArray = [];
				$scope.suffixType.namesArray = [];
				$scope.suffixType.excluded_suffix_types = [];
				$scope.suffixType.excluded_point_types = [];
			};

			$scope.$watch("romaZoom.activeItem", function(selectedSufixType, lastSelectedSufixType){
				if (selectedSufixType.type != undefined) {
					refreshSufixTypeDetails(selectedSufixType.type);
				} else {
					newSuffixTypeMode();
				}
			});

			var newSuffixTypeMode = function() {
				resetModel();
				$scope.c = suffixTypeService.createSuffixType();
				$scope.suffixType.deletable = true;
				$scope.backup = {};
				$scope.editMode = true;
				$scope.isVisible = true;
				$scope.showExclusions = false;
				$scope.suffixType.excluded_suffix_types = [];
				$scope.suffixType.excluded_point_types = [];
			};
			
			var refreshSufixTypeDetails = function(item) {
				suffixTypeService.getSuffixType(item.reference, onGetSufixTypeCallback);
			};

			var onGetSufixTypeCallback = function(result){
				console.log("Sufix type: "+JSON.stringify(result));
				$scope.backup = angular.copy(result);
				resetModel();
				
				$scope.suffixType = result;
				$scope.suffixTypeName = determineName($scope.suffixType.names);
				$scope.suffixType.namesArray = transformToArray($scope.suffixType.names);
				$scope.suffixType.synonymsArray = transformToArray($scope.suffixType.synonyms);
				if($scope.suffixType.excluded_suffix_types == undefined){
					$scope.suffixType.excluded_suffix_types = [];
				}
				if($scope.suffixType.excluded_point_types == undefined){
					$scope.suffixType.excluded_point_types = [];
				}
				$scope.suffixTypeId = result.reference;
				$scope.isVisible = true;
				$scope.romaZoom.isModified = false;
				$scope.editMode = false;
				if (!$scope.$$phase) {
					$scope.$digest();
				}
			};
			
			$scope.cancel = function() {
				resetModel();
				if (angular.equals({}, $scope.backup) == false) {
					onGetSufixTypeCallback(angular.copy($scope.backup));
				}
			}
			
			var checkMandatoryFields =  function() {
				$scope.errorMessage = "";
				$scope.infoMessage = "";
				var suffixType = $scope.suffixType;
				if (suffixType != undefined || suffixType != null) {
					if (suffixType.description == undefined) {
						$scope.errorMessage = "Please specify the description of the suffix type.";
						return false;
					}
				}
				return true;	
			};
			
			$scope.save = function() {
				$("#saveBut").trigger('blurGrid');
				if(!checkMandatoryFields()){
						return;
				}
				if ($scope.suffixType.reference == undefined) {
					// create
					createNamesToScope($scope.suffixType, $scope.suffixType.namesArray);
					createSynonymsToScope($scope.suffixType, $scope.suffixType.synonymsArray);
				} else {
					// update
					addNamesCollectionsToScope($scope.suffixType, $scope.suffixType.namesArray);
					addSynonymsCollectionsToScope($scope.suffixType, $scope.suffixType.synonymsArray);
				}

				$scope.validateChildren = true;
				if (!$scope.$$phase) {
					$scope.$digest();
				}

				console.log("Saving:" + JSON.stringify($scope.suffixType));
				suffixTypeService.saveSuffixType($scope.suffixType,
						onSaveSuccessSuffixTypeCallback,
						onCRUDErrorSuffixTypeCallback);
			}
			
			var onSaveSuccessSuffixTypeCallback = function(result) {
				onGetSufixTypeCallback(result);
				$scope.suffixTypeId = result.reference;
				$scope.infoMessage ="Suffix type successfully saved.";
			}

			var onCRUDErrorSuffixTypeCallback = function(result) {
				console.log("suffix type crud error: "+JSON.stringify(result));
				$scope.errorMessage = createErrorMessage(result.data);
			}
			
			$scope.remove = function(){
				$("#deleteBut").trigger('blurGrid');
				if(confirm("Are you shure you want to delete this suffix type?")){
					suffixTypeService.deleteSuffixType($scope.suffixType, onDeleteSuccessSuffixTypeCallback, onCRUDErrorSuffixTypeCallback);
				}
				
			}

			var onDeleteSuccessSuffixTypeCallback = function(result) {
				newSuffixTypeMode();
				$scope.infoMessage = "Suffix type successfully deleted.";
			}
			
			$scope.triggerLoadExclusions = function(){
				console.log("Triggering the loading of exclusions.");
				$scope.showExclusions = true;
				physicalPointTypeService.getPhysicalPointTypes(function(data){
					$scope.ppTypes = data.items;
					if (!$scope.$$phase) {
						$scope.$digest();
					}
				});
				suffixTypeService.getSuffixTypes(function(data){
					$scope.suffixTypes = data.items;
					if (!$scope.$$phase) {
						$scope.$digest();
					}
				});
				
			}

		};

		return {
			isFeatureOf : function(module) {
				module.controller('SuffixTypeDetailController', [ '$scope', '$http',
						'suffixTypeService', 'physicalPointTypeService', SuffixTypeDetailController ]);
			}
		};
	});