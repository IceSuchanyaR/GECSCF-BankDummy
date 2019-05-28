(function() {
  'use strict';

  angular.module("login", [ 'blockUI', 'ngCookies']).config(['blockUIConfig' , function (blockUIConfig ) {

    // Change the default delay to 100ms before the blocking is visible
   blockUIConfig.delay = 100;

  }]).controller('LoginController', ['$scope', '$window', '$cookieStore', 'authenticationService', function ($scope, $window, $cookieStore, authenticationService) {
    
    var self = this;
    
    $scope.login = login;
    
    self.error = false;
    self.errorMessage = '';
    self.usernameRequired = false;
    self.passwordRequired = false;

    $cookieStore.remove("login_date");
         
    (function initController() {
      authenticationService.clearCredentials();
    })();

    function goToHome(){
      $window.location.href = '/';
    }
    
    function login() {
      self.error = false;
      self.errorMessage = '';
      self.usernameRequired = false;
      self.passwordRequired = false;
       
      var valid = true;
      if(isBlank($scope.username) && isBlank($scope.password)){
        valid = false;
        $scope.usernameRequired = true;
        $scope.passwordRequired = true;
      }else if(isBlank($scope.username)){
        valid = false;
        $scope.usernameRequired = true;
      }else if(isBlank($scope.password)){
        valid = false;
        self.error = true;
        self.errorMessage = 'Invalid username or password.';
      }
      
      if(valid){
        authenticationService.login($scope.username, $scope.password).then(function(success) {
          if(success){
            goToHome();  
          }
          else{
            alert('Login fail');
          }
          
        });
      }
    }
    
    function isBlank(str) {
        return (!str || /^\s*$/.test(str));
    }
    
  }]).factory('authenticationService', ['$http', '$httpParamSerializer', '$cookieStore', 'blockUI', function ($http,  $httpParamSerializer, $cookieStore,  blockUI ) {
     
      var service = {
         login: login,
         clearCredentials: clearCredentials
      };
  
      return service;
      
      // ///////////////////////
      
      function login(username, password){
        blockUI.start("Authentication...");
        var req = {
          method: 'POST',
          url: "/oauth/token",
          headers: {
                "Authorization": "Basic c3ByaW5nLXNlY3VyaXR5LW9hdXRoMi1yZWFkLXdyaXRlLWNsaWVudDpzcHJpbmctc2VjdXJpdHktb2F1dGgyLXJlYWQtd3JpdGUtY2xpZW50LXBhc3N3b3JkMTIzNA==",
                "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
          },
          data: $httpParamSerializer({
            username: username,
            password: password,
            grant_type: 'password',
            client_id: 'spring-security-oauth2-read-write-client'
          })
        }
        return $http(req).then(function(response) {
            $http.defaults.headers.common.Authorization = 
              'Bearer ' + response.data.access_token;
            var expireDate = new Date (new Date().getTime() + (1000 * response.data.expires_in));
            $cookieStore.put("access_token", response.data.access_token, {'expires': expireDate});
            $cookieStore.put("refresh_token", response.data.refresh_token, {'expires': expireDate});
            blockUI.stop();
            return true;
        }).catch(function(response) {
            blockUI.stop();
            return false;
        });
      }
  
      function clearCredentials() {
          $cookieStore.remove('access_token');
          delete $http.defaults.headers.common.Authorization;
      }
  }])

})();