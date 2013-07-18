define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
                items: '=',
                selectedtype: '=',
                itemsregistry: '='
            },
            replace: true,
            controller: 'geoselectionTypeGrid',
            templateUrl: 'html/common/geoselectionTypeGrid.html',
            link: function(scope, element, attrs, controller) {
                controller.list();
            }
        };
    };
});