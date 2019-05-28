'use strict';
angular.module("gec.core",[])
.factory('authorization', ['$http', function($http){
  
    
  var fetchPrivileges = function(){
    return  $http({
        method : "GET",
        url : "/api/privileges",
        params: {}
    }).then(response => {
      this.privileges = response.data;
      return this.privileges;
    });
    
  };
  
  var getMyUserInfo = function(){
    return $http({
        method : "GET",
        url : "/api/users/me",
        params: {}
    })
  };
  
  
  
  var hasAny = function(privileges){
    
    var _has = false;
    
    if(angular.isUndefined(privileges)){
      return false;
    }
    
    privileges.forEach(function(p){
      if(!_has){
        _has = this.has(p);
      }
    });
   
    return _has;
  }
  
  var hasAll = function(privileges){
    
    var _has = false;
    
    if(angular.isUndefined(privileges)){
      return false;
    }
    
    privileges.forEach(function(p){
        _has = this.has(p) && _has;
    });
   
    return _has;
  }

  var has = function(privilege){
     return (this.privileges.indexOf(privilege) !== -1);
  }
  
  return {
    fetchPrivileges: fetchPrivileges,
    getMyUserInfo:getMyUserInfo,
    has: has,
    hasAll: hasAll,
    hasAny: hasAny,
    privileges: []
  }
  
}]);