define([], function() {
	"use strict";

	return function($scope, $http) {
		var self = this;

		self.init = function() {
			$scope.generatedId = Math.floor((Math.random() * -1000) + 1);
		};
		
		self.onClickCheckbox = function($event){
			var thisCheck = $($event.target);
//			console.log(thisCheck.attr("id"))
			var id_ref = thisCheck.attr("id").substring(thisCheck.attr("id").indexOf("_")+1, thisCheck.attr("id").length);
//			console.log(id_ref)
			if (thisCheck.is (':checked'))
			{
				
				$scope.checkeditems.push(parseInt(id_ref));
			}else{
				$scope.checkeditems = jQuery.grep($scope.checkeditems, function(value) {
					  return value != parseInt(id_ref);
					});
			}
		};

		$scope.$watch("items", function(newItems, oldItems){
			if(newItems!==undefined){
				$scope.selectedItems = {};
				$.each($scope.items, function(index, value){
					$scope.selectedItems[value.type.reference] = false;
					if($.inArray(value.type.reference, $scope.checkeditems) > -1){
						$scope.selectedItems[value.type.reference] = true;
					}
				});
			}
		});

		$scope.init = self.init;
		$scope.onClickCheckbox = self.onClickCheckbox;
		

	};
});