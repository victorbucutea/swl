define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
                items: '=',
                triggerValidation: "=",
                candeleteall: "=",
                textmaxlength : "=",
                postalcodemaxlength : "="
            },
            replace: true,
            controller: 'municipalitySynonymsGrid',
            templateUrl: 'html/common/municipalitySynonymsGrid.html',
            link: function(scope, element, attrs, controller) {
                controller.list();
            }
        };
    };
});