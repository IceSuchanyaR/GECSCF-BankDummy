'use strict';
angular.module('gec.sponsor', ['ui.router', 'gec.ui', 'oc.lazyLoad'])
  .config(['$stateProvider', function ($stateProvider) {

    var dependencies = ['/js/app/modules/sponsor/services/SponsorService.js',];

    $stateProvider.state(WebHelper.createListPageState({
      name: 'sponsors',
      url: '/sponsors',
      yaml: '/js/app/modules/sponsor/pages/sponsors.yaml',
      controller: 'SponsorCtrl',
      dependencies: dependencies
        .concat('/js/app/modules/sponsor/controllers/SponsorCtrl.js')
    }));

    $stateProvider.state(WebHelper.createFormPageState({
      name: 'sponsors.services',
      url: '/{sponsorId}/services',
      yaml: '/js/app/modules/sponsor/pages/sponsors.services.yaml',
      controller: 'SponsorServiceCtrl',
      formType: 'form-with-table',
      dependencies: dependencies
        .concat('/js/app/modules/sponsor/controllers/SponsorServiceCtrl.js')
    }));

    $stateProvider.state(WebHelper.createFormPageState({
      name: 'sponsors.services.ptt-document-migration',
      url: '/ptt-document-migration',
      yaml: '/js/app/modules/sponsor/pages/sponsors.services.ptt-document-migration.yaml',
      controller: 'PTTDocumentMigrationCtrl',
      dependencies: dependencies
        .concat('/js/app/modules/sponsor/controllers/PTTDocumentMigrationCtrl.js')
    }));

    $stateProvider.state(WebHelper.createFormPageState({
      formType: 'form-with-table',
      name: 'sponsors.services.ptt-document-status',
      url: '/ptt-document-status',
      yaml: '/js/app/modules/sponsor/pages/sponsors.services.ptt-document-status.yaml',
      controller: 'PTTDocumentStatusCtrl',
      dependencies: dependencies
        .concat('/js/app/modules/sponsor/controllers/PTTDocumentStatusCtrl.js')
    }));

  }]);
