/**
	显示完整的全序号,可做学习示例
	作者 田应平
	创建时间 2017年1月18日 01:30:18
	调用方法:onLoadSuccess:'fixRownumber'
*/
$.extend($.fn.datagrid.methods,{
    fixRownumber : function (jq){
        return jq.each(function (){
            var panel = $(this).datagrid("getPanel");
            var clone = $(".datagrid-cell-rownumber", panel).last().clone();
            clone.css({
                "position" : "absolute",
                left : -1000
            }).appendTo("body");
            var width = clone.width("auto").width();
            if (width > 25) {
                //多加5个像素,保持一点边距 
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).width(width + 5);
                $(this).datagrid("resize");
                //一些清理工作
                clone.remove();
                clone = null;
            } else {
                //还原成默认状态 
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).removeAttr("style");
            }
        });
    }
});