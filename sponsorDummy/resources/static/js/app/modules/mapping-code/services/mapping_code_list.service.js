angular
		.module("gec.mappingCode")
		.factory(
				"mappingCodeListService",
				[
						'$http',
						function($http) {

							var getAllMappingCode = function(params) {
								return $http({
									method : "GET",
									url : "/mappingCode/mappingCodeList",
									params : params
								});
							};
							var getTotalMappingCode = function(params) {
								return $http({
									method : "GET",
									url : "/mappingCode/totalMappingCode",
									params : params
								});
							};
							
							var deleteMappingCode = function(mappingID) {
							return $http({
								method : "DELETE",
								url : "/mappingCode/deleteMappingCode",
								params : {mappingID:mappingID}
							});
						};
							
							var getAllServiceType = function() {
								return $http({
									method : "GET",
									url : "/mappingCode/getServiceType",
									
								});
							};
							
							
							

							return {
								getAllMappingCode : getAllMappingCode,
								getTotalMappingCode: getTotalMappingCode,
								getAllServiceType :getAllServiceType,
								deleteMappingCode: deleteMappingCode
							   
							}
						} ]);