angular.module('gec.dashboard').controller('DashboardController',
	['$scope', '$stateParams', 'DashboardService', function ($scope, $stateParams, DashboardService) {
		var self = this;

		self.modules = [
			{
				title: 'Sponsors',
				open: true,
				list: [{ title: 'Sponsors', uri: 'sponsors' },]
			},
			{
				title: 'Bank',
				open: true,
				list: [
					{ title: 'Register Account', uri: 'register_account_list' },
					{ title: 'Mapping Code', uri: 'mapping_code_list' },
					{ title: 'Mapping Exception List', uri: 'mapping_exception_list' },
					{ title: 'Log Transaction', uri: 'log_transaction_list' },
				]
			}
		];

		DashboardService.runTestCase();

		self.onSelectModule = function (module) {
			$scope.$parent.modules = [module];
		};
	}]
);