angular.module('gec.userManagement').factory("UserManagementService", [
	'$http',
	function ($http) {

		function getRoles() {
			return $http({
				method: "GET",
				url: "js/app/modules/user-management/json/user-role.json",
				// url: "/api/roles", #Api exsited but not work 404!
			});
		}

		function getUser(id) {
			return $http({
				method: "GET",
				url: "/api/users/" + id,
			}).then(function (response) {
				var data = response.data;
				var role = '';
				if (data.roles) {
					data.roles.forEach(function (r) { role += r.name + ' '; });
					data.role = role.trim();
				}

				data.status = data.enabled;

				data.updateTime = new Date(data.updateTime);
				data.createTime = new Date(data.createTime);

				if (data.updateByUser) data.updateBy = data.updateByUser.username;
				if (data.createByUser) data.createBy = data.createByUser.username;
				response.data = data;
				return response;
			});
		}

		function getUsers(params) {
			return $http({
				method: "GET",
				url: "/api/users",
				params: params,
			}).then(function (response) {
				response.data = response.data.map(function (item) {
					var role = '';
					if (item.roles) {
						item.roles.forEach(function (r) {
							r.name = r.name.toString().toLowerCase();
							var buildRoleName = '';
							var strArray = r.name.split('_');
							strArray.forEach(function (str) {
								buildRoleName += str.charAt(0).toUpperCase() + str.slice(1) + ' ';
							});
							role += ', ' + buildRoleName;
						});

						role = role.substring(1);
						item.role = role.trim();
					}
					item.status = (item.enabled) ? 'Activate' : 'Suspend';

					if (item.updateByUser) item.updateBy = item.updateByUser.username;
					return item;
				});
				response.data.sort(compare);
				return response;
			});
		}

		function compare(a, b) {
			if (a.username < b.username) { return -1; }
			if (a.username > b.username) { return 1; }
			if (a.fullName < b.fullName) { return -1; }
			if (a.fullName > b.fullName) { return 1; }
			return 0;
		}

		function createUser(params) {
			var password = buildPassword(params);
			return $http({
				method: "POST",
				url: "/api/users",
				params: {
					role: params.role,
					password: password,
				},
				data: params,
			});
		}

		function buildPassword(params) {
			var dateTime = new Date(params.birthday);
			var day = dateTime.getDate();
			day = day.toString().length == 1 ? '0' + day : day;
			var mounth = dateTime.getMonth() + 1;
			mounth = mounth.toString().length == 1 ? '0' + mounth : mounth;
			var password = day + '' + mounth + '' + dateTime.getFullYear();
			return password;
		}

		function updateUser(params) {
			return $http({
				method: "PUT",
				url: "/api/users/" + params.id,
				params: { role: params.role, },
				data: params,
			});
		}

		function deleteUser(params) {
			return $http({
				method: "DELETE",
				url: "/api/users/" + params.id,
				data: params,
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
			getUser: getUser,
			getRoles: getRoles,
			getUsers: getUsers,
			createUser: createUser,
			deleteUser: deleteUser,
			updateUser: updateUser,
			getPrivileges: getPrivileges,
		};
	}]
);