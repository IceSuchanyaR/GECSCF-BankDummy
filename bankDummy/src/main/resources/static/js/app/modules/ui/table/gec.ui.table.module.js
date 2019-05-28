'use strict';
angular.module('gec.ui.table', ['ui.router', 'gec.ui.form'])
  .run(["$templateCache", function ($templateCache) {


  }])
  .component(
    'gecTablePagination',
    {
      require: {
        table: '^gecDataTable'
      },
      templateUrl: '/js/app/modules/ui/table/templates/gec-table-paging.html',
      controller: [
        '$rootScope',
        '$scope',
        '$http',
        function ($rootScope, $scope, $http) {
          var self = this;
          self.canBack = false, self.canNext = false;

          self.go = function (action) {

            if (action === 'first' || action === 'changePageSize') {
              self.paging.page = 0;
            } else if (action === 'back') {
              self.paging.page--;
            } else if (action === 'next') {
              self.paging.page++;
            } else if (action === 'last') {
              self.paging.page = self.paging.totalPage - 1;
            }
            self.table.reload();
          };

          var _disableAction = function (currentPage, totalPage) {
            if (currentPage === 0) {
              /* disable button First, Back page */
              self.canBack = false;
            } else {
              /* enable button First, Back page */
              self.canBack = true;
            }

            if (currentPage >= (totalPage - 1)) {
              /* disable button Next, Last page */
              self.canNext = false;
            } else {
              /* enable button Next, Last page */
              self.canNext = true;
            }
          }

          self.$onInit = function () {

            self.paging = self.table.paging;

            $rootScope.$on(self.table.config.name + ':paging', function (event) {

              _disableAction(self.paging.page, self.paging.totalPage);
            });

          }

          self.summaryDisplay = function (totalRecord) {
            totalRecord = totalRecord || 0;

            var recordDisplay = null;
            if (totalRecord > 0) {
              recordDisplay = (((self.table.paging.page) * self.table.paging.size) + 1)
                + ' - ';
            } else {
              recordDisplay = '0 - ';
            }

            var endRecord = ((self.table.paging.page + 1) * self.table.paging.size);
            if (totalRecord < endRecord) {
              endRecord = totalRecord;
            }

            recordDisplay += '' + endRecord + ' of '
              + totalRecord;

            return recordDisplay;
          };

          self.pageSizes = [{
            label: '10',
            value: 10
          }, {
            label: '20',
            value: 20
          }, {
            label: '50',
            value: 50
          }];

        }]
    })
  .component('gecTableColumn', {
    bindings: {
      config: '<',
      record: '<data'
    },
    controller: [
      '$scope',
      '$parse',
      'handlerService',
      'visibilityService',
      function ($scope, $parse, handlerService, visibilityService) {
        var self = this;
        this.templateUrl = '/js/app/modules/ui/table/templates/'
          + this.config.type + '.html';

        self.toJSON = function(data){
        	return JSON.parse(data);
        }
        this.$onInit = function () {

          self.show = true;
          if (angular.isDefined(this.config.showIf)) {
            self.show = visibilityService.isVisible(this.config.showIf, self.data);
          }

          var _getter = $parse(this.config.name);
          $scope.data = _getter(this.record);
          if (angular.isDefined(this.config.format)) {
            $scope.hasFormat = true;
            $scope.format = this.config.format;
          }

          $scope.warn = false;
          if (angular.isDefined(this.config.warning)) {
            var _condition = $parse(this.config.warning.condition);
            $scope.warn = _condition(this.record);
          }


        }

        this.onClick = function (data) {
          if (angular.isDefined(self.config.onClick)) {
            handlerService.handle(self.config.onClick.handler, data);
          }
        }



      }],
    template: "<div ng-class=\"{'error-msg' : warn}\" ng-include=\"$ctrl.templateUrl\"></div>",
  })
  .component(
    'tableAction',
    {
      bindings: {
        config: '<',
        data: '<'
      },
      require: {
        table: '^^gecDataTable'
      },
      controller: [
        'handlerService',
        'visibilityService',
        '$parse',
        function (handlerService, visibilityService, $parse) {

          var self = this;
          self.templateUrl = '/js/app/modules/ui/table/templates/action.html';

          self.show = true;
          self.disabled = false;
          if (angular.isDefined(this.config.showIf)) {
            self.show = visibilityService.isVisible(this.config.showIf, self.data);
          }
          if (angular.isDefined(this.config.disabled)) {
            var _condition = $parse(this.config.disabled.condition);
            self.disabled = _condition(self.data);
          }

          self.doAction = function (data) {
            handlerService.handle(self.config.handler, self.data);
          }

        }],
      template: '<div ng-include="$ctrl.templateUrl"></div>',
    });