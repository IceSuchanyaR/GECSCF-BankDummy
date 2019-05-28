angular
		.module("gec.logTransaction")
		.factory(
				"logTransactionViewService",
				[
						'$http',
						function($http) {

							
							var getlogTransactionBylogID = function(logID) {
								return $http({
									method : "GET",
									url : "/logTransaction/logTransactionView",
									params : {logID:logID}
								});
							};

							return {
								
								getlogTransactionBylogID: getlogTransactionBylogID
							}
						} ]);