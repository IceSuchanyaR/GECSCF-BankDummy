(function () {
  'use strict';

  angular.module('gec.sponsor').controller('PTTDocumentStatusCtrl', PTTDocumentStatusCtrl);
  PTTDocumentStatusCtrl.$inject = ['sponsorService', 'page'];
  function PTTDocumentStatusCtrl(sponsorService, page) {
    page.onInit(function (criteria, data) {

    });
    page.handler('getDocumentCheck', sponsorService.getDocumentCheck);
    page.build(this);
  }

})();
