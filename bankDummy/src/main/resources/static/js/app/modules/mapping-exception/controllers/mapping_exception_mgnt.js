angular
		.module('gec.mappingException')
		.controller(
				'mappingExceptionMgntController',
				[
						'$scope',
						'$stateParams',
						'mappingExceptionMgntService',
						function($scope, $stateParams,
								mappingExceptionMgntService) {
							var self = this;
							$scope.mode = "";
							self.showErrorMessage = "";
							self.displayAlertError = false;
							self.displayAlertSuccess = false;
							self.view = {};
							self.configApp = [];
							self.editMode = false;
							$scope.params = {};
							

							var exceptionID = $stateParams.id;

							self.getAllServiceType = function() {
								
								return mappingExceptionMgntService
								.getAllServiceType().then(
										function(response) {
											self.configApp  = response.data;
											
										       });
					        };
					        

							self.getMappingExceptionByExceptionID = function(
									stateParams) {
								return mappingExceptionMgntService
										.getMappingExceptionByExceptionID(exceptionID)
										.then(
												function(response) {
													$scope.params = response.data;
													self.editMode = true;
													$scope.mode = "Edit";
													if ($scope.params.exceptionID == null) {
														self.editMode = false;
														$scope.mode = "New";
														$scope.params.transactionStatus = 'SUCCESS'
														$scope.params.status = 'AC'
													}
												});
							};

						   

							self.submitMappingException = function(params) {
								console.log(params);
								if ($scope.params.accountNo != null) {
									$scope.params.editMode = self.editMode;
									return mappingExceptionMgntService
											.submitMappingException(params)
											.then(
													function(response) {
														$scope.params = response.data;
														if ($scope.params.exceptionID == null) {
															self.editMode = false;
															if ("FAIL" == $scope.params.errorMessageStatus) {
																self.showErrorMessage = $scope.params.errorMessage;
																self.displayAlertError = true;
																window
																		.setTimeout(
																				function() {
																					$(
																							".alert-success")
																							.fadeTo(
																									500,
																									0)
																							.slideUp(
																									500,
																									function() {
																										$(
																												this)
																												.remove();
																									});
																					window.location.href = '/#/mapping_exception_list';
																				},
																				3000);
															}
														} else {
															self.displayAlertError = false;
															self.displayAlertSuccess = true;
															window
																	.setTimeout(
																			function() {
																				$(
																						".alert-success")
																						.fadeTo(
																								500,
																								0)
																						.slideUp(
																								500,
																								function() {
																									$(
																											this)
																											.remove();
																								});
																				window.location.href = '/#/mapping_exception_list';
																			},
																			3000);
														}
													});
								}
							};
							self.getAllServiceType();
							self.getMappingExceptionByExceptionID();
						} ]);
