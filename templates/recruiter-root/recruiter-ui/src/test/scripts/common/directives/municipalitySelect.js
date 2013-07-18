define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
                municipalityreference: '=',
                municipalities: '=',
                municipalitytlabel: '=',
                required: '='
            },
            replace: true,
            controller: 'municipalitySelect',
            templateUrl: 'html/common/municipalities.html',
            link: function(scope, element, attrs, controller) {
                controller.init();
            }
        };
    };
});