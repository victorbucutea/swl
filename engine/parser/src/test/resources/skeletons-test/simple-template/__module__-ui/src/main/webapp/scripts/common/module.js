define([ 'angular'									  ,'ng-resource',
         'common/main-controller'					  ,'components/input-multiple-controller',
         'components/input-multiple-directive'		  ,'components/search-controller',
         'components/search-directive'				  ,'components/message-controller',
         'components/message-directive'				  ,'services/message-service',
         'components/search-filter'					  ,'components/datepicker-directive',
         'datepicker'								  ,'components/fileupload-directive'], 
         function(angular				, ngResource, 
        		  mainController		, inputmultipleController, 
        		  inputmultipleDirective, searchController, 
        		  searchDirective		, messageController,
        		  messageDirective		, messageService, 
        		  searchFilter			, datepickerDirective,
        		  datepicker		    , fileuploadDirective) {
	
			var module = angular.module('common', ['security','ngResource'])
															   .controller('mainController',['$scope','securityService','messageService', mainController])		
			
															   .controller('inputmultipleController',[ '$scope', '$http', '$parse', inputmultipleController])
															   .directive('swdlInputMultiple', inputmultipleDirective)
															 
															   .controller('searchController',[ '$scope', '$http', searchController])
															   .directive('swdlSearch', searchDirective)
															 
															   .controller('messageController',[ '$scope', '$http', messageController])
															   .directive('swdlMessage', messageDirective)
															   
															   .directive('datepicker', datepickerDirective)
															   .directive('fileupload', fileuploadDirective)
															  
															   .service('messageService',  ['$rootScope', messageService])
		
															   .filter('search',['$http',searchFilter]);
});