define([], function() {
	"use strict";

	return function() {
		return {
            scope: {
            	includeUrl: '=',
            	data:'=',
            	title: '='
            },
            replace: true,
            controller: 'searchController',
            templateUrl: 'scripts/common/components/search.html',
            link: function(scope, element, attrs, controller) {
                controller.init(element,attrs);
            }
        };
	};
});