define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
                items: '=',
                triggerValidation: "=",
                candeleteall: "=",
                textmaxlength : "="
            },
            replace: true,
            controller: 'namesGrid',
            templateUrl: 'html/common/namesGrid.html',
            link: function(scope, element, attrs, controller) {
                controller.list();
            }
        };
    };
});