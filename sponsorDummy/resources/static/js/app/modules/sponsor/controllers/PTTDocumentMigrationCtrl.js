(function () {
  'use strict';

  angular.module('gec.sponsor').controller('PTTDocumentMigrationCtrl', PTTDocumentMigrationCtrl);
  PTTDocumentMigrationCtrl.$inject = ['sponsorService', 'page'];
  function PTTDocumentMigrationCtrl(sponsorService, page) {
    var self = this;

    page.onInit(function (criteria, data) {
      self.model.dataTable = [{}];

    });
    page.handler('insertDocument', sponsorService.insertDocument);
    page.build(this);
  }

})();
