define(
		[ 'jqgrid/jquery_jqGrid_src' ],
		function() {
			"use strict";

			return function($scope) {

				var self = this;
				$scope.items = [];
				
				function imageFormat(cellvalue, options, rowObject) {
					var displayValue = cellvalue;
					if (cellvalue == undefined) {
						displayValue = '-'
					}
					return '<a href><span style="overflow: ellipsis;width: 80%;" class="ng-binding">' + displayValue + '</span><i ng-click="" class="icon-folder-open new-tab"></i></a>';
				}
				
				self.list = function() {
					$scope.editingRowId = undefined;

					var generatedId = $.jgrid.randId();
					$scope.gridId = generatedId;
					$("#nameList").attr("id", generatedId);
					$("#namePager").attr("id", generatedId + "Pager");

					// jqgrid configuration
					var nameList = $("#" + generatedId)
							.jqGrid(
									{
										data : $scope.items,
										datatype : "local",
										height : "150",
										width : "auto",
										rowNum : 10,
										colNames : [ "Fused municipality", "Name in Dutch", "Name in French", "Sub-municipality"],
										colModel : [
												{
													name : 'fusedMunicipality',
													index : 'fusedMunicipality',
													width : "200",
													sorttype : "string",
													align : "left",
													editable : false,
													formatter : imageFormat
												},
												{
													name : 'dutchName',
													index : 'dutchName',
													width : "200",
													sorttype : "string",
													align : "left",
													editable : false, 
													formatter : imageFormat 
												},
												{
													name : 'frenchName',
													index : 'frenchName',
													width : "200",
													sorttype : "string",
													align : "left",
													editable : false,
													formatter : imageFormat
												},
												{
													name : 'subMunicipality',
													index : 'subMunicipality',
													width : "200",
													sorttype : "string",
													align : "left",
													editable : false, 
													formatter : imageFormat 
												}
												],
										sortname : "fusedMunicipality",
										sortorder : "asc",
										viewrecords : false,
										altRows : true,
										ignoreCase : true,
										pager : "#" + generatedId + "Pager",
										editurl : 'clientArray',
										localReader: {
											id:"reference"
										}

									});
					nameList.navGrid("#" + generatedId + "Pager", {
						edit : false,
						add : false,
						del : false,
						refresh : false,
						search : false
					});
					nameList.inlineNav("#" + generatedId + "Pager", {
						edit : false,
						add : false,
						save : false,
						cancel : false,
						restoreAfterSelect : false
					});

					$("#" + generatedId + "Pager_center").remove();
					$("#" + generatedId + "Pager_right").remove();

					$("#" + generatedId + "_ildelete").addClass(
							"ui-state-disabled");

					$scope.$watch('items', function(newItems, oldItems) {
						if(newItems !== oldItems){
							$("#" + $scope.gridId).jqGrid('clearGridData').jqGrid(
									'setGridParam', {
										data : newItems
									}).trigger('reloadGrid');
							$scope.editingRowId = undefined;
						}						
					});
					
					$scope.$watch("triggerValidation", function(trigger){
						if(trigger){
							onBlurGrid(e, "#" + $scope.gridId);
						}
					});

				};

				$scope.list = self.list;

			};
		});