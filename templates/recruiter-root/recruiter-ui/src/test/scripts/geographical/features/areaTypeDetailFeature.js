define(['jquery', 'common/utils'], function() {
	var AreaTypeDetailController = function($scope, $http, areaService) {
		"use strict";
		$scope.isVisible = false;
		$scope.validateChildren = false;
		$scope.areaTypeName = null;
		$scope.areaTypeId = null;
		$scope.areaType = {};
		$scope.errorMessage = "";
		$scope.infoMessage ="";
		
		var onSaveSuccessAreaTypeCallback = function(result) {
			$scope.areaTypeId = result.reference;
			refreshAreaTypeDetails(result);
			$scope.infoMessage = "Area type successfully saved.";
		}
		
		var onDeleteSuccessAreaTypeCallback = function(result) {
			$scope.infoMessage = "Area type successfully deleted.";
			$scope.isVisible = true;
			$scope.areaType = areaService.createAreaType();
			resetModel();
		}

		var onCRUDErrorAreaTypeCallback = function(result) {
			console.log("area type crud error: "+JSON.stringify(result));
			$scope.errorMessage = createErrorMessage(result.data);
		};
		
		var checkMandatoryFields =  function() {
			var areaType = $scope.areaType;
			if (areaType !== undefined && areaType != null) {
				//console.log("test check:"+areaType.namesAray)
				if (areaType.namesArray === undefined || areaType.namesArray === null || areaType.namesArray.length == 0) {
					$scope.errorMessage = "Please add a name to the area type.";
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
			if($scope.areaType.reference == undefined) {
				// create
				createNamesToScope($scope.areaType, $scope.areaType.namesArray);
				createSynonymsToScope($scope.areaType, $scope.areaType.synonymsArray);
			} else {
				// update
				addNamesCollectionsToScope($scope.areaType, $scope.areaType.namesArray);
				addSynonymsCollectionsToScope($scope.areaType, $scope.areaType.synonymsArray);
			}

			$scope.validateChildren = true;
			
			if (!$scope.$$phase) {
				$scope.$digest();
			}

			console.log("Saving:"+ JSON.stringify($scope.areaType));
			areaService.saveAreaType($scope.areaType, onSaveSuccessAreaTypeCallback, onCRUDErrorAreaTypeCallback);
			
		};
		
		$scope.remove = function(){
			$("#deleteBut").trigger('blurGrid');
			if(confirm("Are you shure you want to delete this area type?")){
				areaService.deleteAreaType($scope.areaType, onDeleteSuccessAreaTypeCallback, onCRUDErrorAreaTypeCallback);
			}
			
		}
		
		
		var onGetAreaTypeCallback = function(result){
			$scope.areaType = result;
			$scope.areaTypeName =determineName($scope.areaType.names); 
			$scope.areaTypeId = result.reference;
			$scope.isVisible = true;
			$scope.errorMessage = "";
			$scope.areaType.namesArray = transformToArray($scope.areaType.names);
			$scope.areaType.synonymsArray = transformToArray($scope.areaType.synonyms);
		};		
		
		var refreshAreaTypeDetails = function(item) {
			areaService.getAreaType(item.reference, onGetAreaTypeCallback);
		};
		
		$scope.$watch("romaZoom.activeItem", function(selectedAreaType, lastSelectedAreaType){
			if (selectedAreaType.areaType != undefined) {
				refreshAreaTypeDetails(selectedAreaType.areaType);
			} else {
				$scope.isVisible = true;
				$scope.areaType = areaService.createAreaType();
				resetModel();
			}
			
		});
		
		var resetModel = function() {
			$scope.validateChildren = false;
			$scope.areaTypeName = null;
			$scope.areaTypeId = null;
			$scope.errorMessage = "";
			$scope.areaType.namesArray = [];
			$scope.areaType.synonymsArray = [];
		};
		
		

	};

	return {
		isFeatureOf : function(module) {
			module.controller('AreaTypeDetailController', [ '$scope', '$http',
					'areaService', AreaTypeDetailController ]);
		}
	};
});