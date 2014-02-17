define([], function() {
	"use strict";

	return function() {
		return {
			scope : {
				value: '=',
				thumbnailHeight: '@',
				thumbnailWidth: '@'
			},
			templateUrl: 'scripts/common/components/fileupload.html',
			link : function postLink(scope, element, attrs) {
				
				var inputFile = element.find('.inputFile');
				
				if ( !inputFile || ('file' !== inputFile.attr('type'))){
					throw "Template does not contain an input field (with selector '.inputFile') of type='file'.";
				}
				
				
				inputFile.bind('change', (function () {
					if (!window.FileReader) {
						return;
					}
					
					var oFReader = new window.FileReader();
					var rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

					oFReader.onload = function (oFREvent) {
						scope.value = oFREvent.target.result ;
						scope.$apply();
					};

					return function () {
							var aFiles = inputFile.get(0).files;
							
							if (aFiles.length === 0) { return; }
							
							if (!rFilter.test(aFiles[0].type)) {
								console.log(aFiles[0] + ' is not a file');
								return; 
							}
							
							oFReader.readAsDataURL(aFiles[0]);
					};

					
					if (navigator.appName === "Microsoft Internet Explorer") {
						return function () {
							previewContainer.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = previewContainer.value;

						};
					}
				})());

			}
		};
	};
});
