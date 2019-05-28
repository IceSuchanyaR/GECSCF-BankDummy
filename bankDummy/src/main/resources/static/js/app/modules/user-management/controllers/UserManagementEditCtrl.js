angular.module('gec.userManagement').controller('UserManagementEditCtrl', [
	'$scope', '$q', '$stateParams', 'UserManagementService', '$window', 'page',
	function ($scope, $q, $stateParams, UserManagementService, $window, page) {
		var self = this;

		page.onInit(function (params, criteria, data) {
			UserManagementService.getUser(params.id).then(function (response) {
				self.model = response.data;
				if (self.model.birthday)
					self.model.birthday = new Date(self.model.birthday);
			});
		});
		page.handler('getUser', UserManagementService.getUser);
		page.handler('updateUser', function (params) {
			delete params.createBy;
			delete params.updateBy;
			delete params.createTime;
			delete params.updateTime;
			return UserManagementService.updateUser(params);
		});
		page.handler('getRoles', UserManagementService.getRoles);
		page.handler('getPrivileges', UserManagementService.getPrivileges);
		page.handler('onSuccessNavigateToHome', function (params) {
			window.location.href = "/";
		});
		page.build(this);
	}]
);