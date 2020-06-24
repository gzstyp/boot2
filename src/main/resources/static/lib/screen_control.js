/**
	防止panel/window/dialog组件超出溢出浏览器边界
	作者 田应平
	创建时间 2014年11月2日 21:05:26
*/

var pageOnMove = function(left, top) {
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	};
	if (t < 1) {
		t = 1;
	};

	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;

	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(window).width();
	var browserHeight = $(window).height();

	if (right > browserWidth) {
		l = browserWidth - width;
	}

	if (buttom > browserHeight) {
		t = browserHeight - height;
	}

	$(this).parent().css({
		left : l,
		top : t
	});
};
$.fn.dialog.defaults.onMove = pageOnMove;
$.fn.window.defaults.onMove = pageOnMove;
$.fn.panel.defaults.onMove = pageOnMove;