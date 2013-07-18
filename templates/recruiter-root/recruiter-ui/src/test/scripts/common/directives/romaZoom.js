define([], function() {
    "use strict";

    return function() {
        return {
            scope: false,
            replace: true,
            controller: 'romaZoom',
            templateUrl: 'html/common/romaZoom.html',
            link: function(scope, element, attrs, controller) {
            	scope.toplevel = attrs.toplevel;
                controller.zoomIn(attrs.toplevel);
            }
        };
    };
});