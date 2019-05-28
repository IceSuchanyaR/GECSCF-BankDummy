angular.module('gec.profile').controller('ProfileController', [
	'$scope', '$q', '$stateParams', 'ProfileService', '$window', 'page',
	function ($scope, $q, $stateParams, ProfileService, $window, page) {
		var self = this;

		page.onInit(function (params, criteria, data) {
			ProfileService.getProfile().then(function (response) {
				var user = response.data;
				self.model = user;
				self.model.role = user.roles[0].name;
				console.log(user);
			});

		});
		page.handler('getProfile', ProfileService.getProfile);
		page.handler('getPrivileges', ProfileService.getPrivileges);
		page.handler('updateProfile', function (params) {
			return ProfileService.updateProfile(params);
		});
		page.handler('onSuccessNavigateToHome', function (params) {
			window.location.href = "/";
		});
		page.build(this);

	}]
);