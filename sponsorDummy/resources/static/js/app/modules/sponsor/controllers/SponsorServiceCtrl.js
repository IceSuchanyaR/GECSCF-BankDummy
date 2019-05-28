(function () {
  'use strict';

  angular.module('gec.sponsor').controller('SponsorServiceCtrl', SponsorServiceCtrl);
  SponsorServiceCtrl.$inject = ['sponsorService', 'pageNavigator', 'page'];
  function SponsorServiceCtrl(sponsorService, pageNavigator, page) {
    var self = this;
    var sponsorId;
    page.onInit(function (params, criteria, data) {
      sponsorId = params.sponsorId;

    });
    page.handler('getServices', function (params) { return sponsorService.getServices(sponsorId); });
    page.handler('pageNavigator', function (params) {
      if (params.serviceType == 'UPLOAD')
        pageNavigator.navigate({ to: 'sponsors.services.ptt-document-migration' }, params);
      if (params.serviceType == 'CHECK')
        pageNavigator.navigate({ to: 'sponsors.services.ptt-document-status' }, params);
    });
    page.build(this);
  }



})();
