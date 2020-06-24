/**
 * 自定义公共js文件,含有通用的winFn;依赖easyui的euiFn;含有依赖jQuery验证的verifyFn;依赖layer的layerFn;
 * @作者 田应平
 * @QQ号 444141300
 * @原理 模块化,键值对,不管是方法还是属性都是键值对=或:,=是赋值;:是键值对
 * @Web  www.fwtai.com
 * @创建时间 2016年12月27日 00:54:53
*/
;(function(){
    window.AppKey = {
        baseUri : window.location.origin,/**AppKey.baseUri*/
        iconDir : 'lib/easyui/themes/icons/',/**AppKey.iconDir*/
        token : '',/** JSON Web Token */
        confirm : '确定',/**AppKey.confirm*/
        close : '关闭',/**AppKey.close*/
        submit : '提交',/**AppKey.submit*/
        cancel : '取消',/**AppKey.cancel*/
        title : "系统提示",
        msg_error : "哦!网络故障请<a href='javascript:;' onclick='if(self==top){window.location.reload();}else{top.location.reload();}' style='text-decoration:none;color:#01aaed;outline:none;cursor:pointer;'>刷新</a>或稍候重试",/**连接服务器失败*/
        LoadingImg :'.loadingImg',
        notLogin : '未登录或登录超时!',
        loginUrl : 'login',/**跳转到登录界面;用法:AppKey.loginUrl*/
        control : 'control',/**跳转到后台中心;用法:AppKey.control*/
        sysError : '系统出现错误',
        layerArea : '270px',/**AppKey.layerArea*/
        loadMsg : function(){
            var num = Math.random() * 10 + 1;/**随机生成1到10之间的数,包含1不包含10,即<=1 && =>9*/
            num = parseInt(num,10);
            if(num == 1){
                return '加载是件闹心事……';
            }else if(num == 2){
                return '感谢你的等待,请稍等……';
            }else if(num == 3){
                return '网络差,请稍等……';
            }else if(num == 4){
                return '正在载入数据……';
            }else if(num == 5){
                return '网络阻塞,努力加载中……';
            }else if(num == 6){
                return '服务器压力大,努力处理中……';
            }else if(num == 7){
                return '处理中,请稍等……';
            }else if(num == 8){
                return '数据正在赶来,请稍等……';
            }else{
                return '网络不给力,努力加载中……';
            }
        },
        bodyContainer : '#bodyContainer',/**easyui主body*/
        code : {
            code198 : 198,/**自定义code及msg*/
            code199 : 199,/**请求失败|操作失败*/
            code200 : 200,/**请求成功|操作成功*/
            code201 : 201,/**返回空值|暂无数据*/
            code202 : 202,/**请求协议参数不完整*/
            code203 : 203,/**密钥验证失败*/
            code204 : 204,/**系统错误*/
            code205 : 205,/**未登录或登录超时*/
            code206 : 206,/**账号或密码不正确*/
            code207 : 207/**非法操作!或你的账号已被删除,一般用于被迫强制退出登录*/
        }
    },
    /**通用的方法,依赖jQuery*/
    window.winFn = {
        /**用JS获取地址栏参数的方法[采用正则表达式获取地址栏参数],用法:winFn.getParamURI('id');*/
        getParamURI : function(key){
            var reg = new RegExp("(^|&)"+ key +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);//search,查询?后面的参数,并匹配正则
            if(r!=null)return unescape(r[2]);return "";
        },
        /**用JS获取地址栏参数的方法[采用正则表达式获取地址栏参数],用法:winFn.getParamURL('id');*/
        getParamURL : function(key){
            var url = document.location.toString();
            var arrObj = url.split("?");
            if (arrObj.length > 1) {
                var arrPara = arrObj[1].split("&");
                var arr;
                for (var i = 0; i < arrPara.length; i++){
                    arr = arrPara[i].split("=");
                    if (arr != null && arr[0] == key){
                        return arr[1];
                    }
                }
                return "";
            }else{
                return "";
            }
        },
        /**赋值:winFn.setDomValue(dom,value);*/
        setDomValue : function(dom,value){
            try{
                if (value == null || value == '' || value == undefined || value == 'undefined'){
                    $(dom).val('');
                    $(dom).textbox('setValue','');
                } else {
                    $(dom).val(value);
                    $(dom).textbox('setValue',value);
                }
            }catch(e){}
        },
        /**获取值:winFn.getDomValue(dom);if(value == null){}*/
        getDomValue : function(dom){
            var v = $(dom).val();
            if(v == null || v.length <= 0)return null;
            if(v == '_') return null;
            return $.trim(v);
        },
        /**强制获取焦点:winFn.focus('#xx');*/
        focus : function(dom){
            $(dom).focus();
        },
        /**winFn.setDataSelect(defaultText,emptyText,'#id或.class',data,value,showText,select);一般第3个参数都是id，即可'#Xxx'*/
        setDataSelect:function(defaultText,emptyText,selectDom,data,value,showText,select){
            var htm = "<option value=''>"+defaultText+"</option>";
            if (data == null || data == '' || data == undefined || data.length <= 0){
                htm = "<option value=''>"+emptyText+"</option>";
                $(selectDom).attr('disabled','disabled');
                $(selectDom).attr('title',(emptyText == null || emptyText == '' || emptyText == undefined)?'暂无选项':emptyText);
            } else {
                $(selectDom).attr('title',(defaultText == null || defaultText == '' || defaultText == undefined)?'选择选项':defaultText);
                for(var i=0;i<data.length;i++){
                    htm+="<option value="+data[i][value]+">"+data[i][showText]+"</option>";
                }
                if($(selectDom).prop("disabled")){
                    $(selectDom).prop("disabled",false);
                }
            }
            $(selectDom).html(htm);
            if(!verifyFn.isEmpty(select) && data.length > 0){
                for(var i=0;i<data.length;i++){
                    if(data[i][value]==select){
                        $(selectDom).val(select);
                        break;
                    }
                }
            }
        },
        /**恢复下拉选项:winFn.setSelectReset(selectDom,defaultText);*/
        setSelectReset : function(selectDom,defaultText){
            var htm = "<option value=''>"+defaultText+"</option>";
            if($(selectDom).prop("disabled")){
                $(selectDom).prop("disabled",false);
            }
            $(selectDom).html(htm);
            $(selectDom).attr('title',defaultText);
        },
        /**只适用于最顶级的级联的更改事件:winFn.onchange(dom,onchangeCall);可以在成功加载list数据之后再调用本方法;*/
        onchange : function(dom,onchangeCall){
            $(dom).on('change',function(){
                onchangeCall($(dom).val());
            });
        },
        /**禁用滚动条:winFn.overflowHidden();*/
        overflowHidden : function(){
            $("body").eq(0).css("overflow","hidden");
        },
        /**启用滚动条:winFn.overflowAuto();*/
        overflowAuto : function(){
            $("body").eq(0).css("overflow","auto");
        },
        /**自定义正在加载效果,没遮罩层:winFn.ajaxLoading(dom,msg);*/
        ajaxLoading : function(dom,msg){
            if(msg == null || msg == '' || msg == undefined){
                $(dom).hide();
                $(dom+" span").text('');
            }else {
                $(dom).show();
                $(dom+" span").text(msg);
            }
        },
        /**两个时间比较:winFn.dateCompare(domStartTime,domEndTime);*/
        dateCompare : function(domStartTime,domEndTime){
            var start = DateTime.parse($("#"+domStartTime).val());
            var end = DateTime.parse($("#"+domEndTime).val());
            return start > end;
        },
        /**POST请求远程数据,不带正在加载提示信息;winFn.ajaxPost(url,params,succeed,failure);*/
        ajaxPost : function(url,params,succeed,failure){
            $.ajax({
                type : "POST",
                url : url,
                dataType : "json",
                data : params,
                success : function(result){
                    succeed(result);
                },
                error : function(response,err){
                    if (failure != null && failure != ''){
                        failure(err);
                    }
                }
            });
        },
        /**GET请求远程数据,不带正在加载提示信息;winFn.ajaxGet(url,params,succeed,failure);*/
        ajaxGet : function(url,params,succeed,failure){//无正在加载提示信息
            $.ajax({
                type : "GET",
                url : url,
                dataType : "json",
                data : params,
                success : function(result){
                    succeed(result);
                },
                error : function(response,err){
                    if (failure != null && failure != ''){
                        failure(err);
                    }
                }
            });
        },
        /**带token的GET请求远程数据,不带正在加载提示信息;winFn.ajaxTokenGet(url,params,succeed,failure);*/
        ajaxTokenGet : function(url,params,succeed,failure){//无正在加载提示信息
            $.ajax({
                type : "GET",
                url : url,
                headers: {'Authorization': AppKey.token},
                dataType : "json",
                data : params,
                success : function(result){
                    succeed(result);
                },
                error : function(response,err){
                    if (failure != null && failure != ''){
                        failure(err);
                    }
                }
            });
        },
        /**带token的POST请求远程数据,不带正在加载提示信息;winFn.ajaxTokenPost(url,params,succeed,failure);*/
        ajaxTokenPost : function(url,params,succeed,failure){//无正在加载提示信息
            $.ajax({
                type : "POST",
                url : url,
                headers: {'Authorization': AppKey.token},
                dataType : "json",
                data : params,
                success : function(result){
                    succeed(result);
                },
                error : function(response,err){
                    if (failure != null && failure != ''){
                        failure(err);
                    }
                }
            });
        },
        /**带token的GET请求远程数据,不带正在加载提示信息;winFn.ajaxGetToken(url,params,succeed,failure);*/
        ajaxGetToken : function(url,params,succeed,failure){//无正在加载提示信息
            $.ajax({
                type : "GET",
                url : url,
                dataType : "json",
                data : params,
                beforeSend: function(request) {
                    request.setRequestHeader("Authorization", AppKey.token);
                },
                success : function(result){
                    succeed(result);
                },
                error : function(response,err){
                    if (failure != null && failure != ''){
                        failure(err);
                    }
                }
            });
        },
        /**刷新验证码:winFn.refreshCode(domImage,"imgCode");其中imgCode是获取验证码的url,即标签为<img src="imgCode">*/
        refreshCode : function(domImage,url){
            $(domImage).attr("src",url+"?date="+new Date().getTime());
        },
        /**显示分页条上的正在加载效果:winFn.pageBarShow();*/
        pageBarShow : function(){
            $(AppKey.LoadingImg).css({"display":"block"});
        },
        /**隐藏关闭分页条上的正在加载效果:winFn.pageBarHide();*/
        pageBarHide : function(){
            $(AppKey.LoadingImg).css({"display":"none"});
        },
        /**单个元素或节点赋值,不含html:winFn.setText(dom,value);*/
        setText : function(dom,value){
            $(dom).text(value);
        },
        /**用于批量操作选择;checkboxName是name的标识:winFn.checkboxBatch(checkboxName,flag);*/
        checkboxBatch : function(checkboxName,flag){
            var checked = document.getElementsByName(checkboxName);
            var ids = "";
            if(flag == null || flag == '' || flag == undefined){flag = ",";}
            for ( var i = 0; i < checked.length; i++){
                if (checked[i].checked){
                    if ($.trim(ids).length > 0)ids += flag;
                    ids += checked[i].value;
                }
            }
            var arr = new Array();
            arr = ids.split(flag);//转换成数组,然后获取数组的长度即可
            if(arr.length <= 0 || ids == ""){
                return null;
            }else {
                return ids;
            }
        },
        /**获取数组的长度:winFn.arrLength(arr,flag);winFn.arrLength(arr);*/
        arrLength : function(arr,flag){
            if(flag == null || flag == '' || flag == undefined)flag = ",";
            if(arr == null || arr == '')return 0;
            return arr.split(flag).length;
        },
        /**判断是否已选择复选框,用法:if(!winFn.checkboxSelect('#menu_is_select')){};*/
        checkboxSelect : function(selectDom){
            return $(selectDom).prop("checked");
        },
        /**以复选框的选择而全选否则以取消而全部取消选择;局部areaDom块,为空时是整个页面;winFn.checkDom(checkboxObj,checkboxName,areaDom);*/
        checkDom : function(checkboxObj,checkboxName,areaDom){
            $(checkboxObj).on("change", function(){
                var _self = this;
                if(areaDom != null && areaDom != ''){
                    $(areaDom + " input[name='"+checkboxName+"']").each(function(){
                        this.checked = _self.checked;
                    });
                }else{
                    $("input[name='"+checkboxName+"']").each(function(){
                        this.checked = _self.checked;
                    });
                }
            });
        },
        /**以复选框的选中或取消对应的操作:winFn.checkFn(checkboxObj,on,off);*/
        checkFn : function(checkboxObj,on,off){
            $(checkboxObj).on("change", function(){
                var _self = this;
                if (_self.checked){
                    on();
                } else {
                    off();
                }
            });
        },
        /**以复选框的选中显示input或取消隐藏input的操作,原生态的input输入框默认是隐藏:winFn.checkOnOff(checkboxObj,inputDom,on,off);*/
        checkOnOff : function(checkboxObj,inputDom,on,off){
            $(checkboxObj).on("change",function(){
                var _self = this;
                if (_self.checked){
                    $(inputDom).css({"visibility":"visible"});
                    $(inputDom).show();
                    on();
                } else {
                    $(inputDom).css({"visibility":"hidden"});
                    $(inputDom).hide();
                    off();
                }
            });
        },
        /**以复选框的选择或取消对应的起始时间到结束时间的开关:winFn.checkBetween(this,start,end);obj一般填写this; onchange="winFn.checkBetween(this,'#start','#end');"*/
        checkBetween : function(obj,start,end){
            $(start).textbox({
                disabled: !obj.checked
            });
            $(end).textbox({
                disabled: !obj.checked
            });
        },
        /**全部选择或全部取消操作:局部areaDom块,为空时是整个页面;checkboxName是name的标识;flag如果为true时全部选择,为false或其他则全部取消选择;用法:winFn.checkboxAll(checkboxName,flag,areaDom);*/
        checkboxAll : function(checkboxName,flag,areaDom){
            if(flag == null || flag == '' || flag == undefined){
                flag = false;
            }
            if(areaDom == null || areaDom == '' || areaDom == undefined){
                $("input[name='"+checkboxName+"']").each(function(){
                    $(this).prop("checked",flag);
                });
            }else{
                $(areaDom + " input[name='"+checkboxName+"']").each(function(){
                    $(this).prop("checked",flag);
                });
            }
        },
        /**反选复选框:局部areaDom块或整个页面的复选框的反选复选框;checkboxName是name的标识;用法:winFn.checkboxInverse(checkboxName,areaDom);*/
        checkboxInverse : function(checkboxName,areaDom){
            if(areaDom == null || areaDom == '' || areaDom == undefined){
                $("input[name='"+checkboxName+"']").each(function(){
                    var b = $(this).prop("checked");
                    $(this).prop("checked",!b);
                });
            }else{
                $(areaDom + "input[name='"+checkboxName+"']").each(function(){
                    var b = $(this).prop("checked");
                    $(this).prop("checked",!b);
                });
            }
        },
        /**选择奇数的复选框;局部areaDom块,为空时是整个页面:winFn.checkboxOdd(areaDom);*/
        checkboxOdd : function(areaDom){
            if(areaDom == null || areaDom == '' || areaDom == undefined){
                $("input:checkbox").each(function (index){
                    if( index%2 == 0){
                        $("input:checkbox").eq(index).prop("checked",true);
                    }
                });
            }else{
                $(areaDom + "input:checkbox").each(function (index){
                    if( index%2 == 0){
                        $("input:checkbox").eq(index).prop("checked",true);
                    }
                });
            }
        },
        /**选中所有[even偶数]|[odd奇数](index从0开始);局部areaDom块,为空时是整个页面:winFn.checkboxFlag(flag,areaDom);*/
        checkboxFlag : function(flag,areaDom){
            if(areaDom == null || areaDom == '' || areaDom == undefined){
                if(flag != 'even' || flag != 'odd')return;
                if (flag == 'even'){
                    $("[type='checkbox']:even").prop("checked",'true');
                } else {
                    $("[type='checkbox']:odd").prop("checked",'true');
                }
            }else{
                if(flag != 'even' || flag != 'odd')return;
                if (flag == 'even'){
                    $(areaDom + "[type='checkbox']:even").prop("checked",'true');
                } else {
                    $(areaDom + "[type='checkbox']:odd").prop("checked",'true');
                }
            }
        },
        /**清空表单-不含hidden,用法:winFn.clearFormData('#sys_menu_type');*/
        clearFormData : function(formId){
            $(':input',''+formId).not(':button, :submit, :reset, :hidden').val('').removeAttr('selected');
            var $_obj = $(formId).find("select");
            var count = $_obj.length;
            if (count > 0){
                $($_obj).each(function(i){
                    $(this).val('');
                });
            }
        },
        /**Js随机产生4个数字或字母:winFn.randomWord();*/
        randomWord : function(){
            var chars = ['1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','P','Q','R','S','T','U','V','W','X','Y','Z'];
            var res = "";
            for(var i = 0; i < 4 ; i ++){
                var id = Math.ceil(Math.random() * 31);//乘以长度 - 1 ;
                res += chars[id];
            }
            return res;
        },
        /**限制输入长度:winFn.limitLength(dom,count);*/
        limitLength : function(dom,count){
            var value = $(dom).val();
            $(dom).on('keyup',function(e){
                if(count < value.length){
                    e.preventDefault();
                    $(dom).val(value.substr(0,count));
                }
            });
        },
        /**获取下拉列表的所选值或文本*/
        select : {
            /**调用方法winFn.select.getText('#gender');*/
            getText:function(selectDom){
                if($(selectDom).val() == ''){
                    return '';
                }else{
                    return $(selectDom).find("option:selected").text();
                }
            },
            getValue:function(selectDom){
                if($(selectDom).val() == ''){
                    return '';
                }else{
                    return $(selectDom).val();
                }
            }
        },
        /**radio根据Value值设置Radio为选中状态;jquery给单选框的赋值;winFn.radioSetValue(radioName,value);winFn.radioSetValue('STATE',"${pd.STATE}");*/
        radioSetValue : function(radioName,value){
            $("input[name='"+radioName+"'][value="+value+"]").attr("checked",true);
        },
        /**radio取消选中状态;winFn.radioCancel(radioName);*/
        radioCancel : function(radioName){
            $("input:radio[name='"+radioName+"']").attr("checked",false);
        },
        /**获取单选框的值;winFn.getRadioValue(radioName);*/
        getRadioValue : function(radioName){
            var chkRadio = document.getElementsByName(radioName);
            for(var i = 0; i < chkRadio.length; i++){
                if(chkRadio[i].checked)
                    return chkRadio[i].value;
            }
        },
        /**为指定name的value的单选框的赋值,适用于iCheck组件;winFn.setRadioValue(radioName,value);*/
        setRadioValue : function(radioName,value){
            var chkRadio = document.getElementsByName(radioName);
            for(var i = 0; i < chkRadio.length; i++){
                if(chkRadio[i].value == value){
                    $(chkRadio[i]).iCheck('check');
                    break;
                }
            }
        },
        input : {
            /**启用编辑|打开[含下拉菜单或input]:winFn.input.on(inputDom);*/
            on : function(inputDom){
                $(inputDom).removeAttr("disabled");
                $(inputDom).removeAttr("style");
            },
            /**禁用|关闭|设置不可编辑或输入[含下拉菜单或input]*/
            off : function(inputDom){
                $(inputDom).attr("disabled","disabled");
                $(inputDom).css("background-color","#e2e2e2");
            }
        },
        /**初始化表格的行的鼠标滑动样式效果;调用:winFn.tableMouse('cls');*/
        tableMouse : function(cls){
            $("."+ cls + " tbody tr").mouseenter(function(){
                $(this).css({background : "#e6e6e6"});
                $(this).children('td').each(function(index, ele){
                    $(ele).css({color: "#000"});
                });
            }).mouseleave(function(){
                $(this).css({background : "#fff"});
                $(this).children('td').each(function(index, ele){
                    $(ele).css({color: "#383838"});
                });
            });
        },
        /**下拉菜单用法:winFn.uiSelectInit('#sys_menu_edit',function(value){},'100px');第1个参数是父级的节点,第3个参数是下拉列表的宽度*/
        uiSelectInit : function(parentDom,callFn,width){
            $(parentDom+"> .ui-select").selectWidget({
                change       : function (changes){
                    if(callFn != null && callFn != ''){
                        callFn(changes);
                    }
                    return changes;
                },
                effect       : "slide",
                keyControl   : true,
                speed        : 80,
                scrollHeight : 288
            });
            if(width != null && width != ''){
                $(parentDom+' div.select-main').first().css('width',width);
                $(parentDom+' div.select-block').first().css('width',width);
            }
            $(parentDom+' div.select-main').first().attr('title','选择选项');
        },
        /**清空自定义下拉列表;winFn.uiSelectReset('#sys_menu_edit','选择类型',function(){});第1个参数是父级的节点第2个参数是下拉列表默认显示的文字,回调callFn*/
        uiSelectReset : function(parentDom,text,callFn){
            $(parentDom+' .select-arrow').first().text('');
            $(parentDom+' .select-set').first().text(text);
            $(parentDom+" ul").first().each(function(){
                $(this).children().removeClass("active");
            });
            $(parentDom+" ul.select-list li:first").addClass("active");
            if(callFn != null && callFn != ''){
                callFn();
            }
        },
        /**给自定义的下拉列表赋值;用法:winFn.uiSelectSetValue(parentDom父级的dom节点,,text需要默认显示的文字,value传递进去的数值,回调callFn);*/
        uiSelectSetValue : function(parentDom,text,value,callFn){
            $(parentDom+' .select-arrow').first().text('');
            $(parentDom+' .select-set').first().text(text);
            $(parentDom+" ul").first().each(function(){
                $(this).children().removeClass("active");
            });
            var opts = $(parentDom + "> .ui-select option");
            for(var x = 0; x < opts.length; x++){
                if ($(opts[x]).val() == ''+value){
                    $(parentDom+" ul.select-list li:nth-child("+(x+1)+")").addClass("active");
                }
            }
            if(callFn != null && callFn != ''){
                callFn();
            }
        },
        /**给自定义的下拉列表禁用或启用操作;winFn.uiSelectDisabled(parentDom,type,callFn);type为1禁用,否则启用;*/
        uiSelectDisabled : function(parentDom,type,callFn){
            if (type == 1){
                $(parentDom+"> .ui-select").attr("disabled","disabled");
                $(parentDom+"> .select-main").addClass("disabled");
            }else{
                $(parentDom+"> .ui-select").removeAttr("disabled");
                $(parentDom+"> .select-main").removeClass("disabled");
            }
            if(callFn != null && callFn != ''){
                callFn();
            }
        },
        /**获取浏览器可视区域宽度,兼容主流浏览器:winFn.fnGetWidth();*/
        fnGetWidth : function(){
            var viewportwidth = 0;
            if (typeof window.innerWidth != 'undefined'){
                viewportwidth = window.innerWidth;
            }else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined' && document.documentElement.clientWidth != 0){
                viewportwidth = document.documentElement.clientWidth;
            }else {
                viewportwidth = document.getElementsByTagName('body')[0].clientWidth;
            }
            return viewportwidth;
        },
        /**获取浏览器可视区域高度,兼容主流浏览器:winFn.fnGetHeight();*/
        fnGetHeight : function(){
            var viewportheight = 0;
            if (typeof window.innerWidth != 'undefined'){
                viewportheight = window.innerHeight;
            }else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined' && document.documentElement.clientWidth != 0){
                viewportheight = document.documentElement.clientHeight;
            }else {
                viewportheight = document.getElementsByTagName('body')[0].clientHeight;
            }
            return viewportheight;
        },
        /**上传按钮,dom为按钮或a标签;fileField为文件域的dom元素:winFn.domUpload(dom,fileField,callback);*/
        domUpload : function(dom,fileField,callback){
            $(fileField).on("change", function(){
                $(dom).text("上传");
            });
            $(dom).on("click",function(){
                var self = this;
                if (self.uploading === true){//如果正在上传
                    return false;
                }
                var files = $(fileField).get(0).files; // 判断有没有文件
                if (files.length == 0){
                    return false;
                }
                self.uploading = true; // 标记正在上传
                self.originText = self.innerText;
                self.i = 1;
                self.interval = setInterval(function(){
                    self.symbol = ".";
                    if (self.i == 2) self.symbol = " ..";
                    if (self.i == 3) self.symbol = " ...";
                    self.innerText = "上传中" + self.symbol;
                    self.i++;
                    if (self.i > 3){
                        self.i = 1;
                    }
                },1000);
                function successCallback(self){
                    clearInterval(self.interval);
                    self.uploading = false;
                    self.innerText = "上传成功";
                }
                function errorCallback(self){
                    clearInterval(self.interval);
                    self.uploading = false;
                    self.innerText = "上传失败";
                    setTimeout(function(){
                        self.innerText = self.originText;
                    },5000);
                }
                /*callback(data);
                if (data.result == 1){
                    successCallback(self);
                } else {
                    errorCallback(self);
                }*/
            });
        },
        /**限制输入长度用法:maxlength="64" onkeyup="return winFn.inputLimit(this,'xxx');"*/
        inputLimit : function(o,inputDom,showDom){
            function control(inputDom,showDom,max){
                var s = document.getElementById(inputDom).value.length;
                if(s>maxl){
                    document.getElementById(inputDom).value = document.getElementById(inputDom).value.substr(0,maxl);
                }else{
                    document.getElementById(showDom).innerHTML = s;
                }
            }
            var LimitLen = o.getAttribute ? parseInt(o.value.length):"";
            control(inputDom,showDom,nMaxLen);
            if(o.getAttribute && o.value.length>LimitLen){
                document.getElementById(showDom).innerText = 8;
                o.value = o.value.substring(0,LimitLen);
            }
        },
        /**为datagrid过滤数据:winFn.dataFilter(data);*/
        dataFilter : function(data){
            if(data.code == AppKey.code.code207){
                layerFn.pageLogin(data.code,data.msg);
                return '';
            }else if(data.code == AppKey.code.code204){
                layerFn.center(data.code,data.msg);
                return '';
            }else{
                return data;
            }
        },
        /**初始化自定义的复选框|单选框元素样式;用法:winFn.uiCheckboxRadio(areaDom);*/
        uiCheckboxRadio : function(parentDom){
            if(parentDom == null || parentDom == '' || parentDom == undefined){
                $('input[type=checkbox],input[type=radio]').on('ifCreated ifClicked ifChanged ifChecked ifUnchecked ifDisabled ifEnabled ifDestroyed',function(event){
                }).iCheck({
                    checkboxClass : 'icheckbox_square-blue',
                    radioClass : 'iradio_square-blue'
                });
            }else{
                $(parentDom+' input[type=checkbox],'+parentDom+' input[type=radio]').on('ifCreated ifClicked ifChanged ifChecked ifUnchecked ifDisabled ifEnabled ifDestroyed',function(event){
                }).iCheck({
                    checkboxClass : 'icheckbox_square-blue',
                    radioClass : 'iradio_square-blue'
                });
            }
        },
        /**初始化以单个复选框的选择而全选;以取消选择而全部取消选择的自定义复选框;handleDom[单个复选框元素],checkbokName[checkbox复选框的name],parentDom[父级节点的块,如果为空则是整个页面]:winFn.uiCheckboxHandle(handleDom,checkbokName,parentDom);*/
        uiCheckboxHandle : function(handleDom,checkbokName,parentDom){
            $(handleDom).on('ifChecked ifUnchecked',function(event){
                if(parentDom == null || parentDom == '' || parentDom == undefined){
                    if(event.type == 'ifChecked'){
                        $('input[name="'+checkbokName+'"]').iCheck('check');
                    }else if(event.type == 'ifUnchecked'){
                        $('input[name="'+checkbokName+'"]').iCheck('uncheck');
                    }
                }else{
                    if(event.type == 'ifChecked'){
                        $('.checkbox_radio input[name="'+checkbokName+'"]').iCheck('check');
                    }else if(event.type == 'ifUnchecked'){
                        $('.checkbox_radio input[name="'+checkbokName+'"]').iCheck('uncheck');
                    }
                }
            });
        },
        /**指定文件类型,返回false时说明不是指定的文件类型;用法:winFn.checkExt(originalFile,'png,gif,jpg');*/
        checkExt:function(originalFile,arrayExt){
            if($.trim(arrayExt) == '' || arrayExt == null || arrayExt == undefined){
                return false;
            }
            var ext = originalFile.substr(originalFile.lastIndexOf(".")+1,originalFile.length);
            arrayExt = arrayExt.split(',');
            var b = false;
            for(var i=0; i<arrayExt.length;i++){
                if(arrayExt[i].toLowerCase() == ext.toLowerCase()){
                    b = true;
                    break;
                }
            }
            return b;
        },
        /**验证字符串是否是json对象:winFn.jsonVerify(data);是返回true,否则false;*/
        jsonVerify : function(data){
            return typeof(data) == "object" && Object.prototype.toString.call(data).toLowerCase() == "[object object]" && !data.length;
        },
        /**获取form表单参数,用于参数前必须带:winFn.formParams(form);返回的是带&的字符串;推荐使用!*/
        formParams : function(form){
            return $(form).serialize();
        },
        /** 将form表单转为json对象:winFn.formField(form);返回的是{}格式,强烈推荐使用!
         * 用法 var params = winFn.formField('#formRegister');
         * layerFn.ajaxPostHint(url,'操作中,请稍候……',params,function(data){});
        */
        formField : function(formId){
            var kv = $(formId).serializeArray();
            var json = {};
            $.each(kv, function (i, v) {
                json[v.name] = v.value;
            });
            return json;
        },
        /**以form的name获取表单参数:winFn.formAjaxParams(formDom);返回的是Object,key-value*/
        formAjaxParams : function(formDom){
            var params = {};
            var field = $(formDom).serializeArray();
            $.each(field,function(){
                if(params[this.name] != undefined){
                    if (!params[this.name].push){
                        params[this.name] = [params[this.name]];
                    }
                    params[this.name].push(this.value || '');
                }else{
                    params[this.name] = this.value || '';
                }
            });
            return params;
        },
        /**判断表单是否有是否至少已填一个数据,已填有数据返回true;否则返回false;用法:winFn.checkParams(form);*/
        checkParams : function(form){
            var arrs = $(form).serialize().split("&");
            var b = false;
            for(var i = 0; arrs[i]; i++) {
                var tmp = arrs[i].split("=");
                if (tmp[1] != null && tmp[1] != ""){
                    b = true;
                    break;
                }
            }
            return b;
        },
        /**打开新页面,如有参数拼装在url里:winFn.winOpen(url);*/
        winOpen : function(url){
            if(self==top){
                window.location.href = url;
            }else{
                top.location.href = url;
            }
        },
        /**下载文件,含文件名的参数拼装在url里:winFn.downloadFile(url);用法:winFn.downloadFile(url);layerFn.center(AppKey.code.code200,'已进入后台处理,请耐心等待,处理完成将会自动下载!');*/
        downloadFile : function(url){
            $("<form method='post' action='" + url + "'></form>").appendTo("body").submit().remove();
        },
        /**把字符串解析为json格式:winFn.parseJson(str);*/
        parseJson : function(str){
            return $.parseJSON(str);
        },
        /**获取当前YYYY-MM-DD日期:winFn.getCurrentDate();*/
        getCurrentDate : function(){
            var now = new Date();
            var year = now.getFullYear();
            var month = now.getMonth() + 1;
            var day = now.getDate();
            var clock = year + "-";
            if(month < 10) clock += "0";
            clock += month + "-";
            if(day < 10) clock += "0";
            clock += day;
            return clock;
        },
        getDateTime : function(){
            var now = new Date();
            var year = now.getFullYear();
            var month = now.getMonth() + 1;
            var day = now.getDate();
            var clock = year + "-";
            if(month < 10) clock += "0";
            clock += month + "-";
            if(day < 10) clock += "0";
            clock += day;
            var minute = now.getMinutes();
            var hour = now.getHours();
            if(minute < 10){
                minute = ":0"+minute;
            }else{
                minute = ":"+minute;
            }
            if(hour < 10){
                hour = " 0"+hour;
            }else{
                hour = " "+hour;
            }
            clock += hour;
            clock += minute;
            return clock;
        },
        /**单文件上传:winFn.uploadSingle(url,'#form',callBack,error);*/
        uploadSingle : function(url,formDom,callBack,error){
            $(formDom).ajaxSubmit({
                type : 'POST',
                url : url,
                dataType: "json",
                success : function(data){
                    callBack(data);
                },
                error : function(response,textStatus,errorThrown){
                    error(response);
                }
            });
        },
        /**多文件上传:winFn.uploadMulti(url,callBack,error);*/
        uploadMulti : function(url,callBack,error){
        },
        /**按Enter回车键事件:winFn.Enter(fn);*/
        Enter : function(fn){
            opts.event({
                code : 13,
                fn : function(){
                    if (fn != null && fn !=''){
                        fn();
                    }
                }
            });
        },
        /**IE低版本支持默认现实提示信息:winFn.iePlaceholder();*/
        iePlaceholder : function(){
            $('input,textarea').placeholder({customClass:'ie_placeholder'});
        },
        /**元素标签是否启用,默认为启用:tagEnabled(domFlag,true);*/
        tagEnabled : function (domFlag,whether){
            if(whether == null || whether == ''){
                whether = false;
            }
            if (whether){
                $(domFlag).attr("disabled",true);
            } else {
                $(domFlag).attr("disabled",false);
                $(domFlag).removeAttr("disabled");
            }
        },
        /**删除数组little在数组large中的元素;winFn.removeLargeExistLittle(plsition_vals,hole_plsition_vals);*/
        removeLargeExistLittle : function(large,little){
            var result = [];
            for(var i = 0; i < large.length;i++){
                var k=0;
                for(var j=0;j<little.length;j++){
                    if(large[i]!=little[j]){
                        k++;
                        if(k==little.length){
                            result.push(large[i]);
                        }
                    }
                }
            }
            return result;
        },
        /**判断一个元素是否存在于这个数组中,存在true,否则false:winFn.checkinArray(val,arrs);*/
        checkinArray : function(val,arrs){
            return $.inArray(val,arrs) >= 0 ? true :false;
        },
        /**垂直上下分为两部分:winFn.initVertical2part(domTop,domBottom,size);*/
        initVertical2part : function(domTop,domBottom,size){
            if(size == null || size =='') size = 0;
            var height = $(window).height()-size;
            $(domTop).css("height",(height/2)+'px');
            $(domBottom).css("height",(height/2)+'px');
        },
        /**水平左右分为两部分:winFn.initHorizontal2part(domLeft,domRight,size);*/
        initHorizontal2part : function(domLeft,domRight,size){
            if(size == null || size =='') size = 0;
            var width = $(window).width()-size;
            $(domLeft).css({"width":(width/2)+'px',"display":"inline-block"});
            $(domRight).css({"width":(width/2)+'px',"display":"inline-block"});
        },
        /**计算比例值和总值的百分比:winFn.formatPercent(value,total);*/
        formatPercent : function(value,total){
            if(value == total){
                return '100.00';
            }else if(value == 0){
                return '0.00';
            }else{
                var pt = value / total;//获取百分比
                var str_p = pt.toString();
                var str_v = str_p.substring(0,6).substring(2);//截取
                str_v = str_v.slice(0,2) +'.'+ str_v.slice(2);//插入新值
                var zero = str_v.substr(0,1);
                if(zero == '0'){
                    str_v = str_v.substr(1);
                }
                return str_v;
            }
        },
        /**随机生成4个字符串:winFn.rdmCode()*/
        rdmCode : function(){
            return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
        },
        /**通过生成随机数生成uuid唯一值:winFn.getUuid();*/
        getUuid : function(){
            return (winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode());
        },
        /**数字转换为农历中文名称*/
        numberTransform : function(value){
            var result = '初';
            if(value <= 10){
                if(value == 1){
                    return result + '一';
                } else if(value == 2){
                    return result + '二';
                } else if(value == 3){
                    return result + '三';
                } else if(value == 4){
                    return result + '四';
                } else if(value == 5){
                    return result + '五';
                } else if(value == 6){
                    return result + '六';
                } else if(value == 7){
                    return result + '七';
                } else if(value == 8){
                    return result + '八';
                } else if(value == 9){
                    return result + '九';
                } else if(value == 10){
                    return result + '十';
                }
            }else if(value > 10 && value <=20){
                result = '十';
                if(value == 11){
                    return result + '一';
                } else if(value == 12){
                    return result + '二';
                } else if(value == 13){
                    return result + '三';
                } else if(value == 14){
                    return result + '四';
                } else if(value == 15){
                    return result + '五';
                } else if(value == 16){
                    return result + '六';
                } else if(value == 17){
                    return result + '七';
                } else if(value == 18){
                    return result + '八';
                } else if(value == 19){
                    return result + '九';
                } else if(value == 20){
                    return '二十';
                }
            }else{
                result = '廿';
                if(value == 21){
                    return result + '一';
                } else if(value == 22){
                    return result + '二';
                } else if(value == 23){
                    return result + '三';
                } else if(value == 24){
                    return result + '四';
                } else if(value == 25){
                    return result + '五';
                } else if(value == 26){
                    return result + '六';
                } else if(value == 27){
                    return result + '七';
                } else if(value == 28){
                    return result + '八';
                } else if(value == 29){
                    return result + '九';
                } else if(value == 30){
                    return '三十';
                }else if(value == 31){
                    return '三十一';
                }
            }
        },
        /**
         * 动态加载js
         * @param {} src js路径
         * @param {} callback 回调函数
         * @param {} targetEl 目标el
        */
        loadJs: function(src){
            var head = document.getElementsByTagName("head");
            var script = document.createElement("script");
            script.type = "text/javascript";
            script.src = src;
            head[0].appendChild(script);
        },
        /**
         * 动态加载css文件
         * @param {} href css路径
         * @param {} targetEl 目标el
        */
        loadCss: function(href){
            var head = document.getElementsByTagName("head");
            var link = document.createElement("link");
            link.rel = "stylesheet";
            link.type = "text/css";
            link.href = href;
            head[0].appendChild(link);
        },
    },
    /**easyui专属方法,依赖easyui*/
    window.euiFn = {
        datagrid : {
            settings : {
                pageSize : 50,/**euiFn.datagrid.settings.pageSize*/
                pageList : [50,100,150,200]/**euiFn.datagrid.settings.pageList*/
            }
        },
        /**跳转页面:euiFn.toPage(href,text);*/
        toPage : function(href,text){
            $(AppKey.bodyContainer).panel('setTitle',text);
            $(AppKey.bodyContainer).panel('refresh',href);
            $("div").data("key_title",text);
            $("div").data("key_url",href);
        },
        /**刷新整个页面,用法:euiFn.refreshPage();*/
        refreshPage : function(){
            euiFn.toPage($("div").data("key_url"),$("div").data("key_title"));
        },
        /**刷新Datagrid数据:euiFn.refreshDatagrid(datagridId,params,type);当type为1时重新加载并保持在当前页;如type不为1时可以不写;当然搜索时params可能含url地址,如:'roles/listData?'+params;*/
        refreshDatagrid : function(datagridId,params,type){
            $(datagridId).datagrid('clearSelections');
            if(type === 1){
                $(datagridId).datagrid('reload',params);
            }else{
                $(datagridId).datagrid('load',params);
            }
        },
        /**搜索功能,用法:euiFn.searchDatagrid(thisPage.datagrid,thisPage.urlList,params);*/
        searchDatagrid : function(datagridId,url,params){
            if(params==undefined||params.length<=0){
                params=$(datagridId).datagrid('options').queryParams;
            }
            $(datagridId).datagrid('clearSelections');
            $(datagridId).datagrid({url:url,queryParams:params});
        },
        /**用于批量操作选择:euiFn.getIdsBatch(domDatagrid,flag,field);domDatagrid是datagrid的标识,flag是分隔符若为空时是,;field一般指的是id主键若为空时是id*/
        getIdsBatch : function(domDatagrid,flag,field){
            var ids = "";
            if(flag == null || flag == '' || flag == undefined){flag = ",";}
            if(field == null || field == '' || field == undefined){flag = "id";}
            var rows = $('#'+domDatagrid).datagrid('getSelections');
            for (var i = 0; i < rows.length; i++){
                if(ids.length > 0)ids += flag;
                ids += rows[i][field];
            }
            var arr = new Array();
            arr = ids.split(flag);//转换成数组,然后获取数组的长度即可
            if(arr.length <= 0 || ids == ""){
                return null;
            }else {
                return ids;
            }
        },
        datagridTips : function(datagridDom){
            $(datagridDom).datagrid('doCellTip',{'max-width':'300px','delay':500});
        },
        /**清空datagrid数据:euiFn.datagridClear(datagridDom);*/
        datagridClear : function(datagridDom){
            $(datagridDom).datagrid('loadData', { total: 0, rows: [] });
        },
        /**仅选择checkbox="true"的复选框数据,返回多行,若只需一行在业务里处理,然后用rows[0]获取第一行数据:用法:var rows = euiFn.getRowsData(thisPage.datagrid);if(rows != null && rows != ''){}*/
        getRowsData : function(domDatagrid){
            var rows = $(domDatagrid).datagrid('getChecked');
            var len = rows.length;
            if(len <= 0){
                layerFn.center(AppKey.code.code199,'请选择数据行');
                return;
            }
            return rows;
        },
        /**获取指定行的数据,用法:euiFn.getRowData('#datagrid_campus_teacher',index);*/
        getRowData : function(datagridId,index){
            var rows = $(datagridId).datagrid('getRows');
            return rows[index];
        },
        /**右下边提示,一般用于表单提交操作后,如添加;修改;删除的处理显示结果:euiFn.showRb(code,msg);*/
        showRb : function(code,msg){
            code = parseInt(code);
            var imagerUrl = AppKey.iconDir+'warn.png';
            switch (code){
                case AppKey.code.code200:
                    imagerUrl = AppKey.iconDir+'success.png';
                    if(verifyFn.isEmpty(msg)){
                        msg = "操作成功";
                    }
                    break;
                case AppKey.code.code204:
                    imagerUrl = AppKey.iconDir+'error.png';
                    if(verifyFn.isEmpty(msg)){
                        msg = AppKey.sysError;
                    }
                    break;
                default:
                    if(verifyFn.isEmpty(msg)){
                        msg = "操作失败";
                    }
                    break;
            }
            $.messager.show({
                title: AppKey.title,
                msg: '<img src='+imagerUrl+" style='vertical-align:middle;margin-right:3px;'>"+"<span style='font-size:12px;'>"+msg+"</span>",
                timeout: 2000,
                height:'auto',
                showType: 'slide'
            });
        },
        /**根据选择行id查询获取行的数据;用法:euiFn.queryRowDataById(url,id,call);OK*/
        queryRowDataById : function(url,id,call){
            layerFn.ajaxPostHint(url,'加载是件闹心事……',{id:id},function(data){
                if (data.code == AppKey.code.code200){
                    call(data.map);
                }else if(data.code == AppKey.code.code207){
                    layerFn.pageLogin(data.code,data.msg);
                    return;
                }else{
                    layerFn.center(data.code,data.msg);
                }
            },function(err){
                layerFn.timeoutHint(err);
            });
        },
        /**自定义加载效果:euiFn.loadShow(msg);msg为空时关闭*/
        loadShow : function(msg){
            if(msg != null && msg != ''){
                $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
                $("<div class=\"datagrid-mask-msg\"></div>").html(msg).appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2,"font-size":'12px'});
            }else{
                $(".datagrid-mask").remove();
                $(".datagrid-mask-msg").remove();
            }
        },
        /**格式化日期YYYY-MM-DD;euiFn.dateFormatter(value);*/
        dateFormatter : function(value){
            var date = new Date(value);
            var year = date.getFullYear().toString();
            if(isNaN(year))return;
            var month = (date.getMonth() + 1);
            var day = date.getDate().toString();
            if(month < 10)month = "0" + month;
            if(day < 10)day = "0" + day;
            return year + "-" + month + "-" + day;
        },
        /**格式化日期YYYY-MM-DD HH:MM:SS;euiFn.dateFormatter(value);*/
        dateTimeFormatter : function(value){
            var date = new Date(value);
            var year = date.getFullYear().toString();
            if(isNaN(year))return;
            var month = (date.getMonth() + 1);
            var day = date.getDate().toString();
            var h = date.getHours().toString();
            var m = date.getMinutes().toString();
            var s = date.getSeconds().toString();
            if(month < 10)month = "0" + month;
            if(day < 10)day = "0" + day;
            if(h < 10)h = "0" + h;
            if(m < 10)m = "0" + m;
            if(s < 10)s = "0" + s;
            return year + "-" + month + "-" + day + ' '+ h + ':' + m + ':'+ s;
        },
        /**日期选择框的启禁用控制;euiFn.datebox(dom,checked);*/
        datebox : function(dom,checked){
            $(dom).datebox({disabled:checked});
        },
        /**限制日期选择,结束日期必须大于开始日期,如果结束日期小于开始日期则结束日期为开始日期，小于同理.用法:euiFn.dateBoxLimit('#birthStartDate','#birthEndDate');*/
        dateBoxLimit : function(dateBoxStart,dateBoxEnd){
            $(dateBoxEnd).datebox({
                onChange : function(newValue,oldValue){
                    if(newValue != null && newValue != ''){
                        var start = $(dateBoxStart).datebox('getValue');
                        var end = $(dateBoxEnd).datebox('getValue');
                        if(start != null && start != ''){
                            if (start > end){
                                $(dateBoxEnd).datebox('setValue',start);
                            }
                        }
                    }
                }
            });
            $(dateBoxStart).datebox({
                onChange : function(newValue,oldValue){
                    if(newValue != null && newValue != ''){
                        var start = $(dateBoxStart).datebox('getValue');
                        var end = $(dateBoxEnd).datebox('getValue');
                        if(end != null && end != ''){
                            if (start > end){
                                $(dateBoxStart).datebox('setValue',end);
                            }
                        }
                    }
                }
            });
        },
        /**限制日期时间选择,结束日期必须大于开始日期,如果结束日期小于开始日期则结束日期为开始日期，小于同理.用法:euiFn.dateTimeBoxLimit('#birthStartDate','#birthEndDate');*/
        dateTimeBoxLimit : function(dateBoxStart,dateBoxEnd){
            $(dateBoxEnd).datetimebox({
                onChange : function(newValue,oldValue){
                    if(newValue != null && newValue != ''){
                        var start = $(dateBoxStart).datetimebox('getValue');
                        var end = $(dateBoxEnd).datetimebox('getValue');
                        if(start != null && start != ''){
                            if (start > end){
                                $(dateBoxEnd).datetimebox('setValue',start);
                            }
                        }
                    }
                }
            });
            $(dateBoxStart).datetimebox({
                onChange : function(newValue,oldValue){
                    if(newValue != null && newValue != ''){
                        var start = $(dateBoxStart).datetimebox('getValue');
                        var end = $(dateBoxEnd).datetimebox('getValue');
                        if(end != null && end != ''){
                            if (start > end){
                                $(dateBoxStart).datetimebox('setValue',end);
                            }
                        }
                    }
                }
            });
        },
        /**给类型easyui-datebox控件赋当前时间值:euiFn.dateboxSetCurrentDate(dom);*/
        dateboxSetCurrentDate : function(dom){
            var value = $(dom).datebox('getValue');
            if(verifyFn.isEmpty(value)){
                $(dom).datebox('setValue',winFn.getCurrentDate());
            };
        },
        /**获取类型easyui-datebox控件赋值:euiFn.dateboxGetValue(dom);*/
        dateboxGetValue : function(dom){
            return $(dom).datebox('getValue');
        },
        /**给类型easyui-datebox控件赋值:euiFn.dateboxSetValue(dom,value);*/
        dateboxSetValue : function(dom,value){
            $(dom).datebox('setValue',value);
        },
        /**判断是否已选中列表行:euiFn.getRow(datagrid);*/
        getRow : function(datagrid){
            return $(datagrid).datagrid('getSelected');
        },
        /**euiFn.combobox({id:'id',url:url,onChange:function(newValue,oldValue){}});*/
        combobox : function(options){
            $('#'+options.id).combobox({
                url: options.url,
                method: 'get',
                valueField: 'id',
                textField: 'text',
                panelHeight:'auto',
                panelMaxHeight:300,
                onLoadError:function(){
                    layerFn.failure();
                },
                onChange : options.onChange,
                editable:false
            });
        },
        /**带搜索功能的下拉列表:euiFn.selectBox(domBox,url,prompt);默认参数comboValue获取的当前的下拉框的值*/
        selectBox : function(domBox,url,prompt){
            $(domBox).combobox({
                loader : function(params,success,error){
                    var q = params.q || '';
                    if(q.length <= 2){
                        return false;
                    }
                    $.ajax({
                        url : url,
                        type : "POST",
                        data : {comboValue:params.q},
                        dataType : "json",
                        success : function(data){
                            var items = $.map(data,function(item,index){
                                return {
                                    id : item.id,
                                    text : item.text
                                }
                            });
                            success(items);
                        },
                        error : function(response,err){
                            layerFn.timeoutHint(err);
                        }
                    });
                },
                mode : 'remote',
                prompt : (prompt ? prompt : '输入首关键字检索'),
                valueField : 'id',
                textField : 'text',
                editable : true,
                labelPosition : 'top',
                panelHeight : 'auto',
                panelMaxHeight : 300,
                hasDownArrow : true,
            });
        },
        /**euiFn.datagridInit(options);不包含view属性*/
        datagridInit : function(options){
            var datagrid = (options.datagrid) ? options.datagrid : '';
            var idField = (options.idField) ? options.idField : 'ID';
            var fit = (options.fit) ? options.fit : false;
            var url = (options.url) ? options.url : '';
            var title = (options.title) ? options.title : '';
            var pageSize = (options.pageSize) ? options.pageSize : euiFn.datagrid.settings.pageSize;
            var pageList = (options.pageList) ? options.pageList : euiFn.datagrid.settings.pageList;
            var checkOnSelect = (options.checkOnSelect) ? options.checkOnSelect : false;
            var pagination = (options.pagination) ? options.pagination : false;
            var fitColumns = (options.fitColumns) ? options.fitColumns:false;
            var showFooter = (options.showFooter) ? options.showFooter:false;
            var rownumbers = (options.rownumbers) ? options.rownumbers:false;
            var toolbar = (options.toolbar) ? options.toolbar:'';
            var singleSelect = (options.singleSelect) ? options.singleSelect:false;
            var onClickRow = (options.onClickRow) ? options.onClickRow:null;
            var onLoadSuccess = (options.onLoadSuccess) ? options.onLoadSuccess:null;
            var onDblClickRow = (options.onDblClickRow) ? options.onDblClickRow:null;
            var onClickCell = (options.onClickCell) ? options.onClickCell:null;
            var onDblClickCell = (options.onDblClickCell) ? options.onDblClickCell:null;
            var detailFormatter = (options.detailFormatter) ? options.detailFormatter:null;
            var onExpandRow = (options.onExpandRow) ? options.onExpandRow:null;
            var dgMethod = (options.dgMethod) ? options.dgMethod:null;
            var dgAttr = (options.dgAttr) ? options.dgAttr:null;
            var onResize = (options.onResize) ? options.onResize:null;
            var editors = (options.editors) ? options.editors:null;
            var columns = (options.columns) ? options.columns:[[]];
            $(options.datagrid).datagrid(
                {
                    fit: fit,
                    url: url,
                    title:title,
                    pageSize: pageSize,
                    pageList: pageList,
                    checkOnSelect: checkOnSelect,
                    singleSelect: singleSelect,
                    pagination: pagination,
                    fitColumns: fitColumns,
                    showFooter: showFooter,
                    striped: true,
                    autoRowHeight: true,
                    rownumbers: rownumbers,
                    loadMsg: AppKey.loadMsg,
                    toolbar: toolbar,
                    idField: idField,
                    onBeforeLoad: function (param){
                    },
                    onLoadSuccess: function (data){
                        if(onLoadSuccess != null){
                            onLoadSuccess(data);
                        }
                    },
                    onLoadError: function (err){
                        layerFn.timeoutHint(err);
                    },
                    onClickRow : function (index,row){
                        if(onClickRow != null){
                            onClickRow(index,row);
                        }
                    },
                    onDblClickRow: function (index,row){
                        if(onDblClickRow != null){
                            onDblClickRow(index,row);
                        }
                    },
                    onClickCell:function(index,field,value){
                        if(onClickCell != null){
                            onClickCell(index,field,value);
                        }
                    },
                    onDblClickCell:function(index,field,value){
                        if(onDblClickCell != null){
                            onDblClickCell(index,field,value);
                        }
                    },
                    loadFilter: function (data){
                        return winFn.dataFilter(data);
                    },
                    detailFormatter: function (index,row){
                        if(detailFormatter != null){
                            detailFormatter(index,row);
                        }
                    },
                    onExpandRow: function (index,row){
                        if(onExpandRow != null){
                            onExpandRow(index,row);
                        }
                    },
                    dgMethod: function (opts){
                        if(dgMethod != null){
                            dgMethod(opts);
                        }
                    },
                    editors:editors,
                    dgAttr:dgAttr,
                    onResize: function (width,height){
                        if(onResize != null){
                            onResize(width,height);
                        }
                    },
                    columns : columns,
                }
            );
        },
        /**把js对象的datagrid数据行的某行row转为json对象:euiFn.paramsJson(row);*/
        paramsJson : function(row){
            if(row == null || row == '' || row.length <= 0)return null;
            var jsonParams = '{';
            for(var key in row){
                jsonParams = jsonParams + '"'+key+'"' +':"'+row[key]+'",';
            }
            return jsonParams.substring(0,(jsonParams.length-1)) + '}';
        },
        /**把js数组datagrid数据行转为json数组:euiFn.paramArray(datagridId,fields);fields为指定字段,字段间用,分开*/
        paramArray : function(datagridId,fields){
            if(datagridId == null || datagridId.length <= 0 || datagridId == undefined)return null;
            if(fields != null && fields != '' && fields != undefined){
                var rows = $(datagridId).datagrid('getChecked');
                var array = '[';
                $.each(rows,function(index,data){
                    var strs = fields.split(",");
                    var json = '';
                    for (i=0; i<strs.length; i++){
                        var k = strs[i];
                        var v = data[k];
                        if(v != null && v != '' && v != undefined){
                            if(json.length > 0){
                                json += '"'+k+'":"'+v+'",';
                            }else{
                                json += '{"'+k+'":"'+v+'",';
                            }
                        }
                    }
                    json = json.substring(0,(json.length - 1)) + '}';
                    array += json+',';
                });
                array = array.substring(0,(array.length-1)) +']'
                return array;
            }
        },
        /**把js数组datagrid数据行转为json数组:euiFn.paramsRows(rows,fields);fields为指定字段,字段间用,分开,其中 var rows = euiFn.getRowsData(thisPage.datagrid)*/
        paramsRows : function(rows,fields){
            if(rows == null || rows.length <= 0 || rows == undefined)return null;
            if(fields == null || fields == '' || fields == undefined)return null;
            var array = '[';
            $.each(rows,function(index,data){
                var strs = fields.split(",");
                var json = '';
                for (i=0; i<strs.length; i++){
                    var k = strs[i];
                    var v = data[k];
                    if(v != null && v != '' && v != undefined){
                        if(json.length > 0){
                            json += '"'+k+'":"'+v+'",';
                        }else{
                            json += '{"'+k+'":"'+v+'",';
                        }
                    }
                }
                json = json.substring(0,(json.length - 1)) + '}';
                array += json+',';
            });
            array = array.substring(0,(array.length-1)) +']'
            return array;
        },
        /**从row对象JSON.stringify(row)转为json对象后提取为请求参数:euiFn.rowParams(jsonObj,fields);其中的jsonObj是JSON.stringify(row)的数据,fields指定字段,以,隔开,如果为空则是全部字段*/
        rowParams : function(jsonObj,fields){
            if(jsonObj == null || jsonObj.length <= 0 || jsonObj == undefined)return null;
            var params = '';
            if(fields != null && fields != "" && fields != undefined && fields.length > 0){
                for(var k in jsonObj){
                    var arrs = fields.split(",");
                    for (i=0; i < arrs.length; i++){
                        if(arrs[i] == k){
                            var value = jsonObj[k];
                            params += k +'='+value+'&';
                            break;
                        }
                    }
                }
            }else{
                for(var k in jsonObj){
                    var value = jsonObj[k];
                    params += k +'='+value+'&';
                }
            }
            return params.substring(0,(params.length-1));
        },
        /**把js对象的datagrid数据行的某行row转为请求参数:euiFn.paramsObject(object)*/
        paramsObject : function(object){
            if(object == null || object == '' || object.length <= 0)return null;
            var params = '';
            for(key in object){
                params = params+key+'='+object[key]+'&';
            }
            return params.substring(0,(params.length-1));
        },
        /**删除列:euiFn.rowDel(thisPage.datagrid,index);*/
        rowDel : function(datagridId,index){
            $(datagridId).datagrid('deleteRow',index);
        },
        /**添加列:euiFn.rowAdd(thisPage.datagrid,row);*/
        rowAdd : function(datagridId,row){
            $(datagridId).datagrid('appendRow',row);
        },
    },
    /**依赖jQuery*/
    window.verifyFn = {
        /**验证是否指定的文件格式:verifyFn.checkFileType(this,'#excel','请选择Excel格式文件','xls,xlsx');*/
        checkFileType : function(obj,domId,msg,arr){
            var fileType = obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
            arr = arr.split(",");
            var count = 0 ;
            var length = arr.length ;
            for (var i = 0; i < length; i++){
                if(fileType != '.'+arr[i]){
                    count ++;
                }
            }
            if(count === length){
                msg = (verifyFn.isEmpty(msg))?'请选择正确的文件格式':msg;
                $(domId).val('');
                layerFn.center(AppKey.code.code199,msg);
            }
        },
        /**验证是否是正整数,是返回true,否则返回false:verifyFn.checkIsNum(value);*/
        checkIsNum : function(value){
            var reg = /^[0-9]*[1-9][0-9]*$/;
            return reg.test(value);
        },
        /**验证输入框是否已输入数据,未输入返回true,否则返回false:verifyFn.inputRequired(dom);*/
        inputRequired : function(dom){
            var value = winFn.getDomValue(dom);
            if (verifyFn.isEmpty(value)){
                return true;
            } else {
                return false;
            }
        },
        /**验证是否为空:verifyFn.isEmpty(value);*/
        isEmpty : function(object){
            if(typeof object == 'function'){
                if(object == null || object == '' || object == undefined || object == 'undefined')return true;
            }
            if (object != null && object != ''){
                if(object == 'undefined' || object == 'UNDEFINED')return true;
            }
            if(object == null || object == undefined || object == '')return true;
            if($.trim(object).length > 0 && $.trim(object) == 'null')return true;
            if($.trim(object).length > 0 && $.trim(object) == 'NULL')return true;
            if(object.length == 0)return true;
            if($.trim(object).length <= 0)return true;
            if($.trim(object) == '')return true;
            return false;
        },
        /**验证是否为email格式:verifyFn.isEmail(value);*/
        isEmail : function(value){
            if (/^([\w\.\-])+\@\w+(\.[a-zA-Z]+)+$/.test(value))return true;
            return false;
        },
        /**验证手机号是否填写正确:verifyFn.isPhone(value);*/
        isPhone : function(value){
            return (/^(\+\d{2}-)?(\d{2,3}-)?([1][3,4,5,6,7,8][0-9]\d{8})$/.test(value));
        },
        /**判断这两个数据是否相等，区分大小写,不相等返回true:verifyFn.checkEqual(value1,value2);*/
        checkEqual : function(value1,value2){
            if(value1 != value2){
                return true;
            }else {
                return false;
            }
        },
        /**检查是否支持对应的浏览器:verifyFn.checkBrowser(hint,target);*/
        checkBrowser : function(hint,target){
            var ua = navigator.userAgent.toLowerCase(), s, app = {}, url, host = window.location.host;
            (s = ua.match(/msie ([\d.]+)/)) ? app.ie = s[1] : (s = ua.match(/firefox\/([\d.]+)/)) ? app.firefox = s[1] : (s = ua.match(/chrome\/([\d.]+)/)) ? app.chrome = s[1] : (s = ua.match(/opera.([\d.]+)/)) ? app.opera = s[1] : (s = ua.match(/version\/([\d.]+).*safari/)) ? app.safari = s[1] : 0;
            var bro = false;
            if (app.ie){
                bro = true;// 如果浏览器为IE则跳转到;
            } else if (app.chrome){
                bro = false;// 如果浏览器为Chrome则跳转到相对链接html/
            } else if (app.opera){
                bro = false;// 如果浏览器为Opera则则跳转到相对链接/html/
            } else if (app.firefox){
                bro = false;// 如果浏览器为firefox则跳转到上级目录/
            } else if (app.safari){
                bro = true;// 如果浏览器为Safari则跳转到当前域名
            } else {
                bro = true;// 其他浏览器打开跳转到空白页面;
            }
            if (bro){
                document.location.href = hint;
            }else {
                document.location.href = target;
            }
        },
        /**验证手机号或电话号码是否正确:verifyFn.checkTelephone(value);*/
        checkTelephone : function(tel){
            var mobile = /^1\d{10}$/,phone = /^0\d{2,3}-?\d{7,8}$/;
            return mobile.test(tel) || phone.test(tel);
        },
        /** 将表单序列化为JSON对象:verifyFn.initExtend();*/
        initExtend : function(){
            $.fn.serializeObject = function(){
                var o = {};
                var a = this.serializeArray();
                $.each(a, function() {
                    if (o[this.name]) {
                        if (!o[this.name].push) {
                            o[this.name] = [ o[this.name] ];
                        }
                        o[this.name].push(this.value || '');
                    } else {
                        o[this.name] = this.value || '';
                    }
                });
                return o;
            }
        },
    },
    /**layer专属方法*/
    window.layerFn = {
        area : {
            h500xw450 : ['500px','450px']/**layerFn.area.h500xw450*/
        },
        /**提示正在操作：layerFn.loadAjax(msg);*/
        loadAjax : function(msg){
            msg = (msg == null || msg == '' || msg == undefined)?'操作中,请稍候……':msg;
            return top.layer.msg(msg,{icon:16,time:-1,shade:[0.3,'#000'],area:'auto'});
        },
        /**提示信息或弹出框的关闭：layerFn.layerClose(index);*/
        layerClose : function(index){
            top.layer.close(index);
        },
        /**确认操作,仅有取消按钮事件:layerFn.handleVerify(msg,fnVerify,fnCancel);*/
        handleVerify : function(msg,fnVerify,fnCancel){
            top.layer.confirm((msg == null || msg == '')?'<span style="color:red;">确定要操作吗?</span>':'<span style="color:red;">'+msg+'</span>',{
                title : AppKey.title,
                btn : [AppKey.confirm,AppKey.cancel],
                area : AppKey.layerArea
            },function(index){
                layerFn.layerClose(index);
                if (fnVerify != null && fnVerify != ''){
                    fnVerify();
                }
            },function(){
                if (fnCancel != null && fnCancel != ''){
                    fnCancel();
                }
            });
        },
        /**确认操作,且取消按钮和右上角具有同一事件:layerFn.confirm(msg,function(){},function(){});如果需要调用到第2个方法则第1个方法是不能少的,写function(){}即可*/
        confirm : function (msg,fnVerify,fnCancel){
            msg = verifyFn.isEmpty(msg)?"确定要操作吗?":msg;
            var exitIndex = top.layer.confirm('<div style="color:red;font-size:12px;"><img src="'+AppKey.iconDir+'help_icon.png" style="vertical-align:middle;margin-right:3px;"/>'+msg+'</div>',{
                title : AppKey.title,
                area : AppKey.layerArea,
                btn : [AppKey.confirm,AppKey.cancel],
                cancel : fnCancel,
            },function(index){
                layerFn.layerClose(index);
                if(fnVerify){fnVerify();}
            },function(index){
                layerFn.layerClose(index);
                if(fnCancel){fnCancel();}
            });
            layerFn.EscLayer(exitIndex);
        },
        /**ajax的post用法:layerFn.ajaxPostHint(url,msg,params,fnSuccess,fnError);*/
        ajaxPostHint : function(url,msg,params,fnSuccess,fnError){
            self.hintIndex = layerFn.loadAjax(msg);
            $.ajax({
                url : url,
                type : "POST",
                data : params,
                dataType: "json",
                success : function(res){
                    layerFn.layerClose(self.hintIndex);
                    fnSuccess(res);
                },
                error : function(response,err){
                    layerFn.layerClose(self.hintIndex);
                    if (fnError != null && fnError != ''){
                        fnError(err);
                    }
                }
            });
        },
        /**ajax的get用法:layerFn.ajaxGetHint(url,msg,params,fnSuccess,fnError);*/
        ajaxGetHint : function(url,msg,params,fnSuccess,fnError){
            self.hintIndex = layerFn.loadAjax(msg);
            $.ajax({
                url : url,
                type : "GET",
                dataType: "json",
                data : params,
                success : function(res){
                    layerFn.layerClose(self.hintIndex);
                    fnSuccess(res);
                },
                error : function(response,err){
                    layerFn.layerClose(self.hintIndex);
                    if (fnError != null && fnError != ''){
                        fnError(err);
                    }
                }
            });
        },
        /**ajax的get请求且已做请求失败的处理:layerFn.ajaxHint(url,msg,params,fnSuccess);不推荐使用,*/
        ajaxHint : function(url,msg,params,fnSuccess){
            layerFn.ajaxGetHint(url,msg,params,function(data){
                if(data.code == AppKey.code.code207){
                    layerFn.pageLogin(data.code,data.msg);
                    return;
                }else if(data.code == AppKey.code.code205){
                    layerFn.pageLogin(data.code,data.msg);
                    return;
                }else{
                    fnSuccess(data);
                }
            },function(err){
                layerFn.timeoutHint(err);
            });
        },
        /**ajax的post提交,仅适用于增、删、改、查操作且已做请求失败的处理;layerFn.submit(url,params,fnSuccess);成功时才关闭对话框,所以不需要处理失败的回调及code为200以外的数据处理*/
        submit : function(url,params,fnSuccess){
            layerFn.ajaxPostHint(url,'处理中,请稍候……',params,function(data){
                if (data.code == AppKey.code.code200){
                    fnSuccess(data);
                }else if(data.code == AppKey.code.code207){
                    layerFn.pageLogin(data.code,data.msg);
                    return;
                }else if(data.code == AppKey.code.code205){
                    layerFn.pageLogin(data.code,data.msg);
                    return;
                }else{
                    layerFn.center(data.code,data.msg);
                }
            },function(err){
                layerFn.timeoutHint(err);
            });
        },
        /**用法:layerFn.loading.show('消息');layerFn.loading.hide(当前layer弹窗索引);或layerFn.loading.closeAll();*/
        loading : {
            show : function(msg){
                return layerFn.loadAjax(msg);
            },
            hide : function(index){
                if (index != null){
                    layerFn.layerClose(index);
                }
            },
            closeAll : function(){
                top.layer.closeAll();
            }
        },
        /**仅适用于ajax查询操作;layerFn.ajaxRequestHint(url,params,fnSuccess,msg);成功时才关闭对话框,所以不需要处理失败的回调及code为200以外的数据处理*/
        ajaxRequestHint : function(url,params,fnSuccess,msg){
            layerFn.ajaxPostHint(url,((msg == null || msg == '')?'处理中,请稍候……':msg),params,function(data){
                if (data.code == AppKey.code.code200){
                    fnSuccess(data);
                }else if(data.code == AppKey.code.code207){
                    layerFn.pageLogin(data.code,data.msg);
                    return;
                }else{
                    layerFn.center(data.code,data.msg);
                }
            },function(err){
                layerFn.timeoutHint(err);
            });
        },
        /**以id查询单条数据的全部字段信息,不带正在加载的提示信息,无需处理失败的回调及code为200以外的数据处理,用法:layerFn.queryById(url,id,succeed);*/
        queryById : function(url,id,succeed){
            $.ajax({
                type: "POST",
                url: url,
                dataType: "json",
                data:{id:id},
                success:function(data){
                    if(data.code == AppKey.code.code200){
                        succeed(data);
                    }else if(data.code == AppKey.code.code207){
                        layerFn.pageLogin(data.code,data.msg);
                        return;
                    }else{
                        layerFn.center(data.code,data.msg);
                    }
                },
                error:function(response,err){
                    layerFn.timeoutHint(err);
                }
            });
        },
        /**以id查询单条数据的全部字段信息,带正在加载的提示信息,无需处理失败的回调及code为200以外的数据处理,用法:layerFn.queryByIdHint(url,id,msg,fnSuccess);*/
        queryByIdHint : function(url,id,msg,fnSuccess){
            self.hintIndex = layerFn.loadAjax(msg);
            $.ajax({
                url : url,
                type : "POST",
                data : {id:id},
                dataType: "json",
                success : function(data){
                    layerFn.layerClose(self.hintIndex);
                    if(data.code == AppKey.code.code200){
                        fnSuccess(data);
                    }else if(data.code == AppKey.code.code207){
                        layerFn.pageLogin(data.code,data.msg);
                        return;
                    }else{
                        layerFn.center(data.code,data.msg);
                    }
                },
                error : function(response,err){
                    layerFn.layerClose(self.hintIndex);
                    layerFn.timeoutHint(err);
                }
            });
        },
        /**适用于存在就编辑否则就添加,以某字段查询单条数据的全部字段信息,带正在加载的提示信息,无需处理失败的回调及code为200及201以外的数据处理,用法:layerFn.queryGetByIdHint(url,id,msg,function(data){编辑},function(data){添加});*/
        queryGetByIdHint : function(url,id,msg,fnSuccess,fnEmpty){
            self.hintIndex = layerFn.loadAjax(msg);
            $.ajax({
                url : url,
                type : "GET",
                data : {id:id},
                dataType: "json",
                success : function(data){
                    layerFn.layerClose(self.hintIndex);
                    if(data.code == AppKey.code.code200){
                        fnSuccess(data);
                    }else if(data.code == AppKey.code.code207){
                        layerFn.pageLogin(data.code,data.msg);
                        return;
                    }else if(data.code == AppKey.code.code201){
                        fnEmpty(data);
                        return;
                    }else{
                        layerFn.center(data.code,data.msg);
                    }
                },
                error : function(response,err){
                    layerFn.layerClose(self.hintIndex);
                    layerFn.timeoutHint(err);
                }
            });
        },
        /**根据id删除数据;用法:layerFn.delByIdHint(url,id,msg,fnSuccess);无需处理失败的回调及code为200以外的数据处理*/
        delByIdHint : function(url,id,msg,fnSuccess){
            self.hintIndex = layerFn.loadAjax(msg);
            $.ajax({
                url : url,
                type : "POST",
                data : {id:id},
                dataType: "json",
                success : function(data){
                    layerFn.layerClose(self.hintIndex);
                    if (data.code == AppKey.code.code200){
                        fnSuccess(data);
                    }else if(data.code == AppKey.code.code207){
                        layerFn.pageLogin(data.code,data.msg);
                        return;
                    }else{
                        layerFn.center(data.code,data.msg);
                    }
                },
                error : function(response,err){
                    layerFn.layerClose(self.hintIndex);
                    layerFn.timeoutHint(err);
                }
            });
        },
        /**删除[含批量]数据;用法:layerFn.delBatchHint(url,ids,msg,fnSuccess);无需处理失败的回调及code为200以外的数据处理*/
        delBatchHint : function(url,ids,msg,fnSuccess){
            self.hintIndex = layerFn.loadAjax(msg);
            $.ajax({
                url : url,
                type : "POST",
                data : {ids:ids},
                dataType: "json",
                success : function(data){
                    layerFn.layerClose(self.hintIndex);
                    if (data.code == AppKey.code.code200){
                        fnSuccess(data);
                    }else if(data.code == AppKey.code.code207){
                        layerFn.pageLogin(data.code,data.msg);
                        return;
                    }else{
                        layerFn.center(data.code,data.msg);
                    }
                },
                error : function(response,err){
                    layerFn.layerClose(self.hintIndex);
                    layerFn.timeoutHint(err);
                }
            });
        },
        /**用于检测检查是否已登录超时,提示未登录或登录已超时并返回登录页面:layerFn.timeoutHint(err);*/
        timeoutHint : function(err){
            if(err != null && err == 'parsererror'){
                top.layer.closeAll();
                layerFn.pageLogin(AppKey.code.code204,AppKey.notLogin);
            }else{
                layerFn.alert(AppKey.msg_error);
            }
        },
        /**动态为自定义下拉菜单样式初始化:layerFn.selectUI(url,params,defaultText,emptyText,selectDom,value,showText,select,parentDom,callSelectInit,width);*/
        selectUI : function(url,params,defaultText,emptyText,selectDom,value,showText,select,parentDom,callSelectInit,width){
            layerFn.ajaxPostHint(url,'加载中,请稍候……',params,function(data){
                if (data.code == AppKey.code.code207 || data.code == AppKey.code.code205){
                    layerFn.pageLogin(data.code,data.msg);
                    return;
                }else{
                    winFn.setDataSelect(defaultText,emptyText,selectDom,data.listData,value,showText,(select == '' || select == null || select == undefined)?null:select);
                }
                if (callSelectInit != null && callSelectInit != ''){
                    winFn.uiSelectInit(parentDom,callSelectInit,width);
                    $(parentDom+"> select").removeClass('block_hidden');
                }
            });
        },
        /**居中显示,一般用于验证表单提交操作,或用于查询操作的处理系统异常结果:layerFn.center(code,msg);*/
        center : function(code,msg){
            code = parseInt(code);
            var imagerUrl = AppKey.iconDir+'warn.png';
            switch (code){
                case AppKey.code.code200:
                    msg = verifyFn.isEmpty(msg)?"操作成功":msg;
                    imagerUrl = AppKey.iconDir+'success.png' ;
                    break;
                case AppKey.code.code204:
                    msg = verifyFn.isEmpty(msg)?AppKey.sysError:msg;
                    imagerUrl = AppKey.iconDir+'error.png' ;
                    break;
                default:
                    msg = verifyFn.isEmpty(msg)?"操作有误":msg;
                    break;
            }
            top.layer.alert('<div style="font-size: 12px;"><img src='+imagerUrl+' style="vertical-align:middle;margin-right:3px;"/>'+msg+'</div>',{title : AppKey.title,area : AppKey.layerArea});
            layerFn.EscLayer(exitIndex);
        },
        /**按Esc键关闭layer对话框或仅用于查看的layer层:layerFn.EscLayer(index);*/
        EscLayer : function(index){
            opts.event({
                code : 27,
                fn : function(){
                    layerFn.layerClose(index);
                    index = null;
                }
            });
        },
        /**判断是否已登录,未登录直接跳转到jsp登录页面,否则返回当前传递的对象:layerFn.checkLogin(result);*/
        checkLogin : function(result){
            var b = typeof(result) == "object" && Object.prototype.toString.call(result).toLowerCase() == "[object object]" && !result.length;
            if (b){
                return result;
            }else{
                layerFn.closeEvent(AppKey.code.code204,AppKey.notLogin,function(){
                    if(self==top){
                        window.location.href = AppKey.loginUrl;
                    } else {
                        top.location.href = AppKey.loginUrl;
                    }
                });
            }
        },
        /**第2个按钮的没有事件的;第5个参数是按钮名称及后面跟随是对应事件;最后一个参数是cancelCall是右上角关闭的事件*/
        btnExample : function(title,domDivId,area,closedCall,btn3,callBack,cancelCall){
            var btns = (btn3 == null || btn3 == '') ? [AppKey.submit,AppKey.cancel] : [AppKey.submit,AppKey.cancel,btn3];
            return layer.open({
                type : 1,
                title : title,
                content : $('#'+domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                resize : false,
                btn : btns,
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
                btn3 : function(index,layero){
                    if (callBack != null && callBack != '' && btn3 != null && btn3 !=  ''){
                        callBack(index,layero);
                    }
                },
                cancel: function(){
                    if (cancelCall != null && cancelCall != ''){
                        cancelCall();
                    }
                }
            });
        },
        /**只做示例*/
        btnTemplate : function(title,domDivId,area,closedCall,btn){
            btn = (btn == null || btn == '') ? [AppKey.submit,AppKey.cancel] : [AppKey.submit,AppKey.cancel,'<span style="color:red;">'+btn+'</span>'];
            return layer.open({
                type : 1,
                title : title,
                content : $('#'+domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                resize : false,
                btn : btn,
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
                btn2: function(index,layero){
                    layer.close(index);
                },
                btn3: function(index,layero){
                    layer.alert('第3个按钮');
                    return false;
                },
                cancel: function(){
                    layer.alert('右上角按钮');
                }
            });
        },
        /**用于编辑或添加,调用方法:layerFn.addOrEdit(title,domDivId,[width,height],function(index,layero){},btn3,btn3Call);第5个参数是第3个按钮名称[支持html],第6个参数是5个按钮参数是事件;不含右上角关闭的事件*/
        addOrEdit : function(title,domDivId,area,closedCall,btn3,btn3Call){
            var btns = (btn3 == null || btn3 == '') ? [AppKey.submit,AppKey.cancel] : [AppKey.submit,AppKey.cancel,btn3];
            return layer.open({
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                resize : false,
                btn : btns,
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
                btn3 : function(index,layero){
                    if (btn3Call != null && btn3Call != '' && btn3 != null && btn3 != ''){
                        btn3Call(index,layero);
                    }
                    return false;
                }
            });
        },
        /**含2个按钮2个回调,一个右上角的关闭回调,用于编辑或添加,调用方法:layerFn.addOrEditEvent(title,domDivId,['width_px','height_px'],function(index,layero){},function(index,layero){},function(){});第5个参数是第2个按钮的事件;cancelCall是右上角关闭的事件*/
        addOrEditEvent : function(title,domDivId,area,closedCall,fn2Call,cancelCall){
            return layer.open({
                type : 1,
                title : title,
                content : $(domDivId),
                area : area,
                resize : false,
                btn : [AppKey.submit,AppKey.cancel],
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
                btn2: function(index,layero){
                    if (fn2Call != null && fn2Call != ''){
                        fn2Call(index,layero);
                    }
                },
                cancel: function(){
                    if (cancelCall != null && cancelCall != ''){
                        cancelCall();
                    }
                }
            });
        },
        /**含3个按钮3个回调,一个右上角的关闭回调,必须手动关闭对话框:layerFn.dialogBtn3Fn3(title,domDivId,['width_px','height_px'],btn1,function(index,layero){},btn2,function(index,layero){},btn3,function(index,layero){},function(){});*/
        dialogBtn3Fn3 : function(title,domDivId,area,btn1,fn1Call,btn2,fn2Call,btn3,fn3Call,cancelCall){
            return layer.open({
                type : 1,
                title : title,
                content : $(domDivId),
                area : area,
                resize : false,
                btn : [btn1,btn2,btn3],
                yes : function(index,layero){
                    if (fn1Call != null && fn1Call != ''){
                        fn1Call(index,layero);
                    }
                },
                btn2: function(index,layero){
                    if (fn2Call != null && fn2Call != ''){
                        fn2Call(index,layero);
                    }
                    return false;
                },
                btn3: function(index,layero){
                    if (fn3Call != null && fn3Call != ''){
                        fn3Call(index,layero);
                    }
                    return false;
                },
                cancel: function(){
                    if (cancelCall != null && cancelCall != ''){
                        cancelCall();
                    }
                }
            });
        },
        /**用于编辑|添加|查看的居中的弹出框,但是IE8不兼容:layerFn.winUrl('编辑','page?page=edit',[width,height],function(index,layero){},btn);最后一个参数不为空时则是单个按钮*/
        winUrl : function(title,url,area,callback,btn){
            return top.layer.alert('',{
                shade : 0.3,
                type : 2,
                title : title,
                content : url,
                btnAlign: 'c',
                closeBtn: 1,
                resize : false,
                area : area,
                btn : !btn ? [AppKey.submit,AppKey.cancel]:[btn],
                yes : !btn ? function(index,layero){
                    callback(index,layero);
                }:''
            });
        },
        /**用于编辑|添加|查看的居中的弹出框:layerFn.winDom(title,domDivId,[width,height],function(index,layero){},btn);最后一个参数btn不为空时则是单个按钮,且callback可以为null*/
        winDom : function(title,domDivId,area,callback,btn){
            return top.layer.alert('',{
                shade : 0.3,
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                btnAlign: 'c',
                closeBtn: 1,
                resize : false,
                area : area,
                btn : !btn ? [AppKey.submit,AppKey.cancel]:[btn],
                yes : !btn ? function(index,layero){
                    callback(index,layero);
                }:''
            });
        },
        /**用于编辑|添加|查看的右边Right的弹出框,但是IE8不兼容:layerFn.winRUrl('编辑','page?page=edit','1000px',function(index,layero){},btn);最后一个参数不为空时则是单个按钮*/
        winRUrl : function(title,url,width,callback,btn){
            return top.layer.alert('',{
                offset: 'rt',
                shade : 0.3,
                anim : 2,
                type : 2,
                title : title,
                content : url,
                btnAlign: 'c',
                closeBtn: 0,
                resize : false,
                move: false,
                area : [width,'100%'],
                btn : !btn ? [AppKey.submit,AppKey.cancel]:[btn],
                yes : !btn ? function(index,layero){
                    callback(index,layero);
                }:''
            });
        },
        /**用于编辑|添加|查看的右边Right的弹出框:layerFn.winRDom(title,domDivId,'1000px',function(index,layero){},btn);最后一个参数btn不为空时则是单个按钮,且callback可以为null*/
        winRDom : function(title,domDivId,width,callback,btn){
            return top.layer.alert('',{
                offset: 'rt',
                shade : 0.3,
                anim : 2,
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                btnAlign: 'c',
                closeBtn: 0,
                resize : false,
                move: false,
                area : [width,'100%'],
                btn : !btn ? [AppKey.submit,AppKey.cancel]:[btn],
                yes : !btn ? function(index,layero){
                    callback(index,layero);
                }:''
            });
        },
        /**KindEditor编辑;调用方法:layerFn.winKindEditor(title,domDivId,textareaId,[width,height],function(index,layero){},btn3,btn3Call);最后1个参数是倒数第2个按钮名称[支持html]回调,不含右上角关闭的事件*/
        winKindEditor : function (title,domDivId,textareaId,area,closedCall,btn3,btn3Call){
            var WKE = (self==top)?parent:window;
            var btns = (btn3 == null || btn3 == '') ? [AppKey.submit,AppKey.cancel] : [AppKey.submit,AppKey.cancel,btn3];
            return WKE.layer.open({
                type : 1,
                title : title,
                content: $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                resize : false,
                btn : btns,
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);//在后台处理成功时可以KindEditor.remove('#news_id_input_edit');//移除编辑器
                    }
                },
                btn2: function(index,layero){//第2个按钮-取消按钮
                    KindEditor.remove(textareaId);//取消按钮时移除编辑器
                },
                btn3 : function(index,layero){
                    if (btn3Call != null && btn3Call != '' && btn3 != null && btn3 != ''){
                        btn3Call(index,layero);
                    }
                    return false;
                },
                cancel: function(index,layero){//右上角关闭按钮
                    KindEditor.remove(textareaId);//右上角关闭按钮关闭时移除编辑器
                },
                success: function(layero,index){//成功打开layer时执行
                    window.TypEditor = KindEditor.create(textareaId,{
                        resizeType: 0,
                        allowPreviewEmoticons: false,
                        allowImageUpload: false,
                        afterBlur: function(){
                            this.sync();
                        },
                        items: ['|','selectall','quickformat','forecolor', 'hilitecolor','lineheight', 'undo', 'redo', 'cut', 'copy', 'paste','plainpaste', 'wordpaste','|','justifyleft', 'justifycenter', 'justifyright','justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript','superscript', '|', 'title', 'fontname', 'fontsize', '|', 'bold','italic', 'underline', 'strikethrough', 'removeformat', '|', 'image','flash', 'media', 'advtable', 'hr', 'emoticons', 'link', 'unlink','|']
                    });
                }
            });
        },
        /**获取KindEditor数据:layerFn.getKindEditorData();*/
        getKindEditorData : function (){
            if(TypEditor == null || TypEditor == undefined || TypEditor == 'undefined' ||  $.trim(TypEditor) == ""){
                return null;
            }
            return TypEditor.html();
        },
        /**百度ueditor编辑;调用方法:layerFn.winUeditor(title,domDivId,'script_editor',[width,height],data,function(index,layero){},btn3,btn3Call);最后1个参数是倒数第2个按钮名称[支持html]回调,不含右上角关闭的事件,注意script_editor是不带#或.的选择器*/
        winUeditor : function (title,domDivId,scriptId,area,data,closedCall,btn3,btn3Call){
            var WUR = (self==top)?parent:window;
            var btns = (btn3 == null || btn3 == '') ? [AppKey.submit,AppKey.cancel] : [AppKey.submit,AppKey.cancel,btn3];
            return WUR.layer.open({
                type : 1,
                title : title,
                content: $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                resize : false,
                btn : btns,
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
                btn2: function(index,layero){//第2个按钮-取消按钮
                    UE.getEditor(scriptId).destroy();//销毁
                },
                btn3 : function(index,layero){
                    if (btn3Call != null && btn3Call != '' && btn3 != null && btn3 != ''){
                        btn3Call(index,layero);
                    }
                    return false;
                },
                cancel: function(index,layero){//右上角关闭按钮
                    UE.getEditor(scriptId).destroy();//销毁
                },
                success: function(layero,index){//成功打开layer时执行
                    var ue = UE.getEditor(scriptId);//实例化编辑器
                    ue.ready(function(){
                        if(data != null){
                            ue.setContent(data);
                        }else{
                            ue.setContent('');
                        }
                    })
                }
            });
        },
        /**打印功能:layerFn.winPrint(title,domDivId,[width,height],callback);*/
        winPrint : function(title,domDivId,area,callback){
            return top.layer.alert('',{
                shade : 0.3,
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                btnAlign: 'c',
                resize : false,
                area : area,
                btn :['打印',AppKey.cancel],
                yes : function(index,layero){
                    callback(index,layero);
                },
            });
        },
        /**获取layerFn.winUrl()或layerFn.winRUrl()打开新页面的js方法及属性:var iframeWin = layerFn.getIframe(layero);*/
        getIframe : function(layero){
            return window.window[layero.find('iframe')[0]['name']];
        },
        /**适用于查看,带自动关闭的,当time为空或为0时是不会自动关闭;layerFn.viewDialog(title,domDivId,[width,height],time);*/
        viewDialog : function(title,domDivId,area,time){
            time = (time == null || time == '')?0:time;
            return top.layer.open({
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                btn : [AppKey.close],
                time : time,
                resize : false
            });
        },
        /**跳转到登录页面:layerFn.pageLogin(code,msg);*/
        pageLogin : function(code,msg){
            layerFn.closeEvent(code,msg,function(){
                window.location.href = AppKey.loginUrl;
            });
        },
        /**跳转到后台页面:layerFn.pageControl(code,msg);*/
        pageControl : function(code,msg){
            layerFn.closeEvent(code,msg,function(){
                window.location.href = AppKey.control;
            });
        },
        winLayer : {
            /**右上角1,4,6是抖动:layerFn.winLayer.rt(msg);*/
            rt : function(msg){
                layer.alert(msg,{
                    offset : 'rt',
                    shade : 0,
                    anim : 4
                });
            },
            /**右下角:layerFn.winLayer.rb(msg);*/
            rb : function(msg){
                layer.alert(msg,{
                    offset : 'rb',
                    shade : 0,
                    time : 1000,
                    anim : 2
                });
            },
            /**居中:layerFn.winLayer.cc(msg,anim);*/
            cc : function(msg,anim){
                if (anim == null){
                    anim = 0;
                }
                layer.alert(msg,{
                    anim : 2
                });
            }
        },
        /**提示框:layerFn.tips('#id或者.class','请填写内容');*/
        tips : function(dom,msg){
            layer.tips(msg,dom,{
                tips: [2,'#459df5']//2;4
            });
        },
        resultTemplate : function(code,msg){
            layer.open({
                type : 1,
                title : false,
                shade : 0, //遮罩透明度
                closeBtn : 0, //不显示关闭按钮
                area : ['auto','40px'],
                content : '<span style="line-height:40px;height:40px;padding-left:20px;padding-right:20px;display:inline-block;">'+msg+'</span>',
                time : 1000, //1秒后自动关闭
                offset: 'rt'
            });
        },
        /**居中center偏上top;layerFn.showCT(AppKey.code.code200,msg);*/
        showCT : function(code,msg){
            code = parseInt(code);
            var imagerUrl = AppKey.iconDir+'icon_ok.png';
            switch (code){
                case AppKey.code.code200:
                    msg = verifyFn.isEmpty(msg)?"操作成功":msg;
                    break;
                case AppKey.code.code199:
                    msg = verifyFn.isEmpty(msg)?"操作失败":msg;
                    imagerUrl = AppKey.iconDir+'warn_mini.png';
                    break;
                default:
                    msg = verifyFn.isEmpty(msg)?"操作有误":msg;
                    imagerUrl = AppKey.iconDir+'error_mini.png';
                    break;
            }
            layer.open({
                type : 1,
                title : false,
                shade : 0, //遮罩透明度
                closeBtn : 0, //不显示关闭按钮
                area : ['auto', '40px'],
                offset : '8px',
                content : '<div style="line-height:40px;height:40px;padding-left:16px;padding-right:16px;display:inline-block;font-size:14px;"><img src='+imagerUrl+' style="margin-bottom:-2px;margin-right:4px;"/>'+msg+'</div>',
                time : 1500
            });
        },
        /**居中显示且无焦点无遮罩的提示;layerFn.showCC(msg);*/
        showCC : function(msg){
            layer.open({
                type: 1,
                title: false,
                shade: 0,
                closeBtn: 0,
                area: ['auto', '50px'],
                content: '<div style="line-height:50px;height:50px;padding-left:16px;border-radius:6px;padding-right:16px;display:inline-block;font-size:14px;">'+msg+'</div>',
                time: 1500
            });
        },
        /**单个按钮的提示框,不带回调事件:layerFn.alert(msg,code);code不为空且值为[AppKey.code.code198,199,200,201,202,203,204,205,206,207]之一时显示图标*/
        alert : function(msg,code){
            if(code == null || code == ''){
                opts.dialog({msg : msg});
            }else{
                opts.dialog({msg : msg,code:code});
            }
        },
        /**对话框关闭事件,不管按确定按钮或右上角的按钮都会触发事件:layerFn.closeEvent(code,msg,function(){});*/
        closeEvent : function (code,msg,callBack){
            msg = verifyFn.isEmpty(msg)?"操作有误":msg;
            code = parseInt(code);
            var imagerUrl = AppKey.iconDir+'warn.png';
            var img_style = '"vertical-align:middle;margin-right:3px;"';
            var content = '<div style="font-size:12px;"><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
            switch(code){
                case AppKey.code.code200:
                    msg = verifyFn.isEmpty(msg)?"操作成功":msg;
                    imagerUrl = AppKey.iconDir+'success.png';
                    content = '<div style="font-size:12px;"><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                case AppKey.code.code204:
                case AppKey.code.code205:
                case AppKey.code.code207:
                    msg = verifyFn.isEmpty(msg)?AppKey.sysError:msg;
                    imagerUrl = AppKey.iconDir+'error.png';
                    content = '<div style="font-size:12px;"><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                default:
                    break;
            }
            top.layer.alert(content,{
                title : AppKey.title,
                btn : [AppKey.confirm],
                area : AppKey.layerArea,
                cancel : callBack,
            },function(index){
                layerFn.layerClose(index);
                if(callBack != null && callBack != ''){
                    callBack();
                }
            });
        },
    };
    /**插件定义配置*/
    window.opts = {
        /**layer单个按钮的提示框,不带回调事件:opts.dialog(options);不带code就不显示图标;*/
        dialog : function (options){
            var title = options.title || AppKey.title;
            var msg = options.msg || '操作有误';
            var code = options.code || null;
            code = parseInt(code);
            var imagerUrl = AppKey.iconDir+'warn.png';
            var img_style = '"vertical-align:middle;margin-right:3px;"';
            var content = '<div style="font-size: 12px;">'+msg+'</div>';
            switch(code){
                case AppKey.code.code198:
                case AppKey.code.code199:
                case AppKey.code.code201:
                case AppKey.code.code202:
                case AppKey.code.code203:
                case AppKey.code.code205:
                case AppKey.code.code206:
                case AppKey.code.code207:
                    content = '<div style="font-size: 12px;"><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                case AppKey.code.code200:
                    msg = verifyFn.isEmpty(msg)?"操作成功":msg;
                    imagerUrl = AppKey.iconDir+'success.png';
                    content = '<div style="font-size: 12px;"><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                case AppKey.code.code204:
                    msg = verifyFn.isEmpty(msg)?AppKey.sysError:msg;
                    imagerUrl = AppKey.iconDir+'error.png';
                    content = '<div style="font-size: 12px;"><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                default:
                    break;
            }
            var exitIndex = top.layer.alert(content,{
                title : title,
                area : AppKey.layerArea,
                btn : [AppKey.confirm],
            });
            layerFn.EscLayer(exitIndex);
        },
        /**按键盘监听事件回调:opts.event({code:27,fn:function(){}});*/
        event : function(options){
            var code = options.code || null;
            var fn = options.fn || null;
            $(document).keydown(function(e){
                if(e.keyCode == code){
                    if (fn !=null && fn != undefined){
                        fn();
                    }
                }
            });
        },
    };
    /**alert('好的,谢谢!',function(){alert('嗯,再见!')})*/
    window.alert = function(msg,callback){
        var al_t = (self==top)?parent:window;
        al_t.layer.alert(msg,{
            title : AppKey.title,
            area : 'auto',
            btn : [AppKey.confirm],
            cancel : function(index,layero){
                al_t.layer.close(index);
                if(callback != null && callback != ''){
                    callback();
                }
            }
        },function (index) {
            al_t.layer.close(index);
            if(callback != null && callback != ''){
                callback();
            }
        });
    }
    /**confirm('你好,需要服务吗?',function() {alert('好的,谢谢!',function(){alert('嗯,再见!')})});*/
    window.confirm = function(msg,callback){
        var conf_m = (self==top)?parent:window;
        conf_m.layer.confirm(msg,{
                title : AppKey.title,
                btn : [AppKey.confirm,AppKey.cancel],
                area : 'auto',
            },
            function(){
                if(typeof(callback) === "function"){
                    callback("ok");
                }
            });
    }
    fwtai = {};
    /**用法:fwtai.msg('什么情况?');*/
    fwtai.msg = function (msg){
        alert(msg);
    }
    verifyFn.initExtend();
})(jQuery);