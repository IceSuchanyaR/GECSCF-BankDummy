angular
		.module('gec.logTransaction')
		.controller(
				'logTransactionListController',
				[
						'$scope',
						'$stateParams',
						'logTransactionListService',
						function($scope, $stateParams,
								logTransactionListService) {
							var self = this;
							self.data = [];
							self.total = [];
							self.configApp = [];
							self.logTransaction = {};
							$scope.params = { itemNo: '10' };
							self.displayAlert = false;
							self.showErrorMessage = {};
							self.showErrorMessageDelete = "";
							
							
							self.getAllLogtransaction = function(params) {
								self.displayAlert = false;
								return logTransactionListService
										.getAllLogtransaction(params).then(
												function(response) {
													self.data = response.data;
												});
							};
							
							self.resetLogtransaction = function(params) {
								$scope.params = { itemNo: '10' };
								self.getTotalLogtransaction();
								return logTransactionListService
										.getAllLogtransaction(params).then(
												function(response) {
													self.data = response.data;
												});
							};
							
							self.getTotalLogtransaction = function(params) {
								return logTransactionListService
										.getTotalLogtransaction(params).then(
												function(response) {
													self.total = response.data;
												});
							};
							
							self.getAllLogType = function() {
								return logTransactionListService
								.getAllLogType().then(
										function(response) {
											self.configApp  = response.data;
										       });
					        };
					        
							
					        self.getAllLogType($scope.params);
							self.getAllLogtransaction($scope.params);
							self.getTotalLogtransaction($scope.params);
						} ]);
