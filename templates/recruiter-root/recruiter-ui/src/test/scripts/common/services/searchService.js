define(
    [
        'jquery'
    ],
    function(jQuery) {

        var Search = function(url, criteria) {
            "use strict";

            if (!url) throw 'url is mandatory';

            this.url = url;
            this.criteria = criteria;
            this.result = null;
        };

        var SearchService = function($rootScope, $http) {
            "use strict";

            var self = this;
            var lastSearch = null;

            var raiseSearchPerformed = function(search) {
                $rootScope.$broadcast('SearchService.searchPerformed', {raisedBy: self, search: search});
            };

            var raiseSearchFailed = function(search) {
                $rootScope.$broadcast('SearchService.searchFailed', {raisedBy: self, search: search});
            };

            self.perform = function(search) {
                lastSearch = search;
                $http(
                    {
                        method: 'GET',
                        url: SearchService.buildGetUrl(search.url, search.criteria)
                    })
                    .success(function(data) {
                        search.result = data;
                        raiseSearchPerformed(search);
                    })
                    .error(function() {
                        raiseSearchFailed(search);
                    });
            };

            self.onSearchPerformed = function(listener) {
                $rootScope.$on('SearchService.searchPerformed', function(event, payload) {
                    if (payload.raisedBy === self) {
                        listener(payload.search);
                    }
                });
            };

            self.onSearchFailed = function(listener) {
                $rootScope.$on('SearchService.searchFailed', function(event, payload) {
                    if (payload.raisedBy === self) {
                        listener(payload.search);
                    }
                });
            };
        };

        SearchService.buildGetUrl = function(url, params) {
            var urlWithParams = url;
            if (params) {
                var definedParams = {};
                for (var key in params) {
                    if (params.hasOwnProperty(key) && params[key]) {
                        definedParams[key] = params[key];
                    }
                }

                var paramsAsString = jQuery.param(definedParams);
                if (paramsAsString) {
                    urlWithParams = urlWithParams + '?' + paramsAsString;
                }
            }
            return urlWithParams;
        };

        return SearchService;
    });