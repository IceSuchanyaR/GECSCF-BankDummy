'use strict'
angular.module('gec.mappingException', ['ui.router', 'gec.ui', 'oc.lazyLoad']).config(['$stateProvider', function ($stateProvider) {
	$stateProvider.state('mapping_exception_list', {
		url: '/mapping_exception_list',
		params: { model: {} },
		views: {
			'@': {
				controller: 'mappingExceptionListController',
				controllerAs: '$ctrlException',
				templateUrl: WebHelper.templateUrl('/js/app/modules/mapping-exception/template/mapping_exception_list.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/mapping-exception/controllers/mapping_exception_list.js',
			'/js/app/modules/mapping-exception/services/mapping_exception_list.service.js',
		])
	});

	$stateProvider.state('mapping_exception_new', {
		url: '/mapping_exception_new',
		params: { model: {} },
		views: {
			'@': {
				controller: 'mappingExceptionMgntController',
				controllerAs: '$ctrlExceptionMgnt',
				templateUrl: WebHelper.templateUrl('/js/app/modules/mapping-exception/template/mapping_exception_mgnt.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/mapping-exception/controllers/mapping_exception_mgnt.js',
			'/js/app/modules/mapping-exception/services/mapping_exception_mgnt.service.js',
		])
	});

	$stateProvider.state('mapping_exception_mgnt_edit', {
		url: '/mapping_exception_mgnt_edit/{id}',
		params: { model: {} },
		views: {
			'@': {
				controller: 'mappingExceptionMgntController',
				controllerAs: '$ctrlExceptionMgnt',
				templateUrl: WebHelper.templateUrl('/js/app/modules/mapping-exception/template/mapping_exception_mgnt.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/mapping-exception/controllers/mapping_exception_mgnt.js',
			'/js/app/modules/mapping-exception/services/mapping_exception_mgnt.service.js',
		])
	});

	$stateProvider.state('mapping_exception_view', {
		url: '/mapping_exception_view/{id}',
		params: { model: {} },
		views: {
			'@': {
				controller: 'mappingExceptionViewController',
				controllerAs: '$ctrlExceptionView',
				templateUrl: WebHelper.templateUrl('/js/app/modules/mapping-exception/template/mapping_exception_view.html')
			}
		},
		resolve: WebHelper.loadScript([
		    '/js/app/modules/mapping-exception/controllers/mapping_exception_view.js',
			'/js/app/modules/mapping-exception/services/mapping_exception_view.service.js',
		])
	});
}]);