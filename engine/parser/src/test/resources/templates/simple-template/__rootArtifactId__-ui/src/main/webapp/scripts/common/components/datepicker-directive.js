define([], function() {
	"use strict";

	return function() {
		return {
			scope : false,
			require : '?ngModel',
			link : function(scope, element, attrs, ngModel) {
				if (!ngModel)
					return; // do nothing if no ng-model

				var datepicker = element.datepicker({
					format : attrs.dateFormat
				}).data('datepicker');

				// write initial value
				ngModel.$render = function() {
					if (ngModel.$viewValue) {
						datepicker.setValue(new Date(ngModel.$viewValue));
					}
					datepicker.update();
				};

				// Write data to the model on change
				element.datepicker().on('changeDate', function(ev) {
					scope.$apply(function() {
						if (datepicker.date)
							ngModel.$setViewValue(datepicker.date.getTime());
					});
					datepicker.hide();
				});
			}
		};
	};
});