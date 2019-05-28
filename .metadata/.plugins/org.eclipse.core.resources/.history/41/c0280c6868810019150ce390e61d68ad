'use strict';
angular.module('gec.ui.form').component('gecEditableTable', {
  bindings: {
    config: '<',
    model: '='
  },
  require: {
    form: '^gecForm'
  },
  controller: ['$rootScope', '$scope', 'ui', 'dialog', 'handlerService', 'validationFactory',
    function ($rootScope, $scope, ui, dialog, handlerService, validationFactory) {
      var self = this;
      if (angular.isUndefined(self.model)) {
        self.model = [];
      }
      self.deleteItem = function (record) {
        var index = self.model.indexOf(record);
        if (index > -1) {
          self.model.splice(index, 1);
        }
      }
      self.doAction = function (action, data) {
        handlerService.handle(action.handler, data)
      }

      self.add = function () {
        var obj = {};
        self.config.columns.forEach(function (c) {
          if (angular.isDefined(c.defaultValue)) {
            obj[c.name] = c.defaultValue;
          } else {
            obj[c.name] = null;
          }
        })
        self.model.push(obj);
      }

      self.$onInit = function () {
        var _valdations = validationFactory.create(self.config,
          $scope);
        if (angular.isDefined(_valdations)) {
          _valdations.forEach(self.form.addValidation);
        }
      }
    }],
  templateUrl: '/js/app/modules/ui/form/templates/inputs/editable-table.html'
})
  .component('gecEditableTableColumn', {
    bindings: {
      config: '<',
      data: '='
    },
    controller: ['$scope', '$parse', 'handlerService', 'visibilityService',
      function ($scope, $parse, handlerService, visibilityService) {
        var self = this;
        self.templateUrl = '/js/app/modules/ui/form/templates/editable-table-inputs/' + self.config.type + '.html';

        function calendar() {
          if (angular.isDefined(self.config.disabledWeekend) && self.config.disabledWeekend) {
            self.disabledWeekendFunc = function (dateAndMode) { return (dateAndMode.mode === 'day' && (dateAndMode.date.getDay() === 0 || dateAndMode.date.getDay() === 6)); };
          }
          $scope.popup = { opened: false };
          $scope.open = function () { $scope.popup.opened = true; };
          if (self.config.min == "[Tomorrow]") { self.min = moment().add(1, 'days'); }

        }

        function inputNumber() {
          self.onChange = function () {
            if (angular.isNumber(self.config.maxlength) && self.model > maxLengthValidate(self.config.maxlength)) self.model = maxLengthValidate(self.config.maxlength);
            if (self.model > self.config.max) self.model = self.config.max;
            else if (self.model < self.config.min) self.model = self.config.min;
          };
        }

        function maxLengthValidate(maxlength) {
          var value = 1;
          for (var i = 0; i < maxlength; i++) {
            value = value * 10;
          }
          return value - 1;
        }

        function yyyymmdd(plusDay) {
          var x = new Date();
          var y = x.getFullYear().toString();
          var m = (x.getMonth() + 1).toString();
          var d = plusDay ? (x.getDate() + 1).toString() : x.getDate().toString();
          (d.length == 1) && (d = '0' + d);
          (m.length == 1) && (m = '0' + m);
          var yyyymmdd = y + m + d;
          return yyyymmdd;
        }

        function inputText() {
          if (self.config.defaultValue && self.config.defaultValue == '[CurrentDate]' && !self.model)
            self.model = yyyymmdd();
          if (self.config.defaultValue && self.config.defaultValue == '[Tomorrow]' && !self.model)
            self.model = yyyymmdd(1);
        }

        self.$onInit = function () {
          if (self.config.type == 'calendar') calendar();
          else if (self.config.type == 'input-number') inputNumber();
          else if (self.config.type == 'input-text') inputText();

          if (self.config.defaultValue && !self.model) self.model = self.config.defaultValue;

        };

        $scope.$watch('$ctrl.model', function () {
          self.data = self.model;
        });

      }],
    template: "<div ng-class=\"{'error-msg' : warn}\" ng-include=\"$ctrl.templateUrl\"></div>",
  });