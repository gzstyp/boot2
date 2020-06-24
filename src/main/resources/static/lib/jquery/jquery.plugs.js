//示例说明：当要对页面元素进行JQuery扩展时，需要采用:;(function($){})(jQuery);
/*-----------------------------------------------*/
;(function($){
	window.jqArea = {
		init : function(){
			layerFn.center(AppKey.code.code200,'初始化成功哦');
		},
	};
	jqArea.init();
})(jQuery);
/**调用方式:$("p").jqArea({code : 200,msg : '你好'});*/
;(function($){
	//默认参数
    var defaluts = {
        code : 199,
        msg : '警告',
    };
	$.fn.extend({
		layerAlert : function(options){
			var opts = $.extend({},defaluts,options);
			layerFn.center(opts.code,opts.msg);
        },
    });
})(jQuery);
/*-----------------------------------------------*/
//闭包限定命名空间,调用方式:$("p").highLight({foreground:'orange',background:'#ccc'});
;(function($){
	//默认参数
    var defaluts = {
        foreground: 'red',
        background: 'yellow'
    };
	$.fn.extend({
        "highLight": function (options){
            var opts = $.extend({},defaluts,options); //使用jQuery.extend 覆盖插件默认参数
            return this.each(function (){  //这里的this 就是 jQuery对象。这里return 为了支持链式调用
                //遍历所有的要高亮的dom,当调用 highLight()插件的是一个集合的时候。
                var $this = $(this); //获取当前dom 的 jQuery对象，这里的this是当前循环的dom
                //根据参数来设置 dom的样式
                $this.css({
                    backgroundColor: opts.background,
                    color: opts.foreground
                });
            });
        }
    });
})(jQuery);
/*-----------------------------------------------*/
//闭包限定命名空间,调用方式:$("p").highLight({foreground:'orange',background:'#ccc'});
;(function($){
	//默认参数
    var settings = {
        foreground: 'red',
        background: 'yellow'
    };
	$.fn.extend({
        "highLighsssst": function (options){
            var opts = $.extend({},settings, options); //使用jQuery.extend 覆盖插件默认参数
            return this.each(function (){  //这里的this 就是 jQuery对象。这里return 为了支持链式调用
                //遍历所有的要高亮的dom,当调用 highLight()插件的是一个集合的时候。
                var $this = $(this); //获取当前dom 的 jQuery对象，这里的this是当前循环的dom
                //根据参数来设置 dom的样式
                $this.css({
                    backgroundColor: opts.background,
                    color: opts.foreground
                });
            });
        }
    });
})(jQuery);
/*-----------------------------------------------*/
/*
插件名：
作者：
日期：等信息
*/
//一个匿名自执行函数，划分一个独立的作用域，不至于插件中的变量干扰jquery
;(function($){//开始写上;为防止代码压缩出错
    //为jquery扩展方法，也就是插件的主体
    $.fn.extend({
        //方法名
        "method" : function(opts){
            //定义插件的默认参数
            var defaults = {
                width : '',//定义默认宽度
                height : '',//定义默认高度
                speed : '',//定义默认速度
                //等等各种默认参数
            };
            //有些参数用户直接使用默认，有些参数用户要使用自己定义的
            //自定义参数替换默认参数
            //var option = $.extend(default,opts); //为什么不用此行代码？为了保护默认参数
            //extend方法中opts会永久取代default，所以新加一个空对象{}来保存本次所使用的参数，下次使用default依然不变
            var option = $.extend({},defaults,opts);
            this.each(function(){ //用each处理选择器选中的一个或多个dom节点
                /*此处是插件处理过程代码*/
            });
            //最后别忘了保持jquery的链式操作（视情况而定）
            return this; //返回被选中的元素节点，以供后续操作。
        }
    });
})(jQuery);//传入jQuery是为更快查找，避免沿作用域链往上层查找，提高性能