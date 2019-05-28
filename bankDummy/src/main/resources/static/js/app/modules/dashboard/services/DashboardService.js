angular.module("gec.dashboard").factory("DashboardService", [
	'$http', function ($http) {

		function getProfile() {
			return $http({
				method: "GET",
				url: "/api/profile",
			});
		}
		function getPrivileges() {
			return $http({
				method: "GET",
				url: "/api/privileges",
			});
		}

		function enquiryAccountBalance() {
			return $http({
				method: "GET",
				url: "/v1/banks/DFgr/direct-debit-accounts/1A0EWBX3Ul9l9V",
			});
		}

		function enquiryCreditLimit() {
			return $http({
				method: "GET",
				url: "/v1/banks/DFgr/term-loan-accounts/1A0EWBX3Ul9l9V",
			});
		}

		function directDebit() {
			return $http({
				method: "POST",
				url: "/v1/banks/DFgr/direct-debit-transactions",
				data: {
					"transaction_no": "3",
					"source_account_no": "2222222222",
					"destination_account_no": "5555555555",
					"transaction_date": "2019-11-12",
					"transaction_amount": "10",
					"currency_code": "THB"
				},
			});
		}

		function enquiryDirectDebit() {
			return $http({
				method: "GET",
				url: "/v1/banks/DFgr/direct-debit-transactions/p",
			});
		}

		function drawdrow() {
			return $http({
				method: "POST",
				url: "/v1/banks/DFgr/drawdown-transactions",
				data: {
					"transaction_no": "111",
					"sponsor_code": "01",
					"source_account_no": "6666666666",
					"destination_account_no": "7777777777",
					"transaction_date": "2019-11-12",
					"maturity_date": "2019-12-12",
					"document_amount": "100.00",
					"finance_percent": "100",
					"transaction_amount": "30",
					"currency_code": "THB"
				},
			});
		}


		function enquiryDrawdown() {
			return $http({
				method: "GET",
				url: "/v1/banks/DFgr/drawdown-transactions/DWfh",
			});
		}

		function runTestCase() {
//            console.log(enquiryAccountBalance());
//			console.log(enquiryCreditLimit());
//			console.log(enquiryDirectDebit());
			console.log(enquiryDrawdown());
//			console.log(getPrivileges());
//			console.log(getProfile());
//			console.log(drawdrow());
//		    console.log(directDebit());

		}

		return {
			getProfile: getProfile,
			getPrivileges: getPrivileges,
			enquiryAccountBalance: enquiryAccountBalance,
			runTestCase: runTestCase
		};
	}]
);