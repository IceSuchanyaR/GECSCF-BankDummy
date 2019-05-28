angular
	.module('gec.registerAccount')
	.controller(
		'registerBankAccountMgntController',
		[
			'$scope',
			'$stateParams',
			'registerBankAccountMgntService',
			function ($scope, $stateParams,
				registerBankAccountMgntService) {
				var self = this;

				$scope.mode = "";
				self.showErrorMessage = "";
				self.displayAlertError = false;
				self.displayAlertSuccess = false;
				self.view = {};
				self.configApp = [];
//				self.currencyCode = [];
				self.editMode = false;
				$scope.params = {};
				$scope.params.accountStatus = 'ACTIVE'
				self.clearchk = clearchk;



				var accountID = $stateParams.id;

				self.currencymask = function () {
					$('.money').mask("#,##0.00", {
						reverse: true
					});
				};

				self.getBankAccountByAccountID = function (
					stateParams) {
					return registerBankAccountMgntService
						.getBankAccountByAccountID(accountID)
						.then(
							function (response) {
								$scope.params = response.data;
								self.editMode = true;
								$scope.mode = "Edit";
								if ($scope.params.accountID == null) {
									self.editMode = false;
									$scope.mode = "New";
									$scope.params.accountStatus = 'ACTIVE';

								}
							});
				};

				function clearchk() {
					// Check for Reset Value
					if ($scope.params.accountType == null) {
						$scope.params.currencyCode = '';
						$scope.params.ledgerBalance = '';
						$scope.params.creditLimit = '';
					}
					if ("CURRENT" == $scope.params.accountType
						|| "SAVING" == $scope.params.accountType) {
						$scope.params.creditLimit = '';
						$scope.params.customerCreditLimit = false;
					}
					if ("TERM_LOAN" == $scope.params.accountType) {
						$scope.params.ledgerBalance = '';
					}

					if ($scope.params.accountType == 'CURRENT'
						|| $scope.params.accountType == 'SAVING'
						|| $scope.params.accountType == ''
						|| $scope.params.accountType == null) {
						$scope.params.customerCreditLimit = false;

					}
				};

				self.getAccountType = function () {
					return registerBankAccountMgntService
						.getAccountType()
						.then(function (response) {
							self.configApp = response.data;
						});
				};
				
//				self.getCurrencyCode = function () {
//					return registerBankAccountMgntService
//						.getCurrencyCode()
//						.then(function (response) {
//							self.currencyCode = response.data;
//						});
//				};

				self.submitBankAccount = function (params) {
					console.log(params);
					if ($scope.params.accountNo != null) {
						$scope.params.editMode = self.editMode;
						return registerBankAccountMgntService
							.submitBankAccount(params)
							.then(
								function (response) {
									$scope.params = response.data;
									if ($scope.params.accountID == null) {
										self.editMode = false;
										$scope.params.accountStatus = 'ACTIVE';
										if ("FAIL" == $scope.params.errorMessageStatus) {
											self.showErrorMessage = $scope.params.errorMessage;
											self.displayAlertError = true;
											window
												.setTimeout(
													function () {
														$(
															".alert-success")
															.fadeTo(
																500,
																0)
															.slideUp(
																500,
																function () {
																	$(
																		this)
																		.remove();
																});
														window.location.href = '/#/register_account_list';
													},
													3000);
										}
									} else {
										self.displayAlertError = false;
										self.displayAlertSuccess = true;
										window
											.setTimeout(
												function () {
													$(
														".alert-success")
														.fadeTo(
															500,
															0)
														.slideUp(
															500,
															function () {
																$(
																	this)
																	.remove();
															});
													window.location.href = '/#/register_account_list';
												},
												3000);
									}
								});
					}
				};
				self.getBankAccountByAccountID();
				self.getAccountType();
//				self.getCurrencyCode();
				$scope.onChangeValueCurrency = function() {
					if($scope.params.ledgerBalance && $scope.params.ledgerBalance.toString().length > 15) 
						$scope.params.ledgerBalance = parseInt($scope.params.ledgerBalance.toString().slice(0,15));
					if($scope.params.creditLimit && $scope.params.creditLimit.toString().length > 15) 
						$scope.params.creditLimit = parseInt($scope.params.creditLimit.toString().slice(0,15));
				}
			}]);
