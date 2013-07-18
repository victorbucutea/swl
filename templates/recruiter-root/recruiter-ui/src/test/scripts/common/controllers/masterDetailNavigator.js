define([], function() {
	"use strict";

	return function($scope, $http, $compile) {
		var self = this;
		var history = {
			max : 9,/* actually 10 because it is 0 based */
			currentIndex : 0,
			elements : Array(),
			addNew : function(elm) {
				this.elements.push(elm);
				this.currentIndex = this.elements.length - 1;
			},
			back : function() {
				this.currentIndex--;
//				console.log('going back to element ', this.elements[this.currentIndex]);
				return this.elements[this.currentIndex];
			},
			forward : function() {
				this.currentIndex++;
//				console.log('going forward to element ', this.elements[this.currentIndex]);
				return this.elements[this.currentIndex];
			},
			refresh : function() {
				console.log('refreshing to element ', this.elements[this.currentIndex]);
				return this.elements[this.currentIndex];
			},
			canNotGoBack : function() {
				if (this.currentIndex == 0)
					return true;
				
				return !(this.elements[this.currentIndex-1]);
			},
			canNotGoForward : function() {
				return !(this.elements[this.currentIndex+1]);
			}
		}

		$scope.history = history;
		
		function loadContent(element){
			$scope.loadContentTag.children().remove();
			$scope.loadContentTag.append(element);
		}

		$scope.$on('$contentLoaded', function(e, arg) {
			$scope.loadContentTag = arg;
			var children = arg.children();
			var angularElement = angular.element(children);
			history.addNew(angularElement);
		});

		$scope.back = function() {
			var previousElem = history.back();
			loadContent(previousElem);
		}

		$scope.forward = function() {
			var nextElem = history.forward();
			loadContent(nextElem);
		}

		$scope.linkWithZoomItem = function() {
			console.log('linking with zoom item');
		}

		$scope.refresh = function() {
			loadContent(history.refresh());
		}

	};
});
