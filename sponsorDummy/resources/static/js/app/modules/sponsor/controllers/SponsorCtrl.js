(function () {
  'use strict';

  angular.module('gec.sponsor').controller('SponsorCtrl', SponsorCtrl);
  SponsorCtrl.$inject = ['sponsorService', '$q', 'page'];
  function SponsorCtrl(sponsorService, $q, page) {
    page.onInit(function (criteria, data) {

    });
    page.handler('getSponsors', sponsorService.getSponsors);
    page.build(this);
  }

})();
