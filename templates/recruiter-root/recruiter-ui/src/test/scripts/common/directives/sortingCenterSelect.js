define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
            	sortingcenterreference: '=',
                required: '=',
                items: '='
            },
            replace: true,
            controller: 'sortingCenterSelect',
            templateUrl: 'html/common/sortingCenters.html',
            link: function(scope, element, attrs, controller) {
                controller.init();
            }
        };
    };
});