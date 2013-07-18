define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
                items: '=',
                label: '=',
                checkeditems: '='
            },
            replace: true,
            controller: 'typesCheckList',
            templateUrl: 'html/common/typesCheckList.html',
            link: function(scope, element, attrs, controller) {
                controller.init();
            }
        };
    };
});