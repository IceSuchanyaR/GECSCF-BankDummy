
angular
		.module('gec.mappingCode')
		.controller(
				'mappingCodeMgntController',
				[
						'$scope',
						'$stateParams',
						'mappingCodeMgntService',
						function($scope, $stateParams,
								mappingCodeMgntService) {		
							var self = this;
							self.step = "one";
							self.stepOne = stepOne;
							self.showErrorMessage = "";
							self.displayAlertError = false;
							self.displayAlertSuccess = false;
							self.editMode = false;
							self.required = true;
							self.data = [];
							self.total = [];
                            self.configApp = [];
                            self.bankCode = [];
                            self.AllbankCase = [];
                            $scope.status = '';
                            $scope.params = {
                                    delay: 0  };
                            self.alertDuplicate = false;
                            $scope.masterMappingCode = {};
                            
                            self.getTransactionStatus = getTransactionStatus;
                            $scope.unique = [];
							$scope.mappingCodeDetail = [];
							
							 var mappingID = $stateParams.id;
							 
							self.getMappingCodeByMappingID = function(
									stateParams) {
								return mappingCodeMgntService
										.getMappingCodeByMappingID(mappingID)
										.then(
												function(response) {
													$scope.params = response.data;
													$scope.mappingCodeDetail = response.data.masterMappingCodeDetail;
													self.editMode = true;
													self.required = false;
													if (!response.data.mappingID){
														    $scope.mode = 'New';
														} else {
													        $scope.mode = 'Edit';
													    }
													if ($scope.params.mappingID == null) {
														self.editMode = false;
														self.required = true;
													}
												});
							};

							self.stepTwo = function(params) {
								if ($scope.params.mappingID != null) {
									self.step = "two";
									self.required = true;
									return mappingCodeMgntService
									.getMappingCodeByMappingID(mappingID)
									.then(
											function(response) {
												$scope.masterMappingCode = response.data;
												self.editMode = true;
												
											});
								}
								else{
								 return mappingCodeMgntService
									.checkDuplicate(params).then(
											function(response) {
												self.view  = response.data;
												if ("FAIL" == self.view.errorMessageStatus) {
													self.showErrorMessage = self.view.errorMessage;
													self.displayAlertError = true;
													window
													.setTimeout(
															function() {
																$(
																		"#alert-MappingCode")
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
																location.reload();
															},
															5000);
												}
												else{
													 self.step = "two";
													 self.required = true;
												}
											       });	
							}
							 };
							 
							 function stepOne(){
								 self.step = "one";
							 }
                            
                            self.getServiceType = function() {
								return mappingCodeMgntService
								.getServiceType().then(
										function(response) {
											self.configApp  = response.data;
											
										       });
					        };
					        self.getAllMappingCode = function(params) {
								return mappingCodeMgntService
										.getAllMappingCode(params).then(
												function(response) {
													self.data = response.data;
												});
							};
							self.getBankCode = function() {
								return mappingCodeMgntService
								.getBankCode().then(
										function(response) {
											self.bankCode  = response.data;
										       });
					        };
					        self.getMasterBankCase = function(params) {	
								return mappingCodeMgntService
										.getMasterBankCase(params).then(												
												function(response) {	
													 self.AllbankCase = response.data;
												});
							};	
							
							 self.clearMappingCodeDetail = function(){
								 $scope.unique = [];
								 $scope.mappingCodeDetail = [];
							 }
							
							self.submitMappingCode = function(params) {
								self.required = false;
								if (!$scope.mappingCodeDetail || $scope.mappingCodeDetail < 1) {
									self.displayAlertError = true;
									self.showErrorMessage = 'Mapping code can not be empty.';
									throw 'Mapping code can not be empty.'
								};
								$scope.params.masterMappingCodeDetail =  $scope.mappingCodeDetail;
								
								if ($scope.params.mappingID != null) {
									$scope.params.editMode = self.editMode;
								}
								return mappingCodeMgntService
										.submitMappingCode(params)
										.then(function(response) {
											$scope.params = response.data;
											if ($scope.params.mappingID == null) {
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
																		window.location.href = '/#/mapping_code_list';
																	},
																	5000);
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
																	window.location.href = '/#/mapping_code_list';
																},
																5000);
											}
										});
							};
					
							
							function getTransactionStatus(masterMappingCode){
								 return mappingCodeMgntService
									.getTransactionStatus(masterMappingCode).then(
											function(response) {
												$scope.status = response.data;
											});
					        };
					      
					        self.addRow = function () {
					        	self.required = true;
					                var table = {};
								     $scope.masterMappingCode.transactionStatus = $scope.status.bankCaseStatus;
								     $scope.masterMappingCode.bankCaseDisplay =  $scope.status.bankCaseName;
					                table.bankCaseDisplay = $scope.masterMappingCode.bankCaseDisplay;
					                table.transactionStatus = $scope.masterMappingCode.transactionStatus;
					                table.failureReasonCode = $scope.masterMappingCode.failureReasonCode;
					                table.failureReason = $scope.masterMappingCode.failureReason;
					                
					                $scope.mappingCodeDetail =  $scope.mappingCodeDetail || [];
					              
					                $scope.mappingCodeDetail.push(table);
					                
					                $scope.unique = $scope.mappingCodeDetail;
					                
					                var valueArr =  $scope.unique.map(function(item){ return item.bankCaseDisplay });
					                var isDuplicate = valueArr.some(function(item, idx){ 
					                    return valueArr.indexOf(item) != idx 
					                });
					                
					                checkDupicate(isDuplicate);
					                $scope.masterMappingCode = {};
					                $scope.status = {};
					                self.required = false;
					        };
					        
					        
					        function checkDupicate(isDuplicate){
					        	
					        if(isDuplicate){
			                	self.alertDuplicate = true;
                             $scope.mappingCodeDetail = $scope.mappingCodeDetail.filter((set => f => !set.has(f.bankCaseDisplay) && set.add(f.bankCaseDisplay))(new Set));
			                }else{
			                	self.alertDuplicate =false;
			                    }
					        }
					        
					        function alert() {
								$("#alertDuplicate")
								.slideDown(150).delay(3000).slideUp(600);
							}
						
					        self.getMappingCodeByMappingID();
							self.getAllMappingCode();
					    	self.getServiceType();
					    	self.getBankCode();
					    	self.getMasterBankCase();
						} ]);

