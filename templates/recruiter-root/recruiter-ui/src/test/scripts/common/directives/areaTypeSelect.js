define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
            	areatyperef: '=',
                types: '=',
                required: '='
            },
            replace: true,
            controller: 'areaTypeSelect',
            templateUrl: 'html/common/areaTypes.html',
            link: function(scope, element, attrs, controller) {
                controller.init();
            }
        };
    };
});