(function() {
  'use strict';

  angular.module('gec.ui.table').component('gecDocumentTable', {
    bindings: {
        config: '=',
        criteria: '<'
    },
    templateUrl:  '/js/app/common/pging.html',
    controller: [
        '$rootScope',
        '$scope',
        '$filter',
        'blockUI',
        'ui',
        'handlerService',
        'visibilityService',
        function ($rootScope, $scope, $filter, blockUI, ui, handlerService, visibilityService) {
            var self = this;
            self.data = [];
            
            self.columns = [];
            
            self.load = function (criteria, callback) {

              criteria.size = self.paging.size;
              criteria.page = self.paging.page;

              var _criteria = {};

              angular.copy(criteria, _criteria);
              

              Object
                  .keys(_criteria)
                  .map(
                      function (k, v) {
                          if (angular.isObject(_criteria[k])
                              && angular
                                  .isDefined(_criteria[k].value)) {
                              _criteria[k] =  _criteria[k].value;
                          }
                          return v;
                      });
              
              var tableBlockUI = blockUI.instances.get(self.config.name);
              // Start blocking the element.
              tableBlockUI.start();
              
              self.data = [];
              return handlerService.handle(self.config.fetching.handler, _criteria)
                  .then(function (response) {
                      self.data = response.data;
                      self.paging.totalRecord = response
                          .headers('X-Total-Count');
                      self.paging.totalPage = response
                          .headers('X-Total-Page');

                      $rootScope.$broadcast(self.config.name + ':paging', self.paging);
                      if (callback != null) {
                          callback(criteriaData, response);
                      }
                      
                      tableBlockUI.stop();
                      
                      return response;
                  }).then(callback ? callback : angular.noop).catch(function (data) {
                    // Handle error here
                    tableBlockUI.stop();
                  });
          }
          
          self.$onInit = function () {

              if (angular.isUndefined(self.pageData)) {
                  self.pageData = {};
              }

              if (angular.isUndefined(self.pageData.paging)) {

                  self.pageData.paging = self.paging = {
                      size: 20,
                      page: 0,
                      totalRecord: 0,
                      totalPage: 0
                  };

              } else {
                  self.paging = self.pageData.paging;
              }
          }
            
      }]
  })
  .directive('gecDocumentTh', gecDocumentTh)
  .directive('gecDocumentTd', gecDocumentTd);
  
  gecDocumentTh.$inject = ['$compile', '$filter', '$translate'];
  
  function gecDocumentTh($compile, $filter, $translate) {
    return {
      restrict: 'A',
      replace: true,
      link: _link,
      controller: ['$scope', '$rootScope', '$element', '$attrs', _ctrl]
    }

    function _link(scope, elements, attrs) {
      scope.$watch(attrs.gecDocumentTh, function (column) {
        renderTableHeader(scope, elements, column, $translate.use());
      });
    }

    function _ctrl($scope, $rootScope, $element, $attrs) {
      $rootScope.$on('$translateChangeSuccess', function (translateChangeSuccess, currentLange) {
        var column = $scope.$eval($attrs.gecDocumentTh);
        renderTableHeader($scope, $element, column, currentLange.language);
      });
    }

    function renderTableHeader(scope, elements, column, currentLange) {
      var htmlText = '';

      htmlText = getDisplayLanguage(currentLange, column);

      if (column.sortable) {
        htmlText = '<span sort by="{{column.fieldName}}" reverse="reverse" order="orders" >' + htmlText + '</span>';
      }

      var colClass = column.cssTemplateHeader || 'text-center';
      elements.addClass(colClass)
      elements.html(htmlText);
      $compile(elements.contents())(scope);

      if (column.fieldName != 'selectBox') {
        if (angular.isDefined(elements[0].childNodes[0])) {
          if (angular.isDefined(column.headerId)) {
            elements[0].childNodes[0].id = column.headerId;
          } else if (angular.isDefined(column.fieldName)) {
            elements[0].childNodes[0].id = column.fieldName + '-header-label';
          }
        }
      }
    }

    function getDisplayLanguage(currentLange, column) {
      var htmlText = '';
      if (column.fieldName == 'selectBox') {
        htmlText = column.label;
      } else {
    	if(angular.isDefined(column.labels[currentLange])){
    		htmlText = column.labels[currentLange];	
    	}
    	else if (currentLange == 'en_EN') {
          htmlText = column.labelEN;
        } else {
          htmlText = column.labelTH;
        }
      }
      return htmlText;
    }
  }
  
  gecDocumentTd.$inject = ['$compile', '$filter', '$log'];
  
  function gecDocumentTd($compile, $filter, $log) {
    
    var directive = {
      scope: false,
      restrict: 'A',
      replace: true,
      link: _link
    }
    
    return directive;
    
    // ////////////////////////
    
    var log = $log;
    
    function _link(scope, elements, attrs) {
      var pageOptions = scope.$eval(attrs.pageOptions);

      scope.$watch(attrs.gecDocumentTd, function (data) {
        var rowNo = renderNo(scope, attrs, pageOptions);
        scope['$rowNo'] = rowNo;
        var column = scope.$eval(attrs.columnRender);

        var dataRender = '';
        var colClass = column.cssTemplate || 'text-center';
        if(column.alignment != null){
        	colClass = 'text-'+ $filter('lowercase')(column.alignment);
        }

        if (column.fieldName === '$rowNo') {
          elements.addClass(colClass);
          dataRender = rowNo;
          elements.html(dataRender);
        }

        if (angular.isDefined(column.filterType) && column.filterType !== null) {
          dataRender = filterData(column, data);
        } else {
          if (angular.isDefined(column.cellTemplate) && column.cellTemplate !== null) {
            dataRender = column.cellTemplate;
          } else if (angular.isDefined(column.dataRenderer) && column.dataRenderer !== null) {
            dataRender = column.dataRenderer(data);
          } else {
            dataRender = data[column.fieldName];
          }
        }

        if (column.renderer != null) {
          dataRender = column.renderer(dataRender, data);
        }

        elements.addClass(colClass);
        elements.html(dataRender);
        if (!angular.isUndefined(dataRender) && dataRender != null) {
          dataRender = "" + dataRender;
          var calculateValue = dataRender.replace(/,/g, '');
          if (!Number.isNaN(calculateValue)) {
            if (parseFloat(calculateValue) < 0) {
              elements.addClass("hilight-negative");
            }
          }
        }
        $compile(elements.contents())(scope);

        if (angular.isDefined(column.idTemplate) && column.idTemplate !== null) {
          // Check add id is rowNo for checkBox
          if (column.idValueField === '$rowNo') {

            if (elements[0].children.length > 0) {
              elements[0].children[0].id = addId(rowNo, column.idTemplate, column.renderer, column.fieldName);


            } else {

              elements[0].id = addId(rowNo, column.idTemplate, column.renderer, column.fieldName);

            }

          } else {
            if (elements[0].children.length > 0) {
              elements[0].children[0].id = addId(data[column.idValueField != null ? column.idValueField : column.field], column.idTemplate, column.renderer, column.fieldName);
            } else {
              // comment for import channel
              // elements[0].id = addId(rowNo, column.idTemplate,
              // column.renderer, column.fieldName);
              elements[0].id = addId(data[column.idValueField != null ? column.idValueField : column.field], column.idTemplate, column.renderer, column.fieldName);
            }
          }
        }

      });
    }

    function renderNo(scope, attrs, pageOptions) {
      var indexNo = scope.$eval(attrs.indexNo);
      var rowNo = (pageOptions.page * pageOptions.size) + (indexNo + 1)
      return rowNo;
    }

    function filterData(column, dataColumn) {

      var filterType = column.filterType;
      var filterFormat = column.format;
      var data = dataColumn[column.fieldName];
      var result = '';
      if (filterType === 'date') {
        filterFormat = filterFormat || 'dd/MM/yyyy';
        result = $filter(filterType)(data, filterFormat, 'UTC+0700');
      } else if (filterType === 'number') {
        result = $filter(filterType)(data, 2);
      } else {
        result = $filter(filterType)(data, filterFormat);
      }
      return result;
    }

    function addId(rowNo, columnId, renderer, fieldName) {
      if (angular.isDefined(renderer) && renderer != null) {
        rowNo = renderer(rowNo);
      }

      if (fieldName != 'selectBox') {
        return columnId.replace('{value}', rowNo) + "-label";
      } else {
        return columnId.replace('{value}', rowNo);
      }
    }


  }

})();
