angular
		.module('gec.mappingCode')
		.controller(
				'mappingCodeListController',
				[
						'$scope',
						'$stateParams',
						'mappingCodeListService',
						function($scope, $stateParams,
								mappingCodeListService) {
							var self = this;
							self.data = [];
							self.total = [];
							self.configApp = [];
							self.MasterMappingCode = {};
							self.deleteMappingID = [];
							self.displayAlert = false;
							self.showErrorMessage = {};
							self.showErrorMessageDelete = "";
							$scope.params = { itemNo: '10' };
							
							self.getAllMappingCode = function(params) {
								self.displayAlert = false;
								return mappingCodeListService
										.getAllMappingCode(params).then(
												function(response) {
													self.data = response.data;
												});
							};
						
							self.resetMappingCode = function(params) {
								console.log(params);
								$scope.params = { itemNo: '10' };
								self.getTotalMappingCode();
								return mappingCodeListService
										.getAllMappingCode(params).then(
												function(response) {
													self.data = response.data;
												});
							};
							
							self.getTotalMappingCode = function(params) {
								return mappingCodeListService
										.getTotalMappingCode(params).then(
												function(response) {
													self.total = response.data;
												});
							};
  
							self.getAllServiceType = function() {
								return mappingCodeListService
								.getAllServiceType().then(
										function(response) {
											self.configApp  = response.data;
											
										       });
					        };
							self.deleteMappingCode = function(mappingID) {
								if (mappingID != null) {
									return mappingCodeListService
											.deleteMappingCode(mappingID)
											.then(
													function(response) {
														self.MasterMappingCode = response.data;
														if (response.data != null) {
															self
																	.getAllMappingCode();
															self
																	.getTotalMappingCode();
															$scope.params = { itemNo: '10' };

														}
														if ("FAIL" == self.MasterMappingCode.errorMessageStatus) {
															self.showErrorMessageDelete = self.MasterMappingCode.errorMessage;
															self.displayAlert = true;
							
														}

													});
								}
							};

							self.confirmDeleteMappingCode = function(mappingID) {
								self.deleteMappingID = mappingID;
								self.displayAlert = false;
							}

							self.getAllMappingCode($scope.params);
							self.getTotalMappingCode($scope.params);
							self.getAllServiceType($scope.params);
							self.deleteMappingCode();
						} ]);
