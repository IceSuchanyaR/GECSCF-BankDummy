angular
		.module('gec.registerAccount')
		.controller(
				'registerBankAccountListController',
				[
						'$scope',
						'$stateParams',
						'registerBankAccountListService',
						function($scope, $stateParams,
								registerBankAccountListService) {
							var self = this;
							self.data = [];
							self.total = [];
							self.MasterBankAccount = {};
							self.deleteAccountID = [];
							self.displayAlert = false;
							self.showErrorMessage = {};
							self.showErrorMessageDelete = "";
							$scope.bankAccount = {};
							$scope.params = { itemNo: '10' };
							
							self.getAllBankAccount = function(params) {
								self.displayAlert = false;
								return registerBankAccountListService
										.getAllBankAccount(params).then(
												function(response) {
													self.data = response.data;
												});
							};

							self.getTotalBankAccount = function(params) {
								return registerBankAccountListService
										.getTotalBankAccount(params).then(
												function(response) {
													self.total = response.data;
													
												});
							};

							self.resetBankAccount = function(params) {
								$scope.params = { itemNo: '10' };
								self.getTotalBankAccount();
								return registerBankAccountListService
										.getAllBankAccount(params).then(
												function(response) {
													self.data = response.data;
												});
							};
							
							self.deleteBankAccount = function(accountID) {
								if (accountID != null) {
									return registerBankAccountListService
											.deleteBankAccount(accountID)
											.then(
													function(response) {
														self.MasterBankAccount = response.data;
														if (response.data != null) {
															self
																	.getAllBankAccount();
															self
																	.getTotalBankAccount();
															$scope.params = { itemNo: '10' };

														}
														if ("FAIL" == self.MasterBankAccount.errorMessageStatus) {
															self.showErrorMessageDelete = self.MasterBankAccount.errorMessage;
															self.displayAlert = true;
							
														}

													});
								}
							};							

							self.confirmDeleteBankAccount = function(accountID) {
								self.deleteAccountID = accountID;
								self.displayAlert = false;
							}

							self.getAllBankAccount($scope.params);
							self.getTotalBankAccount($scope.params);
							self.deleteBankAccount();
							
						} ]);
