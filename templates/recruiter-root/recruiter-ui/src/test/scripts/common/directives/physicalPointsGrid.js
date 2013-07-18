define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
            	
            },
            replace: true,
            controller: 'physicalPointsGrid',
            templateUrl: 'html/common/physicalPointsGrid.html',
            link: function(scope, element, attrs, controller) {
                controller.list();
            }
        };
    };
});