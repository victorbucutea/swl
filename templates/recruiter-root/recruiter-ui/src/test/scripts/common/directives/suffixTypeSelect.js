define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
            	suffixtyperef: '=',
                types: '=',
                suffixlabel: '=',
                required: '='
            },
            replace: true,
            controller: 'suffixTypeSelect',
            templateUrl: 'html/common/suffixTypes.html',
            link: function(scope, element, attrs, controller) {
                controller.init();
            }
        };
    };
});