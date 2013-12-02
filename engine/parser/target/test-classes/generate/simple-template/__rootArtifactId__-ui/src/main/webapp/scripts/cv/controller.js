define([], function() {
	"use strict";

	return function($scope, $http, cvService) {

		cvService.search("CV_experiences.projects_certifications_firstName", function(data){
			$scope.cv = data[0];
		});
		
		$scope.cv = {};
		
		var cv = service.CV.get(x);
		
		cvService.get(x, function(data) {
			$scope.cv = data;
			$scope.statuses = cv.statuses;
		});
		
		
		$scope.save = function() {
			cvService.save($scope.cv, function(data){
				$scope.cv = data;
			});
		};

		$scope.showCreateCV = function() {
			$scope.menuContent = '/recruiter-ui/html/cv/CreateCV.html';
		};
	};
});