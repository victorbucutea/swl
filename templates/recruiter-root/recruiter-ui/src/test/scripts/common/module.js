define(
    [
        'angular',
        'common/controllers/romaZoom',
        'common/directives/romaZoom',
        'common/controllers/namesGrid',
        'common/directives/namesGrid',
        'common/controllers/lazyList',
        'common/directives/lazyList',
        'common/controllers/geoselectionTypeGrid',
        'common/directives/geoselectionTypeGrid',
        'common/controllers/geoselectionGrid',
        'common/directives/geoselectionGrid',
        'common/controllers/postalCodeGrid',
        'common/directives/postalCodeGrid',
        'common/controllers/masterDetailNavigator',
        'common/directives/masterDetailNavigator',
        'common/services/masterDetailService',
        'common/services/searchService',
        'common/controllers/countryConventionSelect',
        'common/directives/countryConventionSelect',
        'common/controllers/areaTypeSelect',
        'common/directives/areaTypeSelect',
        'common/controllers/countrySelect',
        'common/directives/countrySelect',
        'common/controllers/sortingCenterSelect',
        'common/directives/sortingCenterSelect',
        'common/controllers/suffixTypeSelect',
        'common/directives/suffixTypeSelect',
        'common/controllers/municipalitySynonymsGrid',
        'common/directives/municipalitySynonymsGrid',
        'common/controllers/subMunicipalitiesGrid',
        'common/directives/subMunicipalitiesGrid', 
        'common/controllers/fusedMunicipalitiesStreetsGrid',
        'common/directives/fusedMunicipalitiesStreetsGrid', 
        'common/controllers/suffixTypeNSCategorySelect',
        'common/directives/suffixTypeNSCategorySelect',
        'common/controllers/municipalitySelect',
        'common/directives/municipalitySelect',
        'common/controllers/typesCheckList',
        'common/directives/typesCheckList'
    ],
    function(angular, RomaZoomController, romaZoomDirective,
    		NamesGridController, namesGridDirective, 
    		LazyListController, lazyListDirective,
    		GeoselectionTypeGridController, geoselectionTypeGridDirective,
    		GeoselectionGridController, geoselectionGridDirective,
    		PostalCodeGridController, postalCodeGridDirective,
    		MasterDetailNavigatorController, masterDetailNavigatorDirective,
    		MasterDetailService, SearchService, CountryConventionController, CountryConventionDirective,
    		AreaTypeController, AreaTypeDirective, CountryController, CountryDirective, SortingCenterController, SortingCenterDirective,
			SuffixTypeController, SuffixTypeDirective, MunicipalitySynonymsGridController, municipalitySynonymsGridDirective,
			SubMunicipalitiesGridController, subMunicipalitiesGridDirective, FusedMunicipalitiesStreetsGridController, fusedMunicipalitiesStreetsGridDirective, 
			SuffixTypeNSCategoryController, SuffixTypeNSCategoryDirective, MunicipalityController, MunicipalityDirective,
			TypesCheckListController, TypesCheckListDirective) {

        "use strict";
        
        var module = angular.module('common', [])
            .controller('romaZoom', ['$scope', '$http', RomaZoomController])
            .controller('namesGrid', ['$scope', 'countryService', NamesGridController])
            .controller('postalCodeGrid', ['$scope', '$http', PostalCodeGridController])
            .controller('lazyList', ['$scope', '$compile', LazyListController])
            .controller('geoselectionTypeGrid', ['$scope', GeoselectionTypeGridController])        
            .controller('geoselectionGrid', ['$scope', '$http', GeoselectionGridController])
            .controller('masterDetailNavigator', ['$scope', '$http','$compile', MasterDetailNavigatorController])
            .controller('countryConventionSelect', ['$scope', '$http', CountryConventionController])
            .controller('areaTypeSelect', ['$scope', '$http', AreaTypeController])
            .controller('countrySelect', ['$scope', '$http', CountryController])
            .controller('sortingCenterSelect', ['$scope', '$http', SortingCenterController])
            .controller('suffixTypeSelect', ['$scope', '$http', SuffixTypeController])
            .controller('municipalitySynonymsGrid', ['$scope', MunicipalitySynonymsGridController])
            .controller('subMunicipalitiesGrid', ['$scope', SubMunicipalitiesGridController])
            .controller('fusedMunicipalitiesStreetsGrid', ['$scope', FusedMunicipalitiesStreetsGridController])
            .controller('suffixTypeNSCategorySelect', ['$scope', '$http', SuffixTypeNSCategoryController])
            .controller('municipalitySelect', ['$scope', '$http', MunicipalityController])
            .controller('typesCheckList', ['$scope', '$http', TypesCheckListController])
            .directive('romazoom', romaZoomDirective)
            .directive('namesgrid', namesGridDirective)
            .directive('postalcodegrid', postalCodeGridDirective)
            .directive('lazylist', lazyListDirective)
            .directive('geoselectiongrid', geoselectionGridDirective)
            .directive('geoselectiontypegrid', geoselectionTypeGridDirective)
            .directive('masterdetailnavigator',masterDetailNavigatorDirective )
            .directive('countryconventionselect', CountryConventionDirective)
            .directive('areatypeselect', AreaTypeDirective)
            .directive('countryselect', CountryDirective)
            .directive('sortingcenterselect', SortingCenterDirective)
            .directive('suffixtypeselect', SuffixTypeDirective)
            .directive('municipalitysynonyms', municipalitySynonymsGridDirective)
            .directive('submunicipalities', subMunicipalitiesGridDirective)
            .directive('fusedmunicipalitiesstreets', fusedMunicipalitiesStreetsGridDirective)
            .directive('suffixtypenscategoryselect', SuffixTypeNSCategoryDirective)
            .directive('municipalityselect', MunicipalityDirective)
            .directive('typeschecklist', TypesCheckListDirective)
            ;
        

			return {
				module : module,
				MasterDetailService : MasterDetailService,
           		SearchService: SearchService
			};
		});
