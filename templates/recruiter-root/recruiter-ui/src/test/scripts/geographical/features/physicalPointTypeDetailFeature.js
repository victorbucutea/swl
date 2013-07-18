 define(['jquery', 'common/utils'], function() {
		var PhysicalPointTypeDetailController = function($scope, $http, physicalPointTypeService, suffixTypeService) {
			"use strict";
			
			$scope.backup = {};
			
			$scope.isVisible = false;
			$scope.validateChildren = false;
			$scope.ppType = {
					description: undefined,
					nameNl: undefined,
					nameFr:undefined,
					deletable: true,
					default_suffix_ref: undefined,
					names: [],
					updated_names: [],
					max_no_of_suffixes: undefined,
					excluded_suffixe_objs : {},
					excluded_point_type_objs: {},
					excluded_point_types: [],
					excluded_suffix_types: []
			};
			$scope.original_default_suffix_ref = undefined;
			$scope.suffixTypes = [];
			$scope.suffixTypeLabel = "Default suffix type";
			$scope.errorMessage = "";
			$scope.infoMessage ="";
			$scope.editMode = false;
			
			$scope.ppTypes = [];
			$scope.ppTypesExclLabel = "Excluded physical point types";
			$scope.showExclusions = true;
			$scope.suffixTypesExclLabel = "Excluded suffix types";
			
			$scope.$watch("ppType.default_suffix_ref", function(selectedSuffType, lastSelectedSuffType){
				if (lastSelectedSuffType != undefined) {
					$scope.original_default_suffix_ref = lastSelectedSuffType;
				} else {
					$scope.original_default_suffix_ref = undefined;
				}
			});
			
			var onSaveSuccessPPTypeCallback = function(result) {
				refreshPPTypeDetails(result);
				$scope.infoMessage = "Physical point type successfully saved.";
				$scope.errorMessage = "";
			}
			
			var onDeleteSuccessPPTypeCallback = function(result) {
				$scope.infoMessage = "Physical point type successfully deleted.";
				$scope.errorMessage = "";
				$scope.isVisible = true;
				resetModel();
				$scope.ppType = physicalPointTypeService.createPhysicalPointType();
				$scope.backup = angular.copy($scope.ppType);
				setDefaultValues();
			}

			var onCRUDErrorPPTypeCallback = function(result) {
				console.log("PPT crud error: "+JSON.stringify(result));
				$scope.errorMessage = createErrorMessage(result.data);
				$scope.infoMessage = "";
			};
			
			var checkMandatoryFields =  function() {
				var ppType = $scope.ppType;
				if (ppType !== undefined && ppType != null) {
					if(ppType.description === undefined || ppType.description === null || ppType.description == 0){
						$scope.errorMessage = "Please add a name to the physical point type.";
						return false;
					}
					if(ppType.nameFr === undefined || ppType.nameFr === null || ppType.nameFr.text.length == 0){
						$scope.errorMessage = "Please add a french name to the physical point type.";
						return false;
					}
					
					if(ppType.nameNl === undefined || ppType.nameNl === null || ppType.nameNl.text.length == 0){
						$scope.errorMessage = "Please add a dutch name to the physical point type.";
						return false;
					}
					
					if(ppType.default_suffix_ref === undefined || ppType.default_suffix_ref === null){
						$scope.errorMessage = "Please add a default suffix type to the physical point type.";
						return false;
					}
				}
				return true;	
			};

			$scope.save = function() {
				if(!checkMandatoryFields()){
					return;
				}
				
				if ($scope.ppType.reference == undefined) {
					// create
					$scope.ppType.names = [];
					$scope.ppType.names.push({text: $scope.ppType.nameFr.text, language: "FRENCH"});
					$scope.ppType.names.push({text: $scope.ppType.nameNl.text, language: "DUTCH"});
				} else {
					// update
					$scope.ppType.updated_names = [];
					if ($scope.ppType.nameFr.text != $scope.ppType.names.fr[0].text) {
						$scope.ppType.updated_names.push($scope.ppType.nameFr);
					}
					if ($scope.ppType.nameNl.text != $scope.ppType.names.nl[0].text) {
						$scope.ppType.updated_names.push($scope.ppType.nameNl);
					}
					if($scope.original_default_suffix_ref !== undefined){
						if(confirm("Do you want to automatically add the new default suffix type to all existing suffixes?")){
							$scope.ppType.add_new_type_to_suffixes = true;
						}else{
							$scope.ppType.add_new_type_to_suffixes = false;
						}
						if(confirm("Do you want to automatically remove the original default suffix type from all existing suffixes?")){
							$scope.ppType.remove_old_type_from_suffixes = true;
						}else{
							$scope.ppType.remove_old_type_from_suffixes = false;
						}
					}
				}

				$scope.validateChildren = true;
				
				if (!$scope.$$phase) {
					$scope.$digest();
				}

				console.log("Saving:"+ JSON.stringify($scope.ppType));
				physicalPointTypeService.savePhysicalPointType($scope.ppType, onSaveSuccessPPTypeCallback, onCRUDErrorPPTypeCallback);
				
			};
			
			$scope.remove = function(){
				if(!$scope.ppType.deletable){
					$scope.errorMessage = "This physical point type is not deletable.";
					return;
				}
				if(confirm("Are you shure you want to delete this physical point type?")){
					physicalPointTypeService.deletePhysicalPointType($scope.ppType, onDeleteSuccessPPTypeCallback, onCRUDErrorPPTypeCallback);
				}
				
			}
			
			$scope.cancel = function() {
				resetModel();
				onGetPPTypeCallback(angular.copy($scope.backup));
			}			
			
			var onGetPPTypeCallback = function(result){
				console.log("PPType: "+JSON.stringify(result))
				$scope.backup = angular.copy(result);
				$scope.ppType = result;
				if($scope.ppType.names["fr"]){
					$scope.ppType.nameFr = angular.copy($scope.ppType.names["fr"][0]);
				}else{
					$scope.ppType.nameFr = undefined;
				}
				if($scope.ppType.names["nl"]){
					$scope.ppType.nameNl = angular.copy($scope.ppType.names["nl"][0]);
				}else{
					$scope.ppType.nameNl = undefined;
				}
				if($scope.ppType.excluded_suffix_types == undefined){
					$scope.ppType.excluded_suffix_types = [];
				}
				if($scope.ppType.excluded_point_types == undefined){
					$scope.ppType.excluded_point_types = [];
				}
				$scope.editMode = false;
				$scope.isVisible = true;
			};		
			
			var refreshPPTypeDetails = function(item) {
				$scope.errorMessage = "";
				$scope.infoMessage = "";
				$scope.showExclusions = false;
				$scope.original_default_suffix_ref = undefined;
				physicalPointTypeService.getPhysicalPointType(item.reference, onGetPPTypeCallback);
			};
			
			$scope.$watch("romaZoom.activeItem", function(selectedPPType, lastSelectedPPType){
				if (selectedPPType.type != undefined) {
					refreshPPTypeDetails(selectedPPType.type);
				} else {
					$scope.isVisible = true;
					resetModel();
					$scope.ppType = physicalPointTypeService.createPhysicalPointType();
					$scope.backup = angular.copy($scope.ppType);
					setDefaultValues();
				}
				
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
				
			});
			
			var resetModel = function() {
				$scope.validateChildren = false;
				$scope.errorMessage = "";
				$scope.infoMessage = "";
				$scope.editMode = false;
				$scope.ppType = {};
//				$scope.showExclusions = false;
				$scope.original_default_suffix_ref = undefined;
				setDefaultValues();
			};
			
			var setDefaultValues = function(){
				$scope.ppType.names = {};
				$scope.ppType.excluded_suffix_types = [];
				$scope.ppType.excluded_point_types = [];
				$scope.ppType.deletable = true;
				$scope.editMode = true;
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
				module.controller('PhysicalPointTypeDetailController', [ '$scope', '$http',
						'physicalPointTypeService', 'suffixTypeService', PhysicalPointTypeDetailController ]);
			}
		};
	});