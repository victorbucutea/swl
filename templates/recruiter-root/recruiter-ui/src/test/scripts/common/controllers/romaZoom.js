define([], function() {
	"use strict";

	return function($scope, $http) {
		var self = this;
		
		var model = {
			items : [],
			activeItem : null,
			isModified: false,
			pathToParent: null,
			currentPath: null,
			parentReference:null,
			categories: null,
		    totalcount: null,
		    htmlcontent: null,
		    slicedata: null,
		    sliceSize: 300,
		    newTab: false,
		    entityTypeKey: null,
		    pathToCreate: null,
		    contextData: {}
		    
		};
		
		$scope.romaZoom = model;
		
		//lazy list attributes
		$scope.loadSlice = function(fromIndex, toIndex) {
			console.log('service called to load data from ' + fromIndex + ' to ' + toIndex);
			$http.get($scope.romaZoom.currentPath)
					.success(function(data) {
						var currentSlice = {};
						currentSlice["fromIndex"] = fromIndex;
						if(data.items !== undefined){
							currentSlice["slice"] = data.items.slice(fromIndex, Math.min(toIndex, data.items.length));
							$scope.totalcount = data.items.length;
						}else{
							currentSlice["slice"] = [];
							$scope.totalcount = 0;
						}
						$scope.slicedata = currentSlice;
						$scope.romaZoom.pathToParent = data.pathToParent;
						$scope.romaZoom.pathToCreate = data.pathToCreate;
						$scope.romaZoom.entityTypeKey = data.entityTypeKey;
		});
		
	};
		
		$scope.initializeLazyList = function() {
			//load categories and totalCount
			$scope.slicedata = null;
//			$http.get('scripts/geographical/features/streetDetail.json').success(function(data) {
//				$scope.categories = data.search.search;
//				$scope.totalcount = data.search.totalCount;
//			});
		};

		self.zoomIn = function(url, reference) {
			if (!url) {
				return;
			}
			//console.log("zoom in:"+url);
			if($scope.romaZoom.entityTypeKey!=undefined && reference!=undefined){
				model.contextData[$scope.romaZoom.entityTypeKey]=reference;
			}else{
				if($scope.romaZoom.entityTypeKey!=undefined){
					model.contextData[$scope.romaZoom.entityTypeKey]=null;
				}
			}
			//model.activeItem = null;
			model.currentPath = url;
			model.parentReference = reference;
			$scope.initializeLazyList();
			$scope.loadSlice(0, model.sliceSize);
		};

		//details displayed in current tab
		self.onClick = function(item, $event) {
			if($scope.romaZoom.isModified && confirm("Your changes will be lost. Do you want to continue?")){
				$scope.romaZoom.isModified = false;
				model.activeItem = item;
				model.newTab = true;
			}else if($scope.romaZoom.isModified){
				$event.stopPropagation();
			}else{
				model.activeItem = item;
				model.newTab = true;
			}
		}
		
		//details displayed in a new tab
		self.openInNewTab = function(item, $event){
			if($scope.romaZoom.isModified && confirm("Your changes will be lost. Do you want to continue?")){
				$scope.romaZoom.isModified = false;
				model.activeItem = item;
				model.newTab = true;
			}else if($scope.romaZoom.isModified){
				$event.stopPropagation();
			}else{
				model.activeItem = item;
				model.newTab = true;
			}
		}
		
		$http.get("html/common/listContent.html").success(function(content) {
        	$scope.htmlcontent = content;
        });

		$scope.zoomIn = self.zoomIn;
		$scope.onClick = self.onClick;

		$scope.classOf = function(item) {
			return item === $scope.romaZoom.activeItem ? 'active' : undefined;
		};
		
		self.returnHome = function (){
			self.zoomIn($scope.toplevel);
		};
		
		$scope.returnHome = self.returnHome;
		
		self.upOneLevel = function(){
			if(model.pathToParent !== undefined){
				self.zoomIn(model.pathToParent);
			}else{
				model.contextData={};
				self.returnHome();
			}
		};
		
		$scope.upOneLevel = self.upOneLevel;
		
		self.refresh = function(){
			//console.log("refresh");
			if(model.currentPath !== undefined){
				self.zoomIn(model.currentPath, model.parentReference);
			}else{
				self.returnHome();
			}
		};
		
		$scope.refresh = self.refresh; 
		
		self.add = function(){
			$scope.romaZoom.activeItem = new Object();
			$scope.romaZoom.activeItem.pathToDetails = model.pathToCreate;
			console.log("add:"+ $scope.romaZoom.pathToDetails);
		};
		
		$scope.add = self.add;
		

	};
});
