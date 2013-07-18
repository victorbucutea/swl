define(
    [
        'angular',
        'geographical/features/countryDetailFeature',
        'geographical/features/cityDetailFeature',
        'geographical/services/countryService',
        'geographical/services/cityService',
        'geographical/services/geoselectionService',
        'common/module',
        'geographical/features/geoSelectionDetailFeature',
        'geographical/features/areaTypeDetailFeature',
        'geographical/features/areaDetailFeature',
        'geographical/services/areaService',
        'geographical/features/physicalPointTypeDetailFeature',
        'geographical/services/physicalPointTypeService',
        'geographical/features/fusedMunicipalityDetailFeature', 
        'geographical/features/subMunicipalityDetailFeature', 
        'geographical/services/municipalityService',
        'geographical/services/postalLocationService',
        'geographical/services/suffixTypeService', 
        'geographical/features/suffixTypeDetailFeature',
        'geographical/features/streetDetailFeature',
        'geographical/services/streetService'
    ],
 function(angular, countryDetail, cityDetail, /*streetDetail,*/ CountryService, CityService, GeoselectionService, common, geoSelectionDetail, 
		 areaTypeDetail, areaDetail, AreaService, physicalPointTypeDetail, PhysicalPointTypeService, fusedMunicipalityDetailFeature, subMunicipalityDetailFeature, MunicipalityService,
		 PostalLocationService, SuffixTypeService, suffixTypeDetail, streetDetail, StreetService) {
        "use strict";

        var module = angular.module('geographical', [])
         	.service('countryService', ['$rootScope', '$resource', CountryService])
         	.service('cityService', ['$rootScope', '$resource', CityService])
        	.service('geoselectionService',['$rootScope','$resource', '$q','$http', GeoselectionService])
        	.service('areaService',['$rootScope','$resource', AreaService])
        	.service('physicalPointTypeService',['$rootScope','$resource', PhysicalPointTypeService])
            .service('municipalityService',['$rootScope','$resource', MunicipalityService])
            .service('postalLocationService',['$rootScope','$resource', PostalLocationService])
            .service('suffixTypeService',['$rootScope','$resource', SuffixTypeService])
        	.service('streetService',['$rootScope','$resource', StreetService])
            .config(
            [
                '$routeProvider',
                function($routeProvider) {
                    $routeProvider.when('/geographical', {templateUrl: 'html/geographical/layout/home.html'});	  
                }
            ]);

        countryDetail.isFeatureOf(module);
        cityDetail.isFeatureOf(module);
        geoSelectionDetail.isFeatureOf(module);
        areaTypeDetail.isFeatureOf(module);
        areaDetail.isFeatureOf(module);
        physicalPointTypeDetail.isFeatureOf(module);
        fusedMunicipalityDetailFeature.isFeatureOf(module);
        subMunicipalityDetailFeature.isFeatureOf(module);
        suffixTypeDetail.isFeatureOf(module);
        streetDetail.isFeatureOf(module);
        
        return {
            module: module
        };
    });