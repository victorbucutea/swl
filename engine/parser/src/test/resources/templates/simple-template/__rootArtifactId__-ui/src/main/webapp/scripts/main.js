
require.config({
	
	baseUrl : 'lib',
	
	paths: {
        cv: '../scripts/cv',
        common: '../scripts/common',
        components: '../scripts/common/components',
        services: '../scripts/common/services',
        security: '../scripts/common/security',
        angular: 'https://ajax.googleapis.com/ajax/libs/angularjs/1.1.3/angular'
	},
	shim : {
		'angular' : { deps: ['jquery'], exports: 'angular'},
		'ng-resource':['angular'],
		'datepicker': ['jquery'],
		'fileupload': ['jquery']
	}
});


require([ 'angular',
          'common/utils',
          'common/module',
          'security/module',
          'cv/module'
          ], 
          function() {
					angular.module('main', ['common','cv']);
					angular.bootstrap(document, [ 'main' ]);
		}); 