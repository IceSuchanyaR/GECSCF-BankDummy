angular
		.module("gec.mappingException")
		.factory(
				"mappingExceptionListService",
				[
						'$http',
						function($http) {

							var getAllMappingException = function(params) {
								return $http({
									method : "GET",
									url : "/mappingException/mappingExceptionList",
									params : params
								});
							};
							var getTotalException = function(params) {
								return $http({
									method : "GET",
									url : "/mappingException/totalMappingException",
									params : params
								});
							};
							
							var deleteMappingException = function(exceptionID) {
							return $http({
								method : "DELETE",
								url : "/mappingException/deleteMappingException",
								params : {exceptionID:exceptionID}
							});
						};
							
							var getAllServiceType = function() {
								return $http({
									method : "GET",
									url : "/mappingException/getServiceType",
									
								});
							};
							
							
							

							return {
								getAllMappingException : getAllMappingException,
								getTotalException: getTotalException,
								getAllServiceType :getAllServiceType,
								deleteMappingException: deleteMappingException
							   
							}
						} ]);