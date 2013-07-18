define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
            	selectedtype: '='
            },
            replace: true,
            controller: 'geoselectionGrid',
            templateUrl: 'html/common/geoselectionGrid.html',
            link: function(scope, element, attrs, controller) {
                controller.list();
            }
        };
    };
});