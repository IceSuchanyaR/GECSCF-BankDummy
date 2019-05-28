angular
		.module('gec.mappingException')
		.controller(
				'mappingExceptionListController',
				[
						'$scope',
						'$stateParams',
						'mappingExceptionListService',
						function($scope, $stateParams,
								mappingExceptionListService) {
							var self = this;
							self.data = [];
							self.total = [];
							self.configApp = [];
							self.MasterMappingCode = {};
							self.deleteExceptionID = [];
							self.displayAlert = false;
							self.showErrorMessage = {};
							self.showErrorMessageDelete = "";
							$scope.params = { itemNo: '10' };
							
							self.getAllMappingException = function(params) {
								self.displayAlert = false;
								return mappingExceptionListService
										.getAllMappingException(params).then(
												function(response) {
													self.data = response.data;
												});
							};
						
							self.resetMappingException = function(params) {
								$scope.params = { itemNo: '10' };
								self.getTotalException();
								return mappingExceptionListService
										.getAllMappingException(params).then(
												function(response) {
													self.data = response.data;
												});
							};
							
							self.getTotalException = function(params) {
								return mappingExceptionListService
										.getTotalException(params).then(
												function(response) {
													self.total = response.data;
												});
							};
  
							
							self.getAllServiceType = function() {
							
								return mappingExceptionListService
								.getAllServiceType().then(
										function(response) {
											self.configApp  = response.data;
											
										       });
					        };
							self.deleteMappingException = function(exceptionID) {
								if (exceptionID != null) {
									return mappingExceptionListService
											.deleteMappingException(exceptionID)
											.then(
													function(response) {
														self.MasterMappingCode = response.data;
														if (response.data != null) {
															self
																	.getAllMappingException();
															self
																	.getTotalException();
															$scope.params = { itemNo: '10' };

														}

													});
								}
							};

							self.confirmDeleteMappingException = function(exceptionID) {
								self.deleteExceptionID = exceptionID;
								self.displayAlert = false;
							}

							self.getAllMappingException($scope.params);
							self.getTotalException($scope.params);
							self.getAllServiceType($scope.params);
							self.deleteMappingException();
						} ]);
