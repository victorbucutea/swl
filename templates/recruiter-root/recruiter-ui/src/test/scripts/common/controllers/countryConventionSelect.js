define([], function() {
    "use strict";
    
    return function($scope, $element, $http) {
        var self = this;
        
        $scope.convention;
        
        self.init = function() {
        	$scope.conventions = [{ "value": "NONE", "text": "" }, { "value": "REIMS", "text": "REIMS" }];
		};
			
		$scope.init = self.init;
        
    };
});