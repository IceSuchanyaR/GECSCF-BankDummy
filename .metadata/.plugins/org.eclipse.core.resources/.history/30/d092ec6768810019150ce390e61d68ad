angular.module("gec.changePassword").factory("ChangePasswordService", [
	'$http', function ($http) {

		function getProfile() {
			return $http({
				method: "GET",
				url: "/api/profile",
			});
		}

		function getPrivileges() {
			return $http({
				method: "GET",
				url: "/api/privileges",
			});
		}
		
		function updateNewPassword(paarams) {
			return $http({
				method: "POST",
				url: "/api/change-password",
				params: paarams,
			});
		}

		return {
			getProfile: getProfile,
			getPrivileges: getPrivileges,
			updateNewPassword: updateNewPassword,
		};
	}]
);