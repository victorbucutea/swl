define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
            },
            replace: true,
            controller: 'masterDetailNavigator',
            templateUrl: 'html/common/masterDetailNavigator.html',
            link: function(scope, element, attrs, controller) {
            }
        };
    };
});