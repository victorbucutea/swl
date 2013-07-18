define([], function() {
    "use strict";

    function deleteFormat( cellvalue, options, rowObject ){
        return '<img src="images/trash.gif" onclick="" />';
    }
    
    return function($scope, $element, $http) {
        var self = this;
	
        var updateItems = function(grid, rowid, typeOperation){
			
			var gridIds = grid.getDataIDs();
			
			if(rowid<0){
				var row = grid.getRowData(rowid);
				if(typeOperation=='C'){
					if($scope.items.added_items==undefined){
						$scope.items.added_items = new Array();
					}
					var found = false;
					for ( var i = 0; i < $scope.items.added_items.length; i++) {
						if($scope.items.added_items[i]["reference"] == rowid){
							$scope.items.added_items[i]["firstPostCode"] = row["firstPostCode"];
							$scope.items.added_items[i]["lastPostCode"] = row["lastPostCode"];
							found = true;
						}
					}
					if(!found){
						var length = $scope.items.added_items.length;
						$scope.items.added_items[length] = new Object();
						$scope.items.added_items[length]["reference"] = rowid;
						$scope.items.added_items[length]["firstPostCode"] = row["firstPostCode"];
						$scope.items.added_items[length]["lastPostCode"] = row["lastPostCode"];
					}
				}
				if(typeOperation=='U'){
					for ( var i = 0; i < $scope.items.added_items.length; i++) {
						if($scope.items.added_items[i]["reference"] == rowid){
							$scope.items.added_items[i]["firstPostCode"] = row["firstPostCode"];
							$scope.items.added_items[i]["lastPostCode"] = row["lastPostCode"];
						}
					}
				}
				if(typeOperation=='D'){
					for ( var i = 0; i < $scope.items.length; i++) {
						if($scope.items.added_items[i]["reference"] == rowid){
							$scope.items.added_items.splice(i,1);
						}
					}
				}
			}else{
				var row = grid.getRowData(rowid);
				if(typeOperation=='U'){
					if($scope.items.updated_items==undefined){
						$scope.items.updated_items = new Array();
					}
					var found = false;
					for ( var i = 0; i < $scope.items.updated_items.length; i++) {
						if($scope.items.updated_items[i]["reference"] == rowid){
							$scope.items.updated_items[i]["firstPostCode"] = row["firstPostCode"];
							$scope.items.updated_items[i]["lastPostCode"] = row["lastPostCode"];
							found = true;
						}
					}
					if(!found){
						var length = $scope.items.updated_items.length;
						$scope.items.updated_items[length] = new Object();
						$scope.items.updated_items[length]["reference"] = parseInt(rowid);
						$scope.items.updated_items[length]["firstPostCode"] = row["firstPostCode"];
						$scope.items.updated_items[length]["lastPostCode"] = row["lastPostCode"];
					}
				}
				if(typeOperation=='D'){
					if($scope.items.updated_items!=undefined){
						for ( var i = 0; i < $scope.items.updated_items.length; i++) {
							if($scope.items.updated_items[i]["reference"] == rowid){
								$scope.items.updated_items.splice(i,1);
							}
						}
					}
					if($scope.items.removed_items==undefined){
						$scope.items.removed_items = new Array();
					}
					var length = $scope.items.removed_items.length;
					$scope.items.removed_items[length] = new Object();
					$scope.items.removed_items[length]["reference"] = parseInt(rowid);
					$scope.items.removed_items[length]["firstPostCode"] = row["firstPostCode"];
					$scope.items.removed_items[length]["lastPostCode"] = row["lastPostCode"];
					
				}
			}
			if(!$scope.$$phase) {
				  $scope.$apply();
			}
			//console.log("updated items:"+ JSON.stringify($scope.items));
		}
	
	var afterSaveCallback = function (rowid, response){
		var typeOperation = (rowid<0)?'C':'U';
		var $this = $(this);
		var gridIds = $this.getRowData();
		updateItems($this, rowid, typeOperation);

		return true;
	}
	
	var validate = function (grid, event){
		if(!grid.getGridParam("selrow")){
			return true;
		}
		var nameValidation = validateEmptyRow(grid);
		var uniqueValidation = validateUniqueRow(grid);
		var firstLowerThanValidation = validateFirstLowerThanLastRow(grid);
		var valuesAreNumbersValidation = validateValuesAreNumbersRow(grid);
		var valuesLengthValidation = validateLengthOfPostalCodesRow(grid);
		if(nameValidation[0] && uniqueValidation[0] && firstLowerThanValidation[0] && valuesAreNumbersValidation[0] 
				&& valuesLengthValidation[0]){
			grid.jqGrid("saveRow", $scope.editingRowId, editParam);
			return true;
		}else{
			var errMsg = nameValidation[1]+"<br/>"+uniqueValidation[1]+"<br/>"+firstLowerThanValidation[1]+"<br/>"+valuesAreNumbersValidation[1] 
						+"<br/>"+valuesLengthValidation[1];
		    var parentDiv = grid.parent(); 
		    var parentTop = parentDiv.offset().top;
		    var parentLeft = parentDiv.offset().left;
			$.jgrid.info_dialog($.jgrid.errors.errcap,errMsg,$.jgrid.edit.bClose, {left:parentLeft, top:parentTop});
			grid.jqGrid('setSelection', grid.getGridParam("selrow"), true);	
			grid.editingRowId = grid.getGridParam("selrow");
			grid.jqGrid("editRow", $scope.editingRowId, editParam);	
			event.stopPropagation();
			return false;
		}
	}
	
	var onBlurGrid = function (e, gridId){
		if ( $scope.editingRowId ) { 
			if($(e.target).closest(gridId).length == 0) { 
				if(validate($(gridId), e)){
					$(gridId).jqGrid("resetSelection");
					$scope.editingRowId = undefined;
				}
			} 
		} 
	}
	
	var validateEmptyRow = function (grid){
		if($scope.editingRowId){
			var clModel = grid.getGridParam("colModel");
			for(var i=0;i<clModel.length;i++){
				if(!$("#"+$scope.editingRowId+"_"+clModel[i].index).val()){
					return [false,"Please fill in the First and Last postal code fields"];
				}
			}
		}
		return [true, ""];
	}
	
	var validateUniqueRow = function (grid){
		if($scope.editingRowId){
			var gridIds = grid.getDataIDs();
			var newFirstPostCodeValue = $("#"+$scope.editingRowId+"_firstPostCode").val();
			var newLastPostCodeValue = $("#"+$scope.editingRowId+"_lastPostCode").val();
			for(var i=0;i<gridIds.length;i++){
				if(gridIds[i]!=$scope.editingRowId){
					var row = grid.getRowData(gridIds[i]);
					var firstPostCodeValue = row["firstPostCode"];
					var lastPostCodeValue = row["lastPostCode"];
					if(newFirstPostCodeValue == firstPostCodeValue && newLastPostCodeValue == lastPostCodeValue){
						return [false,"Duplicated record"];
					}
				}
			}
		}
		return [true, ""];
	}
	
	var validateFirstLowerThanLastRow = function (grid){
		if($scope.editingRowId){
			var firstPostCodeValue = $("#"+$scope.editingRowId+"_firstPostCode").val();
			var lastPostCodeValue = $("#"+$scope.editingRowId+"_lastPostCode").val();
			if (parseInt(firstPostCodeValue) > parseInt(lastPostCodeValue)) {
				return [false,"First postal code must be lower than Last postal code"];
			}
		}
		return [true, ""];
	}
	
	var validateLengthOfPostalCodesRow = function (grid){
		if($scope.editingRowId){
			var firstPostCodeValue = $("#"+$scope.editingRowId+"_firstPostCode").val();
			var lastPostCodeValue = $("#"+$scope.editingRowId+"_lastPostCode").val();
			if (firstPostCodeValue.length > 6 || lastPostCodeValue.length > 6) {
				return [false,"Postal codes must be of maximum 6 characters"];
			}
		}
		return [true, ""];
	}
	
	var validateValuesAreNumbersRow = function (grid){
		if($scope.editingRowId){
			var firstPostCodeValue = $("#"+$scope.editingRowId+"_firstPostCode").val();
			var lastPostCodeValue = $("#"+$scope.editingRowId+"_lastPostCode").val();
			if (isNaN(firstPostCodeValue) || isNaN(lastPostCodeValue)) {
				return [false,"Postal codes must be numbers"];
			}
		}
		return [true, ""];
	}
	
	var editParam = {
	        keys: false,
	        oneditfunc: function (id) { $scope.editingRowId = id; },
	        afterrestorefunc: function (id) {$scope.editingRowId = undefined; },
	        url: 'clientArray',
	        reloadAfterSubmit:false,
	        aftersavefunc : afterSaveCallback
	};

        self.list = function() {
            $scope.editingRowId = undefined;
            
            var generatedId = $.jgrid.randId();
            $scope.gridId = generatedId;
			$("#postalCodeList").attr("id",generatedId);
			$("#postalCodePager").attr("id",generatedId+"Pager");
			
            // jqgrid configuration
            var postalCodeList = $("#"+generatedId).jqGrid({
            	data: $scope.items.data,
                datatype: "local",
                height: "100",
                width: "auto",
                rowNum: 5,
                colNames: ["First postal code", "Last postal code"],
                colModel:[
                    {name:'firstPostCode',index:'firstPostCode', width:"130", sorttype:"string", align:"left", editable: true},
                    {name:'lastPostCode',index:'lastPostCode', width:"130", sorttype:"string", align:"left", editable: true}
                ],
                sortname: "firstPostCode",
                sortorder: "asc",
                viewrecords: false,
                altRows: true,
                ignoreCase: true,
				pager: "#"+generatedId+"Pager",
				editurl: 'clientArray',
				localReader: {
					id:"reference"
				},
				onSelectRow: function(id){
						var $this = $(this);
						if (id && $scope.editingRowId != id) {
							$this.jqGrid("editRow", id, editParam);	
						}
						$("#"+generatedId+"_ildelete").removeClass("ui-state-disabled");
						return true;
				},
				beforeSelectRow: function(id, event){
					if(id && $scope.editingRowId != id){
						var $this = $(this);
						return validate($this, event);
					}
					return true;
				}
								
            });
			postalCodeList.navGrid( "#"+generatedId+"Pager",{ edit:false,add:false,del:false,refresh:false,search:false }); 
			postalCodeList.inlineNav( "#"+generatedId+"Pager", {edit: false, add:false, save:false, cancel:false, restoreAfterSelect: false});
			
			$( "#"+generatedId+"Pager_center").remove();
			$( "#"+generatedId+"Pager_right").remove();
						
			postalCodeList.navButtonAdd( "#"+generatedId+"Pager",
									{caption:"",
									title:"Delete selected row", 
									buttonicon :'ui-icon-trash', 
									onClickButton:function(event){
												if($scope.editingRowId){
													$("#"+generatedId).jqGrid("delRowData", $scope.editingRowId, {reloadAfterSubmit:false});
													updateItems($("#" + generatedId), $scope.editingRowId, 'D');
													$("#"+generatedId).jqGrid("resetSelection");
													$scope.editingRowId = undefined;
												}
												$("#"+generatedId+"_ildelete").addClass("ui-state-disabled");
											},
									id: generatedId+"_ildelete"
								}); 
			postalCodeList.navButtonAdd( "#"+generatedId+"Pager",
									{caption:"",
									title:"Add new row", 
									buttonicon :'ui-icon-plus', 
									onClickButton:function(event){ 
										
										if(validate($("#"+generatedId), event)){
												var newRowId = Math.floor((Math.random()*-1000)+1);
												$("#"+generatedId).jqGrid('addRowData', newRowId, {}, "first");
												$("#"+generatedId).jqGrid('setSelection', newRowId, true);	
										}else{
											return false;
										}
									},
									id: generatedId+"_iladd"
								});
			
			$('body').bind('click', function(e) { 
				onBlurGrid(e, "#"+generatedId);
			});
					
			
			
			$("#"+generatedId+"_ildelete").addClass("ui-state-disabled");
			
			$scope.$watch('items.data', function(newItems, oldItems) {
				if(newItems !== oldItems){
					$("#" + $scope.gridId).jqGrid('clearGridData').jqGrid(
							'setGridParam', {
								data : newItems
							}).trigger('reloadGrid');
					$scope.editingRowId = undefined;
				}						
			});
		};
			
			$scope.list = self.list;
        
    };
});