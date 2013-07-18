define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
                countryreference: '=',
                countries: '=',
                required: '='
            },
            replace: true,
            controller: 'countrySelect',
            templateUrl: 'html/common/countries.html',
            link: function(scope, element, attrs, controller) {
                controller.init();
            }
        };
    };
});