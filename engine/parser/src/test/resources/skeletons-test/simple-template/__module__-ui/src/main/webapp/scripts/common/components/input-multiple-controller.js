define([ 'jquery' ], function() {
	"use strict";

	return function($scope, $http, $parse) {
		"use strict";
		
		
		$scope.change = function(value) {
			$scope.selectedValue = value;
			$scope.inputMultipleContent = $scope.includeUrl;
		};
		
		$scope.itemLabel = function(value){
			var parsedLabelFunc = $parse($scope.labelExpression);
			var parsedLabel = parsedLabelFunc(value);
			if (parsedLabel) {
				return parsedLabel;
			} else {
				return "( new )";
			}
		};

		
		$scope.topPosition = function(id,$index) {
			var popover = $('#'+id+'-'+$index);
			
			if (!popover)
				return;
			
			var lineHeight = 10;
			var topPos = -(popover.outerHeight() / 2) + lineHeight;
			return topPos;
		};

		
		
		$scope.$on('dirty', function(event) {
			$scope.selectedValue.dirty = true;
		});

		
		
		$scope.close = function() {
			$scope.selectedValue = "";
		};

		
		
		$scope.create = function() {
			var newValue = {};
			if (!$scope.data) {
				$scope.data = [];
			}
			$scope.data.push(newValue);
			$scope.change(newValue);
		};
		
		
		$scope.remove = function(){
			$scope.selectedValue.deleted = true;
			$scope.close();
		};

	};
});