;(function($){
	var opts = {};
	var selectbox = {
		init: function(o) {
			var $o = $(o),
			_name = $o.attr('name'),
			_selectValue = $o.find('.opts > a.selected').attr('val')? $o.find('.opts > a.selected').attr('val'):$o.find('.opts > a:first').attr('val'),
			_selectHtml = $o.find('.opts > a.selected').html()? $o.find('.opts > a.selected').html():$o.find('.opts > a:first').html();
			$o.addClass('sb');
			$o.append($('<div class="sb_icon arrow" />')).append($('<input type="hidden" name="' + _name + '" value="' + _selectValue + '">'));
			$('<div class="selected">' + _selectHtml + '</div>').insertBefore($o.children(':first'));
			$o.children('.opts').show();
			var optsWidth = $o.children('.opts').width();
			if(optsWidth == 0){
				var optsChildWidth = [];
				var tempWidth = 0;
				$o.children('.opts').children('a').each(function(){
					optsChildWidth.push($(this).width());
				});
				for(var i=0; i<optsChildWidth.length ; i++){
					if(optsChildWidth[i] > tempWidth)
						tempWidth = optsChildWidth[i]
				}
				optsWidth = tempWidth + 10;
			}
			$o.children('.opts').hide();
			$o.children('.opts').css('width', (optsWidth + 15));
			var _width = (opts.width != 'auto')? opts.width : $o.children('.opts').width();
			$o.css({'width': _width, 'height': opts.height}).find('div.selected').css({'height': opts.height, 'line-height': opts.height +'px'});
			$o.find('.sb_icon').css({'top': ($o.height() - $o.find('.sb_icon').height())/2});
			$o.click(selectbox.toggle);
			$o.find('.opts > a').click(selectbox.select).hover(selectbox.hover);
			$(document).click(selectbox.hide);
		},
		toggle: function(e) {
			e.stopPropagation();
			var $o = $(this);
			var $opts = $o.children('.opts');
			$o.find('a.selected').removeClass('none');
			selectbox.hide(null, $('.sb').not($o));
			$o.toggleClass('sb_active');
			$opts.css({
				'width': Math.max($opts.width(), $o.width()),
				'top': $o.height(),
				'left': - parseInt($o.css('border-left-width'))
			}).toggle($o.hasClass('sb_active'));
		},
		hide: function(e, objs) {
			var $o = objs ? objs : $('.sb');
			$o.removeClass('sb_active').children('.opts').hide().find('a.selected').removeClass('none');	
		},
		select: function(e) {
			e.stopPropagation();
			var $o = $(this).parents('.sb:first');
			$o.trigger('click');
			$o.find('a.selected').removeClass('selected');
			$(this).addClass('selected');
			$o.find('div.selected').html($(this).html());
			$o.find('input').val($(this).attr('val'));
		},
		hover: function(e){
			e.stopPropagation();
			var $o = $(this).parents('.sb:first');
			$o.find('a.selected').addClass('none');
		}
	};
	_init = function(o){
		var type = $(o).attr('type');
		if(type == 'selectbox'){
			selectbox.init(o);
		}
	};
	$.fn.inputbox = function(options){
        opts = $.extend({}, $.fn.inputbox.defaults, options);
        return this.each(function(){
            _init(this);
        });
    };
    $.fn.inputbox.defaults = {
		type: 'selectbox',
		width : 'auto',
		height : 24
    };
})(jQuery);