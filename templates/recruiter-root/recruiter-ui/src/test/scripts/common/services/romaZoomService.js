define([], function() {
	return function($rootScope) {
		"use strict";

		var self = this;
		var currentSelectedItem = undefined;
        var lastSelectedItem = undefined;
		var currentItemTarget = undefined;
		var lastItemTarget = undefined;

		self.undoSelect = function() {
			if (lastItemTarget !== undefined) {
				$rootScope.$broadcast('MasterDetailService.undoSelection', {
					raisedBy : self,
					item : lastSelectedItem,
					itemTarget : lastItemTarget,
					currentItemTarget : currentItemTarget
				});
				lastItemTarget = undefined;
			}
		};

		self.onUndoSelection = function(listener) {
			$rootScope.$on('MasterDetailService.undoSelection', function(event,
					payload) {
				if (payload.raisedBy === self) {
					listener(payload.item, payload.itemTarget,
							payload.currentItemTarget);
				}
			});
		};

	};
});