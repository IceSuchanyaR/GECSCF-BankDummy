angular
		.module("gec.mappingException")
		.factory(
				"mappingExceptionMgntService",
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
							
							var submitMappingException = function(mappingException) {
								return $http({
									method : "POST",
									url : "/mappingException/manageMappingException",
									data : mappingException
								});
							};
							
                            var getAllServiceType = function() {
								return $http({
									method : "GET",
									url : "/mappingException/getServiceType",
									
								});
							};

							return {
								
								getMappingExceptionByExceptionID: getMappingExceptionByExceptionID,
								submitMappingException: submitMappingException,
							   getAllServiceType : getAllServiceType
							}
						} ]);