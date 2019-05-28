'use strict';
angular
  .module('gec.ui.form', ['ui.router'])
  .component('gecFormFooter', {
    require: {
      form: '^gecForm'
    },
    transclude: true,
    templateUrl: '/js/app/modules/ui/form/templates/gec-form-footer.html'
  })
  .component('gecCalendar', {
    bindings: {
      config: '<',
      model: '=',
      max: '=',
      min: '=',
      disabled: '=ngDisabled'
    },
    controller: [
      '$scope', '$rootScope', 'handlerService',
      function ($scope, $rootScope, handlerService) {

    	var self = this;
    	
        if (angular.isDefined(this.config.disabledWeekend) && this.config.disabledWeekend) {
          this.disabledWeekendFunc = function (dateAndMode) {
            return (dateAndMode.mode === 'day' && (dateAndMode.date.getDay() === 0 || dateAndMode.date.getDay() === 6));
          }
        } else if (angular.isDefined(self.config.availableDate)) {
        	if ($scope.listOfAvailableDate === undefined || $scope.listOfAvailableDate === null) {
        		$scope.listOfAvailableDate = [];
			}
        	this.disabledWeekendFunc = function (dateAndMode) {
				var date = dateAndMode.date,
					mode = dateAndMode.mode;
				if (mode === 'day') {
					$scope.listOfAvailableDate.forEach(function (each) {
						if (each.setHours(0, 0, 0, 0) === date.setHours(0, 0, 0, 0)) {
							return false;
						}
					});
					return true;
				} else if (mode === 'month') {
					$scope.listOfAvailableDate.forEach(function (each) {
						if (each.getMonth() === date.getMonth() && each.getFullYear() === date.getFullYear()) {
							return false;
						}
					});
					return true;
				} else if (mode === 'year') {
					$scope.listOfAvailableDate.forEach(function (each) {
						if (each.getFullYear() === date.getFullYear()) {
							return false;
						}
					});
					return true;
				} else {
					return false;
				}
			}
        }
        function _loadAvailableDate(data) {
        	if (angular.isDefined(self.config.availableDate)) {
        		if (angular.isDefined(self.config.availableDate.handler)) {
        			handlerService.handle(self.config.availableDate.handler, data).then(function (dates) {
        				$scope.listOfAvailableDate = dates;
        				if ($scope.listOfAvailableDate != null && $scope.listOfAvailableDate.length > 0 
        						&& angular.isDefined(self.config.defaultValue) && self.config.defaultValue == '$first') {
        					self.model = $scope.listOfAvailableDate[0];
        				} else {
        					self.change(self.model);
        				}
        			});
        		}
            }
        }
        this.$onInit = function () {
          $scope.popup = {
            opened: false
          };
          $scope.open = function () {
            $scope.popup.opened = true;
          };
          if (this.config.min == "[CurrentDate]") {
            this.min = moment().add(0, 'days');
          }
          if (this.config.min == "[Tomorrow]") {
            this.min = moment().add(1, 'days');
          }
          if (this.config.max == "[CurrentDate]") {
            this.max = moment().add(0, 'days');
          }
          if (this.config.max == "[Tomorrow]") {
            this.max = moment().add(1, 'days');
          }
          if (angular.isDefined(this.config.defaultFromDate)) {
            var today = new Date();
            today.setDate(today.getDate());
            today.setHours(0);
            today.setMinutes(0);
            this.model = today;
          }
          if (angular.isDefined(this.config.defaultToDate)) {
            var today = new Date();
            today.setDate(today.getDate());
            today.setHours(23);
            today.setMinutes(59);
            this.model = today;
          }
          
          self.change = function (data) {
        	  if (angular.isDefined(self.config.onChange)) {
        		  var cData = {};
        		  if (data == null) {
        			  cData = {};
        		  } else {
        			  if (angular.isString(data)) {
        				  cData[self.config.name] = data;
        			  } else {
        				  cData = data;
        			  }
        		  }
        		  if (angular.isString(self.config.onChange)) {
        			  $rootScope.$broadcast(self.config.onChange, cData);
        		  }
        		  else if (angular.isDefined(self.config.onChange.handler)) {
        			  handlerService.handle(self.config.onChange.handler, cData);
        		  }
              }
          }

            $scope.$watch("$ctrl.model", function (newValue, oldValue) {
              if (newValue != oldValue) {
                self.change(newValue);
              }
            }, true);

            $scope.$on(self.config.name + ':load', function (event, data) {
            	_loadAvailableDate(data);
            });

            if (angular.isUndefined(self.config.autoLoad)) {
              self.config.autoLoad = true;
            }
            if (self.config.autoLoad) {
            	_loadAvailableDate({});
            }

        };

      }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/calendar.html'
  })
  .component('gecValueTimeInput', {
    bindings: {
      config: '<',
      model: '=',
      max: '=',
      min: '=',
      disabled: '=ngDisabled'
    },
    controller: [
      '$scope',
      function ($scope) {
        var self = this;

        function mapTime(time) {
          if (!time) return '00';
          if (time < 10) return '0' + time;
          if (time.length === 1) return "0" + time;
          return time;
        }

        self.onChangeTime = function () {
          if (!self.hh) self.hh = 0;
          if (!self.mm) self.mm = 0;
          self.model = (mapTime(self.hh) || '00') + ':' + (mapTime(self.mm) || '00');
        };

        $scope.$watch('$ctrl.model', function (newValue, oldValue, scope) {
          if (newValue != oldValue) {
            var time = newValue.split(':');
            self.hh = time[0];
            self.mm = time[1];
          }
        });

        self.limitTimeControl = function () {
          if (self.hh > 23) self.hh = 23;
          if (self.mm > 59) self.mm = 59;
          if (self.hh < 0) self.hh = 0;
          if (self.mm < 0) self.mm = 0;
        };

        $scope.$watchGroup(['$ctrl.hh', '$ctrl.mm'], function (newValues, oldValues, scope) {
          var hh = self.hh;
          var mm = self.mm;
          if (!newValues[0]) {
            hh = newValues[0];
          }
          if (!newValues[1]) {
            mm = newValues[1];
          }
          if (self.hh && self.mm) {
            self.model = (hh || '00') + ':' + (mm || '00');
          }
          else {
            self.model = null;
          }

        });

        self.$onInit = function () {
          if (self.model) {

            var time = self.model.split(':');
            self.hh = time[0];
            self.mm = time[1];

          }
        };

      }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/time.html'
  })
  .component('gecValueTimeArrayInput', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: [
      '$scope',
      function ($scope) {
        var self = this;

        self.add = function () {
          if (self.model == null) {
            self.model = [];
          }
          self.duplicated = self.model.find(checkDuplication);
          self.empty = ((!self.hh) || (!self.mm)) ? true : false;
          if (!self.duplicated && !self.empty) {
            self.model.push({ hh: self.mapTime(self.hh) || '00', mm: self.mapTime(self.mm) || '00' });
            self.hh = null;
            self.mm = null;
          }

        };

        self.deleteTime = function (time) {
          self.model = self.model.filter(function (item) { return item !== time; });
        };

        self.mapTime = function (time) {
          if (!time) { return '00'; }
          if (time.toString().length == 1) { return '0' + time; }

          return time;
        };

        function checkDuplication(time) {
          return ((self.hh == time.hh) && (self.mm == time.mm));
        }

        self.onChangeTime = function () {
          if (!self.hh) self.hh = 0;
          if (!self.mm) self.mm = 0;
          self.duplicated = self.model.find(checkDuplication);
        };

        self.limitTimeControl = function () {
          if (self.hh > 23) self.hh = 23;
          if (self.mm > 59) self.mm = 59;
          if (self.hh < 0) self.hh = 0;
          if (self.mm < 0) self.mm = 0;
        };

        self.$onInit = function () {
          if (!self.model) {
            self.model = [];
          }
          else {
            self.duplicated = self.model.find(checkDuplication);
          }
        };

      }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/timearray.html'
  })
  .component('gecUsernameDisplay', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: [
      '$scope', 'authorization',
      function ($scope, authorization) {
        authorization.getMyUserInfo().then(function (response) {
          var myInfo = response.data;
          $scope.userName = myInfo.displayName;
        });
      }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/user-name-display.html'
  })
  .component('gecInputText', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', 'handlerService', function ($scope, handlerService) {
      var self = this;
      self.originalConfig = angular.copy(self.config);
      $scope.$on(self.config.name + ':reconfig', function (event, data) {
        if (angular.isDefined(self.config.maxlength) && angular.isDefined(self.config.maxlength.handler)) {
          handlerService.handle(self.config.maxlength.handler)
            .then(function (response) {
              if (angular.isDefined(response.data)) { self.config.maxlength = response.data; }
              else { self.config.maxlength = response; }
            }).catch(function (response) {
              self.config.maxlength = self.originalConfig.maxlength.default;
            });
        }
      });
      this.$onInit = function () {
        if (angular.isDefined(self.config.maxlength) && angular.isDefined(self.config.maxlength.handler)) {
          handlerService.handle(self.config.maxlength.handler)
            .then(function (response) {
              if (angular.isDefined(response.data)) { self.config.maxlength = response.data; }
              else { self.config.maxlength = response; }
            }).catch(function (response) {
              self.config.maxlength = self.originalConfig.maxlength.default;
            });
        } else {
          if (self.config.defaultValue && !self.model) self.model = self.config.defaultValue;
        }
      }

    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/text-input.html'
  })
  .component('gecInputNumber', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', function ($scope) {
      var self = this;
      self.onChange = function onChange() {
        if (self.model > self.config.max) self.model = self.config.max;
        else if (self.model < self.config.min) self.model = self.config.min;
      };
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/number-input.html'
  })
  .component('gecTextarea', {
    bindings: {
      config: '<',
      model: '='
    },
    templateUrl: '/js/app/modules/ui/form/templates/inputs/textarea.html'
  })
  .component('gecInputEmail', {
    bindings: {
      config: '<',
      model: '=',
      form: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', function ($scope) {
      this.$onInit = function () {}
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/email.html'
  })
  .component('gecTypeahead', {
    bindings: {
      config: '<',
      model: '=',
      formData: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', '$http', '$log', 'handlerService', function ($scope, $http, $log, handlerService) {
      var self = this;
      this.handler = function (h) {
        var _handler = handlerService.handler(h);
        return _handler ? _handler : function (q) {
          $log.error('Please defined handler' + h);
          $log.debug(q);
        };
      }

      this.createRequest = function (config) {

        config.query.params = config.query.params || {
          limit: 5,
          offset: 0
        }

        return function (value) {
          var queryParam = config.query.queryParam || 'q';
          config.query.params[queryParam] = value;

          var _transformer = function (item) {
            return {
              identity: ([config.name,
              config.query.valueField,
                'option'].join('-')),
              label: item[config.query.labelField],
              value: item[config.query.valueField]
            };

          }

          return $http
            .get(config.query.url, {
              params: config.query.params
            })
            .then(function(response){ return response.data.map(_transformer)});
        };
      }
      $scope.onBlur = function (model) {
        if (angular.isString(model) && angular.isDefined(self.config.needToSelect) && self.config.needToSelect) {
          model = null;
          self.model = null;
        }
        if (angular.isDefined(self.config.onBlur) && angular.isDefined(self.config.onBlur.handler)) {
          var h = self.handler(self.config.onBlur.handler);
          h(self.formData, self.model);
        }
      };
      this.$onInit = function () {
        if (angular.isDefined(self.config.query)) {

          if (angular.isDefined(self.config.query.handler)) {
            $scope.query = function (q) {
              var h = self.handler(self.config.query.handler);
              return h(q, self.formData);
            }
          } else {
            $scope.query = self.createRequest(self.config);
          }
        }
      }

    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/typeahead.html'
  })
  .component('gecRadio', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', 'handlerService', function ($scope, handlerService) {
      this.$onInit = function () {
        var self = this;
        $scope.options = self.config.options;

        self.change = function (data) {
          if (angular.isDefined(self.config.onChange)) {
            var cData = {};
            if (data == null) {
              cData = {};
            }
            else {
              if (angular.isString(data)) {
                cData[self.config.name] = data;
              }
              else {
                cData = data;
              }
            }

            if (angular.isString(self.config.onChange)) {
              $rootScope.$broadcast(self.config.onChange, cData);
            }
            else if (angular.isDefined(self.config.onChange.handler)) {
              handlerService.handle(self.config.onChange.handler, cData);
            }

          }
        }

        $scope.$watch("$ctrl.model", function (newValue, oldValue) {
          if (newValue != oldValue) {
            console.debug(newValue);
            self.change(newValue);
          }
        }, true);

      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/radio.html'
  })
  .component('gecRadioInput', {
    bindings: {
      config: '<',
      inputModel: '=',
      radioModel: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', function ($scope) {
      this.$onInit = function () {

      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/radio-input.html'
  })
  .component('gecCheckbox', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', function ($scope) {

      this.$onInit = function () {

      }

    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/checkbox.html'
  })
  .component('gecDropdown', {
    bindings: {
      config: '<',
      model: '=',
      data: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$rootScope', '$scope', '$parse', 'ui', 'handlerService', function ($rootScope, $scope, $parse, ui, handlerService) {

      var self = this;

      if (angular.isUndefined(self.config.name)) {
        self.config.name = ui.randomId('dropdown');
      }
      self.trackBy = function (option, idx) {
        if (angular.isDefined(self.config.trackBy)) {
          var _getter = $parse(self.config.trackBy);
          return _getter(option);
        }
        else {
          return idx;
        }
      }

      $scope.getTemplateUrl = function () {
        if (angular.isDefined(self.config.convertToNumber) && self.config.convertToNumber) {
          return '/js/app/modules/ui/form/templates/inputs/dropdown-number.html';
        }
        else if (angular.isDefined(self.config.valueAsObject) && self.config.valueAsObject) {
          return '/js/app/modules/ui/form/templates/inputs/dropdown-object.html';
        }
        else {
          return '/js/app/modules/ui/form/templates/inputs/dropdown.html';
        }
      }

      function _load(data) {
        if (angular.isDefined(self.config.options)) {
          if (angular.isDefined(self.config.options.handler)) {
            handlerService.handle(self.config.options.handler, data).then(function (opts) {
              $scope.options = opts;
              if ($scope.options != null && $scope.options.length > 0 && angular.isDefined(self.config.defaultValue) && self.config.defaultValue == '$first') {
                self.model = $scope.options[0].value;
              } else {
                self.change(self.model);
              }
            });
          }
          else {
            // static
            $scope.options = self.config.options;
            if ($scope.options != null && $scope.options.length > 0 && angular.isDefined(self.config.defaultValue)) {
              if (self.config.defaultValue == '$first') {
                self.model = self.config.options[0].value;
              } else {
                self.model = self.config.defaultValue;
              }
            } else {
              self.change(self.model);
            }
          }

        }
      }

      this.$onInit = function () {

        self.change = function (data) {
          if (angular.isDefined(self.config.onChange)) {
            var cData = {};
            if (data == null) {
              cData = {};
            }
            else {
              if (angular.isString(data)) {
                cData[self.config.name] = data;
              }
              else {
                cData = data;
              }
            }

            if (angular.isString(self.config.onChange)) {
              $rootScope.$broadcast(self.config.onChange, cData);
            }
            else if (angular.isDefined(self.config.onChange.handler)) {
              handlerService.handle(self.config.onChange.handler, cData);
            }

          }
        }

        $scope.$watch("$ctrl.model", function (newValue, oldValue) {
          if (newValue != oldValue) {
            self.change(newValue);
          }
        }, true);

        $scope.$on(self.config.name + ':load', function (event, data) {
          _load(data);
        });

        if (angular.isUndefined(self.config.autoLoad)) {
          self.config.autoLoad = true;
        }
        if (self.config.autoLoad) {
          _load({});
        }
      }
    }],
    template: '<div ng-include="getTemplateUrl()" />'
  })
  .component('gecCheckboxes', {
    bindings: {
      config: '<',
      model: '=',
      data: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$rootScope', '$scope', '$parse', 'ui', 'handlerService', function ($rootScope, $scope, $parse, ui, handlerService) {

      var self = this;
      self.colStyle = 'col-md-6';
      if (angular.isUndefined(self.config.name)) {
        self.config.name = ui.randomId('dropdown');
      }

      if (angular.isUndefined(self.config.alignment)) {
        self.config.alignment = ui.randomId('horizontal');
      }

      self.trackBy = function (option, idx) {
        if (angular.isDefined(self.config.trackBy)) {
          var _getter = $parse(self.config.trackBy);
          return _getter(option);
        }
        else {
          return idx;
        }
      }

      this.$onInit = function () {

        self.change = function (data) {
          if (angular.isDefined(self.config.onChange)) {
            if (data == null) {
              data = {};
            }
            data[self.config.name] = self.model;
            $rootScope.$broadcast(self.config.onChange, data);
          }
        }

        $scope.$on(self.config.name + ':load', function (event, data) {

          if (angular.isDefined(self.config.options)) {
            if (angular.isDefined(self.config.options.handler)) {
              handlerService.handle(self.config.options.handler, data).then(function (opts) {
                $scope.options = opts;
              });
            }
            else {
              // static
              $scope.options = self.config.options;
            }

            // alignment
            if (self.config.alignment == 'vertical') {
              self.colStyle = 'col-12';
            }
          }

        });

        if (angular.isUndefined(self.config.autoLoad) || angular.isDefined(self.config.options.handler)) {
          self.config.autoLoad = true;
        }
        if (self.config.autoLoad) {
          $scope.$broadcast(self.config.name + ':load', {});
        }
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/checkboxes.html'
  })
  .component('gecValueText', {
    bindings: {
      config: '<',
      model: '='
    },
    controller: ['$rootScope', '$scope', '$parse', '$filter', 'handlerService', function ($rootScope, $scope, $parse, $filter, handlerService) {
      var self = this;
      $scope.value = self.model;

      this.$onInit = function () {

        function _parse(data) {
          if (angular.isDefined(self.config.displayField)) {
            var _getter = $parse(self.config.displayField);
            return _getter(data);
          }
          else {
            return data;
          }
        }

        $rootScope.$on(self.config.name + ':render', function (event, data) {
          if (angular.isDefined(self.config.onRender)) {
            self.model = handlerService.handle(self.config.onRender.handler, { data: data, model: self.model });
          }
        });

        $scope.$watch("$ctrl.model", function (newValue, oldValue) {
          if (newValue != oldValue) {
            $scope.value = _parse(newValue);
            var result = newValue;
            if (angular.isDefined(self.config.filter)) {
              var filterFormat = self.config.filter.format;
              var filterType = self.config.filter;
              if (filterType === 'date') {
                filterFormat = filterFormat || 'dd/MM/yyyy';
                result = $filter(filterType)(newValue, filterFormat, 'UTC+0700');
              } else if (filterType === 'number') {
                result = $filter(filterType)(newValue, 2);
              } else {
                result = $filter(filterType)(newValue, filterFormat);
              }

              $scope.value = result;
            }
          }
        }, true);
      }
    }],
    template: '<p class="form-control-static" ng-bind="value | translate"></p>'
  })
  .component('gecValueNumber', {
    bindings: {
      config: '<',
      model: '='
    },
    templateUrl: '/js/app/modules/ui/form/templates/inputs/labelnumber.html'
  })
  .component('gecValueAmount', {
    bindings: {
      config: '<',
      model: '='
    },
    templateUrl: '/js/app/modules/ui/form/templates/inputs/amount.html'
  })
  .component('gecValueDatetime', {
    bindings: {
      config: '<',
      model: '='
    },
    templateUrl: '/js/app/modules/ui/form/templates/inputs/datetime.html'
  })
  .component('gecInputPassword', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', function ($scope) {
      var self = this;
      self.$onInit = function () {
        if (self.config.defaultValue && !self.model) self.model = self.config.defaultValue;
      };

    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/password-input.html'
  })
  .component('gecAccountNo', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {


      this.$onInit = function () {

      }


    }],
    template: '<input type="text" ng-disabled="$ctrl.disabled" ui-mask="999-9-99999-9" ng-model="$ctrl.model" ui-mask-placeholder="" class="form-control" />'
  })
  .component('gecImageForm', {
    bindings: {
      config: '<',
      model: '='
    },
    templateUrl: '/js/app/modules/ui/form/templates/inputs/image-form.html'
  })
  .component('gecLabel', {
    bindings: {
      config: '<',
      suffixesConfig: '<',
      inputDisabled: '=inputDisabled',
      model: '='
    },
    templateUrl: '/js/app/modules/ui/form/templates/inputs/form-label.html',
    controller: ['$rootScope', '$scope', '$translate', function ($rootScope, $scope, $translate) {

      var self = this;
      self.show = true;
      self.onCheckValue = function () {
        if (self.inputDisabled) $scope.$parent.$parent.$ctrl.data.expiryDate = null;
      };
      function checkDisplayValue() {
        if ($scope.$parent.$parent.config.label.type == 'checkbox' && $scope.$parent.$parent.$ctrl.data.expiryDate)
          self.inputDisabled = false;
      };

      if ($scope.$parent.$parent.config.label.type == 'checkbox') {
        $scope.$watch('$parent.$parent.$ctrl.data.expiryDate', function (val) {
          checkDisplayValue();
        });
      }


      if (angular.isDefined(self.config)) {
        if (self.config.type == 'radio') {

          $scope.$watch('$ctrl.model', function (val) {

            if (val === self.config.radioValue) {
              self.inputDisabled = false;
            } else {
              self.inputDisabled = true;
            }

          });
        }

        this.$onInit = function () {
          if (angular.isDefined(self.config.type)) {
            self.type = self.config.type;
            self.value = self.config.displayValue;
          }
          else {
            if (angular.isObject(self.config)) {
            	var curLang = $translate.use();
                self.value = self.config[curLang];
                $rootScope.$on('$translateChangeSuccess', function (translateChangeSuccess, currentLange) {
                	self.value = self.config[currentLange.language];
                });
            }
            else if (angular.isString(self.config)) {
              self.value = self.config;
            }
            
            if(angular.isDefined(self.suffixesConfig) && angular.isString(self.suffixesConfig)){
            	self.suffixesValue = self.suffixesConfig;
            }
          }

          if (angular.isDefined(self.config.default) && self.config.default) {
            self.model = self.config.radioValue;
          }

        }
      }
      else {
        self.show = false
      }

    }]
  })
  .directive('gecInputNumberOld', [function () {
    return {
      restrict: 'E',
      replace: true,
      template: '<input type="text" class="form-control" />',
      require: 'ngModel',
      link: function (scope, element, attr, ngModelCtrl) {

        var parser = function (input) {

          if (input) {
            var transformed = input.replace(/[^0-9]/g, '');
            if (transformed !== input) {
              ngModelCtrl.$setViewValue(transformed);
              ngModelCtrl.$render();
            }
            return transformed;
          }
          return undefined;
        }

        ngModelCtrl.$parsers.push(parser);
      }
    };
  }])
  .component('gecGroupChecklist', {
    bindings: {
      config: '=',
      model: '='
    },
    require: {
      form: '^gecForm'
    },
    controller: ['$rootScope', '$scope', '$filter', 'blockUI', 'ui', 'validationFactory', 'handlerService', function ($rootScope, $scope, $filter, blockUI, ui, validationFactory, handlerService) {
      var self = this;

      if (angular.isUndefined(self.config.name)) {
        self.config.name = ui.randomId('group-checklist');
      }

      self.itemId = function (item) {
        if (angular.isDefined(self.config.itemIdField)) {
          return item[self.config.itemIdField];
        }
        else {
          return item;
        }
      }

      self.label = function (item) {
        if (angular.isDefined(self.config.itemLabelField)) {
          return item[self.config.itemLabelField];
        }
        else {
          return item;
        }
      }

      self.compareItem = function (obj1, obj2) {
        if (obj1 !== undefined && obj2 !== undefined) {
          if (angular.isDefined(self.config.itemIdField)) {
            return obj1[self.config.itemIdField] === obj2[self.config.itemIdField];
          }
          else {
            return obj1 === obj2;
          }
        } else {
          return false;
        }
      }

      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.groups = [];
            var groupBlockUI = blockUI.instances.get(self.config.name + '-group-checklist');
            // Start blocking the element.
            groupBlockUI.start();
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.groups = response.data;
              groupBlockUI.stop();
            });
          }
        }
      }

      self.$onDestroy = function () {
        $scope.$on(self.config.name + ':load', angular.noop);
        $scope.$on(self.form.name + ':clear', angular.noop);
      }
      self.$onInit = function () {
        var autoLoad = true;
        if (angular.isDefined(self.config.autoLoad)) {
          autoLoad = self.config.autoLoad;
        }

        if (autoLoad) {
          _loadData();
        }

        $scope.$on(self.config.name + ':load', function (event, data) {
          console.debug('Random ' + ui.randomId('group-checklist'));
          _loadData(data);
        });

        $scope.$on(self.form.name + ':clear', function () {
          $scope.errors = {};
        });

        if (angular.isDefined(self.config.required) && self.config.required) {
          self.form.addValidation(function () {
            $scope.errors = {};

            if (self.model == null || self.model.length == 0) {
              $scope.errors[self.config.name] = {
                cause: 'errors.core.required',
                params: { field: $filter('translate')(self.config.label) }
              };
            }
            return angular.isUndefined($scope.errors[self.config.name]);
          })
        }
      }

    }],
    templateUrl: 'ui/template/gecGroupChecklist.tpl'
  })
  .component('gecChecklist', {
    bindings: {
      config: '<',
      model: '=',
      datas: '=',
      disabled: '=ngDisabled'
    },
    require: {
      form: '^gecForm'
    },
    controller: ['$rootScope', '$scope', '$filter', 'ui', 'validationFactory', 'handlerService',
      function ($rootScope, $scope, $filter, ui, validationFactory, handlerService) {
        var self = this;

        if (angular.isUndefined(self.config.name))
          self.config.name = ui.randomId('checklist');

        if (angular.isDefined(self.datas[self.config.comparator]))
          if (!angular.isFunction(self.datas[self.config.comparator]))
            throw 'Type error!! :: comparator is not function!! : ' + self.config.comparator + ' : ' + self.datas[self.config.comparator];

        if (angular.isDefined(self.config.items))
          self.items = self.config.items;

        $scope.$on(self.config.name + ':load', function (event, data) { _loadData(data); });

        function prepareCheckList() {
          self.doList = {};
          self.doList.items =
            (self.datas[self.config.items] && self.datas[self.config.items].length > 0)
              ? self.datas[self.config.items]
              : self.config.options;

          self.doList.comparator = function productTypeComparison(obj1, obj2) {
            return obj1.value === obj2.value;
          }
        }

        function _loadData(data) {
          prepareCheckList();

          if (angular.isDefined(self.config.fetching)) {
            self.value = [];
            if (angular.isDefined(self.config.fetching.handler)) {
              handlerService.handle(self.config.fetching.handler, data).then(function (response) {
                self.value = response.data;
              });
            } else if (angular.isDefined(self.config.fetching.referTo)) {
              self.value = self.config.fetching.referTo;
            }
          }
        }

        self.$onInit = function () {
          _loadData();

          $scope.$on(self.form.name + ':clear', function () { $scope.errors = {}; });

          if (angular.isDefined(self.config.required) && self.config.required) {
            self.form.addValidation(function () {
              $scope.errors = {};

              if (self.model == null || self.model.length == 0) {
                $scope.errors[self.config.name] = {
                  cause: 'errors.core.required',
                  params: { field: $filter('translate')(self.config.label) }
                };
              }
              return angular.isUndefined($scope.errors[self.config.name]);
            })
          }
        }

      }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/check-list.html',

  })
  .component('gecFormTable', {
    bindings: {
      config: '<',
      model: '='
    },
    require: {
	  form: '^gecForm'
	},
    controller: ['$rootScope', '$scope', '$filter', 'ui', 'dialog', 'handlerService', function ($rootScope, $scope, $filter, ui, dialog, handlerService) {
      var self = this;
      $scope.viewMode = true;
      self.addBtnDisabled = false;
      
      if (angular.isDefined(self.config.disabledAdd)){
      	self.addBtnDisabled = self.config.disabledAdd;
      }
      
      $rootScope.$on(self.config.name + ':enabled', function (event, data) {
        self.addBtnDisabled = false;
      });
      
      $rootScope.$on(self.config.name + ':disabled', function (event, data) {
        self.addBtnDisabled = true;
      });
      
      if (angular.isDefined(self.config.form)) {
        $scope.viewMode = false;
        self.config.form.submission = function (data, callback) {
          if (angular.isUndefined(self.model)) {
            self.model = [];
          }

          if (angular.isDefined(self.config.transformer)) {
            data = handlerService.handle(self.config.transformer.handler, data);
          }
          
          var duplicate = false;
          
          if (angular.isDefined(self.config.checking)) {
            duplicate = handlerService.handle(self.config.checking.handler, data);
          }
          
          if (angular.isDefined(self.config.checkingList)) {
            data = handlerService.handle(self.config.checkingList.handler, data);
          }
          
          if (angular.isArray(data)) {
          	data.forEach(function (d) {
          		self.model.push(d);
          	});
          }else{
          	if (!duplicate){
	          	self.model.push(data);
	         }
          }
          
          if (angular.isDefined(callback)) {
            callback(data);
          }
        }

        self.add = function () {
          dialog.form({
            data: {
              form: self.config.form,
              model: {},
              footer: {
                actions: [{
                  label: 'core.button.add',
                  type: 'submit',
                  buttonType: 'primary'
                }]
              },
              headerMessage: ( angular.isDefined(self.config.dialogTitle) ?  self.config.dialogTitle : 
              	(angular.isDefined(self.config.addButtonLabel) ? self.config.addButtonLabel : ''))
            }
          });
        }
      }
      
      self.sort = function() {
          if (angular.isDefined(self.model) && angular.isDefined(self.config.sortBy)) {
            var data = angular.copy(self.model);
            self.model = $filter('orderBy')(data, self.config.sortBy);
          }
        }
        
        self.deleteItem = function (record) {
          var index = self.model.indexOf(record);
          if (index > -1) {
            self.model.splice(index, 1);
          }
        }
      
      	self.menuContext = function (record) {
      		var result = [];
      		var deleteBtn = {html: ('<a class="dropdown-item" href="#"><span>'
	                    	        + '<i class="fa fa-trash aria-hidden="true"></i>'
	                    	        + $filter('translate')('core.button.delete')+'</span></a>') , 
	                    	        click : function ($itemScope, $event, modelValue, text, $li) {
	                    	        	self.deleteItem(modelValue);
	                    	        }}
      	    if (angular.isDefined(self.config.actions)){
      	    	result = self.config.actions.filter(function(it){
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
                           handlerService.handle(action.handler, modelValue);
                    	}
                     }
                   };
                });
	            if (self.config.showDeleteBtn){
	            	if (angular.isArray(result)){
	            		result = result.concat(deleteBtn);
	            	}else{
	            		result = [].concat(deleteBtn);
	            	}
	            }
      	    }else{
      	    	if (self.config.showDeleteBtn){
	            	result = [].concat(deleteBtn);
	            }
      	    }
            return result;
        }
      
      self.$onInit = function () {
        if (angular.isDefined(self.config.required) && self.config.required) {
          console.debug(self.form);
          self.form.addValidation(function () {
            $scope.errors = {};

            if (self.model == null || self.model.length == 0) {
              $scope.errors[self.config.name] = {
                cause: 'errors.core.required',
                params: { field: $filter('translate')(self.config.label) }
              };
            }
            return angular.isUndefined($scope.errors[self.config.name]);
          })
        }
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/form-table.html'
  })
  .component('gecSelectionTable', {
    bindings: {
      config: '<',
      model: '='
    },
    require: {
      form: '^gecForm'
    },
    controller: ['$rootScope', '$scope', '$filter', 'ui', 'handlerService', function ($rootScope, $scope, $filter, ui, handlerService) {
      var self = this;

      if (angular.isUndefined(self.config.name)) {
        self.config.name = ui.randomId('selection-table');
      }

      $scope.selectAll = function (isChecked) {
        if (isChecked) {
          self.model = angular.copy(self.data);
        }
        else {
          self.model = [];
        }
        $scope.isSelectAll = !isChecked;
      }

      self.itemId = function (item) {
        if (angular.isDefined(self.config.itemIdField)) {
          return item[self.config.itemIdField];
        }
        else {
          return item;
        }
      }


      $scope.$on(self.config.name + ':load', function (event, data) {
        _loadData(data);
      });

      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.data = [];
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.data = response.data;
            });
          }
        }
      }

      self.$onInit = function () {
        _loadData();
        
        if (angular.isDefined(self.config.required) && self.config.required) {
            self.form.addValidation(function () {
              $scope.errors = {};
              if (self.model == null || self.model.length == 0) {
                $scope.errors[self.config.name] = {
                  cause: 'errors.core.required',
                  params: { field: $filter('translate')(self.config.label) }
                };
              }
              return angular.isUndefined($scope.errors[self.config.name]);
            })
          }
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/selection-table.html'
  }).component('gecPolicies', {
    bindings: {
      config: '<',
      model: '='
    },
    controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
      var self = this;

      $scope.$on(self.config.name + ':load', function (event, data) {
        _loadData(data);
      });

      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.model = [];
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.model = response.data;
            });
          }
        }
      }

      self.$onInit = function () {
        _loadData();
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/policies.html'
  }).component('gecViewPaymentTransaction', {
    bindings: {
      config: '<',
      model: '='
    },
    controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
      var self = this;

      $scope.$on(self.config.name + ':load', function (event, data) {
        _loadData(data);
      });

      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.model = [];
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.model = response.data;
            });
          }
        }
      }

      self.$onInit = function () {
        _loadData();
        self.prefixLanguageKey = self.config.prefixLanguageKey;
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/payment-view.html'
  }).component('gecViewLoanTransaction', {
    bindings: {
      config: '<',
      model: '='
    },
    controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
      var self = this;

      $scope.$on(self.config.name + ':load', function (event, data) {
        _loadData(data);
      });

      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.model = [];
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.model = response.data;
            });
          }
        }
      }

      self.$onInit = function () {
        _loadData();
        self.prefixLanguageKey = self.config.prefixLanguageKey;
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/loan-view.html'
  }).component('gecCreateLoanSearch', {
    bindings: {
      config: '<',
      model: '='
    },
    controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
      var self = this;
      $scope.$on(self.config.name + ':load', function (event, data) {
        _loadData(data);
      });
      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.model = [];
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.model = response.data;
            });
          }
        }
      }
      self.$onInit = function () {
        _loadData();
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/create-loan-search.html'
  }).component('gecCreateLoanSelection', {
    bindings: {
      config: '<',
      model: '='
    },
    controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
      var self = this;
      $scope.$on(self.config.name + ':load', function (event, data) {
        _loadData(data);
      });
      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.model = [];
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.model = response.data;
            });
          }
        }
      }
      self.$onInit = function () {
        _loadData();
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/create-loan-selection.html'
  }).component('gecCreateLoanPlan', {
    bindings: {
      config: '<',
      model: '='
    },
    controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
      var self = this;
      $scope.$on(self.config.name + ':load', function (event, data) {
        _loadData(data);
      });
      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.model = [];
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.model = response.data;
            });
          }
        }
      }
      self.$onInit = function () {
        _loadData();
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/create-loan-plan.html'
  }).component('gecCreateLoanSubmitPlan', {
    bindings: {
      config: '<',
      model: '='
    },
    controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
      var self = this;
      $scope.$on(self.config.name + ':load', function (event, data) {
        _loadData(data);
      });
      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.model = [];
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.model = response.data;
            });
          }
        }
      }
      self.$onInit = function () {
        _loadData();
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/create-loan-submit-plan.html'
  }).component('gecTransactionTable', {
    bindings: {
      config: '<',
      model: '='
    },
    controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
      var self = this;
      $scope.$on(self.config.name + ':load', function (event, data) {
        _loadData(data);
      });
      var _loadData = function (data) {
        if (angular.isDefined(self.config.fetching)) {
          if (angular.isDefined(self.config.fetching.handler)) {
            self.model = [];
            handlerService.handle(self.config.fetching.handler, data).then(function (response) {
              self.model = response.data;
            });
          }
        }
      }
      self.$onInit = function () {
        _loadData();
      }
    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/transaction-table.html'
  }).component('gecAccountDisplay', {
    bindings: {
      config: '<',
      model: '<',
      formData: '<'
    },
    templateUrl: '/js/app/modules/ui/form/templates/inputs/account-display.html'
  })
  .directive('convertToNumber', function () {
    return {
      require: 'ngModel',
      link: function (scope, element, attrs, ngModel) {
        ngModel.$parsers.push(function (val) {
          return val != '' ? parseInt(val, 10) : null;
        });
        ngModel.$formatters.push(function (val) {
          return val != null ? '' + val : null;
        });
      }
    };
  })
  .directive('gecOrganizationLogo', ['ui', function (ui) {
    return {
      restrict: 'E',
      transclude: true,
      replace: false,
      scope: {
        model: '=',
        ngModel: '=',
        id: '=',
        name: '=',
        logoAttr: '<',
        nameAttr: '<',
        objAttr: '@',
        idAttr: '@'
      },
      link: function (scope, elements, attrs) {
        var logoAttr = scope.logoAttr || 'fundingLogo';
        var nameAttr = scope.nameAttr || 'fundingName';
        console.log(scope.nameAttr);
        console.log(scope.objAttr);
        console.log(scope.ngModel);
        var idAttr = scope.idAttr || 'fundingId';
        scope.decodeBase64 = function (data) {
          var isBase64 = false;
          try {
            isBase64 = btoa(atob(str)) == str;
          } catch (err) {
            isBase64 = false;
          }
          return (data ? (isBase64 ? atob(data) : data) : ui.constants.NOLOGO);
        };

        if (!angular.isArray(scope.ngModel)) {
          scope.ngModel = [scope.ngModel];
        }
        scope.$watch("ngModel", function (newValue, oldValue) {
          scope.organizations = {};
          angular.forEach(newValue, function (value) {
            var val = scope.objAttr == undefined || scope.objAttr == '' ? value : value[scope.objAttr];
            this[val[idAttr]] = {
              logo: val[logoAttr],
              name: val[nameAttr]
            };
          }, scope.organizations);
        });

      },
      templateUrl: 'ui/template/gecOrganizationLogo.html'
    }
  }])
  .directive('convertToString', function () {
    return {
      require: 'ngModel',
      link: function (scope, element, attrs, ngModel) {
        ngModel.$parsers.push(function (val) {
          if (angular.isDefined(val)) {
            return angular.isString(val) ? val : val.value;
          }
          else {
            return null;
          }
        });
      }
    };
  }).component('gecInputDialog', {
    bindings: {
      config: '<',
      model: '='
    },
    templateUrl: '/js/app/modules/ui/form/templates/inputs/input-dialog.html',
    controller: ['dialog', function (dialog) {

      var self = this;

      var config = self.config;
      self.displayVale = '';

      if (angular.isDefined(config.dialog.form)) {
        config.dialog.form.submission = function (data, callback) {
          callback(data)
        }
      }

      self.open = function () {
        dialog.custom({
          data: {
            form: config.dialog.form,
            model: angular.copy(self.model),
            headerMessage: (angular.isDefined(config.dialog.title) ? config.dialog.title : '')
          },
          callback: function (obj) {
            if (obj) {
              self.model = obj;
            }
          }

        });
      }

      self.$onInit = function () {

      }

    }]
  }).component('gecLogoAction', {
    bindings: {
      config: '<',
      model: '=',
      data: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', 'handlerService', function ($scope, handlerService) {
      var self = this;

      self.hasLogo = false;
      self.logo = null;

      var _decodeBase64 = function (data) {
        if (data == null || angular.isUndefined(data)) {
          self.hasLogo = false;
          return '';
        }
        return atob(data);
      }

      self.doAction = function () {
        if (angular.isDefined(self.config.action)) {
          handlerService.handle(self.config.action.handler, self.data);
        }
      }

      self.$onInit = function () {
        if (angular.isDefined(self.model) && self.model != null) {
          self.hasLogo = true;
          self.logo = self.model;
        }

        $scope.$watch("$ctrl.model", function (newValue, oldValue) {
          if (newValue != oldValue) {
            self.hasLogo = true;
            self.logo = (newValue);
          }
        }, true);

      }

    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/logo-action.html'
  }).component('gecActions', {
    bindings: {
      config: '<',
      data: '='
    },
    controller: ['$scope', 'handlerService', function ($scope, handlerService) {
      var self = this;
      self.doAction = function (handler) {
        if (angular.isDefined(handler)) {
          handlerService.handle(handler, self.data);
        }
      }

      self.$onInit = function () {

      }

    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/actions.html'
  }).component('gecConfigButton', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', function ($scope) {
      var self = this;

      self.$onInit = function () {

      }

    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/config-button.html'
  })
  .component('gecFileUpload', {
    bindings: {
      fileUpload: '=',
      config: '='
    },
    controller: ['$scope', 'handlerService', function ($scope, handlerService) {
      var self = this;
      self.acceptFileExtention = '';
      self.upload = function () {
        if (angular.isDefined(self.config.handler)) {
          handlerService.handle(self.config.handler, self.fileUpload);
        }
      }

      function _load(data) {
        if (angular.isDefined(self.config.extensions)) {
          if (angular.isDefined(self.config.extensions.fetching)) {
            handlerService.handle(self.config.extensions.fetching.handler, data).then(function (response) {
              $scope.acceptFileExtention = response || '';
            });
          }
          else {
            $scope.acceptFileExtention = self.config.extensions;
            console.debug($scope.acceptFileExtention);
          }
        }

      }
      if (angular.isDefined(self.config.extensions.fetching)) {
        $scope.$on(self.config.name + ':load-extensions', function (event, data) {
          _load(data);
        });
      }

      self.$onInit = function () {
        $scope.fileName = '';
        if (angular.isDefined(self.config.handler)) {
          $scope.hasAction = true;
        }

        $scope.$watch('$ctrl.fileUpload', function () {
          if (!angular.isUndefined(self.fileUpload)) {
            $scope.fileName = self.fileUpload.name || '';
          }
        });

        if (angular.isUndefined(self.config.autoLoad) || angular.isDefined(self.config.extensions.fetching)) {
          self.config.autoLoad = true;
        }
        if (self.config.autoLoad) {
          _load({});
        }
      }

    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/file-upload.html'
  })
  .directive('gecFileModel', ['$parse', function ($parse) {
    return {
      restrict: 'A',
      link: function (scope, elements, attrs) {
        var model = $parse(attrs.gecFileModel);

        var modelSetter = model.assign;

        elements.bind('change', function () {
          scope.$apply(function () {
            modelSetter(scope, elements[0].files[0]);
          });
        });
      }
    }
  }])
  .component('gecCreditTermMultipleCheckbox', {
    bindings: {
      config: '<',
      model: '=',
      disabled: '=ngDisabled'
    },
    controller: ['$scope', function ($scope) {
      var self = this;

      this.$onInit = function () {

      }

    }],
    templateUrl: '/js/app/modules/ui/form/templates/inputs/credit-term-multile-checkbox.html'
  }).component('gecDualControlHistory', {
	  bindings: {
		  config: '<',
		  model: '='
	  },
	  controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
    	  var self = this;
    	  $scope.$on(self.config.name + ':load', function (event, data) {
    		  _loadData(data);
    	  });
    	  var _loadData = function (data) {
    		  if (angular.isDefined(self.config.fetching)) {
    			  if (angular.isDefined(self.config.fetching.handler)) {
    				  self.model = [];
    				  handlerService.handle(self.config.fetching.handler, data).then(function (response) {
    					  self.model = response.data;
    				  });
    			  }
    		  }
    	  }
    	  self.$onInit = function () {
    		  _loadData();
    		  self.prefixLanguageKey = self.config.prefixLanguageKey;
    	  }
	  }],
      templateUrl: '/js/app/modules/ui/form/templates/dual-control/dual-control-history.html'
  }).component('gecDualControlGeneralData', {
	  bindings: {
		  config: '<',
		  model: '='
	  },
	  controller: ['$rootScope', '$scope', 'ui', 'handlerService', function ($rootScope, $scope, ui, handlerService) {
    	  var self = this;
    	  $scope.$on(self.config.name + ':load', function (event, data) {
    		  _loadData(data);
    	  });
    	  var _loadData = function (data) {
    		  if (angular.isDefined(self.config.fetching)) {
    			  if (angular.isDefined(self.config.fetching.handler)) {
    				  self.model = [];
    				  handlerService.handle(self.config.fetching.handler, data).then(function (response) {
    					  self.model = response.data;
    				  });
    			  }
    		  }
    	  }
    	  self.$onInit = function () {
    		  _loadData();
    		  self.prefixLanguageKey = self.config.prefixLanguageKey;
    	  }
	  }],
      templateUrl: '/js/app/modules/ui/form/templates/dual-control/dual-control-general-data.html'
  });