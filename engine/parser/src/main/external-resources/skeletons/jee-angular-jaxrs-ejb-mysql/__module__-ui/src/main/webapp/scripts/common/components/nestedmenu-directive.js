define([], function() {
	"use strict";

	return function() {
		return {
            scope: {
            	selectedtype: '='
            },
            replace: true,
            controller: 'nestedmenuController',
            templateUrl: 'scripts/common/components/nestedmenu.html',
            link: function(scope, element, attrs, controller) {
                controller.prompt();
            }
        };
	};
});