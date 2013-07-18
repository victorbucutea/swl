function PhoneDetailCtrl($scope,$http, $routeParams) {

	$scope.phoneId = $routeParams.phoneId;

	$http.get('phones/' + $routeParams.phoneId + '.json').success(
			function(data) {
				$scope.phone = data;
				$scope.mainImageUrl = data.images[0];
			});
	
	
	// set image event handler for changing the ng-src 
	// of the image to the new umage url
	$scope.setImage = function(imageUrl) {
		    $scope.mainImageUrl = imageUrl;
	};
}

function PhoneListCtrl($scope, $http) {
	$http.get("phones/phones.json").success(function(data) {
		$scope.phones = data;
	});
	$scope.orderProp = 'age';
}
