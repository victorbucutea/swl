define([], function() {
	"use strict";

	return function($resource, messageService) {
		var self = this;

		
		// TODO replace with $location service
		// TODO extract operations into a generic CRUD service
		var candidateRestService = $resource("/recruiter-ui/json/candidates/:id", {}, {
			getAll : {
				method : 'GET',
				isArray : true
			}
		});

		self.getAll = function(callbackFunc) {
			if (!callbackFunc) {
				callbackFunc = function() {};
			}
			return candidateRestService.getAll(callbackFunc, function(data, status, headers, config) {
				messageService.error("Cannot get Candidates. ");
			});
		};

		self.get = function(cvId, callbackFunc) {
			return candidateRestService.get({
				id : cvId
			}, callbackFunc);
		};

		self.save = function(cv, successCallbackFunc, errorCallbackFunc) {
			return cv.$save(function(data) {
				self._success(successCallbackFunc, data, "Candidate successfully saved");
			}, function(data) {
				self._error(errorCallbackFunc, data, "Cannot save Candidate.");
			});
		};

		self.remove = function(cv, successCallbackFunc, errorCallbackFunc) {
			cv.$remove(function(data) {
				self._success(successCallbackFunc, data, "Candidate deleted.");
			}, function(data) {
				self._error(errorCallbackFunc, data, "Error while deleting Candidate.");
			});
		};

		self.create = function() {
			return new candidateRestService();
		};

		self._success = function(succesFunc, data, message) {
			if (successFunc) {
				successFunc(data);
			} else {
				messageService.confirm(message);
			}
		};

		self._error = function(errorFunc, data, message) {
			if (errorFunc)
				errorFunc(data);
			else {
				messageService.error(message);
			}
		};

		return self;
	};
});