angular
		.module("gec.mappingCode")
		.factory(
				"mappingCodeViewService",
				[
						'$http',
						function($http) {

							
							var getMappingCodeByMappingID = function(mappingID) {
								return $http({
									method : "GET",
									url : "/mappingCode/mappingCodeView",
									params : {mappingID:mappingID}
								});
							};
							
							var getTotalMappingCodeDetail = function(mappingID) {
								return $http({
									method : "GET",
									url : "/mappingCode/totalMappingCodeDetail",
									params : {mappingID:mappingID}
								});
							};

							return {
								
								getMappingCodeByMappingID: getMappingCodeByMappingID,
								getTotalMappingCodeDetail: getTotalMappingCodeDetail
							}
						} ]);