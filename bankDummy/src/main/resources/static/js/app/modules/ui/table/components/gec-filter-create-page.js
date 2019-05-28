'use strict';
angular
    .module('gec.ui.table')
    .component(
        'gecFilterCreatePage',
        {
            bindings: {
                config: '=',
                criteria: '=',
                pageData: '=',
                forPage: '<',
                onClear: '&?',
                onSearch: '&?'
            },
            transclude: true,
            templateUrl: '/js/app/modules/ui/table/templates/gec-filter-create-page.html',
            controller: [
                '$rootScope',
                '$scope',
                'ui',
                'handlerService',
                'dynamicFilterService',
                function ($rootScope, $scope, ui, handlerService, dynamicFilterService) {

                    var self = this;

                    var _defaultCriteria = {};
                    if (angular.isUndefined(self.config.name)) {
                        self.config.name = ui.randomId('filter');
                    }

                    if (angular.isDefined(self.config.table) && angular.isUndefined(self.config.table.name)) {
                        self.config.table.name = ui.randomId('table');
                    }

                    if (angular.isUndefined(self.config.form.name)) {
                        self.config.form.name = ui.randomId('form');
                    }

                    if (angular.isUndefined(self.config.rows) && angular.isDefined(self.config.items)) {
                        self.config.form.rows = [];
                        self.config.form.rows.push(self.config.items);
                    }
                    
                    var originItems = angular.copy(self.config.form.rows);
                    
                    function _createComponent(component, configFilter, suffixLabel) {
						 var labels = {};
						 if(angular.isDefined(configFilter.labels)) {
							 angular.forEach(configFilter.labels, function(lb) {
								 labels[lb.language] = lb.label;
							 });
						 }
						 component.label = labels;
						 component.suffixesLabel = suffixLabel;
						 return component;
					 }
                    
                    function _createComponents(componentFilters, configFilters, pagePrefixLabel) {
                    	var result = [];
                    	angular.forEach(configFilters, function(filter) {
        					 console.log(filter.documentFilterType)
        					 if(angular.isDefined(componentFilters[filter.documentFilterType])) {
        						 var f = componentFilters[filter.documentFilterType];
        						 console.log(f)
        						 if(angular.isDefined(f.components)) {
        							 if(angular.isArray(f.components)) {
        								var suffixFrom = 'core.dynamic.filter.calendar.suffix_from';
        								var suffixTo = 'core.dynamic.filter.calendar.suffix_to';
        								if(pagePrefixLabel) {
        									 suffixFrom = pagePrefixLabel + '.filter.calendar.suffix_from';
        									 suffixTo = pagePrefixLabel + '.filter.calendar.suffix_to';
        								}
        								result.push(_createComponent(f.components[0], filter, suffixFrom));
        								result.push(_createComponent(f.components[1], filter, suffixTo));
        							 }
        						 } else {
        							 result.push(_createComponent(f.component, filter));
        						 }
        					}
        				});
                    	return result;
                    }
                    
                    function _render(additionalItems){
                    	 self.config.form.rows = angular.copy(originItems);
                    	 console.log("gecFilterCreatePage _render()");
                    	 if (angular.isDefined(self.config.additionalItem)) {
                             self.hasAdditionalItems = true;
                             self.additionalItems = [];
                             var pagePrefixLabel = self.config.additionalItem.prefixLabel;
                             dynamicFilterService.configs(self.forPage).then(function(response){
                             	  var componentFilters = response.data;
                             	  if(angular.isDefined(self.config.additionalItem.fetching)){
                             		 handlerService.handle(self.config.additionalItem.fetching.handler)
                             		 .then(function(response){
                             			 var configFilters = response.data.filters;
                             			 if(angular.isDefined(configFilters)) {
                             				 var additionalFilterItem = _createComponents(componentFilters, configFilters, pagePrefixLabel);
                             				 angular.forEach(additionalFilterItem, function(each) {
                             					self.additionalItems.push(each);
                             				 });
                             				 if(self.config.form.rows.length > 0){
                             					 self.config.form.rows.push({items: self.additionalItems});
                             				 }
                             			}
                             		 })
                             	  }
                             });
                         }
                    }
                    
                    $scope.$on(self.config.name + ':reload', function (event, additionalItems) {
                    	_render(additionalItems);
                    });
                    
                   // $translateChangeSuccess
                    self.clear = function () {
                    	self.config.form.rows.forEach(function (row) {
                    		row.items.filter(function(item){return angular.isDefined(item.name);})
                    		.forEach(function(item){
                    			self.criteria[item.name] = item.defaultValue;
                    			$rootScope.$broadcast(item.name + ':load', _criteria);
                    		});
                        })
                        var _criteria = {};
                    	angular.copy(self.criteria, _criteria);
                    	if (angular.isDefined(self.config.table)){
                    		$rootScope.$broadcast(self.config.table.name + ':load', _criteria);
                        }
                    	if (angular.isDefined(self.config.summary)){
                    		$rootScope.$broadcast(self.config.summary.name + ':load', _criteria);
                    	}
                    	if(self.onClear) {
                    		self.onClear();
                    	}
                    };
                    
                    self.search = self.onSearch;

                    self.$onInit = function () {
                        $scope.isCollapsed = true;
                        console.log("gecFilterCreatePage $onInit()");
                        _render();
                        
                        if (angular.isUndefined(self.config.form.submission) && angular.isDefined(self.config.table)) {
                            self.config.form.submission = {
                                event: (self.config.table.name + ':load')
                            };
                        }

                        self.config.form.rows
                            .forEach(function (row) {
                                row.items.filter(function(item){return angular.isDefined(item.name);})
                                .filter(function(item){return angular.isDefined(item.defaultValue);})
                        		.forEach(function(item){
                        			self.criteria[item.name] = item.defaultValue;
                        		});
                            });

                        angular.copy(self.criteria, _defaultCriteria);
                    }

                }]
        });