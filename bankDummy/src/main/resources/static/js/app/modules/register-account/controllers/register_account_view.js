angular.module('gec.registerAccount')
  .controller('registerBankAccountViewController',
    ['$scope','$stateParams','registerBankAccountViewService', function ($scope ,$stateParams, registerBankAccountViewService) {
      var self = this;
      
      self.view = {};
      $scope.params = {};
      $scope.bankCode = '044';
      var accountID = $stateParams.id;
      
     
      self.getBankAccountByAccountID = function (stateParams) {
    	  return registerBankAccountViewService.getBankAccountByAccountID(accountID).then(function (response) {
                self.view = response.data;
          });
      };
     
      self.getBankAccountByAccountID();
    }]);
