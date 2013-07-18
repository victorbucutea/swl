define([], function() {
    "use strict";
    
    return function($scope, $element, $http) {
        var self = this;
        
        $scope.nscategory;
        
        self.init = function() {
        	$scope.categories = [{ "value": "C", "text": "C" }, { "value": "P", "text": "P" }];
		};
		
		$scope.init = self.init;
        
    };
});