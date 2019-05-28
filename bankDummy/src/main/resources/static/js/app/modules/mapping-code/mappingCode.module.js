'use strict'
angular.module('gec.mappingCode', ['ui.router', 'gec.ui', 'oc.lazyLoad']).config(['$stateProvider', function ($stateProvider) {
	$stateProvider.state('mapping_code_list', {
		url: '/mapping_code_list',
		params: { model: {} },
		views: {
			'@': {
				controller: 'mappingCodeListController',
				controllerAs: '$ctrlMapping',
				templateUrl: WebHelper.templateUrl('/js/app/modules/mapping-code/template/mapping_code_list.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/mapping-code/controllers/mapping_code_list.js',
			'/js/app/modules/mapping-code/services/mapping_code_list.service.js',
		])
	});

	$stateProvider.state('mapping_code_new', {
		url: '/mapping_code_new',
		params: { model: {} },
		views: {
			'@': {
				controller: 'mappingCodeMgntController',
				controllerAs: '$ctrlMappingMgnt',
				templateUrl: WebHelper.templateUrl('/js/app/modules/mapping-code/template/mapping_code_mgnt.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/mapping-code/controllers/mapping_code_mgnt.js',
			'/js/app/modules/mapping-code/services/mapping_code_mgnt.service.js',
		])
	});

	$stateProvider.state('mapping_code_mgnt_edit', {
		url: '/mapping_code_mgnt_edit/{id}',
		params: { model: {} },
		views: {
			'@': {
				controller: 'mappingCodeMgntController',
				controllerAs: '$ctrlMappingMgnt',
				templateUrl: WebHelper.templateUrl('/js/app/modules/mapping-code/template/mapping_code_mgnt.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/mapping-code/controllers/mapping_code_mgnt.js',
			'/js/app/modules/mapping-code/services/mapping_code_mgnt.service.js',
		])
	});

	$stateProvider.state('mapping_code_view', {
		url: '/mapping_code_view/{id}',
		params: { model: {} },
		views: {
			'@': {
				controller: 'mappingCodeViewController',
				controllerAs: '$ctrlMappingView',
				templateUrl: WebHelper.templateUrl('/js/app/modules/mapping-code/template/mapping_code_view.html')
			}
		},
		resolve: WebHelper.loadScript([
			'/js/app/modules/mapping-code/controllers/mapping_code_view.js',
			'/js/app/modules/mapping-code/services/mapping_code_view.service.js',
		])
	});
}]);