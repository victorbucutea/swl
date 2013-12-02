define([], function() {
	"use strict";

	return function() {
		return {
            scope: {
            },
            replace: true,
            controller: 'messageController',
            templateUrl: 'scripts/common/components/message.html',
            link: function(scope, element, attrs, controller) {
                controller.prompt();
            }
        };
	};
});