(function () {
    'use strict';

    var app = angular.module("gec", ['ui.router', 'pascalprecht.translate', 'ngCookies',
        'ui.bootstrap', 'oc.lazyLoad', 'gec.ui', 'gecscf.ui',
        'gec.sponsor',
        'gec.mappingCode',
        'gec.registerAccount',
        'gec.mappingException',
        'gec.logTransaction',
        'gec.dashboard',
        'gec.profile',
        'gec.changePassword',
        'gec.userManagement',
    ]);
    app.config(function ($locationProvider, $uiRouterProvider, $translateProvider, $translatePartialLoaderProvider) {
        $locationProvider.html5Mode({
            enabled: false,
            requireBase: false
        });

        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'js/{part}/{lang}/scf_label.json'
        });

        $translateProvider.preferredLanguage('en_EN');
        $translatePartialLoaderProvider.addPart('translations');
        $translateProvider.useSanitizeValueStrategy('escapeParameters');
    });

    app.run(['$window', '$http', '$filter', '$cookieStore', function ($window, $http, $filter, $cookieStore) {

        $window.Date.prototype.toISOString = function () {
            return moment(this).format();
        }
        var isLoginPage = window.location.href.indexOf("login") != -1;

        if (!($cookieStore.get("login_date")))
            $cookieStore.put("login_date", new Date());

        if (isLoginPage) {
            if ($cookieStore.get("access_token")) {
                window.location.href = "/";
            }
        } else {
            if ($cookieStore.get("access_token")) {
                $http.defaults.headers.common.Authorization =
                    'Bearer ' + $cookieStore.get("access_token");
            } else {
                window.location.href = "/login";
            }
        }
    }]);


    app.controller('tabMenuController', ['tabMenuService', '$cookieStore', function (tabMenuService, $cookieStore) {
        var self = this;

        self.modules = [];
        self.username = '';
        self.roles = [];

        var menuManageUser = {
            title: 'User',
            role: 'MANAGE_ALL',
            open: false,
            list: [
                { title: 'User Management', uri: 'user-management' },
            ]
        };

        var menuBank = {
            title: 'Bank',
            role: 'MANAGE_BANK',
            open: false,
            list: [
                { title: 'Register Account', uri: 'register_account_list' },
                { title: 'Mapping Response Bank', uri: 'mapping_code_list' },
                { title: 'Mapping Response Exception', uri: 'mapping_exception_list' },
                { title: 'Log Txn. Core Bank Dummy', uri: 'log_transaction_list' },
            ]
        };

        var menuSponsor = {
            title: 'Sponsor',
            role: 'MANAGE_SPONSOR',
            open: false,
            list: [{ title: 'Sponsors', uri: 'sponsors' },]
        };

        tabMenuService.getProfile().then(function (response) {
            self.username = response.data.username;
        });

        tabMenuService.getPrivileges().then(function (response) {
            response.data.forEach(function (item) {
                if (item == 'MANAGE_ALL') {
                    self.modules.push(menuManageUser);
                    self.modules.push(menuSponsor);
                    self.modules.push(menuBank);
                    self.roles.push('Common');
                }
                if (item == 'MANAGE_SPONSOR') {
                    self.modules.push(menuSponsor);
                    self.roles.push('Sponsor');
                }
                if (item == 'MANAGE_BANK') {
                    self.modules.push(menuBank);
                    self.roles.push('Bank');
                }
                openFirstMenu();
            });
        });

        function openFirstMenu() {
            if (self.modules.length == 1)
                self.modules[0].open = true;
        }

        function currentLogin() {
            var dateTime = new Date($cookieStore.get("login_date"));

            var day = dateTime.getDate();
            day = day.toString().length == 1 ? '0' + day : day;

            var mounth = dateTime.getMonth() + 1;
            mounth = (mounth + 1).toString().length == 1 ? '0' + mounth : mounth;

            var hh = dateTime.getHours();
            hh = hh.toString().length == 1 ? '0' + hh : hh;

            var mm = dateTime.getMinutes();
            mm = mm.toString().length == 1 ? '0' + mm : mm;

            var loginTime = day + '/' + mounth + '/' + dateTime.getFullYear() + ' ' + hh + ':' + mm;
            return loginTime;
        }

        self.loginTime = currentLogin();

    }]);
    app.service('tabMenuService', ['$http', '$q', '$log', function ($http, $q, $log) {
        function getProfile() {
            return $http({ method: "GET", url: "/api/profile", });
        }
        function getPrivileges() {
            return $http({ method: "GET", url: "/api/privileges", });
        }
        return {
            getProfile: getProfile,
            getPrivileges: getPrivileges,
        };
    }]);

    app.controller('BodyCtrl', ['bodyService', '$scope', '$window', function (bodyService, $scope, $window) {
        var self = this;
        self.cssMain = '';
        self.classSideMenu = '';


        self.$onInit = function () {
            if ($window.innerWidth < 845) showSideMenu();
        };

        self.onShowHideSideMenu = function () {
            if (!self.cssMain) {
                showSideMenu();
            } else {
                hideSideMenu();
            }
        };

        function showSideMenu() {
            self.cssMain = 'width: 100%; margin-left: 0%';
            self.classSideMenu = 'd-none';
        }

        function hideSideMenu() {
            self.cssMain = '';
            self.classSideMenu = '';
        }

    }]);
    app.service('bodyService', ['$http', '$q', '$log', function ($http, $q, $log) {
        return {};
    }]);
})();