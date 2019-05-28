'use strict';
angular.module('gecscf.ui').directive('hilightNegative', [function () {

	function link(scope, element, attrs) {
		scope.$watch(attrs.ngBind, function(value) {
			element.removeClass("hilight-negative");
			if (!angular.isUndefined(value) && value != null){
			  if(Number.isNaN(value)){
  				if (value.charAt(0) == "(" && value.substr(value.length - 1) == ")"){
  					value = value.slice(1, -1);
  					value = "-"+value;
  				}
			  }
				value = ""+value;
				var calculateValue = value.replace(/,/g, '');
				if (!Number.isNaN(calculateValue)){
					if (parseFloat(calculateValue) < 0){
						element.addClass("hilight-negative");
					}
				}
			}
		});
		
		scope.$watch(attrs.hilightNegative, function(value) {
			if (!value){
				element.removeAttr("hilight-negative");
				element.removeClass("hilight-negative");
			}	
		});
	}

	return {
		link: link
	};

}])