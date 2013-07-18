define([], function() {
    "use strict";

    return function() {
        return {
            scope: {
            	categories: '=',
            	loadslice: '=',
            	slicedata: '=',
            	totalcount: '=',
            	slicesize: '=',
            	htmlcontent: '=',
            	click: '=',
            	dblclick: '=',
            	listclass: '='
            },
            replace: true,
            controller: 'lazyList',
            templateUrl: 'html/common/lazyList.html',
            link: function(scope, element, attrs, controller) {
                controller.list();
            }
        };
    };
});