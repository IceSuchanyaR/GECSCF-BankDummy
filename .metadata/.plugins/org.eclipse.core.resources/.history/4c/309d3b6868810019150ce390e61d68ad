"use strict";
var templateVersion = (new Date()).getTime();

var WebHelper = WebHelper || {};

WebHelper = {
	createListPageState: function (config) {
		return {
			name: config.name,
			url: config.url,
			params: { criteria: {}, pageData: {} },
			views: {
				'@': {
					controller: config.controller,
					controllerAs: '$ctrl',
					templateUrl: '/js/app/modules/ui/templates/pages/list.html'
				}
			},
			resolve: this.resolveListPage({
				url: config.yaml,
				dependencies: config.dependencies
			})
		}
	},
	createFormPageState: function (config) {
		return {
			name: config.name,
			url: config.url,
			params: { criteria: {}, model: {}, pageData: {} },
			views: {
				'@': {
					controller: config.controller,
					controllerAs: '$ctrl',
					templateUrl: function(elem, attr) {
					  if(angular.isDefined(config.formType)){
					    return '/js/app/modules/ui/templates/pages/'+config.formType+'.html';
					  }
					  else{
					    return '/js/app/modules/ui/templates/pages/form.html';
					  }
			    }
				}
			},
			resolve: this.resolveFormPage({
				url: config.yaml,
				dependencies: config.dependencies
			})
		}
	},
	resolveListPage: function (config) {

		var base = {
			_criteria: {},
			_pageData: {},
			_init: angular.noop,
			onInit: function (func) {
				this._init = func;
				return this;
			},
			handlers: [],
			fetchings: [],
			handler: function (name, handler) {
				this.handlers[name] = handler;
				return this;
			},
			build: function (ctrl) {
				ctrl.page = this.page;
				ctrl.handlers = this.handlers;
				ctrl.model = ctrl.criteria = this._criteria;
				ctrl.pageData = this._pageData;
				this._init(this.params, ctrl.criteria, ctrl.pageData);
				return this;
			}
		};

		return {
			page: ['$http', '$transition$', '$q', 'authorization', 'handlerService', function ($http, $transition, $q, authorization, handlerService) {
				return $http({
					method: 'GET',
					url: config.url,
					headers: {
						'Content-Type': undefined
					}
				}).then(function (response) {
					var result = YAML.parse(response.data);
					base.page = result.page;
					base.handler = function (name, handler) {
						handlerService.handler(name, handler);
						return this;
					};
					
					base.fetching = function (name, handler) {
            handlerService.handler(name, handler);
            this.fetchings[name] = handler;
            return this;
          };
          
					base.fetch = function(){
					  var chain = $q.when();
					  this.fetchings.forEach(function (f) {
					    chain = chain.then(f);
					  });
					  
					  return this;
					};
					base.params = $transition.params();
					return authorization.fetchPrivileges();
				}).then(function (privileges) {
					return base;
				});
			}],
			data: ['$transition$', function ($transition$) {
				return {
					criteria: $transition$.params().criteria,
					data: $transition$.params().data
				}
			}],
			deps: [
				'$ocLazyLoad',
				'$q',
				function ($ocLazyLoad, $q) {
					var deferred = $q.defer();
					var srcs = Array.isArray(config.dependencies) ? config.dependencies
						: config.dependencies.split(/\s+/);
					var version = (new Date()).getTime();
					var promise = deferred.promise;
					srcs.forEach(function (src) {
						src = src + '?v=' + version;
						promise = promise.then(function () {
							return $ocLazyLoad.load(src);
						});
					});
					deferred.resolve();
					return promise;
				}]
		}
	},
	resolveFormPage: function (config) {
		var base = {
			_model: {},
			_criteria: {},
			_pageData: {},
			_init: angular.noop,
			onInit: function (func) {
				this._init = func;
				return this;
			},
			handlers: [],
			fetchings: [],
			handler: function (name, handler) {
				this.handlers[name] = handler;
				return this;
			},
      fetching: function (name, handler) {
        this.handlers[name] = handler;
        this.fetchings[name] = handler;
        return this;
      },
			build: function (ctrl) {
				var self = this;
				ctrl.page = self.page;
				ctrl.handlers = self.handlers;
				ctrl.model = self._model;
				ctrl.criteria = self._criteria;
				ctrl.pageData = self._pageData;
				ctrl.params = self.params;
				self._init(this.params, ctrl.pageData);
			}
		};
		return {
			page: ['$http', '$transition$', 'handlerService', 'authorization', function ($http, $transition, handlerService, authorization) {
				return $http({
					method: 'GET',
					url: config.url,
					headers: {
						'Content-Type': undefined
					}
				}).then(function (response) {
					var result = YAML.parse(response.data);
					base.page = result.page;
					base.params = $transition.params();
					base.handler = function (name, handler) {
						handlerService.handler(name, handler);
						return this;
					};
					
					authorization.fetchPrivileges();
					return base;
				});
			}],
			data: ['$transition$', function ($transition) {
				return {
					model: $transition.params().model
				}
			}],
			deps: [
				'$ocLazyLoad',
				'$q',
				function ($ocLazyLoad, $q) {
					var deferred = $q.defer();
					var srcs = Array.isArray(config.dependencies) ? config.dependencies
						: config.dependencies.split(/\s+/);
					var version = (new Date()).getTime();
					var promise = deferred.promise;
					srcs.forEach(function (src) {
						src = src + '?v=' + version;
						promise = promise.then(function () {
							return $ocLazyLoad.load(src);
						});
					});
					deferred.resolve();
					return promise;
				}]
		}
	},
	loadScript: function (srcs, callback) {
		return {
			deps: ['$ocLazyLoad', '$q', 'blockUI',
				function ($ocLazyLoad, $q, blockUI) {

					var deferred = $q.defer();
					var promise = false;
					srcs = Array.isArray(srcs) ? srcs : srcs.split(/\s+/);

					if (!promise) {
						promise = deferred.promise;
					}
					var version = (new Date()).getTime();
					srcs.forEach(function (src) {
						src = src + '?v=' + version;
						promise = promise.then(function () {
							return $ocLazyLoad.load(src);
						});
					});

					deferred.resolve();

					return callback ? promise.then(function () {
						return callback();
					}) : promise;
				}]
		}
	},
	templateUrl: function load(url) {
		return url + '?v=' + templateVersion;
	},
	getValue: function getVal(object, attrName) {
		return angular.isObject(object) ? object[attrName] : angular
			.isUndefined(object) ? undefined : object;
	}
};