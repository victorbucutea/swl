define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
                items: '=',
                triggerValidation: "=",
                candeleteall: "="
            },
            replace: true,
            controller: 'subMunicipalitiesGrid',
            templateUrl: 'html/common/subMunicipalitiesGrid.html',
            link: function(scope, element, attrs, controller) {
                controller.list();
            }
        };
    };
});