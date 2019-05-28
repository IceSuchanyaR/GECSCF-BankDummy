'use strict';
angular.module('gec.profile', ['ui.router', 'gec.ui', 'oc.lazyLoad']).config([
	'$stateProvider',
	function ($stateProvider) {
		$stateProvider.state(WebHelper.createFormPageState({
			name: 'profile',
			url: '/profile',
			yaml: '/js/app/modules/profile/page/profile.yaml',
			controller: 'ProfileController',
			formType: 'form-with-table',
			dependencies: [
				'/js/app/modules/profile/controllers/ProfileController.js',
				'/js/app/modules/profile/services/ProfileService.js',
			]
		}));
	}]
);