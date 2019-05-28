angular.module('gec.userManagement').controller('UserManagementNewCtrl', [
	'$scope', '$q', '$stateParams', 'UserManagementService', '$window', 'page',
	function ($scope, $q, $stateParams, UserManagementService, $window, page) {
		var self = this;

		page.onInit(function (params, criteria, data) {
		});
		page.handler('getUser', UserManagementService.getUser);
		page.handler('createUser', UserManagementService.createUser);
		page.handler('getRoles', UserManagementService.getRoles);
		page.handler('getPrivileges', UserManagementService.getPrivileges);
		page.handler('onSuccessNavigateToHome', function (params) {
			window.location.href = "/";
		});
		page.build(this);
	}]
);