(function () {
  'use strict';

  angular.module('gec.sponsor').factory('sponsorService', sponsorService);

  sponsorService.$inject = ['$http', '$q'];
  function sponsorService($http, $q) {

    var services = {
      getSponsors: getSponsors,
      getServices: getServices,
      getDocumentCheck: getDocumentCheck,
      insertDocument: insertDocument,
    };

    return services;

    function getSponsors(params) {
      // Simulator like quary like but actually just mock
      if (params && !params.sponsorName || params.sponsorName && 'ptt'.indexOf(params.sponsorName.toString().toLowerCase()) >= 0) {
        return $http({
          method: 'GET',
          url: 'js/data/sponsor.json',
        });
      } else {
        throw 'Not found sponsors name.';
      }
    }

    function getServices(sponsorId) {
      return $http({
        method: 'GET',
        url: 'js/data/' + sponsorId + '/services.json',
      });
    }

    function getDocumentCheck(params) {
      return $http({
        method: 'GET',
        url: '/document/v1/update-status-document-logs',
        params: params,
      });
    }

    function prepareParam(data) {
      return data.dataTable.map(function (item) {
        //convert string to boolean value delete_document
        if (item.delete_document == 'false') item.delete_document = false;
        if (item.delete_document == 'true') item.delete_document = true;

        //remove undefined
        delete item.undefined;

        //remove null,undefined,blank property object
        var propNames = Object.getOwnPropertyNames(item);
        for (var i = 0; i < propNames.length; i++) {
          var propName = propNames[i];
          if (item[propName] === null || item[propName] === undefined || item[propName] === '') {
            delete item[propName];
          }
        }

        return item;
      });
    }

    function insertDocument(params) {
      console.log(params)
      return $http({
        method: 'POST',
        url: params.url,
        headers: {
          'Authorization': undefined,
          'Content-Type': 'application/json;charset=UTF-8',
          'X-API-Key': params.apiKey
        },
        data: { scode: params.scode, document_list: prepareParam(params), }
      });
    }

  }
})();
