define([], function() {
	"use strict";
	return function($scope,$http,$q) {
		var self = this;
		$scope.isVisible = false;
		var geoselectionId = $.jgrid.randId();
		$("#geoselectionList").attr("id", geoselectionId);
		$("#geoselectionPager").attr("id", geoselectionId + "Pager");

		var geoselectionData = [ {
			name : "Liege - Luxemburg",
			linkedToAProduction : "Yes"
		}, {
			name : "Antwerpen - Linburg ",
			linkedToAProduction : "Yes"
		} ];
		
		var geoselectionData2 = [ {
			name : "Liege2 - Luxemburg2",
			linkedToAProduction : "Yes2"
		}, {
			name : "Antwerpen2 - Linburg2 ",
			linkedToAProduction : "Yes2"
		} ];
		

		self.list = function() {
			var geoselectionList = $("#" + geoselectionId).jqGrid({
				data : geoselectionData,
				datatype : "local",
				height : "100",
				width : "310",
				colNames : [ 'Name', 'Linked To a production' ],
				colModel : [ {name : 'name',index : 'name',width : 220,sorttype : "int",align : "left"}, 
				             {name : 'linkedToAProduction',index : 'linkedToAProduction',width : 80} ],
				rowNum : 5,
				sortname : 'id',
				editurl : 'clientArray',
				altRows : true,
				ignoreCase : true
			});
		}

		$scope.$watch('selectedtype', function() {
			var selectedType = $scope.selectedtype;
			
			if ( !selectedType )
				return;
			
			////////dummy! remove when geoselection specs are available//////////////
			console.log('watch intercepted for gridid '+selectedType.gridId +' entity id '+selectedType.entityId+
					', frenchname '+selectedType.frenchName + ', dutchName '+selectedType.dutchName);
			
			geoselectionData2[0].name= geoselectionData2[0].name + selectedType.entityId;
			geoselectionData2[0].linkedToAProduction = geoselectionData2[0].linkedToAProduction + selectedType.gridId;
			geoselectionData2[1].name= geoselectionData2[1].name + selectedType.entityId;
			geoselectionData2[1].linkedToAProduction = geoselectionData2[1].linkedToAProduction + selectedType.gridId;
			/////////////////////////////////////////////////////////////////////////
			
			$("#" + geoselectionId).jqGrid('clearGridData')
								   .jqGrid('setGridParam', {data : geoselectionData2})
								   .trigger('reloadGrid');
		});
	}
});