'use strict';
angular.module('gec.userManagement', ['ui.router', 'gec.ui', 'oc.lazyLoad']).config([
	'$stateProvider',
	function ($stateProvider) {
		$stateProvider.state(WebHelper.createListPageState({
			name: 'userManagement',
			url: '/user-management',
			yaml: '/js/app/modules/user-management/page/user-management.yaml',
			controller: 'UserManagementCtrl',
			formType: 'form-with-table',
			dependencies: [
				'/js/app/modules/user-management/controllers/UserManagementCtrl.js',
				'/js/app/modules/user-management/services/UserManagementService.js',
			]
		}));

		$stateProvider.state(WebHelper.createFormPageState({
			name: 'userManagement.creation',
			url: '/new',
			yaml: '/js/app/modules/user-management/page/user-management.creation.yaml',
			controller: 'UserManagementNewCtrl',
			formType: 'form-with-table',
			dependencies: [
				'/js/app/modules/user-management/controllers/UserManagementNewCtrl.js',
				'/js/app/modules/user-management/services/UserManagementService.js',
			]
		}));

		$stateProvider.state(WebHelper.createFormPageState({
			name: 'userManagement.modification',
			url: '/{id}/edit',
			yaml: '/js/app/modules/user-management/page/user-management.modification.yaml',
			controller: 'UserManagementEditCtrl',
			formType: 'form-with-table',
			dependencies: [
				'/js/app/modules/user-management/controllers/UserManagementEditCtrl.js',
				'/js/app/modules/user-management/services/UserManagementService.js',
			]
		}));

		$stateProvider.state(WebHelper.createFormPageState({
			name: 'userManagement.detail',
			url: '/{id}/view',
			yaml: '/js/app/modules/user-management/page/user-management.detail.yaml',
			controller: 'UserManagementEditCtrl',
			formType: 'form-with-table',
			dependencies: [
				'/js/app/modules/user-management/controllers/UserManagementEditCtrl.js',
				'/js/app/modules/user-management/services/UserManagementService.js',
			]
		}));
	}]
);