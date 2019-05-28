angular.module('gec.profile').factory("ProfileService", [
	'$http',
	function ($http) {

		function getProfile() {
			return $http({
				method: "GET",
				url: "/api/profile",
			});
		}

		function updateProfile(user) {
			return $http({
				method: "POST",
				url: "/api/profile",
				data: user,
			});
		}

		function getPrivileges() {
			return $http({
				method: "GET",
				url: "/api/privileges",
			}).then(function (response) {
				var result = [];
				response.data.forEach(function (item) {
					result.push({ privilege: item });
				});
				response.data = result;
				return response;
			});
		}

		return {
			getProfile: getProfile,
			getPrivileges: getPrivileges,
			updateProfile: updateProfile,
		};
	}]
);