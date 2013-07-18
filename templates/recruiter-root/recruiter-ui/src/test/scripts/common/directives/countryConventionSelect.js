define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
                convention: '='
            },
            replace: true,
            controller: 'countryConventionSelect',
            templateUrl: 'html/common/countryConvention.html',
            link: function(scope, element, attrs, controller) {
                controller.init();
            }
        };
    };
});