'use strict';
angular.module('gec.ui', ['ui.router', 'ngDialog', 'blockUI', 'gec.core', 'gec.ui.form', 'gec.ui.dialog', 'gec.ui.table', 'gec.ui.custom'])
.config(['blockUIConfig', 'ngDialogProvider', function (blockUIConfig, ngDialogProvider) {

  // Change the default delay to 100ms before the blocking is visible
 blockUIConfig.delay = 100;
 
 ngDialogProvider.setDefaults({
   className: 'ngdialog-theme-default',
   plain: false,
   showClose: false,
   closeByDocument: false,
   closeByEscape: false,
   appendTo: false,
   disableAnimation: true,
   cache: false
 });
 ngDialogProvider.setOpenOnePerName(true);

}])
  .run(["$templateCache", function ($templateCache) {

    $templateCache
      .put(
        'ui/template/gecOrganizationLogo.html',
        '<div style="display: inline-block"><img  ng-repeat="org in organizations" style="height: 32px; width: 32px;float: left;margin-left: 2px;" data-ng-src="data:image/png;base64,{{decodeBase64(org.logo)}}" title="{{org.name}}"></img>'
        + '</div>');

    $templateCache
      .put('ui/template/gecGroupChecklist.tpl',
        '<div class="form-group col-12"><label class="control-label" for="{{$ctrl.config.name}}" ng-bind="$ctrl.config.label | translate"></label><div ng-repeat="group in $ctrl.groups track by $index" class="col-12 form-group row"><label id="{{group[$ctrl.config.groupLabelField]}}-label" class="col-12 control-label">{{group[$ctrl.config.groupLabelField]}}</label><div ng-repeat="item in group[$ctrl.config.groupItemsField] track by $ctrl.itemId(item)" class="col-md-6 control-label"><div class="col row"><span class="col-lg-12 col-12"> <label class="checkbox-inline label-thin"> <input type="checkbox" checklist-model="$ctrl.model" checklist-value="item" checklist-comparator="$ctrl.compareItem" ng-disabled="$ctrl.config.disabled" ng-click="toggleSelection()" ng-init="toggleSelection()" /> <span>{{$ctrl.label(item)}}</span></label></span></div></div></div><span class="error-msg" ng-bind="errors[$ctrl.config.name].cause | translate:errors[$ctrl.config.name].params "></span></div>'
      );

    $templateCache
      .put('ui/template/gecDropdown.tpl',
        '<select class="form-control" name="{{$ctrl.config.name}}" ng-disabled="$ctrl.disabled" ng-model="$ctrl.model" ng-if="!$ctrl.config.convertToNumber" ng-change="$ctrl.change()"> <option ng-repeat="opt in options track by opt.value" value="{{opt.value}}">{{opt.label | translate}}</option> </select> <select class="form-control" ng-change="$ctrl.change()" ng-model="$ctrl.model" ng-disabled="$ctrl.disabled" ng-if="$ctrl.config.convertToNumber" convert-to-number> <option ng-repeat="opt in options track by opt.value" value="{{opt.value}}">{{opt.label | translate}}</option> </select>');
  }])
  .constant('NOLOGO', 'iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAADQklEQVRYhe2WwYscRRTGf68dhrBsBpEEprNLMLtBJGfZUw7eBFEIohcXBJnGgAvTehIPIuJBPEjsOUTEWQTRgBhEFvToX6AHIQcvRpBgS9jDMixhWTb9eZiq7tqd6t24K17Mg2G6Xr/3va+qvqrX8H83OyogHWanDHv0OOBCW+VovHNYTOcBcJ4T+vQ4BICrwM2TETC6wGPHJNA9Er6fD75pfSn7CGMJ+Mq5KkkVgJlBM4E9STh/AiTOv4r4Vaa322p0DHuxnR5f73fohpmtuUFP0m+u6LKZTVzMe2BvBBj9w2rUWyBUIfbqPLPY8u2VxXgCkA4zvwogJuXI+fPBbqyQpD2gcqQ6hiXQLBWIDYxljGXgpTbGJ7CrNb7Y8M56BcxsuyzGd9wsFuMYtpIOs2tu0A3yP0iHmZ/55WgmbJbFusPPtmcIPKBdwrgUQX/9H+I0qWmeyT1PEH9OvZoDO890K7rUp0C3gB9cfBds6PwjwK/A02Ar7nkV2AK+B91Btu0InwN6EIpQzANL04ElFr8jfyqL9bcA0mHWq2cuezcQ4YfAysFEiT5ehKLj8ZMg5maFna2ws6BnouVPZqsNfnM7BiJk927xmZ/FvRaQ+TTPlvyzqC+fxxthKdo3DHbujjx+Ldh9IkzOvflaAqDqfkLU7AXgSgNqPv9n75OIb5+F+FWNHxJ4VlX1i4ueixNQBRZ2t3n3X/ts2jsil5hdU1W97wb1MQ9n2gOecL/zcQJ8WRbj02UxPo1YYKr8XcRC7YePW3IXA/zeDAGhb4EFYEHS8y0gxzZJr9b401pAKEJspyzGmwBpPtj6twkYTBr8rN6yQANK+sPBVCS0fipdTvPB5y6+ixeh6ZOgCT0VS5SBxwclvkIowitm/O4InGqZx0XgYsT/cjx+n62b4Y/3Ge8MCNgcTnxHfigCClo3gNnhfcWwMzF/9LwL3QCuI24fgrkGXHC/QTtT/gCuS/oi9rqFtb1TFuPbAGmePRmNMDZ9++7ng822dStH41vAWj/PFoFXDr5vufH+O3tIoONEctAahYt7EI0JeoLttOA0XXV6amZiOoZdOOgU96tg+J1hG7MxCmN+jOOEMdVfxiMzMQ/tb9luGPW21lHPAAAAAElFTkSuQmCC')
  .factory('ui', ['ngDialog', 'NOLOGO', function (ngDialog, NOLOGO) {

    var randomId = function (prefix) {
      var text = "";
      var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

      for (var i = 0; i < 5; i++) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
      }

      return (prefix ? prefix : '') + text;
    }

    return {
      randomId: randomId,
      constants: {
        NOLOGO: NOLOGO
      }
    }

  }])
  .factory('handlerService', ["$rootScope", "$q", '$log', 'blockUI', "dialog", "pageNavigator", function ($rootScope, $q, $log, blockUI, dialog, pageNavigator) {
    var handler = function (h, handler) {
      if (handler) {
        this.register(h, handler);
      }
      return this.handlers[h];
    };

    var register = function (h, handler) {
      this.handlers[h] = handler;
    }



    var handle = function (config, data, callback) {

      var self = this;

      var _handle = function (h, data) {
        var handler = self.handler(h);
        if (angular.isUndefined(handler)) {
          $log.error('Not found handler named "' + h + '"');
          $log.debug(self.handlers);
        }
        else {
          return handler(data);
        }
      }

      if (angular.isString(config)) {
        return _handle(config, data);
      }
      else if (angular.isFunction(config)) {
        return config(data, callback);
      }
      else if (angular.isDefined(config.confirmation)) {

        var confirmation = config.confirmation;

        dialog.confirm({
          data: {
            headerMessage: confirmation.message,
            prefixLanguageKey: angular.isDefined(confirmation.success) ? confirmation.success.prefixLanguageKey : null,
            showRequestNo: angular.isDefined(confirmation.success) ? confirmation.success.showRequestNo : false,
            successMessage: angular.isDefined(confirmation.success) ? confirmation.success.message : 'Success',
            failMessage: angular.isDefined(confirmation.fail) ? confirmation.fail.message : 'Fail',
          },
          confirm: function () {
            return angular.isDefined(confirmation.handler) ?
              _handle(confirmation.handler, data) : null;
          },
          onSuccess: function (d, cb) {
            return angular.isDefined(confirmation.success) ?
              self.handle(confirmation.success.handler, d, cb) : null;
          },
          onFail: function (error, cb) {

            return angular.isDefined(confirmation.fail) ?
              self.handle(confirmation.fail.handler, error, cb) : null;
          },
          callback: callback
        });
      }
      else if (angular.isDefined(config.dialog)) {
        var resolved = data;
        if (angular.isDefined(config.dialog.resolve)) {
          resolved = _handle(config.dialog.resolve.handler, data);
          $q.when(resolved).then(function (qData) {
            //this helps me to bind data from $resource or $http or object
            dialog.form({
              data: {
                form: config.dialog.form,
                footer: config.dialog.footer,
                model: qData,
                handle: function (handler, callback) {
                  if (angular.isDefined(handler)) {
                    _handle(handler, data).then(callback);
                  }
                  else {
                    callback();
                  }
                },
                headerMessage: (angular.isDefined(config.dialog.title) ? config.dialog.title : '')
              },
              onClose: function (model) {
                if (angular.isDefined(config.dialog.onClose)) {
                  var params = {
                    newData: model,
                    resolvedData: qData,
                    oldData: data
                  }
                  _handle(config.dialog.onClose.handler, params);
                }
              }
            });
          });
        }
        else {
          dialog.form({
            data: {
              form: config.dialog.form,
              footer: config.dialog.footer,
              model: resolved,
              handle: function (handler, callback) {
                if (angular.isDefined(handler)) {
                  _handle(handler, data).then(callback);
                }
                else {
                  callback();
                }
              },
              headerMessage: (angular.isDefined(config.dialog.title) ? config.dialog.title : '')
            },
            onClose: function (model) {
              if (angular.isDefined(config.dialog.onClose)) {
                var params = {
                  newData: model,
                  resolvedData: resolved,
                  oldData: data
                }
                _handle(config.dialog.onClose.handler, params);
              }
            }
          });
        }
      }
      else if (angular.isDefined(config.navigation)) {
        console.log(config.navigation);
        pageNavigator.navigate(config.navigation, data);
      }
      else if (angular.isDefined(config.event)) {
        $log.debug("Fire " + config.event);
        $rootScope.$broadcast(config.event, data);
      }
      else if (angular.isDefined(config.events)) {
          $log.debug("Fire " + config.event);
          config.events.forEach(function(e){
        	  $rootScope.$broadcast(e, data);
          });
         
        }
      else {

        $log.debug("configuration may be wrong");
        $log.debug(config);
      }
    }

    return {
      handlers: {},
      handler: handler,
      register: register,
      handle: handle
    }
  }])
  .factory('validationFactory', ['$parse', '$filter', function ($parse, $filter) {

    var create = function (config, scope) {

      var validations = [];

      var _isBlank = function (value) {
        return (value == '' || value == null || value == undefined)
      }
      
      // Required
      
      // Format
      if (config.type == 'email') {
    	  validations.push(function (model) {
    	  if(!scope.errors){
   	    	 scope.errors = {};
   	      }

          if (!(/^[A-Za-z0-9._]+@[A-Za-z]+\.[A-Za-z.]{2,5}$/.test(model[config.name]))) {
            scope.errors[config.name] = {
              cause: 'errors.core.invalid_format',
              params: { field: $filter('translate')(config.label) }
            };
          }
          else{
        	scope.errors = {};
          }  
          return angular.isUndefined(scope.errors[config.name]);
        });
      }
      
      if (config.required) {
	      validations.push(function (model) {
	    	if(!scope.errors){
	    	   scope.errors = {};
	    	}
	        
	        var needCheck = true;
	
	        if (angular.isDefined(config.required.condition)) {
	          var _getter = $parse(config.required.condition);
	          needCheck = _getter(model);
	        }
	        if (needCheck && _isBlank(model[config.name])) {
	          scope.errors[config.name] = {
	            cause: 'errors.core.required',
	            params: { field: $filter('translate')(config.label || config.title) }
	          };
	        }
	        else{
	        	scope.errors = {};
	        }
	        
	        return angular.isUndefined(scope.errors[config.name]);
	      });
	  }
      

      if (angular.isDefined(config.validations)) {

        config.validations.forEach(function (validation) {

          // Expression condition
          if (angular.isDefined(validation.condition)) {
            validations.push(function (model) {
              if (angular.isUndefined(scope.errors[config.name])) {
                var _getter = $parse(validation.condition);
                var isValid = _getter(model);
                scope.errors = {};
                if (!isValid) {
                  scope.errors[config.name] = {
                    cause: validation.cause,
                    params: model
                  };
                }
              }
              return angular.isUndefined(scope.errors[config.name]);

            });
          }
        });
      }

      return validations;
    };

    return {
      create: create
    };

  }])
  .factory('visibilityService', ["authorization", "$parse", function (authorization, $parse) {


    var isVisible = function (config, data) {
      var result = true;
      if (angular.isDefined(config.privilege)) {
        if (angular.isDefined(config.privilege.has)) {
          result = authorization.has(config.privilege.has);
        }
        else if (angular.isDefined(config.privilege.hasAny)) {
          result = authorization.hasAny(config.privilege.hasAny);
        }
        else if (angular.isDefined(config.privilege.hasAll)) {
          result = authorization.hasAll(config.privilege.hasAll);
        }
      }
      if (result && angular.isDefined(config.condition)) {
        var _condition = $parse(config.condition);
        result = _condition(data);
      }
      return result;
    }

    return {
      isVisible: isVisible
    }
  }])
  .factory('pageNavigator', [
    '$state',
    '$stateParams',
    '$window',
    '$log',
    'blockUI',
    function ($state, $stateParams, $window, $log, blockUI) {

      var navigate = function (config, param) {
        if (angular.isDefined(config.to)) {

          if (config.to == 'back') {
            $state.go("^");
          }
          else {
            $state.go(config.to, param, {notify: false}).then(function() {
            	$window.scrollTo(0, 0);
            });
          }
        }
      }

      var goTo = function (page, params, keepStateObject) {
        var currentState = $state.current.name == '' ? '/' : $state.current.name;
        if (keepStateObject) {
          pageStates[currentState] = keepStateObject;
        }
        previousPages.push({
          page: currentState
        });

        if (params === undefined) {
          params = {};
        }

        this.isBack = false;
        $state.go(page, params);
      }

      var goBack = function (reset, params) {
        var previousPage = previousPages.pop();
        if (params === undefined) {
          params = {};
        }
        if (previousPage != null) {

          this.isBack = true;
          $state.go(previousPage.page, params);

        } else {
          $state.go("^");
        }
      }

      var getStateData = function () {
        return pageStates[$state.current.name];
      }

      return {
        pageStates: [],
        previousPages: [],
        isBack: false,
        stateData: getStateData,
        goBack: goBack,
        goTo: goTo,
        navigate: navigate
      }

    }
  ]).directive('pageReady', function ($parse) {
    return {
      restrict: 'A',
      link: function ($scope, elem, attrs) {
        elem.ready(function () {
          $scope.$apply(function () {
            var func = $parse(attrs.elemReady);
            func($scope);
          })
        })
      }
    }
  }).filter('bankAccountNo', function () {
    return function (accountNo) {
   	  var accountNoDisplay = "";
      var pattern = new RegExp("^\\d{10}$");
      if (angular.isUndefined(accountNo) || accountNo === null || accountNo === ''){
      	return accountNoDisplay;
      }
      if (accountNo.match(pattern)) {
        var word1 = accountNo.substring(0, 3);
        var word2 = accountNo.substring(3, 4);
        var word3 = accountNo.substring(4, 9);
        var word4 = accountNo.substring(9, 10);
        accountNoDisplay = word1 + '-' + word2 + '-' + word3 + '-' + word4;
      } else {
        accountNoDisplay = accountNo.charAt(0).toUpperCase() + accountNo.slice(1).toLowerCase();
      }
      return accountNoDisplay;
    };
  })
  .factory('dynamicFilterService', ["$http", function ($http) {
	  
	  function getConfigs(forPage){
		  var url = 'js/app/modules/ui/document-filters.json';
		  if(forPage === 'CREATE_PAYMENT'){
			  url = 'js/app/modules/ui/document-filters-create-payment-page.json';
		  } else if(forPage === 'CREATE_SUPPLIER_LOAN'){
			  url = 'js/app/modules/ui/document-filters-create-supplierloan-page.json';
		  }
		  return $http({
				method: 'GET',
				url: url,
				headers: {
					'Content-Type': undefined
				}
			})
	  }
	  return {
		  configs: getConfigs
	  }
  }]);