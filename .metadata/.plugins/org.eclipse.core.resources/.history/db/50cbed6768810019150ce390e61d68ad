'use strict'
angular.module('gec.dashboard', ['ui.router', 'gec.ui', 'oc.lazyLoad']).config(['$stateProvider', function ($stateProvider) {
	$stateProvider.state('test', {
		url: '/test',
		params: { model: {} },
		views: {
			'@': {
				controller: 'DashboardController',
				controllerAs: '$ctrl',
				templateUrl: WebHelper.templateUrl('/js/app/modules/dashboard/template/dashboard.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/dashboard/services/DashboardService.js',
			'/js/app/modules/dashboard/controllers/DashboardController.js',
		])
	});
}]);