define([], function() {
	"use strict";
	return function($rootScope, module) {
		var warnings = new Array();
		var errors = new Array();
		var confirmations = new Array();
		var info = new Array();

		this.warn = function(message) {
			this.clearMessages();
			warnings.push(message);
			$rootScope.$broadcast("_warn", warnings);
		};

		this.error = function(message) {
			this.clearMessages();
			errors.push(message);
			$rootScope.$broadcast("_err", errors);
		};

		this.confirm = function(message) {
			this.clearMessages();
			confirmations.push(message);
			$rootScope.$broadcast("_confirm", confirmations);
		};
		
		this.info = function(message) {
			this.clearMessages();
			info.push(message);
			$rootScope.$broadcast("_info", info);
		};

		this.clearMessages = function() {
			warnings = [];
			$rootScope.$broadcast("_warn", "");
			errors = [];
			$rootScope.$broadcast("_err", "");
			confirmations = [];
			$rootScope.$broadcast("_confirm", "");
			info =[];
			$rootScope.$broadcast("_info", "");
		};
	};
});