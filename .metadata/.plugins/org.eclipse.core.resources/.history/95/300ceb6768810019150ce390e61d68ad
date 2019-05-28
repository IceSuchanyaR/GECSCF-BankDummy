'use strict'
angular.module('gec.changePassword', ['ui.router', 'gec.ui', 'oc.lazyLoad']).config(['$stateProvider', function ($stateProvider) {

	$stateProvider.state(WebHelper.createFormPageState({
		name: 'change-password',
		url: '/change-password',
		yaml: '/js/app/modules/change-password/page/change-password.yaml',
		controller: 'ChangePasswordController',
		formType: 'form-with-table',
		dependencies: [
			'/js/app/modules/change-password/services/ChangePasswordService.js',
			'/js/app/modules/change-password/controllers/ChangePasswordController.js',
		]
	}));
}]);