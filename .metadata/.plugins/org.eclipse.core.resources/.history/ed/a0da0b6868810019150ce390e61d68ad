'use strict';
angular.module('gec.ui.form')
    .component('gecButtonAction', {
        bindings: {
            config: '<',
            formData: '=',
            formValidation: '=',
            callback: '<'
        },
        templateUrl: '/js/app/modules/ui/form/templates/gec-button-action.html',
        controller: [
            '$log',
            'handlerService',
            'visibilityService',
            '$scope',
            function ($log, handlerService, visibilityService , $scope) {

                var self = this;
                
                self.showItem = function () {
                    if (angular.isDefined(self.config.showIf)) {
                        return visibilityService.isVisible(self.config.showIf, self.formData);
                    }
                    return true
                }

                self.show = true;
                if (angular.isDefined(self.config.showIf)) {
                    self.show = visibilityService.isVisible(self.config.showIf , self.formData);
                }
                
                self.disabled = false;
                if (angular.isDefined(self.config.disabledIf)) {
                  $scope.$watch(function () { 
                    return visibilityService.isVisible(self.config.disabledIf , self.formData); },
                          function (newValue) {
                    self.disabled = newValue;
                  })
                }

                self.doAction = function (data) {
                 	console.log("Do Action");
                    if (angular.isDefined(self.config.handler) && self.config.type != 'submit') {
                      console.log(data);
                      if(angular.isUndefined(data)){
                        data = {};
                      }
                      if (angular.isDefined(self.config.includedData)){
                        console.log("Included Data");
                        data = self.formData;
                        if (angular.isDefined(self.config.useFormValidation) && self.config.useFormValidation) {
                          var result = {
                            isValid: true
                          };
                          angular.forEach(self.formValidation, function (validation) {
                            this.isValid = validation(data) && this.isValid;
                          }, result);

                          if (!result.isValid) {
                            return;
                          }
                        }
                      }
                      handlerService.handle(self.config.handler, data);
                      if (angular.isString(self.config.handler) && angular.isDefined(self.callback)) {
                        return self.callback();
                      }
                    }
                    else {
                        if(self.config.type !== 'submit'){
                            $log.error('The "' + self.config.label + '" action of requires an attribute named "handler".');
                        }
                    }
                }
            }]
    });