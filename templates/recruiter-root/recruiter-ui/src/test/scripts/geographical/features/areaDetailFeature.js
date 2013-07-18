define(['jquery', 'common/utils'], function() {
	var AreaDetailController = function($scope, $http, areaService) {
		"use strict";
		$scope.isVisible = false;
		$scope.validateChildren = false;
		$scope.areaName = null;
		$scope.areaId = null;
		$scope.area = {};
		$scope.selectedAreaType = null;
		$scope.errorMessage = "";
		$scope.infoMessage ="";
		
		var onSaveSuccessAreaCallback = function(result) {
			$scope.areaId = result.reference;
			refreshAreaDetails(result);
			$scope.infoMessage = "Area successfully saved.";
		}
		
		var onDeleteSuccessAreaCallback = function(result) {
			$scope.infoMessage = "Area successfully deleted.";
			$scope.isVisible = true;
			$scope.area = areaService.createArea();
			resetModel();
			$scope.selectedAreaType = $scope.romaZoom.contextData["Area types"];
			
			if (!$scope.$$phase) {
				$scope.$digest();
			}
		}

		var onCRUDErrorAreaCallback = function(result) {
			console.log("area crud error: "+JSON.stringify(result));
			$scope.errorMessage = createErrorMessage(result.data);
		};
		
		var checkMandatoryFields =  function() {
			if($scope.selectedAreaType === undefined || $scope.selectedAreaType == null){
				$scope.errorMessage = "Please select area type for area.";
				return false;
			}
			
			var area = $scope.area;
			if (area !== undefined && area != null) {
				//console.log("test check:"+areaType.namesAray)
				if (area.namesArray === undefined || area.namesArray === null || area.namesArray.length == 0) {
					$scope.errorMessage = "Please add a name to the area.";
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
			if($scope.area.reference == undefined) {
				// create
				createNamesToScope($scope.area, $scope.area.namesArray);
				createSynonymsToScope($scope.area, $scope.area.synonymsArray);
			} else {
				// update
				addNamesCollectionsToScope($scope.area, $scope.area.namesArray);
				addSynonymsCollectionsToScope($scope.area, $scope.area.synonymsArray);
			}

			$scope.validateChildren = true;
			
			if (!$scope.$$phase) {
				$scope.$digest();
			}
			$scope.area.areaTypeRef = $scope.selectedAreaType;
			console.log("Saving:"+ JSON.stringify($scope.area));
			areaService.saveArea($scope.area, onSaveSuccessAreaCallback, onCRUDErrorAreaCallback);
			
		};
		
		$scope.remove = function(){
			$("#deleteBut").trigger('blurGrid');
			if(confirm("Are you shure you want to delete this area?")){
				//TO DO confirm if it has links
				areaService.deleteArea($scope.area, onDeleteSuccessAreaCallback, onCRUDErrorAreaCallback);
			}
			
		}
		
		
		var onGetAreaCallback = function(result){
			console.log("Area: "+JSON.stringify(result))
			$scope.area = result;
			$scope.areaName = determineName($scope.area.names); 
			$scope.areaId = result.reference;
			$scope.isVisible = true;
			$scope.errorMessage = "";
			$scope.infoMessage = "";
			$scope.area.namesArray = transformToArray($scope.area.names);
			$scope.area.synonymsArray = transformToArray($scope.area.synonyms);
			$scope.selectedAreaType = result.areaTypeRef;
			if (!$scope.$$phase) {
				$scope.$digest();
			}
		};		
		
		var refreshAreaDetails = function(item) {
			areaService.getArea(item.reference, onGetAreaCallback);
		};
		
		$scope.$watch("romaZoom.activeItem", function(selectedArea, lastSelectedArea){
			if (selectedArea.area != undefined) {
				refreshAreaDetails(selectedArea.area);
			} else {
				$scope.isVisible = true;
				$scope.area = areaService.createArea();
				resetModel();
				$scope.selectedAreaType = $scope.romaZoom.contextData["Area types"];
				
				if (!$scope.$$phase) {
					$scope.$digest();
				}
			}
			areaService.getAreaTypes(function(data){
				$scope.areaTypes = data.items;
				if (!$scope.$$phase) {
					$scope.$digest();
				}
			});
		});
		
		var resetModel = function() {
			$scope.validateChildren = false;
			$scope.areaName = null;
			$scope.areaId = null;
			$scope.errorMessage = "";
			$scope.infoMessage = "";
			$scope.selectedAreaType = null;
			$scope.area.namesArray = [];
			$scope.area.synonymsArray = [];
		};
		

	};

	return {
		isFeatureOf : function(module) {
			module.controller('AreaDetailController', [ '$scope', '$http',
					'areaService', AreaDetailController ]);
		}
	};
});