'use strict';
angular
    .module('gec.ui.table')
    .component(
        'gecDataTable',
        {
            bindings: {
                config: '=',
                model: '=',
                criteria: '<',
                pageData: '<'
            },
            transclude: true,
          
            template : '<div ng-include="getTemplateUrl()"></div><div ng-transclude="" />',
            controller: [
                '$rootScope',
                '$scope',
                'UIFactory',
                '$filter',
                'blockUI',
                'ui',
                'handlerService',
                'visibilityService',
                function ($rootScope, $scope , UIFactory, $filter, blockUI, ui, handlerService, visibilityService) {
                    var self = this;
                    self.data = [];
                    self.paging = {
                        size: null,
                        page: null
                    };
                    self.menuContext = [];

                    if (angular.isUndefined(self.config.name)) {
                        self.config.name = ui.randomId('table');
                    }
                    
                    if(angular.isUndefined(self.config.pagination)){
                      self.config.pagination = true;
                    }

                    self.showTable = function(config, data){
                      if (angular.isDefined(config.showIf)) {
                        return visibilityService.isVisible(config.showIf, data);
                      }
                      return true;
                    }
                   
                    self.hasLogo = function(record){
                      return angular.isDefined(record[self.config.logoField]);
                    }
                    
                    self.logo = function(record){
                      var data = record[self.config.logoField];
                      return (data ? data : UIFactory.constants.NOLOGO_LG);
                    }
                    
                    self.select = function(record){
                      handlerService.handle(self.config.onSelect.handler, record);
                    }
                    
                    $scope.getTemplateUrl = function(){
                      if(self.config.type == 'logo-list'){
                        return '/js/app/modules/ui/custom/templates/gec-logo-list.html';
                      }
                      else if(self.config.type == 'document-table'){
                        return '/js/app/modules/ui/custom/templates/document-table.html';
                      }
                      else{
                        return '/js/app/modules/ui/table/templates/data-table.html';
                      }
                    }
                    
                    self.columns = [];
                    
                    function _fecthColumns(){
                      if(angular.isDefined(self.config.columns) && angular.isDefined(self.config.columns.fetching)){
                        var tableBlockUI = blockUI.instances.get(self.config.name);
                        handlerService.handle(self.config.columns.fetching.handler)
                        .then(function (response) {
                          self.columns = response.data.items.map(function(data){
                        	var labels = {};
                        	if(angular.isDefined(data.labels)){
                        		angular.forEach(data.labels, function(lb){
                        			labels[lb.language] = lb.label;
                        		});
                        	}
                            return {
                              fieldName: (angular.isDefined(data.documentField) ? data.documentField.documentFieldName:undefined),
                              labelEN: data['labelEN'] ? data['labelEN'] : data['label'],
                              labelTH: data['labelTH'] ? data['labelTH'] : data['label'],
                              labels: labels,	  
                              cellTemplate: data['cellTemplate'],
                              sortable: false,
                              filterType: data['filterType'],
                              format: data['format'],
                              renderer: data['renderer'],
                              dataRenderer: data['dataRenderer'],
                              headerId: data['headerId'],
                              component: data['component'],
                              alignment: data['alignment']
                            }
                          });
                          tableBlockUI.stop();
                        }).catch(function (data) {
                          // Handle error here
                          tableBlockUI.stop();
                        });
                      }
                    }
                    
                    self.load = self.config.load = function (criteria, callback) {
                        if(!criteria){
                            criteria = {};
                        }
                        criteria.size = self.paging.size;
                        criteria.page = self.paging.page;

                        var _criteria = {};

                        angular.copy(criteria, _criteria);
                        

                        Object
                            .keys(_criteria)
                            .map(
                                function (k, v) {
                                    if (angular.isObject(_criteria[k])
                                        && angular
                                            .isDefined(_criteria[k].value)) {
                                        _criteria[k] =  _criteria[k].value;
                                    }
                                    return v;
                                });
                        
                        var tableBlockUI = blockUI.instances.get(self.config.name);
                        // Start blocking the element.
                        tableBlockUI.start();
                        
                        self.data = [];
                        return handlerService.handle(self.config.fetching.handler, _criteria)
                            .then(function (response) {
                                self.data = response.data;
                                self.paging.totalRecord = response
                                    .headers('X-Total-Count');
                                self.paging.totalPage = response
                                    .headers('X-Total-Page');

                                $rootScope.$broadcast(self.config.name + ':paging', self.paging);
                                if (callback != null) {
                                    callback(criteriaData, response);
                                }
                                
                                tableBlockUI.stop();
                                
                                return response;
                            }).then(callback ? callback : angular.noop).catch(function (data) {
                              // Handle error here
                              tableBlockUI.stop();
                            });
                    }

                    self.menuContext = function (record) {
                    	
                    	return self.config.actions.filter(function(it){
                            if(angular.isDefined(it.showIf)){
                              return visibilityService.isVisible(it.showIf, record);
                            }
                            return it;
                          }).map(function (action) {
                        	  return {
                    	        html: ('<a class="dropdown-item" href="#"><span>'
                    	        	+ '<i class="fa ' + action.icon  + '" aria-hidden="true"></i>'
                    	        	+ $filter('translate')(action.label)+'</span></a>'),
                    	        click: function ($itemScope, $event, modelValue, text, $li) {
                    	        	if (angular.isDefined(action.handler)) {
                                      handlerService.handle(action.handler, record);
                    	        	}
                    	        }
                        	  };
                          });
                    }

                    self.reload = function () {
                        self.load(self.criteria);
                    }

                    $scope.$on(self.config.name + ':load', function (event, criteria) {
                        self.criteria = criteria;
                        self.load(criteria);
                    });
                    
                    $scope.$on(self.config.name + ':fetchColumns', function (event, criteria) {
                        _fecthColumns();
                    });

                    $scope.$on(self.config.name + ':reload', function (event) {
                        self.reload();
                    });

                    self.$onInit = function () {

                        if (angular.isUndefined(self.pageData)) {
                            self.pageData = {};
                        }

                        if (angular.isUndefined(self.pageData.paging)) {

                            self.pageData.paging = self.paging = {
                                size: 20,
                                page: 0,
                                totalRecord: 0,
                                totalPage: 0
                            };

                        } else {
                            self.paging = self.pageData.paging;
                        }

                        if (angular.isDefined(self.config.autoSearch)
                            && self.config.autoSearch) {
                            self.load(self.criteria);
                        }

                        _fecthColumns();
                    }
                }]
        });