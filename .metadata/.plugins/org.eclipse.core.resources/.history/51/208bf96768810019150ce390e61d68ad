angular
		.module("gec.mappingException")
		.factory(
				"mappingExceptionViewService",
				[
						'$http',
						function($http) {

							
							var getMappingExceptionByExceptionID = function(exceptionID) {
								return $http({
									method : "GET",
									url : "/mappingException/mappingExceptionView",
									params : {exceptionID:exceptionID}
								});
							};
							
//							var getTotalMappingCodeDetail = function(mappingID) {
//								return $http({
//									method : "GET",
//									url : "/mappingCode/totalMappingCodeDetail",
//									params : {mappingID:mappingID}
//								});
//							};

							return {
								
								getMappingExceptionByExceptionID: getMappingExceptionByExceptionID
//								getTotalMappingCodeDetail: getTotalMappingCodeDetail
							}
						} ]);