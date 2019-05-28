'use strict';
angular
    .module('gec.ui.form')
    .component('gecForm',
        {
            bindings: {
                config: '<',
                model: '=',
                type: '@',
                criteria: '<',
                submitCallback: '<submitCallback'
            },
            templateUrl: '/js/app/modules/ui/form/templates/gec-form.html',
            transclude: true,
            controller: [
                '$log',
                '$rootScope',
                'ui',
                'handlerService',
                'visibilityService',
                'pageNavigator',
                function ($log, $rootScope, ui, handlerService, visibilityService,  pageNavigator) {

                    var self = this;
                    var _validations = [];

                    self.showItem = function(config, data){
                      if (angular.isDefined(config.showIf)) {
                        return visibilityService.isVisible(config.showIf, data);
                      }
                      return true;
                    }
                    
                    self.addValidation = function (validation) {
                        _validations.push(validation);
                    }

                    var _submit = function (data) {
                        if (angular.isDefined(self.config.submission)) { 
                          
                          var _data = {};

                          angular.copy(data, _data);
                          
                          Object
                          .keys(_data)
                          .map(
                              function (k, v) {
                                  if (angular.isObject(_data[k])
                                      && angular
                                          .isDefined(_data[k].value)) {
                                    _data[k] =  _data[k].value;
                                  }
                                  return v;
                              });
                            handlerService.handle(self.config.submission, _data, self.submitCallback)
                        }
                    }

                    self.getFormValidation = function(){
                        return _validations;
                    };

                    self.submit = function (data) {
                        var result = {
                            isValid: true
                        };
                        angular.forEach(_validations, function (validation) {
                            this.isValid = validation(data) && this.isValid;
                        }, result);

                        if (result.isValid) {
                            _submit(data);
                        }
                    };

                    var _styles = {
                        form: {
                            itemColumnClass: 'col-md-6',
                            bodyClass: 'tile-body'
                        },
                        dialog: {
                            itemColumnClass: 'col-12',
                            bodyClass: 'modal-body'
                        }
                    };

                    this.$onInit = function () { 
                        // Setting default
                        this.type = angular.isDefined(this.type) ? this.type
                            : 'form';
                        this.style = _styles[this.type];
                    }

                }]
        });