/**
 * Main controller handles application-wide concerns :
 * 
 * 1. Login/logout & display login information 2. Displaying list of authorized
 * menus (URLs) 3. Recover/reset password dialogs
 */

define([], function() {
	"use strict";

	return function($scope, securityService, messageService) {
		
		if (!securityService.getPrincipalName()){
			$scope.authenticationRequired = true;
		}
		
		$scope.$on('authenticationRequired', function() {
			$scope.authenticationRequired = true;
		});

		$scope.login = function() {
			securityService.login($scope.username, $scope.password, function(data) {
				
				$scope.principalName = securityService.getPrincipalName();
				$scope.authenticationRequired = false;
			}, function(data) {
				messageService.error("Login failed!");
			});
		};

		$scope.logout = function() {
			securityService.logout(function(){
				$scope.principalName = null;
			});
		};

	};

});