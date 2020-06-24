;(function($){
    $.fn.extend({
        /**
         * 表单加载json对象数据 formId是表单的id，jsonValue是传递的json对象
        */
        initForm : function (formId,jsonValue){
            var _form = $('#'+formId);//获取表单dom对象
            $.each(jsonValue, function (name, ival) {
                var _formInput = _form.find("[name=" + name + "]");//根据属性选择器获取表单元素对象
                if(_formInput.length == 0){//判断jsonValue中的key 在指定的formId中是否能找到这个控件
                    return true;//进入下一次循环
                }
                var _tagNameType =  $(_formInput)[0].tagName.toLowerCase();//获取输入框的tagName属性，判断是哪中标签
                if(_tagNameType == 'input'){
                    var _attrType = _formInput.attr("type");//按钮type类型
                    if (_attrType == "checkbox") {//多选框赋值
                        if (ival !== null) {
                            var checkboxObj = $("[name=" + name + "]");
                            var checkArray = ival.split(",");
                            for (var i = 0; i < checkboxObj.length; i++) {
                                for (var j = 0; j < checkArray.length; j++) {
                                    if (checkboxObj[i].value == checkArray[j]) {
                                       checkboxObj[i].click();//触发点击事件
                                    }
                                }
                            }
                        }
                    }else if (_attrType == "radio") {//单选按钮赋值
                        _formInput.each(function () {
                            var radioObj = $("[name=" + name + "]");
                            for (var i = 0; i < radioObj.length; i++) {
                                if (radioObj[i].value == ival) {
                                    radioObj[i].click();//触发点击事件
                                }
                            }
                        });
                    }else if (_attrType == "text") {//普通文本框赋值
                        _form.find("[name=" + name + "]").val(ival);
                    }else{//类似hidden 的input框做隐藏域使用时，也需要赋值，否则后台取不到设置的id值
                        _form.find("[name=" + name + "]").val(ival);
                    }
                }else if(_tagNameType == 'textarea'){//多行文本框赋值
                    _form.find("[name=" + name + "]").val(ival);
                }else if(_tagNameType == 'select'){ //下拉列表赋值
                    var _id = '#'+$("[name=" + name + "]").attr("id");
                    if(null==ival){
                        return true;//没有选中的选项，直接进入下一轮循环
                    }
                    if(ival>=0){
                        var _selected_condition = "option[value=" + ival + "]";//选择器的值 例如："option[value = 3]"
                        $(_id).find(_selected_condition).prop('selected', true);
                    }else{
                        var _selectArray = ival.split(",");
                        //这个是原始select的选中方式
                        for (var j = 0; j < _selectArray.length; j++) {
                            var _selected_condition = "option[value=" + _selectArray[j] + "]";//选择器的值 例如："option[value = 3]"
                            $(_id).find(_selected_condition).prop('selected', true);
                        }
                    }
                    //这个是bootstrap-multiselect的选中方式
                    //$(_id).multiselect('select', _selectArray,true);//让multiselect处于选中状态
                }
            })
        }
    })
})(jQuery);