'use strict'
angular.module('gec.logTransaction', ['ui.router', 'gec.ui', 'oc.lazyLoad']).config(['$stateProvider', function ($stateProvider) {

	$stateProvider.state('log_transaction_list', {
		url: '/log_transaction_list',
		params: { model: {} },
		views: {
			'@': {
				controller: 'logTransactionListController',
				controllerAs: '$ctrlLogList',
				templateUrl: WebHelper.templateUrl('/js/app/modules/log-transaction/template/log_transaction_list.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/log-transaction/controllers/log_transaction_list.js',
			'/js/app/modules/log-transaction/services/log_transaction_list.service.js',
		])
	});

	$stateProvider.state('log_transaction_view', {
		url: '/log_transaction_view/{id}',
		params: { model: {} },
		views: {
			'@': {
				controller: 'logTransactionViewController',
				controllerAs: '$ctrlLogView',
				templateUrl: WebHelper.templateUrl('/js/app/modules/log-transaction/template/log_transaction_view.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/log-transaction/controllers/log_transaction_view.js',
			'/js/app/modules/log-transaction/services/log_transaction_view.service.js',
		])
	});

}]);