angular.module('gec.userManagement').controller('UserManagementCtrl', [
	'$scope', '$q', '$stateParams', 'UserManagementService', '$window', 'page',
	function ($scope, $q, $stateParams, UserManagementService, $window, page) {
		var self = this;

		page.onInit(function (params, criteria, data) {
			UserManagementService.getRoles().then(function (response) {
				console.log(response);
			});
		});
		page.handler('getUsers', UserManagementService.getUsers);
		page.handler('getRoles', UserManagementService.getRoles);
		page.handler('createUser', UserManagementService.createUser);
		page.handler('deleteUser', UserManagementService.deleteUser);
		page.build(this);
	}]
);