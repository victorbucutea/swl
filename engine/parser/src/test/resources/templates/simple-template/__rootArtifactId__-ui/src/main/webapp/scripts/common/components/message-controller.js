define([], function() {
	"use strict";

	return function($scope,$http) {
		
		
		$scope.$on("_confirm", function (event,arg) {
			$scope.successes = arg;
		});
		
		$scope.$on("_err", function (event,arg) {
			$scope.errors = arg;
		});
		
		$scope.$on("_warn", function (event,arg) {
			$scope.warnings = arg;
		});
		
		$scope.$on("_info", function (event,arg) {
			$scope.infos = arg;
		});
		
		
		this.prompt  = function ( ) {
		};
	};
});