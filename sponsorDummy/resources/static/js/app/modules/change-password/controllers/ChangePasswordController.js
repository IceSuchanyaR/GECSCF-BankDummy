angular.module('gec.changePassword').controller('ChangePasswordController', [
	'$scope', '$stateParams', 'ChangePasswordService', '$window', 'page',
	function ($scope, $stateParams, ChangePasswordService, $window, page) {
		var self = this;

		page.onInit(function (criteria, data) {
			ChangePasswordService.getProfile().then(function (response) {
				self.model.username = response.data.username;
			});

		});
		page.handler('getProfile', ChangePasswordService.getProfile);
		page.handler('onSuccessNavigateToLogin', function (params) {
			window.location.href = "/login";
		});
		page.handler('updateNewPassword', function (params) {
			return ChangePasswordService.updateNewPassword({
				newPassword: params.newPassword,
				confirmNewPassword: params.confirmNewPassword,
				currentPassword: params.currentPassword,
			});
		});
		page.build(this);
	}]
);