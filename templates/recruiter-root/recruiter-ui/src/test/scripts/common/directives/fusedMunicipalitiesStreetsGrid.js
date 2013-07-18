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
            controller: 'fusedMunicipalitiesStreetsGrid',
            templateUrl: 'html/common/fusedMunicipalitiesStreetsGrid.html',
            link: function(scope, element, attrs, controller) {
                controller.list();
            }
        };
    };
});