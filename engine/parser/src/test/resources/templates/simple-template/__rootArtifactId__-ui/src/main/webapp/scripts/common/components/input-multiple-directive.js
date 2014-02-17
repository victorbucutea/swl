define([], function() {
	"use strict";

	return function() {
		return {
            scope: {
            	includeUrl: '=',
                title: '=',
                data: '=',
                label: '='
            },
            replace: true,
            controller: 'inputmultipleController',
            templateUrl: 'scripts/common/components/input-multiple.html',
            link: function(scope, element, attrs, controller) {
            	scope.labelExpression = attrs.label;
            }
        };
	};
});