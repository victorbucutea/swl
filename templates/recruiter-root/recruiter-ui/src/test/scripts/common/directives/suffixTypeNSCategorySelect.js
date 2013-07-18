define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
            	nscategory: '='
            },
            replace: true,
            controller: 'suffixTypeNSCategorySelect',
            templateUrl: 'html/common/suffixTypeNSCategory.html',
            link: function(scope, element, attrs, controller) {
                controller.init();
            }
        };
    };
});