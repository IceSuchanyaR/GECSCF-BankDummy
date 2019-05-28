angular
		.module("gec.registerAccount")
		.factory(
				"registerBankAccountMgntService",
				[
						'$http',
						function($http) {

							
							var getBankAccountByAccountID = function(accountID) {
								return $http({
									method : "GET",
									url : "/registerBankAccount/bankAccountView",
									params : {accountID:accountID}
								});
							};
							
							var submitBankAccount = function(masterBankAccount) {
								return $http({
									method : "POST",
									url : "/registerBankAccount/manageBankAccount",
									data : masterBankAccount
								});
							};
							
							var getAccountType = function() {
                                return $http({
                                    method : "GET",
                                    url : "/registerBankAccount/getAccountType"
                                    
                                });
                            };
                            
//                            var getCurrencyCode = function() {
//                                return $http({
//                                    method : "GET",
//                                    url : "/registerBankAccount/getCurrencyCode"
//                                    
//                                });
//                            };

							return {
								
							   getBankAccountByAccountID: getBankAccountByAccountID,
							   submitBankAccount: submitBankAccount,
							   getAccountType : getAccountType
//							   getCurrencyCode: getCurrencyCode
							}
						} ]);