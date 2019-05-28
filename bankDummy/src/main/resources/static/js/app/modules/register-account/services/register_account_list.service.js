angular
		.module("gec.registerAccount")
		.factory(
				"registerBankAccountListService",
				[
						'$http',
						function($http) {

							var getAllBankAccount = function(params) {
								return $http({
									method : "GET",
									url : "/registerBankAccount/bankAccountList",
									params : params
								});
							};
							var getTotalBankAccount = function(params) {
								return $http({
									method : "GET",
									url : "/registerBankAccount/totalBankAccount",
									params : params
								});
							};
							var deleteBankAccount = function(accountID) {
								return $http({
									method : "DELETE",
									url : "/registerBankAccount/deleteBankAccount",
									params : {accountID:accountID}
								});
							};
							

							return {
							   getAllBankAccount : getAllBankAccount,
							   getTotalBankAccount: getTotalBankAccount,
							   deleteBankAccount: deleteBankAccount
							   
							}
						} ]);