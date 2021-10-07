

// *********************************************************************************
Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";
 
    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;
     
    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};
// *********************************************************************************
var Common = {
	isIE : function() {
		var agent = navigator.userAgent.toLowerCase();
		if (!(navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) && !(agent.indexOf("msie") != -1) ) return false;
		return rue;
	},
	importJS : function(js_file) {
		$('script:last').after('<script type="text/javascript" src="'+ js_file +'"></script>');
	},
	clearSelectBox : function($selector, is_all) {
		if (typeof(is_all) == 'undefined') is_all = true;
		var $first = null;
		if (!is_all) $first = $('option:first', $selector);
		$selector.empty();
		if ($first != null) $selector.append($first);
	},
	addSelectBox : function($selector, json) {
		for (var k in json) {
			$selector.append('<option value="'+ k +'">'+ json[k] +'</option>');
		}
	},
	replaceSelectBox : function($selector, json, is_all) {
		this.clearSelectBox($selector, is_all);
		this.addSelectBox($selector, json);
	},
	getDiffMonth : function(diff) {
		if (typeof diff == 'undefined') diff = 0;
		var today = new Date(globalNowDateTime.substring(0, 10));
		if (diff != 0) today.setMonth(today.getMonth() + diff);
		
		var yy = today.getFullYear().toString();
		var mm = (today.getMonth() + 1).toString();

		var new_ym =  yy +'-'+ (mm[1] ? mm : '0'+mm[0]);
		return new_ym;
	},
	getDiffDate : function(diff) {
		if (typeof diff == 'undefined') diff = 0;
		var today = new Date(globalNowDateTime.substring(0, 10));
		switch (diff) {
			case 'first' :
				today = new Date(globalNowDateTime.substring(0, 8) +'01');
				break;
			case 'last' :
				var yy = globalNowDateTime.substring(0, 4);
				var mm = parseInt(globalNowDateTime.substring(5, 7), 10);
				today = new Date(yy, mm, 0);
				break;
			default :
				if (diff != 0) today.setDate(today.getDate() + diff);
		}
		
		var yy = today.getFullYear().toString();
		var mm = (today.getMonth() + 1).toString();
		var dd = today.getDate().toString();

		var new_date =  yy +'-'+ (mm[1] ? mm : '0'+mm[0]) +'-'+ (dd[1] ? dd : '0'+dd[0]);
		return new_date;
	},
	isUndefined : function(el) {
		if (typeof(el) == 'undefined') return true;
		return false;
	}
};
// *********************************************************************************
var Debug = {
	showAddDebug : function(text) {
		this.addDebug(text);
		this.showDebug();
	},
	addDebug : function(text) {
		$('#wrap-debug .debug-text').append($('<div><pre>'+ text +'</pre></div>'));
	},
	showDebug : function() {
		$('#wrap-debug').show();
	},
	hdieDebug : function() {
		$('#wrap-debug').hide();
	}
};
// *********************************************************************************
var Modal = {
	showLoading : function() {
		jQuery('.wrap-loading').show();
	},
	hideLoading : function() {
		jQuery('.wrap-loading').hide();
	},
	closeBox4Form : function($frm) {
		jQuery('.close', $frm.closest('.modal-box')).trigger('click');
	},
	showLayerPopup : function(modal_id, container) {
		var $modal;
		if (typeof(container) == 'undefined') {
			$modal = jQuery('#'+ modal_id);
		} else {
			$modal = jQuery('#'+ modal_id, container);
		}
		if ($modal.length < 1) return false;

		var parent_modal_id = '';

		$modal.fadeIn();
		jQuery("body").append('<div class="modal-overlay modal-overlay_'+ modal_id +'"></div>');
		jQuery(".modal-overlay").fadeTo(100, 0.6);

		//블라인드 팝업갯수 구하기 (숫자 0부터)
		var modalBoxCnt  = jQuery('[class=modal-box]').length - 1;
		for (i=0; i <= modalBoxCnt; i++) {
			var modalArrayId = $("[class=modal-box]:eq("+ i +")").attr("id");
			if (modal_id != modalArrayId) {
				var $now_modal = jQuery('#'+ modalArrayId);
				if ($now_modal.is(':visible') && $now_modal.css('z-index') == '1000') {
					parent_modal_id = modalArrayId;
				}
				$now_modal.css("z-index", "1");
				$modal.css("z-index","1000");
			}
		}

		jQuery(window).resize(function() {
			var win_h = jQuery(window).height();
			var win_w = jQuery(window).width();
			var box_h = $modal.height() + 5;

			if (win_h > box_h) {
				css_top  = (win_h - $modal.outerHeight()) / 2;
				css_left = (win_w - $modal.outerWidth()) / 2;
			} else {
				$modal.height(win_h - 10);
				jQuery('.modal-body', $modal).height(win_h - 110);
				css_top  = 10;
				css_left = (win_w - $modal.outerWidth()) / 2;
			}
			var css = {};
			css['top'] = css_top;
			css['left'] = css_left;
			css['position'] = 'fixed';
			//css['margin'] = '0 auto';
			$modal.css(css);
		});

		jQuery(window).resize();

		jQuery('.close', $modal).click(function() {
			_closeModal();
			return false;
		});
		jQuery("."+ modal_id +"_close").click(function() {
			_closeModal();
			return false;
		});
		function _closeModal() {
			$modal.fadeOut(1, function() {
				if (parent_modal_id) {
					jQuery("#"+ parent_modal_id).css("z-index", "1000");
					jQuery(".modal-overlay_"+modal_id).remove();
				} else {
					for (k = modalBoxCnt; k >= 0; k--) {
						var modalArrayId = jQuery("[class=modal-box]:eq("+ k +")").attr("id");
						if (modalArrayId == modal_id) {
							var openModal = jQuery("[class=modal-box]:eq("+ parseInt(k - 1) +")").attr("id");
							jQuery("#"+ openModal).css("z-index", "1000");
							jQuery(".modal-overlay_"+modal_id).remove();
							break;
						}
					}
				}
			});
		}
	},
	hideLayerPopup : function(modal_id) {
		jQuery('.close', jQuery('#'+ modal_id)).trigger('click');
		jQuery("."+ modal_id +"_close").trigger('click');
	},
	bindLayerPopupOne : function($element) {
		$element.bind('click', function(e) {
			var $btn = $(this);
			if ($btn.hasClass('btnGs1') || $btn.hasClass('btnSb1') || $btn.hasClass('btnLb1')) return false;
			e.preventDefault();

			var modal_id = $btn.attr('data-modal-id');
			Modal.showLayerPopup(modal_id);
		});
	},
	unbindLayerPopupOne : function($element) {
		$element.unbind('click');
	},
	bindLayerPopup : function() {
		jQuery('[data-modal-id]').each(function() {
			Modal.bindLayerPopupOne(jQuery(this));
		});
	},
};
// *********************************************************************************
var DatePicker = {
	changeYearButtons : function($el) {
		setTimeout(function() {
			var widgetHeader = $el.datepicker("widget").find(".ui-datepicker-header");
			var prevMonthBtn = $el.datepicker('widget').find('.ui-datepicker-prev');
			var nextMonthBtn = $el.datepicker('widget').find('.ui-datepicker-next');
			var prevYrBtn = $('<a href="" class="ui-datepicker-prev-year"></a>');
			prevYrBtn.append($('<span class="ui-icon ui-icon-circle-arrow-w">이전해</span>'));
			prevYrBtn.unbind("click").bind("click", function() {
				$.datepicker._adjustDate($el, -1, 'Y');
				return false;
			}).unbind('mouseover').bind('mouseover', function() {
				$(this).addClass('ui-state-hover ui-datepicker-prev-year-hover');
			}).unbind('mouseout').bind('mouseout', function() {
				$(this).removeClass('ui-state-hover ui-datepicker-prev-year-hover');
			});
			var nextYrBtn = $('<a href="" class="ui-datepicker-next-year"></a>');
			nextYrBtn.append($('<span class="ui-icon ui-icon-circle-arrow-e">다음해</span>')).unbind("click").bind("click", function() {
				$.datepicker._adjustDate($el, +1, 'Y');
				return false;
			}).unbind('mouseover').bind('mouseover', function() {
				$(this).addClass('ui-state-hover ui-datepicker-next-year-hover');
			}).unbind('mouseout').bind('mouseout', function() {
				$(this).removeClass('ui-state-hover ui-datepicker-next-year-hover');
			});
			prevMonthBtn.before(prevYrBtn);
			nextMonthBtn.after(nextYrBtn);
			//prevYrBtn.insertBefore(prevMonthBtn);
			//prevYrBtn.appendTo(widgetHeader);
			//nextYrBtn.appendTo(widgetHeader);
		}, 1);
	},
	initElement : function($selectors, max) {
		var gDate = max ? '1900-01-01' : '2010-01-01';
		var changeYearButtons = function() {
		};
		$selectors.each(function() {
			var before_date = '';
			$(this).datepicker({
				showOn: "button",
				dateFormat: 'yy-mm-dd', 
				changeMonth: true, 
				changeYear: true ,
				showAnim: "slide",
				showButtonPanel: true,
				//buttonImage: "images/calendar.png",
				buttonImage: "/js/images/icon_calendar.gif",
				buttonImageOnly: true,
				yearRange: 'c-99:c+5',
				minDate: gDate,
				//yearSuffix: '년',
				showMonthAfterYear: true,
				monthNames      : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
				monthNamesShort : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
				nextText : '다음달',
				prevText : '이전달',
				//stepMonths: 12,
				dayNames      : ['일', '월', '화', '수', '목', '금', '토'],
				dayNamesShort : ['일', '월', '화', '수', '목', '금', '토'],
				dayNamesMin   : ['일', '월', '화', '수', '목', '금', '토'],
				currentText: '오늘',
				closeText : '닫기',
				beforeShow: function(input) {
					//console.log('beforeShow');
					//console.log(input);
					DatePicker.changeYearButtons($(input));
					return;
					setTimeout(function() {
						var widgetHeader = $(input).datepicker("widget").find(".ui-datepicker-header");
						var prevYrBtn = $('<button title="PrevYr">&lt;&lt; Prev Year</button>');
						prevYrBtn.unbind("click").bind("click", function() {
							$.datepicker._adjustDate($(input), -1, 'Y');
						});
						var nextYrBtn = $('<button title="NextYr">Next year &gt;&gt;</button>');
						nextYrBtn.unbind("click").bind("click", function() {
							$.datepicker._adjustDate($(input), +1, 'Y');
						});
						prevYrBtn.appendTo(widgetHeader);
						nextYrBtn.appendTo(widgetHeader);
					}, 1);
				},
				onChangeMonthYear: function(input) {
					DatePicker.changeYearButtons($(this));
					return;
					setTimeout(function() {
						var widgetHeader = $(input).datepicker("widget").find(".ui-datepicker-header");
						var prevYrBtn = $('<button title="PrevYr">&lt;&lt; Prev Year</button>');
						prevYrBtn.unbind("click").bind("click", function() {
							$.datepicker._adjustDate($(input), -1, 'Y');
						});
						var nextYrBtn = $('<button title="NextYr">Next year &gt;&gt;</button>');
						nextYrBtn.unbind("click").bind("click", function() {
							$.datepicker._adjustDate($(input), +1, 'Y');
						});
						prevYrBtn.appendTo(widgetHeader);
						nextYrBtn.appendTo(widgetHeader);
					}, 1);
				},
			}).blur(function() {
				var date;
				if ($(this).val().trim() == '') {
					date = '';
				} else {
					date = Utils.formatterDate($(this).val());
					if (date == '') date = before_date;
				}
				$(this).val(date);
			}).focus(function(event) {
				before_date = $(this).val();
			});
			if ($(this).hasClass('disable')) $(this).datepicker( "option", "disabled", true );
		});
	},
}

// *********************************************************************************
$(document).ready(function() {
	Modal.bindLayerPopup();
	/*$(document).click(function(event) {
		if (BizCommon.isDisableButton($(this))) {
			event.preventDefault();
			return false;
		}
	});*/
});
//Common.importJS('/js/bizrental.grid.js');
