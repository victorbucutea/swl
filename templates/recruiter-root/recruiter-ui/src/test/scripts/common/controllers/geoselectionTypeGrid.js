define([], function() {
	"use strict";

	return function($scope) {
		var self = this;
		var generatedId = $.jgrid.randId();
		var lastsel;
		$("#geoselectionTypeList").attr("id", generatedId);
		$("#geoselectionTypePager").attr("id", generatedId + "Pager");
		
		
		function ItemMap() {
			this.asJSON = function(parentName, typeId) {
				var JSONStr = {};
				JSONStr[parentName] = this.data[typeId];
				return JSON.stringify(JSONStr);
			};
			this.data = {};
			this.isEmpty = function() {
				for ( var k in this.data) {
					return false;
				}
				return true;
			};
			this.clear = function() {
				this.data = {};
			};
		}


		var itemsRegistry = {
			updatedItems : new ItemMap(),
			createdItems : new ItemMap(),
			deletedItems : new ItemMap(),
			updateItem : function(rowData) {
				var reference = rowData.entityId;
				var gridId = rowData.id;

				if (!reference) {
					// updating a freshly created item, call create with grid id
					// to replace the old entry
					this.createItem(gridId, rowData.frenchName, rowData.dutchName);
					return;
				}

				// hold updated items in a map with the key being the reference
				// of the geoselection type
				this.updatedItems.data[reference] = Array();
				var updatedItem = this.updatedItems.data[reference];
				
				updatedItem.push({text : rowData.frenchName,language : "FRENCH",reference : rowData.frenchNameRef});
				updatedItem.push({text : rowData.dutchName, language : "DUTCH", reference : rowData.dutchNameRef});
			},

			createItem : function(gridId, newFrenchName, newDutchName) {
				this.createdItems.data[gridId] = [ {text : newFrenchName,language : "FRENCH"}, 
				                                   {text : newDutchName, language : "DUTCH"} ];
			},

			deleteItem : function(reference, gridId) {
				// if item is deleted, we should not care if it's been updated
				// or created
				delete this.createdItems.data[gridId];
				delete this.updatedItems.data[reference];

				if (!reference) {
					// deleting a freshly added item,no actions to be taken
					return;
				}

				this.deletedItems.data[reference] = reference;
			}
		}


		initGridModel();

		function initGridModel() {

			$scope.$watch('items', function(items) {
				$("#" + generatedId).clearGridData()
									.setGridParam({data : transformToGridModel(items)})
									.trigger('reloadGrid');
			});

			function transformToGridModel(data) {
				if (data === undefined)
					return;
				var gridModel = new Array(data.length);
				var dataItems = data.items;
				for ( var i = 0; i < dataItems.length; i++) {
					var type = dataItems[i].type;
					gridModel[i] = {};
					gridModel[i].id = type.reference;
					gridModel[i].entityId = type.reference;
					var typeName = type.name;
					if (typeName.nl) {
						gridModel[i].dutchName = typeName.nl[0].text;
						gridModel[i].dutchNameRef = typeName.nl[0].reference;
					}
					if (typeName.fr) {
						gridModel[i].frenchName = typeName.fr[0].text;
						gridModel[i].frenchNameRef = typeName.fr[0].reference;
					}
				}
				return gridModel;
			}
		}


		function notifyParentScopeOfNewSelection(rowData) {
			// assignment below triggers watch on geoselection grid
			if (!rowData) {
				$scope.selectedtype = null;
			} else {
				$scope.selectedtype = {
					gridId : rowData.id,
					entityId : rowData.entityId,
					frenchName : rowData.frenchName,
					dutchName : rowData.dutchName
				};
			}
			$scope.itemsregistry = itemsRegistry;
			$scope.$apply();
		}



		function validate(submitedValue, colname, resp, o) {
			if (!submitedValue)
				return [ false, "Please enter a value for " + colname ];

			var columnValueAlreadyExistsResponse = [ true ];
			var data = $(this).getGridParam("data");
			var colId = "frenchName";
			var currentRow = $scope.selectedtype.gridId; 

			if (colname.indexOf("Dutch") != -1) {
				colId = "dutchName";
			}

			for ( var i = 0; i < data.length ; i++) {
				if (data[i].id == currentRow ) {
					continue;
				}
				if (data[i][colId] == submitedValue) {
					columnValueAlreadyExistsResponse = [ false,
							" already exists for column '" + colname + "' at row " + data[i].id + "!" ];
				}
			}

			return columnValueAlreadyExistsResponse;
		}

		var delSettings = {
			onclickSubmit : function(options) {
				options.processing = true;
				var currentItem = $(this).jqGrid('getLocalRow', lastsel);
				
				itemsRegistry.deleteItem(currentItem.entityId, currentItem.id);
				
				$(this).delRowData(lastsel);
				$.jgrid.hideModal("#delmod" + generatedId);
				notifyParentScopeOfNewSelection(null);
				// enable add button
				$('#' + generatedId + '_add').removeClass("ui-state-disabled");
				return true;
			},
			processing : true
		}

		self.list = function() {
			var geoselectionList = $("#" + generatedId).jqGrid({
				datatype : 'local',
				colNames : [ 'id', 'Geoselection type id', 'Dutch Name', 'French Name', 'No of geoselections', ' ' ],
				colModel : [ {
					name : 'id',
					index : 'id',
					width : 130,
					sorttype : "int",
					align : "left",
					hidden : true
				}, {
					name : 'entityId',
					index : 'entityId',
					width : 130,
					sorttype : "int",
					align : "left"
				}, {
					name : 'dutchName',
					index : 'entityId asc',
					width : 150,
					editable : true,
					editrules : {
						custom : true,
						custom_func : validate
					}
				}, {
					name : 'frenchName',
					index : 'entityId asc ',
					width : 150,
					editable : true,
					editrules : {
						custom : true,
						custom_func : validate
					}
				}, {
					name : 'numberOfGeoselections',
					index : 'entityId asc',
					width : 100,
					align : "right",
					editable : true
				}, {
					name : 'deleteButton',
					width : 40,
					formatter : 'actions',
					formatoptions : {
						keys : true,
						delbutton : true,
						editbutton : false,
						delOptions : delSettings
					}
				} ],
				rowNum : 10,
				sortname : 'id',
				editurl : 'clientArray',
				altRows : true,
				ignoreCase : true,
				pager : "#" + generatedId + "Pager",
				onSelectRow : function(id, status, e) {

					// disable add button
					$('#' + generatedId + '_add').addClass("ui-state-disabled");
					if(lastsel !== undefined){
						$(this).jqGrid('restoreRow', lastsel);
					}
					$(this).jqGrid('editRow', id, {
						keys : true,
						aftersavefunc : function() {
							notifyParentScopeOfNewSelection(rowData);
							// enable add button
							$('#' + generatedId + '_add').removeClass("ui-state-disabled");
							itemsRegistry.updateItem(rowData);
						}
					});
					lastsel = id;
					var rowData = $(this).jqGrid('getLocalRow', id);
					notifyParentScopeOfNewSelection(rowData);
				}
			});

			geoselectionList.navGrid("#" + generatedId + "Pager", {
				edit : false,
				add : false,
				del : false,
				refresh : false,
				search : false
			});

			geoselectionList.navButtonAdd("#" + generatedId + "Pager", {
				caption : "",
				title : "Add new row",
				buttonicon : 'ui-icon-plus',
				onClickButton : function(event) {
					var grid = $("#" + generatedId);
					var newRowId = Math.floor((Math.random() * -1000) + 1);
					grid.jqGrid('addRowData', newRowId, {}, "last");
					grid.jqGrid('setSelection', newRowId, true);
				},
				id : generatedId + "_add"
			});

		}

	}
});