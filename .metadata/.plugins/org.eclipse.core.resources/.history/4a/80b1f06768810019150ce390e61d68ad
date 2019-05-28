angular
		.module("gec.logTransaction")
		.factory(
				"logTransactionListService",
				[
						'$http',
						function($http) {

							var getAllLogtransaction = function(params) {
								return $http({
									method : "GET",
									url : "/logTransaction/logTransactionList",
									params : params
								});
							};
							
							var getTotalLogtransaction = function(params) {
								return $http({
									method : "GET",
									url : "/logTransaction/totalLogtransaction",
									params : params
								});
							};
							
							var getAllLogType = function() {
								return $http({
									method : "GET",
									url : "/logTransaction/getLogType",
									
								});
							};

							return {
								getAllLogtransaction : getAllLogtransaction,
								getTotalLogtransaction : getTotalLogtransaction,
								getAllLogType : getAllLogType
							}
						} ]);