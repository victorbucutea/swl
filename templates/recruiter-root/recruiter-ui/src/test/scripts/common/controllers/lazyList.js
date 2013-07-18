define(
		[ 'scroll_event/jquery_scroll_startstop_event', 'blockui/jquery_blockui' ],
		function() {
			"use strict";

			return function($scope, $compile) {

				var self = this;
				var liElementHeight = 22;
				
				$scope.listdata = {
						fromIndex : 0,
						slice : []
				};
				
				self.list = function() {
					$scope.scrollIntoView = false;
					if($scope.totalcount != undefined){
						$('#list-scroll-div').css("paddingn-top",  parseInt($scope.totalcount)-$scope.slicesize);
					}
					
				};

				$scope.list = self.list;

//				$scope.$watch('categories', function() {
//					if ($scope.categories != null) {
//						generateIndexRibbon();
//					}
//				});
				
				$scope.$watch('htmlcontent', function() {
					if ($scope.htmlcontent != null) {
						//compile the HTML content and add it in the correct place in DOM
						var newHTMLData = $compile($scope.htmlcontent)($scope);
						$('#list-scroll').append(newHTMLData);
					}
				});
				
				
				$scope.$watch('slicedata', function() {
//					if(parseInt($scope.totalcount)<$scope.slicesize){
//						console.log("aaa:"+$scope.totalcount)
//						$scope.slicesize = parseInt($scope.totalcount);
//					}
					if ($scope.slicedata != null && $scope.slicedata["slice"].length > 0) {
						console.log("slicedata changed")
						//remove list markers
						$('.nav-indicator').remove();
						
						var firstIndex =  parseInt($scope.listdata.fromIndex);
						var lastIndex = Math.max(firstIndex + parseInt($scope.listdata.slice.length) - 1, 0);
						var startIndex = parseInt($scope.slicedata["fromIndex"]);
						var newSliceLength = parseInt($scope.slicedata["slice"].length);
						
						
						addDataToList(firstIndex, startIndex, newSliceLength);
						
				     	if($scope.discardToIndex !== undefined){
				     		console.log("Discard upper data until index="+$scope.discardToIndex);
				     		discardUpperData($scope.discardToIndex);
				     	}else if($scope.discardFromIndex !== undefined){
				     		console.log("Discard lower data from index="+$scope.discardFromIndex);
				     		discardLowerData($scope.discardFromIndex);
				     	}

				     	firstIndex =  parseInt($scope.listdata.fromIndex);
						lastIndex = Math.max(firstIndex + parseInt($scope.listdata.slice.length) - 1, 0);
						console.log('lastIndex = ' + lastIndex);
				     	
//						generateListMarkers();
						
						$('#list-scroll ul li').click(function(e) {
							e.preventDefault();
						});
				     	
				     	//padding
				     	if (lastIndex == parseInt($scope.totalcount)-1) {
				     		console.log("padding bottom")
//				     		var currentMarkers = $('.nav-indicator').length;
				     		var currentMarkers = 0;
				     		$scope.deltaPadding = parseInt($scope.totalcount)* liElementHeight - (parseInt($scope.listdata.slice.length) + currentMarkers)*liElementHeight;
				     	} else if (firstIndex == 0) {
				     		console.log("padding top")
				     		$scope.deltaPadding = 0;
				     	}else{
				     		if($scope.ribbonPressed){
				     			$scope.deltaPadding = firstIndex * liElementHeight;
				     			$scope.ribbonPressed = false;
				     		}else{
				     			var viewportHeight = $("#list-scrollcontainer").outerHeight();
								var viewportDim = Math.floor(viewportHeight/liElementHeight);
								var deltaLoadedData = parseInt($scope.listdata.slice.length)/2 - viewportDim/2;
				     			$scope.deltaPadding = firstIndex * liElementHeight - parseInt($scope.listdata.slice.length)/2 ;
				     		}
				     		console.log("padding middle")
				     	}
				     	if($scope.deltaPadding !== undefined){
				     		console.log("First actual index before padding:"+firstIndex);
					     	scrollIntoView($scope.deltaPadding);
				     	}
				     	
				     	$scope.deltaPadding = undefined;
				     	$scope.discardToIndex = undefined;
				     	$scope.discardFromIndex = undefined;
					}else{
						$scope.listdata.slice.length = 0;
					}
					$("#list-scrollcontainer").parent().unblock(); 
				});
				
				var scrollIntoView = function(upperPadding){
					console.log("Scroll into view: "+upperPadding);
					$('#list-scroll').css("padding-top", upperPadding);
				};

				$scope.$watch('totalcount', function() {
					console.log("Total count updated")
					if ($scope.totalcount != null) {
						$('#list-scroll-div').height(parseInt($scope.totalcount) * liElementHeight);
					}
				});
				
				var onScrollStop = function(startIndex){
					startIndex = Math.floor(startIndex);
					var firstIndex =  parseInt($scope.listdata.fromIndex);
					var lastIndex = Math.max(firstIndex + parseInt($scope.listdata.slice.length) - 1, 0);
//					var currentMarkers = $('.nav-indicator').length;
					var currentMarkers = 0;
					var listDim = lastIndex*liElementHeight;
					var viewportHeight = $("#list-scrollcontainer").outerHeight();
					var viewportDim = Math.floor(viewportHeight/liElementHeight);
					var deltaLoadedData = $scope.slicesize/2 - viewportDim/2;
					var maxExistingScrollPosition = startIndex*liElementHeight + viewportHeight;
					
					//default values
					var indexFrom = 0;
					var indexTo = $scope.slicesize-1;
					
					if(listDim >= maxExistingScrollPosition && firstIndex <= startIndex){
						//no need to load data
						console.log("lisDim: "+listDim +" first idx: "+firstIndex)
						console.log("No need to load data: "+maxExistingScrollPosition+" "+ (startIndex));
						$("#list-scrollcontainer").parent().unblock(); 
						return;
					}else if(listDim < maxExistingScrollPosition){
						console.log("----------------Moving Down----------------------")
						if(startIndex-lastIndex > deltaLoadedData){
							//upper gap between data, discard current list and load the new one
							console.log("upper gap between data, discard current list and load the new one");
							var firstViewableIndex = startIndex;
							indexFrom = firstViewableIndex - deltaLoadedData;
							indexTo = Math.min(indexFrom + $scope.slicesize-1, parseInt($scope.totalcount)-1);
							console.log(indexFrom+" "+indexTo);
							if(indexTo - indexFrom < $scope.slicesize-1){
								indexFrom = indexTo -  $scope.slicesize + 1;
								console.log("End of list reached, recalculate index from: "+indexFrom);
							}
							//discard all above lastIndex
							$scope.discardToIndex = lastIndex;
						}else if(startIndex <= lastIndex && lastIndex <= maxExistingScrollPosition/liElementHeight){
							//overlap
							var overlap = deltaLoadedData + lastIndex - Math.floor(startIndex);
							console.log("overlaped data: "+overlap);
							var recordsToLoad = $scope.slicesize - overlap;
							indexFrom = lastIndex + 1;
							indexTo = Math.min(indexFrom + recordsToLoad -1, parseInt($scope.totalcount)-1);
							if(indexTo - indexFrom + overlap < $scope.slicesize-1){
								overlap = $scope.slicesize + indexTo - overlap  - indexFrom; 
								if(overlap<0){
									//$scope.deltaPadding = undefined;
							     	$scope.discardToIndex = undefined;
							     	$scope.discardFromIndex = undefined;
							     	console.log("nok")
							     	$("#list-scrollcontainer").parent().unblock(); 
									return;
								}
								console.log("End of list reached, recalculate overlap: "+overlap);
							}
							//discard 
							$scope.discardToIndex = indexFrom - overlap - 1;
							console.log("Discard overlap:"+$scope.discardToIndex)
						}else if(startIndex - lastIndex <= deltaLoadedData){
							//last index is out of viewport at distance < deltaLoadedData
							var recordsToKeep = deltaLoadedData - (Math.floor(startIndex) - lastIndex);
							console.log("last index is out of viewport at distance < deltaLoadedData => keep some data:"+recordsToKeep);
							var recordsToLoad = $scope.slicesize - recordsToKeep;
							indexFrom = lastIndex + 1;
							indexTo = Math.min(indexFrom + recordsToLoad - 1, parseInt($scope.totalcount)-1);
							if(indexTo - indexFrom + recordsToKeep < $scope.slicesize-1){
								recordsToKeep = $scope.slicesize + indexTo - recordsToKeep  - indexFrom; 
								if(recordsToKeep<0){
									//$scope.deltaPadding = undefined;
							     	$scope.discardToIndex = undefined;
							     	$scope.discardFromIndex = undefined;
							     	console.log("nok")
							     	$("#list-scrollcontainer").parent().unblock(); 
									return;
								}
								console.log("End of list reached, recalculate recordsToKeep: "+recordsToKeep);
							}
							//discard 
							$scope.discardToIndex = indexFrom - recordsToKeep - 1;
						}
					} else if (firstIndex*liElementHeight >= startIndex*liElementHeight && firstIndex > 0){
						console.log("----------------Moving Up----------------------")
						console.log("startIndex: "+startIndex)
						if(firstIndex*liElementHeight - maxExistingScrollPosition > deltaLoadedData*liElementHeight){
							//lower gap between data, discard current list and load the new one
							console.log("lower gap between data, discard current list and load the new one");
							var firstViewableIndex = Math.floor(maxExistingScrollPosition/liElementHeight);
							indexTo = firstViewableIndex + deltaLoadedData;
							indexFrom =  Math.max(indexTo - $scope.slicesize-1, 0);
							console.log(indexFrom+" "+indexTo);
							if(indexTo - indexFrom < $scope.slicesize-1){
								indexTo = indexFrom +  $scope.slicesize - 1;
								console.log("Beginning of list reached, recalculate index from: "+indexFrom);
							}
							//discard starting with first index
							$scope.discardFromIndex = firstIndex;
						}else if(firstIndex*liElementHeight >= startIndex*liElementHeight && firstIndex*liElementHeight <= maxExistingScrollPosition){
							//overlap
							var overlap = deltaLoadedData + Math.floor(maxExistingScrollPosition/liElementHeight) - firstIndex;
							console.log("overlaped data: "+overlap);
							var recordsToLoad = $scope.slicesize - overlap;
							indexTo = firstIndex - 1;
							indexFrom = Math.max(indexTo - recordsToLoad - 1, 0);
							if(indexTo - indexFrom + overlap < $scope.slicesize-1){
								overlap = overlap + indexTo -  $scope.slicesize - indexFrom; 
								if(overlap<0){
									//$scope.deltaPadding = undefined;
							     	$scope.discardToIndex = undefined;
							     	$scope.discardFromIndex = undefined;
							     	console.log("nok")
							     	$("#list-scrollcontainer").parent().unblock(); 
									return;
								}
								console.log("Beginning of list reached, recalculate overlap: "+overlap);
							}
							//discard 
							$scope.discardFromIndex = indexTo + overlap + 1;
							console.log("Discard overlap:"+$scope.discardFromIndex)
						}else if(firstIndex*liElementHeight - maxExistingScrollPosition <= deltaLoadedData*liElementHeight){
							//first index is out of viewport at distance < deltaLoadedData
							var recordsToKeep = deltaLoadedData - (firstIndex - Math.floor(maxExistingScrollPosition/liElementHeight));
							console.log("first index is out of viewport at distance < deltaLoadedData => keep some data:"+recordsToKeep);
							var recordsToLoad = $scope.slicesize - recordsToKeep;
							indexTo = firstIndex - 1;
							indexFrom = Math.max(indexTo - recordsToLoad - 1, 0);
							if(indexTo - indexFrom + recordsToKeep < $scope.slicesize-1){
								recordsToKeep = recordsToKeep + indexTo -  $scope.slicesize - indexFrom; 
								if(recordsToKeep<0){
									//$scope.deltaPadding = undefined;
							     	$scope.discardToIndex = undefined;
							     	$scope.discardFromIndex = undefined;
							     	console.log("nok")
							     	$("#list-scrollcontainer").parent().unblock(); 
									return;
								}
								console.log("Beginning of list reached, recalculate recordsToKeep: "+recordsToKeep);
							}
							//discard 
							$scope.discardFromIndex = indexTo + recordsToKeep + 1;
						}
					}

					console.log("first index:"+firstIndex);
					console.log("last index:"+lastIndex);
					console.log("index from:"+indexFrom);
					console.log("index to:"+indexTo);
					if(indexFrom>=indexTo){
						//$scope.deltaPadding = undefined;
				     	$scope.discardToIndex = undefined;
				     	$scope.discardFromIndex = undefined;
				     	$("#list-scrollcontainer").parent().unblock(); 
						return;
					}
					$scope.loadslice(indexFrom, indexTo+1);
				};
	            
				$('#list-scrollcontainer').bind('scroll', function(e){
					var firstIndex =  parseInt($scope.listdata.fromIndex);
					var lastIndex = Math.max(firstIndex + parseInt($scope.listdata.slice.length) - 1, 0);
					var maxExistingScrollPosition = $("#list-scrollcontainer").scrollTop() + $("#list-scrollcontainer").outerHeight();
					var listDim = lastIndex*liElementHeight;
					console.log("first index: "+firstIndex);
					console.log("list dimension: "+listDim);
					if(!(listDim > maxExistingScrollPosition && firstIndex < $("#list-scrollcontainer").scrollTop()/liElementHeight)){
						$("#list-scrollcontainer").parent().block({
							 message: $('#displayBox'),
							 theme: true,
							 blockMsgClass: "listBlockMsgClass"
						});
					}
	            });
				
				$('#list-scrollcontainer').bind('scrollstop', function(e){
					var elem = $(e.currentTarget);
					$scope.ribbonPressed = false;
					onScrollStop (elem.scrollTop()/liElementHeight);
	            });
				
				self.scrollToIndex = function(index) {
					console.log('going to scroll to index ' + index);
					$scope.ribbonPressed = true;
					$("#list-scrollcontainer").scrollTop(index*liElementHeight);
					onScrollStop (index);
				}
				
				$scope.scrollToIndex = self.scrollToIndex;		
				
				var generateIndexRibbon = function() {
					for (var i = 0; i < $scope.categories.length; i++) {
						var currentCategory = $scope.categories[i];
						$('#list-categories').append($compile('<li><a ng-click="scrollToIndex(' + currentCategory.index + 
								');"' + '>' + currentCategory.label + '</a></li>')($scope));
					}
					//set the line-height of li elements, dynamically based on the size of the parent
					var parentHeightPx = $('#list-scrollcontainer').height();
					var liElementHeightPx = (parentHeightPx - $scope.categories.length * 2) / $scope.categories.length;
					if (parseInt(liElementHeightPx) < parseInt($("#list-categories li:first").css('line-height'))) {
						//adjust the height so that all of them will be present
						var liElements = $("#list-categories li");
						for (var i = 0; i < liElements.length; i++) {
							console.log(Math.floor(liElementHeightPx));
							$(liElements[i]).css('line-height', liElementHeightPx + 'px');
						}
					}
				}
				
				var generateListMarkers = function(){
					for (var i = 0; i < $scope.categories.length; i++) {
						$("#"+$scope.categories[i].index).before(createNewLiMarkerElement($scope.categories[i].index, $scope.categories[i].label));
					}
				}
				
				var addDataToList = function(firstIndex, startIndex, newSliceLength){
					if ($scope.listdata.slice.length==0) {
						$scope.listdata.slice = $scope.slicedata["slice"];
						$scope.listdata.fromIndex = startIndex;
					} else if(startIndex < firstIndex) {
						console.log("append at beginning");
						for (var j = newSliceLength - 1; j >= 0; j--) {
							$scope.listdata.slice.unshift($scope.slicedata["slice"][j]);
						}
						$scope.listdata.fromIndex = startIndex;
					} else {
						console.log("append at end");
						for (var j = 0; j < newSliceLength; j++) {
							$scope.listdata.slice.push($scope.slicedata["slice"][j]);
						}
						if (newSliceLength < $scope.slicesize) {
							//we did not brought an entire slice data
							$scope.listdata.fromIndex = $scope.listdata.fromIndex + newSliceLength;
						} else {
							//we brought the entire slice data
							$scope.listdata.fromIndex = startIndex;
						}
						
					}
				}
		     	
				var discardUpperData = function(){
					do {
						$scope.listdata.slice.shift();
					} while ($scope.listdata.slice.length > $scope.slicesize);
				}
				
				var discardLowerData = function(){
					do {
						$scope.listdata.slice.pop();
					} while ($scope.listdata.slice.length > $scope.slicesize);
				}

				var createNewLiMarkerElement = function(newId, newLabel) {
					var newLi = $('<li id="' + newId + '" class="nav-indicator"><a href="#">' + newLabel + '</a></li>');
					newLi.height(liElementHeight);
					return newLi;
				}

			};
		});