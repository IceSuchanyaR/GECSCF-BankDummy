'use strict';
angular
    .module('gec.ui.table')
    .directive('checkImage',['UIFactory','$http', function(UIFactory,$http) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                attrs.$observe('ngSrc', function(ngSrc) {
                    $http.get(ngSrc).success(function(){
                    }).error(function(){
                        element.attr('src', 'data:image/png;base64,' + UIFactory.constants.NOLOGO); // set default image
                    });
                });
            }
        };
    }]);