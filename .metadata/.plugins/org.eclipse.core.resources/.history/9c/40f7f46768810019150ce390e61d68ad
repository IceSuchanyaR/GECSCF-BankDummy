angular
		.module("gec.mappingCode")
		.factory(
				"mappingCodeMgntService",
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
							var getMasterBankCase = function(params) {	
								return $http({
									method : "GET",
									url : "/mappingCode/getMasterBankCase",
									params : params
								});
							};
							var getServiceType = function() {
								return $http({
									method : "GET",
									url : "/mappingCode/getServiceType"									
								});
							};
							
							var checkDuplicate = function(params) {
								return $http({
									method : "GET",
									url : "/mappingCode/checkDuplicate",
									params : params
								});
							};							
							
							var submitMappingCode = function(masterMappingCode) {
								return $http({
									method : "POST",
									url : "/mappingCode/manageMappingCode",
									data : masterMappingCode
								});
							};
							
							var getBankCode = function() {
								return $http({
									method : "GET",
									url : "/mappingCode/getBankCode"
								});
							};
							
							var getTransactionStatus = function(params) {
								return $http({
									method : "GET",
									url : "/mappingCode/getTransactionStatus",
									params : params
								});
							};
							
							var getMappingCodeByMappingID = function(mappingID) {
								return $http({
									method : "GET",
									url : "/mappingCode/mappingCodeView",
									params : {mappingID:mappingID}
								});
							};
							
							return {
								getMappingCodeByMappingID : getMappingCodeByMappingID,
								getAllMappingCode : getAllMappingCode,
								getTotalMappingCode: getTotalMappingCode,
								getServiceType :getServiceType,
								getBankCode : getBankCode,
								getMasterBankCase: getMasterBankCase,
								checkDuplicate : checkDuplicate,
								getTransactionStatus : getTransactionStatus,
								submitMappingCode : submitMappingCode
							}
						} ]);