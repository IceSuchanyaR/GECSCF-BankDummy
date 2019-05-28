angular.module('gec.logTransaction')
  .controller('logTransactionViewController',
    ['$scope', '$stateParams', 'logTransactionViewService', function ($scope, $stateParams, logTransactionViewService) {
      var self = this;

      self.view = {};
      $scope.params = {};
      self.total = [];
      var logID = $stateParams.id;


      self.getlogTransactionBylogID = function (stateParams) {
        return logTransactionViewService.getlogTransactionBylogID(logID).then(function (response) {
          self.view = response.data;
          self.view.updateTime = buildDateTime(self.view.updateTime);
          console.log(new Date(self.view.updateTime).getFullYear());
        });
      };

      function buildDateTime(time) {
        var dateTime = new Date(time);

        var day = dateTime.getDate();
        day = day.toString().length == 1 ? '0' + day : day;

        var mounth = dateTime.getMonth() + 1;
        mounth = (mounth + 1).toString().length == 1 ? '0' + mounth : mounth;

        var hh = dateTime.getHours();
        hh = hh.toString().length == 1 ? '0' + hh : hh;

        var mm = dateTime.getMinutes();
        mm = mm.toString().length == 1 ? '0' + mm : mm;

        var ss = dateTime.getSeconds();
        ss = ss.toString().length == 1 ? '0' + ss : ss;

        var result = day + '/' + mounth + '/' + dateTime.getFullYear() + '  ' + hh + ':' + mm + ':' + ss;
        return result;
      }

      self.getlogTransactionBylogID();

    }]);
