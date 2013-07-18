//configuration of RequireJS
require.config({
    baseUrl: 'lib',

    paths: {
        common: '../scripts/common',
        geographical: '../scripts/geographical',
        jqgrid: 'jquery-jqgrid/js',
        blockui: 'jquery-blockui/js',
        ngResource: 'angular/modules/ngResource',
        scroll_event: 'jquery-scroll-event'
    },

    shim: {
        'jquery-ui': ['jquery'],
        'bootstrap': ['jquery'],
        'angular': {deps: ['jquery'], exports: 'angular'},
        'ngResource': ['angular'],
        'jqgrid/i18n/grid_locale_en': ['jquery'],
        'jqgrid/jquery_jqGrid_src' : ['jquery', 'jquery-ui', 'jqgrid/i18n/grid_locale_en'],
        'blockui/jquery_blockui': ['jquery', 'jquery-ui'],
        'scroll_event/jquery_scroll_startstop_event': ['jquery'],
        'common/utils': {deps: ['jquery']}
    },
    locale:  localStorage.getItem('locale') || 'fr'
});

//configuration of AngularJS
require(
    [
        'angular',
        'common/module',
        'geographical/module',
        'ngResource'
    ],
    function(angular, common, geographical) {
        angular.module('main', ['ngResource', common.module.name, geographical.module.name]);
        angular.bootstrap(document, ['main']);
    });