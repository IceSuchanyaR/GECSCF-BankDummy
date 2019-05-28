angular
		.module("gec.registerAccount")
		.factory(
				"registerBankAccountViewService",
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

							return {
								
							   getBankAccountByAccountID: getBankAccountByAccountID
							}
						} ]);