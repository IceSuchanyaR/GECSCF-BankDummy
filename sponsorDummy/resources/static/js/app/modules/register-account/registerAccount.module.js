'use strict'
angular.module('gec.registerAccount', ['ui.router', 'gec.ui', 'oc.lazyLoad']).config(['$stateProvider', function ($stateProvider) {

	$stateProvider.state('register_account_list', {
		url: '/register_account_list',
		params: { model: {} },
		views: {
			'@': {
				controller: 'registerBankAccountListController',
				controllerAs: '$ctrl',
				templateUrl: WebHelper.templateUrl('/js/app/modules/register-account/template/register_account_list.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/register-account/controllers/register_account_list.js',
			'/js/app/modules/register-account/services/register_account_list.service.js',
		])
	});

	$stateProvider.state('register_account_new', {
		url: '/register_account_new',
		params: { model: {} },
		views: {
			'@': {
				controller: 'registerBankAccountMgntController',
				controllerAs: '$ctrlMgnt',
				templateUrl: WebHelper.templateUrl('/js/app/modules/register-account/template/register_account_mgnt.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/register-account/controllers/register_account_mgnt.js',
			'/js/app/modules/register-account/services/register_account_mgnt.service.js',
		])
	});

	$stateProvider.state('register_account_mgnt_edit', {
		url: '/register_account_mgnt_edit/{id}',
		params: { model: {} },
		views: {
			'@': {
				controller: 'registerBankAccountMgntController',
				controllerAs: '$ctrlMgnt',
				templateUrl: WebHelper.templateUrl('/js/app/modules/register-account/template/register_account_mgnt.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/register-account/controllers/register_account_mgnt.js',
			'/js/app/modules/register-account/services/register_account_mgnt.service.js',
		])
	});

	$stateProvider.state('register_account_view', {
		url: '/register_account_view/{id}',
		params: { model: {} },
		views: {
			'@': {
				controller: 'registerBankAccountViewController',
				controllerAs: '$ctrlView',
				templateUrl: WebHelper.templateUrl('/js/app/modules/register-account/template/register_account_view.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/register-account/controllers/register_account_view.js',
			'/js/app/modules/register-account/services/register_account_view.service.js',
		])
	});
}]);