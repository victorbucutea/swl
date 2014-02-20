/**
 * 
 * Calls authentication service, returns stored security information or fetches
 * additional credentials, retries deferred unauthorized responses.
 * 
 */
define([], function() {
	"use strict";
	return function($rootScope, $resource, $templateCache, $location, httpBuffer, messageService) {

		var credentials = {};

		var securityResource = $resource("/recruiter-ui/rest/auth/:action", {}, {
			login : {
				method : 'POST',
				params : {
					action : "login"
				}
			},
			logout : {
				method : 'POST',
				params : {
					action : "logout"
				}
			}
		});

		this.login = function(username, password, successClbck, errorClbck) {
			securityResource.login({
				name : username,
				password : password
			}, function(data, status, headers) {
				/* successful login callback */
				credentials = data;
				httpBuffer.retryAll();
				successClbck(data, status, headers);
			}, errorClbck);
		};

		this.logout = function(successClbck, errorClbck) {
			securityResource.logout({}, function(data, status, headers) {
				/* successful logout callback */
				credentials = null;
				successClbck(data, status, headers);
				// clear the http cache so that server can deny access to partial templates
				$templateCache.removeAll();
				// go back to main page
				$location.path("/");
			}, errorClbck);
		};

		this.getRoles = function() {

			if (credentials == null) {
				return null;
			}

			return credentials.roles;
		};

		this.getPrincipalName = function() {
			if (credentials == null) {
				return null;
			}

			return credentials.name;
		};

	};
});
