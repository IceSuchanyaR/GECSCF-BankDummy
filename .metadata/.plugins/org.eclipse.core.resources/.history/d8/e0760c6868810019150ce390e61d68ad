'use strict';
angular.module('gec.ui.form').component('gecFormItem', {
  bindings: {
    config: '<',
    data: '='
  },
  require: {
    form: '^gecForm'
  },
  templateUrl: '/js/app/modules/ui/form/templates/gec-form-item.html',
  controller: [
    '$rootScope',
    '$scope',
    '$parse',
    'visibilityService',
    'validationFactory',
    function ($rootScope, $scope, $parse, visibilityService, validationFactory) {
      var self = this;
      var config = $scope.config = self.config;

      self.$onInit = function () {
        self.show = true;
        if (angular.isDefined(self.config.showIf)) {
          if (angular.isDefined(self.config.showIf.condition)) {
            var _getter = $parse(self.config.showIf.condition);
            console.log(self.data);
            var _show = _getter(self.data);
            self.show = _show;

            $scope.$watch('$ctrl.data', function (newValue, oldValue) {
              if (newValue != oldValue) {
                var _getter = $parse(self.config.showIf.condition);
                var _show = _getter(newValue);
                self.show = _show;
              }
            }, true);
          }
          else {
            self.show = visibilityService.isVisible(self.config.showIf);
          }
        }
        if (angular.isDefined(self.config.label) && angular.isDefined(self.config.label.showIf)) {
          self.show = visibilityService.isVisible(self.config.label.showIf);
        }

        self.disabled = false;
        if (angular.isDefined(self.config.disabled)) {
          if (angular.isDefined(self.config.disabled.condition)) {
            var _getter = $parse(self.config.disabled.condition);
            var _isDisabled = _getter(self.data);
            self.disabled = _isDisabled;

            $scope.$watch('$ctrl.data', function (newValue, oldValue) {
              if (newValue != oldValue) {
                var _getter = $parse(self.config.disabled.condition);
                var _isDisabled = _getter(newValue);
                self.disabled = _isDisabled;
              }
            }, true);
          }
          else {
            self.disabled = self.config.disabled;
          }
        }

        $scope.model = this.data;
        $scope.$on(self.form.name + ':clear', function () {
          $scope.errors = {};
        });

        var _valdations = validationFactory.create(config, $scope);
        if (angular.isDefined(_valdations)) {
          _valdations.forEach(this.form.addValidation);
        }
      };
    }]
});