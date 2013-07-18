define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
                items: '='
            },
            replace: true,
            controller: 'postalCodeGrid',
            templateUrl: 'html/common/postalCodeGrid.html',
            link: function(scope, element, attrs, controller) {
                controller.list();
            }
        };
    };
});