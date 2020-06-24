/**  
 * 提示消息框的样式  datagrid鼠标经过提示单元格内容
 * 创建时间：2015年4月28日 12:49:22
 * 作者：田应平
 * 用法:
 * onLoadSuccess:function(data){   
		$('#datagrid的ID').datagrid('doCellTip',{'max-width':'300px','delay':500});
   },
*/
;(function(){
	$.extend($.fn.datagrid.methods,{
	    //开打提示功能 
		doCellTip: function(jq,params){
	        function showTip(data, td, e){
	            if ($(td).text() == "") return;
	            data.tooltip.text($(td).text()).css({
	                top: (e.pageY + 10) + 'px',
	                left: (e.pageX + 20) + 'px',
	                'z-index': $.fn.window.defaults.zIndex,
	                display: 'block'
	            });
	        };
	        return jq.each(function(){
	            var grid = $(this);
	            var options = $(this).data('datagrid');
	            if (!options.tooltip){
	                var panel = grid.datagrid('getPanel').panel('panel');
	                var defaultCls = {
	                    'border': '1px solid #333',
	                    'padding': '1px',
	                    'color': '#333',
	                    'font-size':'12px',
	                    'background': '#f7f5d1',
	                    'position': 'absolute',
	                    'max-width': '300px',
	                    'padding':'4px 4px 4px 4px',
	                    'border-radius': '4px',
	                    '-moz-border-radius': '4px',
	                    '-webkit-border-radius': '4px',
	                    'display': 'none'
	                };
	                var tooltip = $("<div id='celltip'></div>").appendTo('body');
	                tooltip.css($.extend({},defaultCls, params.cls));
	                options.tooltip = tooltip;
	                panel.find('.datagrid-body').each(function(){
	                    var delegateEle = $(this).find('> div.datagrid-body-inner').length ? $(this).find('> div.datagrid-body-inner')[0] : this;
	                    $(delegateEle).undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove').delegate('td', {
	                        'mouseover': function(e){
	                            if (params.delay){
	                                if (options.tipDelayTime) clearTimeout(options.tipDelayTime);
	                                var that = this;
	                                options.tipDelayTime = setTimeout(function(){
	                                    showTip(options, that, e);
	                                },
	                                params.delay);
	                            } else {
	                                showTip(options, this, e);
	                            }
	                        },
	                        'mouseout': function(e){
	                            if (options.tipDelayTime) clearTimeout(options.tipDelayTime);
	                            options.tooltip.css({
	                                'display': 'none'
	                            });
	                        },
	                        'mousemove': function(e){
	                            var that = this;
	                            if (options.tipDelayTime){
	                                clearTimeout(options.tipDelayTime);
	                                options.tipDelayTime = setTimeout(function(){
	                                    showTip(options, that, e);
	                                },
	                                params.delay);
	                            } else {
	                                showTip(options, that, e);
	                            }
	                        }
	                    });
	                });
	            }
	        });
	    },
	    /**关闭消息提示功能*/
	    cancelCellTip: function(jq){
	        return jq.each(function(){
	            var data = $(this).data('datagrid');
	            if (data.tooltip){
	                data.tooltip.remove();
	                data.tooltip = null;
	                var panel = $(this).datagrid('getPanel').panel('panel');
	                panel.find('.datagrid-body').undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove');
	            }
	            if (data.tipDelayTime){
	                clearTimeout(data.tipDelayTime);
	                data.tipDelayTime = null;
	            }
	        });
	    }
	});
})(jQuery);