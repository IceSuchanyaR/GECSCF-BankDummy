'use strict';
angular.module('gec.ui.dialog', ['ngDialog', 'gec.ui.form'])
  .factory('dialog', ['ngDialog', 'pageNavigator', 'NOLOGO', function (ngDialog, pageNavigator, NOLOGO) {
    var BASE_TEMPLATE_URL = '/js/app/modules/ui/dialog/templates/';
    var vm = this;

    var confirmDialog = function (config) {
      var self = this;
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'confirm-dialog.html',
        data: config.data,
        preCloseCallback: function (confirm) {
          if (confirm) {
            var promise = config.confirm()
            return promise.then(function (response) {
              self.success({
                data: {
                  headerMessage: config.data.successMessage,
                  prefixLanguageKey: config.data.prefixLanguageKey,
                  requestNo: angular.isDefined(config.data.showRequestNo) ? response.data.requestNo: null
                },
                preCloseCallback: function () {
                  var cb = angular.isDefined(config.callback) ? config.callback : angular.noop;
                  cb(response);
                  if (angular.isDefined(config.onSuccess)) {
                    config.onSuccess(response);
                  }
                }
              });
            }).catch(function (response) {
              self.fail({
                data: {
                  headerMessage: config.data.failMessage,
                  bodyMessage: response.data.code,
                  items: response.data.items
                },
                preCloseCallback: function () {
                  var cb = angular.isDefined(config.callback) ? config.callback : angular.noop;
                  cb(response);
                  if (angular.isDefined(config.onFail)) {
                    config.onFail(response);
                  }

                }
              });
            });
          } else {
            if (config.onCancel != undefined) {
              config.onCancel();
            }
            return true;
          }
        }
      });
    }

    var notifyDialogAdapter = function (config) {
      notifyDialog({
        data: {
          headerMessage: config.headerMessage,
          bodyMessage: config.bodyMessage,
        },
        preCloseCallback: function () {
          var cb = angular.isDefined(config.callback) ? config.callback : angular.noop;
          cb(config.params);

          if (angular.isString(config.navigateTo))
            pageNavigator.navigate({ to: config.navigateTo }, config.params);
          else if (angular.isObject(config.navigate))
            pageNavigator.navigate({ to: config.navigate.to }, config.navigate.params || config.params);
          else if (angular.isObject(config.navigateTo))
            pageNavigator.navigate({ to: config.navigateTo.to }, config.navigateTo.params || config.params);
        }
      });
    };

    var notifyDialog = function (config) {
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'notify-dialog.html',
        preCloseCallback: config.preCloseCallback,
        data: {
          headerMessage: config.headerMessage,
          data: config.data,
          buttons: config.buttons
        }
      });
    }

    var successDialog = function (config) {
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'success-dialog.html',
        preCloseCallback: config.preCloseCallback,
        data: {
          headerMessage: config.headerMessage,
          data: config.data,
          buttons: config.buttons
        }
      });
    }

    var failDialog = function (config) {
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'fail-dialog.html',
        preCloseCallback: config.preCloseCallback,
        data: {
          data: config.data,
          buttons: config.buttons
        }
      });
    }

    var warningDialog = function (config) {
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'warning-dialog.html',
        preCloseCallback: config.preCloseCallback,
        data: {
          data: config.data,
          buttons: config.buttons
        }
      });
    }

    var customDialog = function (config) {
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'custom-dialog.html',
        data: config.data,
        cache: false,
        preCloseCallback: config.callback
      });
    }


    var formDialog = function (config) {
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'form-dialog.html',
        data: config.data,
        cache: true,
        preCloseCallback: function (confirm) {

          if (confirm && angular.isDefined(config.onClose)) {
            config.onClose(confirm);
          }
          config.data = {};
        }
      });
    }

    var transactionSuccessDialog = function (config) {
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'transaction-success-dialog.html',
        preCloseCallback: config.preCloseCallback,
        data: {
          headerMessage: config.headerMessage,
          data: config.data,
          buttons: config.buttons
        }
      });
    }

    var transactionFailDialog = function (config) {
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'transaction-fail-dialog.html',
        preCloseCallback: config.preCloseCallback,
        data: {
          data: config.data,
          buttons: config.buttons
        }
      });
    }

    var transactionIncompleteDialog = function (config) {
      return ngDialog.open({
        template: BASE_TEMPLATE_URL + 'transaction-incomplete-dialog.html',
        preCloseCallback: config.preCloseCallback,
        data: {
          data: config.data,
          buttons: config.buttons
        }
      });
    }

    return {
      notify: notifyDialogAdapter,
      confirm: confirmDialog,
      success: successDialog,
      fail: failDialog,
      warning: warningDialog,
      form: formDialog,
      custom: customDialog,
      transactionSuccess: transactionSuccessDialog,
      transactionFail: transactionFailDialog,
      transactionIncomplete: transactionIncompleteDialog,
    }

  }]) 