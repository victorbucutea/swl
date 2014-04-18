define([ 'angular', 
         'cv/controller', 
         'cv/cv-service'], 
		function(angular, cvController , cvService) {
			var module = angular.module('cv', ['common'])
												 .controller('cvController',[ '$scope', '$http', 'cvService', cvController ])
												 .config([ '$routeProvider', function($routeProvider) {$routeProvider.when('/cv', {templateUrl : 'html/cv/index.html'});}])
												 .service('cvService', [ '$resource', 'messageService',  cvService]);
});