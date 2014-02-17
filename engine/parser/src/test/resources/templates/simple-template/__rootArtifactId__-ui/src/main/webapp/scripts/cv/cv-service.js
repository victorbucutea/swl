define([], function() {
	"use strict";

	return function($resource, messageService) {
		var self = this;

		var cvRestService = $resource("/recruiter-ui/rest/cvs/:id", {}, {
			getAll : {
				method : 'GET',
				isArray : true
			},
			search : {
				method : 'GET',
				isArray: true
			}
		});
		
		
		self.search = function(searcherName,callbackFunc) {
			if (!callbackFunc) {
				callbackFunc = function() {};
			}
			cvRestService.search({id:'search_'+searcherName, firstName:'Ghita'},callbackFunc);
		};

		self.getAll = function(callbackFunc) {
			if (!callbackFunc) {
				callbackFunc = function() {};
			}
			return cvRestService.getAll(callbackFunc, function(data, status, headers, config) {
				messageService.error("Cannot get CVs. ");
			});
		};

		self.get = function(cvId, callbackFunc) {
			return cvRestService.get({
				id : cvId
			}, callbackFunc);
		};

		self.save = function(cv, successCallbackFunc, errorCallbackFunc) {
			return cv.$save(function(data) {
				self._success(successCallbackFunc, data, "CV successfully saved");
			}, function(data) {
				self._error(errorCallbackFunc, data, "Cannot save CV.");
			});
		};

		self.remove = function(cv, successCallbackFunc, errorCallbackFunc) {
			cv.$remove(function(data) {
				self._success(successCallbackFunc, data, "CV deleted.");
			}, function(data) {
				self._error(errorCallbackFunc, data, "Error while deleting CV.");
			});
		};

		self.create = function() {
			return new cvRestService();
		};

		self._success = function(succesFunc, data, message) {
			if (typeof successFunc != 'undefined' ) {
				successFunc(data);
			} else {
				messageService.confirm(message);
			}
		};

		self._error = function(errorFunc, data, message) {
			if (typeof errorFunc != 'undefined' )
				errorFunc(data);
			else {
				messageService.error(message);
			}
		};

		return self;
	};
});