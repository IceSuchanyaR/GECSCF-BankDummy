angular.module('gec.mappingException')
  .controller('mappingExceptionViewController',
    ['$scope','$stateParams','mappingExceptionViewService', function ($scope ,$stateParams, mappingExceptionViewService) {
      var self = this;
      
      self.view = {};
      $scope.params = {};
      self.total = [];
      var exceptionID = $stateParams.id;
      
     
      self.getMappingExceptionByExceptionID = function (stateParams) {
    	  return mappingExceptionViewService.getMappingExceptionByExceptionID(exceptionID).then(function (response) {
                self.view = response.data;
          });
      };
     
//      self.getTotalMappingCodeDetail = function(stateParams) {
//			return mappingExceptionViewService
//					.getTotalMappingCodeDetail(exceptionID).then(
//							function(response) {
//								self.total = response.data;
//								
//							});
//		};
      
      self.getMappingExceptionByExceptionID();
//      self.getTotalMappingCodeDetail();
    }]);
