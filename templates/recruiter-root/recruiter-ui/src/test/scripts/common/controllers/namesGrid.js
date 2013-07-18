define(
		[ 'jqgrid/jquery_jqGrid_src' ],
		function() {
			"use strict";

			return function($scope, countryService) {

				var self = this;
				$scope.items = [];
				
				function deleteFormat(cellvalue, options, rowObject) {
					return '<img src="images/trash.gif" onclick="" />';
				}

				var updateItems = function(grid, rowid, typeOperation){
					
					var gridIds = grid.getDataIDs();
					
					if(rowid<0){
						var row = grid.getRowData(rowid);
						if(typeOperation=='C'){
							if($scope.items==undefined){
								$scope.items = new Array();
							}
							var found = false;
							for ( var i = 0; i < $scope.items.length; i++) {
								if($scope.items[i]["reference"] == rowid){
									$scope.items[i]["text"] = row["text"];
									$scope.items[i]["language"] = row["language"];
									found = true;
								}
							}
							if(!found){
								var length = $scope.items.length;
								$scope.items[length] = new Object();
								$scope.items[i]["reference"] = rowid;
								$scope.items[length]["text"] = row["text"];
								$scope.items[length]["language"] = row["language"];
								$scope.items[length]["created"] = true;
							}
						}
						if(typeOperation=='U'){
							for ( var i = 0; i < $scope.items.length; i++) {
								if($scope.items[i]["reference"] == rowid){
									$scope.items[i]["text"] = row["text"];
									$scope.items[i]["language"] = row["language"];
								}
							}
						}
						if(typeOperation=='D'){
							for ( var i = 0; i < $scope.items.length; i++) {
								if($scope.items[i]["reference"] == rowid){
									$scope.items.splice(i,1);
								}
							}
						}
					}else{
						for ( var i = 0; i < $scope.items.length; i++) {
							if($scope.items[i]["reference"] == rowid){
								var row = grid.getRowData(rowid);
								switch (typeOperation)
								{
									case 'U':
										$scope.items[i]["text"] = row["text"];
										$scope.items[i]["language"] = row["language"];
										$scope.items[i]["updated"] = true;
									  break;
									case 'D':
										$scope.items[i]["removed"] = true;
										if($scope.items[i]["updated"]){
											$scope.items[i]["updated"] = false;
										}
									  break;  
									default:
									  console.log("error the operation type is invalid"+type);
								}
							}	
						}
					}
					
					//console.log("updated items:"+ JSON.stringify($scope.items));
				}
				
				var afterSaveCallback = function(rowid, response) {
					var typeOperation = (rowid<0)?'C':'U';
					var $this = $(this);
					updateItems($this, rowid, typeOperation);
					return true;
				}

				var validate = function(grid, event) {
					if (!grid.getGridParam("selrow")) {
						return true;
					}
					var nameValidation = validateEmptyRow(grid);
					var uniqueValidation = validateUniqueRow(grid);
					if (nameValidation[0] && uniqueValidation[0]) {
						grid.jqGrid("saveRow", $scope.editingRowId, editParam);
						return true;
					} else {
						var errMsgs = [];
						if(!nameValidation[0]){
							errMsgs.push(nameValidation[1]);
						}
						if(!uniqueValidation[0]){
							errMsgs.push(uniqueValidation[1]);
						}
						showErrMessages(errMsgs, grid);
						$('#'+grid.getGridParam("selrow")).click();
						$scope.editingRowId = grid.getGridParam("selrow");
						grid.jqGrid("editRow", $scope.editingRowId, editParam);
						return false;
					}
				};
				
				var showErrMessages = function (errMsgArray, grid){
					if(errMsgArray !== undefined && errMsgArray.length > 0){
						var errMsg = "";
						for(var i=0;i<errMsgArray.length;i++){
							errMsg = errMsg + errMsgArray[i] + "<br/>";
						}
						var parentDiv = grid.parent();
						var parentTop = parentDiv.offset().top;
						var parentLeft = parentDiv.offset().left;
						$.jgrid.info_dialog($.jgrid.errors.errcap, errMsg,
							$.jgrid.edit.bClose, {
								left : parentLeft,
								top : parentTop
							});
					}
				}

				var onBlurGrid = function(e, gridId) {
					if ($scope.editingRowId) {
						if ($(e.target).closest(gridId).length == 0) {
							if (validate($(gridId), e)) {
								$(gridId).jqGrid("resetSelection");
								$scope.editingRowId = undefined;
							} else {
								e.stopPropagation();
							}
						}
					}
				}

				var validateEmptyRow = function(grid) {
					if ($scope.editingRowId) {
						var clModel = grid.getGridParam("colModel");
						for ( var i = 0; i < clModel.length; i++) {
							if (!$("#" + $scope.editingRowId + "_"
											+ clModel[i].index).val()) {
								return [ false, "Please fill in the Name Label" ];
							}
						}
					}
					return [ true, "" ];
				}

				var validateUniqueRow = function(grid) {
					if ($scope.editingRowId) {
						var gridIds = grid.getDataIDs();
						var newName = $("#" + $scope.editingRowId + "_text")
								.val();
						var newLang = $("#" + $scope.editingRowId + "_language")
								.val();
						for ( var i = 0; i < gridIds.length; i++) {
							if (gridIds[i] != $scope.editingRowId) {
								var row = grid.getRowData(gridIds[i]);
								var name = row["text"];
								var lang = row["language"];
								if (newName.toLowerCase() == name.toLowerCase() && newLang.toLowerCase() == lang.toLowerCase()) {
									return [ false, "Duplicated record" ];
								}
							}
						}
					}
					return [ true, "" ];
				}

				var editParam = {
					keys : false,
					oneditfunc : function(id) {
						$scope.editingRowId = id;
					},
					afterrestorefunc : function(id) {
						$scope.editingRowId = undefined;
					},
					url : 'clientArray',
					reloadAfterSubmit : false,
					aftersavefunc : afterSaveCallback
				};

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
										colNames : [ "Name Label", "Language"],
										colModel : [
										         
												{
													name : 'text',
													index : 'text',
													width : "100",
													sorttype : "string",
													align : "left",
													edittype : "text",
													editoptions: {maxlength: $scope.textmaxlength},
													editable : true
												},
												{
													name : 'language',
													index : 'language',
													width : "150",
													sorttype : "string",
													align : "left",
													editable : true,
													formatter : 'select',
													edittype : "select",
													editoptions : {
														value : "FRENCH:French;DUTCH:Dutch;GERMAN:German;ENGLISH:English;UNKNOWN:Unknown"
													}
												}
												],
										sortname : "text",
										sortorder : "asc",
										viewrecords : false,
										altRows : true,
										ignoreCase : true,
										pager : "#" + generatedId + "Pager",
										editurl : 'clientArray',
										localReader: {
											id:"reference"
										},
										onSelectRow : function(id) {
											var $this = $(this);
											if (id && $scope.editingRowId != id) {
												$this.jqGrid("editRow", id,
														editParam);
											}
											if($this.getDataIDs().length>1 || $scope.candeleteall){
												$("#" + generatedId + "_ildelete")
												.removeClass(
														"ui-state-disabled");
											}
											
											return true;
										},
										beforeSelectRow : function(id, event) {
											if (id && $scope.editingRowId != id) {
												var $this = $(this);
												return validate($this, event);
											}
											return true;
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

					nameList
							.navButtonAdd(
									"#" + generatedId + "Pager",
									{
										caption : "",
										title : "Delete selected row",
										buttonicon : 'ui-icon-trash',
										onClickButton : function(event) {
											if ($scope.editingRowId) {
												$("#" + generatedId).jqGrid(
																"delRowData",
																$scope.editingRowId,
																{
																	reloadAfterSubmit : false
																});												
												updateItems($("#" + generatedId), $scope.editingRowId, 'D');												
												$("#" + generatedId).jqGrid("resetSelection");
												$scope.editingRowId = undefined;
											}
											$("#" + generatedId + "_ildelete")
													.addClass(
															"ui-state-disabled");
										},
										id : generatedId + "_ildelete"
									});
					nameList.navButtonAdd("#" + generatedId + "Pager", {
						caption : "",
						title : "Add new row",
						buttonicon : 'ui-icon-plus',
						onClickButton : function(event) {

							if (validate($("#" + generatedId), event)) {
								var newRowId = Math
										.floor((Math.random() * -1000) + 1);
								$("#" + generatedId).jqGrid('addRowData',
										newRowId, {}, "first");
								$("#" + generatedId).jqGrid('setSelection',
										newRowId, true);
							} else {
								return false;
							}
						},
						id : generatedId + "_iladd"
					});

					$('body').bind('click', function(e) {
						onBlurGrid(e, "#" + generatedId);
					});
					$(':input').bind('click', function(e) {
						onBlurGrid(e, "#" + generatedId);
					});
					$(':button').bind('blurGrid', function(e) {
						onBlurGrid(e, "#" + generatedId);
					});
					

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