define(['angular',
        'services/security-service',
        'security/http-buffer', 
        'security/security-interceptor'],
        function(angular, securityService, httpBuffer, securityInterceptor) {

			var module = angular.module('security', [])
											   .factory('httpBuffer',[ '$rootScope', '$injector', httpBuffer ])
											   .service('securityService', [ '$rootScope', '$resource', '$templateCache','$location','httpBuffer', 'messageService', securityService ])
											   .config(['$httpProvider', securityInterceptor ]);
			
});