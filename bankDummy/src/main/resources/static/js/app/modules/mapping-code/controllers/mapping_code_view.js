angular.module('gec.mappingCode')
  .controller('mappingCodeViewController',
    ['$scope','$stateParams','mappingCodeViewService', function ($scope ,$stateParams, mappingCodeViewService) {
      var self = this;
      
      self.view = {};
      $scope.params = {};
      self.total = [];
      var mappingID = $stateParams.id;
      
     
      self.getMappingCodeByMappingID = function (stateParams) {
    	  return mappingCodeViewService.getMappingCodeByMappingID(mappingID).then(function (response) {
                self.view = response.data;
                
          });
      };
     
      self.getTotalMappingCodeDetail = function(stateParams) {
			return mappingCodeViewService
					.getTotalMappingCodeDetail(mappingID).then(
							function(response) {
								self.total = response.data;
								
							});
		};
      
      self.getMappingCodeByMappingID();
      self.getTotalMappingCodeDetail();
    }]);
