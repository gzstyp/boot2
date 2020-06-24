<%@ page language="java" pageEncoding="UTF-8"%>
<body>
<div fit="true" class="easyui-panel" border="false" style="padding:0px;">
	<div id="toolbar_degree" style="padding-top:4px; padding-bottom:4px;height:auto;">
		<table style="font-size:12px;">
			<tr>
				<td><button type="button" class="button button-primary button-rounded button-small" onclick="layerFn.showCT(AppKey.code.code200,'居中显示-的操作成功');" >居中显示-的操作成功</button></td>
				<td><button type="button" class="button button-primary button-rounded button-small" onclick="euiFn.showRb(AppKey.code.code199,'修改失败');" >修改失败</button></td>
				<td><button type="button" class="button button-primary button-rounded button-small" onclick="euiFn.showRb(AppKey.code.code200,'操作成功');" >操作成功</button></td>
				<td><button type="button" class="button button-primary button-rounded button-small-caolvse" onclick="euiFn.showRb(AppKey.code.code204,'系统异常');" >系统异常</button></td>
				<td><button type="button" class="button button-rounded button-small" onclick="thisPage.tips();" >居中显示-操作失败</button></td>
				<td><button type="button" class="button button-rounded button-small-select" onclick="thisPage.uploadFile();" >文件上传</button></td>
				<td><button type="button" class="button button-caution button-rounded button-small" onclick="layerFn.winRDom('编辑','#div_edit','1000px',function(index,layero){alert('提交');});">编辑DOM</button></td>
				<td><button type="button" class="button button-caution button-rounded button-small-huangse" onclick="layerFn.winRUrl('查看URL','page?page=view','1000px',null,'关闭');">查看url</button></td>
				<td><button type="button" class="button button-caution button-rounded button-small-huangse" onclick="thisPage.openEdit();">编辑url</button></td>
				<td><button type="button" class="button button-caution button-rounded button-small-huangse" onclick="thisPage.editURL();">居中编辑url</button></td>
				<td><button type="button" class="button button-caution button-rounded button-small-huangse" onclick="thisPage.editDom();">居中编辑Dom</button></td>
				<td><button type="button" class="button button-primary button-rounded button-small" onclick="thisPage.dialog();">弹出框</button></td>
			</tr>
		</table>
		<a href="javascript:layerFn.winLayer.rt('右上角');" class="easyui-linkbutton" style="color:#3385ff;">右上角</a>
		<a href="javascript:layerFn.winLayer.rb('右下角');" class="easyui-linkbutton" iconCls="icon-add">右下角</a>
		<button class="easyui-linkbutton" onclick="layerFn.winLayer.cc('居中?');">居中</button>
		<button class="easyui-linkbutton" onclick="layerFn.showCT(AppKey.code.code199,'操作失败!');">居中偏上</button>
		<button class="easyui-linkbutton" onclick="thisPage.labelCheckbox();">复选框取值</button>
		<button class="easyui-linkbutton" onclick="thisPage.labelRadio();">单选框取值</button>
		<button class="easyui-linkbutton" onclick="thisPage.allCheckbox();">全选复选框</button>
		<button class="btn_148x28_blue" onclick="layerFn.center(AppKey.code.code200,'操作成功');">操作成功</button>
		<button class="btn_148x28_blue" onclick="layerFn.center(AppKey.code.code204,'系统异常');">系统异常</button>
		<button class="button button-caution button-rounded button-small-huangse" onclick="thisPage.area();">省市级级联</button>
		<button type="button" class="button button-rounded button-small-lanse" onclick="thisPage.alert();" >alert插件开发</button>
		<button type="button" class="button button-rounded button-small-lanse" onclick="thisPage.confirm();" >confirm</button>
		<button type="button" class="button button-rounded button-small-lanse" onclick="thisPage.closeEvent();" >关闭带事件</button>
		<button type="button" class="button button-primary button-rounded button-small-caolvse" onclick="thisPage.table();" >表格操作</button>
		<button type="button" class="button button-primary button-rounded button-small" onclick="thisPage.viewDom();">查看DOM</button>
	</div>
	<div id='button_div_select'>
		<select id="button_select_menus" class="ui-select block_hidden"></select>
	</div>
	<div class="checkbox_radio checkbox_radio_clear">
	  <ul>
	  	<li>
	      <input type="checkbox" id='allCheckbox'/>
	      <label for="allCheckbox">全选</label>
	    </li>
	    <li>
	      <input type="checkbox" name='hobby' value="游戏" id='game'/>
	      <label for="game">游戏</label>
	    </li>
	    <li>
	      <input type="checkbox" name='hobby' value="游泳" id='youyong' checked="checked"/>
	      <label for="youyong">游泳</label>
	    </li>
	    <li>
	      <input type="checkbox" name='hobby' value="泡妞" id='paoniu'/>
	      <label for="paoniu">泡妞</label>
	    </li>
	  </ul>
	</div>
	<button type="button" class="button button-rounded button-small-select" onclick="thisPage.setNv();" >女生</button>
	<div class="checkbox_radio checkbox_radio_clear">
	  <ul>
	    <li>
	      <input type="radio" name="sex" value="男生" id='nan'/>
	      <label for="nan">男生</label>
	    </li>
	    <li>
	      <input type="radio" name="sex" value="女生" id='nv'/>
	      <label for="nv">女生</label>
	    </li>
	    <li>
	      <input type="radio" name="sex" value="保密" id='baomi' checked="checked" />
	      <label for="baomi">保密</label>
	    </li>
	  </ul>
	</div>
	表格样式示例
	<table cellspacing="0" cellpadding="0" class="view" width="100%" border="0">
		<thead>
			<tr><td>编号</td><td>名字</td><td>性别</td></tr>
		</thead>
		<tbody>
			<tr><td>1001</td><td>孙悟空</td><td>男</td></tr>
			<tr><td>1002</td><td>诸葛亮</td><td>男</td></tr>
			<tr><td>1003</td><td>花千骨</td><td>女</td></tr>
			<tr><td>1004</td><td>猪八戒</td><td>男</td></tr>
			<tr><td>1005</td><td>秦始皇</td><td><a href="javascript:">0男</a></td></tr>
			<tr><td>1006</td><td>武则天</td><td><a href="javascript:">女</a></td></tr>
		</tbody>
	</table>
	<table cellspacing="0" cellpadding="0" class="view" width="100%" style="margin-top:10px;">
		<thead>
			<tr><td>编号</td><td>名字</td><td>性别</td></tr>
		</thead>
		<tbody>
			<tr><td>1001</td><td>孙悟空</td><td>0男0</td></tr>
			<tr><td>1003</td><td>花千骨</td><td>女</td></tr>
			<tr><td>1005</td><td>秦始皇</td><td><a href="javascript:thisPage.msg('男');">男</a></td></tr>
			<tr><td>1006</td><td>武则天</td><td><a href="javascript:thisPage.msg('女');">女</a></td></tr>
		</tbody>
	</table>
	<div id='div_edit' class="divLayer">
		<table class="view" width="100%" >
			<thead>
				<tr><td>编号</td><td>名字</td><td>性别</td></tr>
			</thead>
			<tbody>
				<tr><td>1001</td><td>猪八戒</td><td>公</td></tr>
				<tr><td>1003</td><td>花千骨</td><td><a href="javascript:layerFn.tips('#sex','那个贱货是女的');" id='sex'>提示</a></td></tr>
				<tr><td>1005</td><td>秦始皇</td><td><a href="javascript:thisPage.msg('母');">女</a></td></tr>
				<tr><td>1006</td><td>武则天</td><td><a href="javascript:thisPage.msg('公');">男</a></td></tr>
			</tbody>
		</table>
	</div>
	<!-- 上传文件 -->
	<div id='div_import' style="display:none;">
		<form name="form" id="form">
			<table cellspacing="0" cellpadding="8" style="margin-left:10px;margin-right:10px;margin-top:40px;">
				<tr>
					<td>
						<input id="file_excel" name='file_excel' style="width:340px;height:32px;line-height:32px;" class="easyui-filebox" data-options="buttonText:'选择文件',buttonAlign:'right',editable:false,onChange:function(newValue,oldValue){thisPage.checkExt(this,newValue);}"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 表格操作 -->
	<div id='table_edit' class="divLayer">
		<table id="tableRows" class="view" width="100%">
		    <thead>
				<tr>
			      <td>字段名称</td>
			      <td>字段类型</td>
			      <td>字段注释</td>
			      <td>是否为空</td>
			      <td>删除操作</td>
			    </tr>
			</thead>
			<tbody>
			</tbody>
	  </table>
	</div>
	<!-- 查看DOM -->
	<div id='view_dom' class="divLayer">
		<p>可以按Esc关闭对话框</p>
	</div>
	<!-- 省市县级联 -->
	<div id='area_dom' class="divLayer">
		<select id="TFPROVINCEID" class="select" value='' onchange="jQArea.onSelectChange(this,'TFCITYID');"><option value=''>请选择省/市</option></select>
		<select id="TFCITYID" class="select" value='' style="display:none" onchange="jQArea.onSelectChange(this,'TFDISTRICTID');"><option value=''></option></select>
		<select id="TFDISTRICTID" class="select" value='' style="display:none;" onchange="jQArea.onSelectChange(this,'TFTOWNSID');"><option value=''></option></select>
		<select id="TFTOWNSID" class="select" value='' style="display:none;" onchange="jQArea.onSelectChange(this,'TFVALLAGEID');"><option value=''></option></select>
		<select id="TFVALLAGEID" class="select" value='' style="display:none;"><option value=''></option></select>
	</div>
</div>
<script type="text/javascript">
    ;(function ($) {
        var tableRows='#tableRows';
        thisPage = {
            datagrid : '#datagrid_sys_menu',
            toolbar : '#toolbar_sys_menu',
            url : 'menu/listData',
            init : function(){
                var _self = this;
                _self.initSelect();
                //winFn.uiCheckboxRadio('.checkbox_radio');
                winFn.uiCheckboxRadio();
                winFn.uiCheckboxHandle('#allCheckbox','hobby','.checkbox_radio');
                winFn.tableMouse('view');
            },
            /**动态添加自定义下拉列表*/
            initSelect : function(){
                layerFn.selectUI('menu/ownerMenus',{id:'00189863b0184315af96b86f0f223e56'},'选择菜单','暂无菜单','#button_select_menus','id','name',null,'#button_div_select',function(value){
                    euiFn.showRb(AppKey.code.code200,value);
                },'100px');
            },
            labelCheckbox : function(){
                var hobby = winFn.checkboxBatch('hobby');
                if(verifyFn.isEmpty(hobby)){
                    layerFn.center(AppKey.code.code204,'请选择爱好');
                    return;
                }
                layerFn.center(AppKey.code.code200,hobby);
            },
            labelRadio : function(){
                var sex = winFn.getRadioValue('sex');
                if(verifyFn.isEmpty(sex)){
                    layerFn.center(AppKey.code.code204,'请选择性别');
                    return;
                }
                layerFn.center(AppKey.code.code200,sex);
            },
            /**全选复选框事件*/
            allCheckbox : function(){
                $('.checkbox_radio input[name="hobby"]').iCheck('check');
            },
            /**单选框赋值*/
            setNv : function(){
                winFn.setRadioValue('sex','女生');
            },
            tips : function(){
                parent.layer.msg('保存失败，请重试！', { icon:7, anim:6 ,offset: 't'});//2错误;7警告;
            },
            msg : function(msg){
                layerFn.center(AppKey.code.code200,msg);
            },
            viewDom : function(){
                var exitIndex = layerFn.winDom('查看DOM','#view_dom',['300px','250px'],null,'关闭');
                layerFn.EscLayer(exitIndex);
            },
            openEdit : function(){
                layerFn.winRUrl('编辑URL','page?page=edit','1000px',function(index,layero){
                    var iframeWin = layerFn.getIframe(layero);
                    iframeWin.thisPage.submit(function(data){
                        euiFn.showRb(data.code,data.msg);
                        layerFn.loading.hide(index);
                    });
                });
            },
            editURL : function(){
                layerFn.winUrl('居中编辑','page?page=edit',['800px','600px'],function(index,layero){
                    var iframeWin = layerFn.getIframe(layero);
                    iframeWin.thisPage.submit(function(data){
                        euiFn.showRb(data.code,data.msg);
                        layerFn.loading.hide(index);
                    });
                });
            },
            editDom : function(){
                layerFn.winDom('居中编辑','#div_edit',['600px','400px'],function(index,layero){
                    layerFn.tips('#sex','那个贱货是女');
                });
            },
            dialog : function(){
                layerFn.winPrint('打印','#div_edit',['600px','270px'],function(index,layero){
                    layerFn.layerClose(index);
                });
            },
            /**文件上传*/
            uploadFile : function(){
                $("#file_excel").textbox('setValue','');
                layerFn.addOrEdit('文件上传','#div_import',['380px','270px'],function(index,layero){
                    var value = $("#file_excel").textbox('getValue');
                    if(value == "" ){
                        layerFn.center(AppKey.code.code199,'请选择Excel文件!');
                        return false;
                    }
                    var ext = winFn.checkExt(value,'xls,xlsx');
                    if(!ext){
                        layerFn.center(AppKey.code.code199,'请选择Excel文件格式');
                        return false;
                    }
                    self.loadIndex = layerFn.loading.show('正在上传……');
                    winFn.uploadSingle('file/uploadSingle','#form',function(data){
                        layerFn.loading.hide(self.loadIndex)
                        if (data.code == AppKey.code.code200){
                            layerFn.loading.hide(index)
                            path = data.map.path.replace(/\\/g,'/');
                            euiFn.showRb(data.code,path);
                        }else{
                            layerFn.center(data.code,data.msg);
                        }
                    },function(er){
                        layerFn.center(AppKey.code.code199,'上传失败,错误码:'+er);
                    });
                });
            },
            /**检查是否指定的文件格式*/
            checkExt : function(obj,value){
                var exts = 'xls,xlsx';
                var ext = winFn.checkExt(value,exts);
                if(value !='' && !ext){
                    $("#"+obj.id).textbox('setValue','');
                    layerFn.center(AppKey.code.code199,'请选择Excel文件格式,可用格式:'+exts);
                }
            },
            area : function(){
                layerFn.addOrEdit('省市县级联','#area_dom',['650px','450px'],function(index){
                    var value = $('#TFCITYID').attr('value');
                    layerFn.center(AppKey.code.code200,value);
                });
                jQArea.init();
            },
            alert : function(){
                layerFn.alert('只做提示对话框,没有事件,更多使用方式请看代码,只做示例代码,不推荐使用!',AppKey.code.code199);
            },
            confirm : function(){
                layerFn.confirm('确认对话框,只有确定按钮才有回调',function(){layerFn.center(AppKey.code.code199,'事件的回调');});
            },
            closeEvent : function(){
                layerFn.closeEvent(AppKey.code.code204,'测试单个按钮事件',function(){
                    layerFn.center(AppKey.code.code200,'不管按确定按钮还是右上角的按钮都会引发关闭事件的回调');
                });
            },
            /**表格操作*/
            table : function(){
                var exitIndex = layerFn.addOrEdit('表格操作','#table_edit',['600px','400px'], function(index){
                },'<span style="color:#1e9fff;">添加行</span>',function(){
                    thisPage.addRow();
                });
                layerFn.EscLayer(exitIndex);
            },
            /**添加行*/
            addRow : function(){
                var uuid = winFn.getUuid();//在应用中如果能确定uuid是传递来的参数且是唯一的话,uuid可直接替换成传递来的参数作为唯一值,然后还可以根据该uuid是否已重复添加了
                var exist = $(tableRows +" tbody tr#" + uuid).html();//判断是否已添加
                if(exist == null || exist == ''){
                    var rowTemplate = '<tr id="'+uuid+'"><td>字段名称</td><td><select name="type" id="type"><option value="">类型</option><option value="1">字符串</option><option value="2">正整数</option><option value="3">char类型</option></select></td><td><input type="text" /></td><td><input type="checkbox" /></td><td><a href="javascript:;" title="删除当前行" onclick=thisPage.delRow("'+uuid+'")>删除</a></td></tr>';
                    var tableHtml = $(tableRows +" tbody").html();
                    tableHtml += rowTemplate;
                    $(tableRows +" tbody").html(tableHtml);
                    //winFn.tableMouse('view');
                }
            },
            /**删除行*/
            delRow : function(uuid){
                $("#"+uuid).remove();
            },
        };
	thisPage.init();
})(jQuery);
//闭包限定命名空间,调用方式:$("p").highLight({foreground:'orange',background:'#ccc'});
(function ($) {
    //默认参数
    var defaluts = {
        foreground : 'red',
        background : 'yellow',
    };
	$.fn.extend({
		highLight : function(options){
            var opts = $.extend({},defaluts,options); //使用jQuery.extend 覆盖插件默认参数
            return this.each(function (){//这里的this 就是 jQuery对象。这里return 为了支持链式调用
                //遍历所有的要高亮的dom,当调用 highLight()插件的是一个集合的时候。
                var $this = $(this); //获取当前dom 的 jQuery对象，这里的this是当前循环的dom
                //根据参数来设置 dom的样式
                $this.css({
                    backgroundColor: opts.background,
                    color: opts.foreground
                });
            });
        },
    });
})(jQuery);
if('${pd.TFPROVINCEID}' != ''){//省份
	jQArea.selectChange('0','选择省份','暂无选项','TFPROVINCEID','${pd.TFPROVINCEID}');//编辑修改
	$('#TFPROVINCEID').css('display','inline');
}else{
	//jQArea.init();
}
if('${pd.TFCITYID}' != ''){//市/区
	jQArea.selectChange('${pd.TFPROVINCEID}','选择市/区','暂无选项','TFCITYID','${pd.TFCITYID}');
	$('#TFCITYID').css('display','inline');
}
if('${pd.TFDISTRICTID}' != ''){//区/县
	jQArea.selectChange('${pd.TFCITYID}','选择区/县','暂无选项','TFDISTRICTID','${pd.TFDISTRICTID}');
	$('#TFDISTRICTID').css('display','inline');
}
if('${pd.TFTOWNSID}' != ''){//乡镇
	jQArea.selectChange('${pd.TFDISTRICTID}','选择乡镇','暂无选项','TFTOWNSID','${pd.TFTOWNSID}');
	$('#TFTOWNSID').css('display','inline');
}
if('${pd.TFVALLAGEID}' != ''){//村庄
	jQArea.selectChange('${pd.TFTOWNSID}','选择村庄','暂无选项','TFVALLAGEID','${pd.TFVALLAGEID}');
	$('#TFVALLAGEID').css('display','inline');
}
</script>
</body>