(function() {
'use strict';
angular
    .module('gec.ui.table')
    .component(
        'gecDataSummary',
        {
            bindings: {
                config: '=',
                criteria: '<'
            },
            templateUrl: '/js/app/modules/ui/table/templates/gec-data-summary.html' ,
            controller: [
                '$rootScope',
                '$scope',
                '$filter',
                'blockUI',
                'ui',
                'handlerService',
                'visibilityService',
                function ($rootScope, $scope, $filter, blockUI, ui, handlerService, visibilityService) {
                    var self = this;
                    self.data = [];
                    
                    if (angular.isUndefined(self.config.name)) {
                        self.config.name = ui.randomId('summary');
                    }
                    
                    self.load = self.config.load = function (criteria, callback) {

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
                        
                        var block = blockUI.instances.get(self.config.name);
                        // Start blocking the element.
                        block.start();
                        
                        self.data = [];
                        return handlerService.handle(self.config.fetching.handler, _criteria)
                            .then(function (response) {
                                self.data =  response.data;
                                block.stop();
                                
                                return response;
                            }).then(callback ? callback : angular.noop).catch(function (data) {
                              // Handle error here
                              block.stop();
                            });
                    }

                    self.reload = function () {
                        self.load(self.criteria);
                    }

                    $rootScope.$on(self.config.name + ':load', function (event, criteria) {
                        self.criteria = criteria;
                        self.load(criteria);
                    });

                    $rootScope.$on(self.config.name + ':reload', function (event) {
                        self.reload();
                    });

                    self.$onInit = function () {

                       self.load(self.criteria);

                    }
                }]
        });

})();