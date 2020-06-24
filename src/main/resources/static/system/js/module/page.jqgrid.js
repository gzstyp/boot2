;(function($){
   var $jqGrid = $("#jqGrid");
    thisPage = {
        initDom : function (){
            thisPage.initJqGrid();
        },
        initJqGrid : function (){
            $jqGrid.jqGrid({
                url: '/jqgrid/list',
                datatype: "json",
                mtype : 'get',
                colModel: [
                    {label: '主键', name: 'uid',index: "uid", width: 45, key:true,hidden:true,},
                    {label: '账号', name: 'account',align:'left',index: "account",width: 75,},//editable : false
                    {label: '注册时间', name: 'added', width: 75 ,editable : false},
                    {label: '是否系统账号', name: 'type',index: "type",width:100,formatter:function(cellvalue,options,rowObject){
                            return cellvalue == 1 ? "是":"否";
                        }
                    },
                    {label: '登录次数', name: 'times',align:'left',index: "times",width: 75,},
                    {label: '最后登录日期', name: 'logintime',index: "logintime",width:32,formatter:function(cellvalue,options,rowObject){return cellvalue}},//cellvalue - 当前cell的值,options - 该cell的options设置，包括{rowId, colModel,pos,gid},rowObject - 当前cell所在row的值，如{ id=1, name="name1", price=123.1, ...}
                    /*{label: '操作', name: 'operate',width:100,formatter:function(cellvalue,options,rowObject){
                            return "<a href='javascript:thisPage.del(\""+rowObject.uid+"\");'>删除</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href='javascript:thisPage.editPass(\""+rowObject.uid+"\",\""+rowObject.account+"\");'>修改密码</a>"
                        }
                    },*/
                ],
                viewrecords: true,
                height: 350,
                rowNum: 5,
                rowList : [5,10,20,30,50],
                rownumbers: false,// 是否显示行号
                hiddengrid: false,//面板是否收缩
                autowidth:true,//是否自动调整列宽
                multiselect: true,//支持多项选择
                pager: "#jqGridPager",//分页控件的id
                subGrid: false,//是否启用子表格
                jsonReader : {
                    root: "list",
                    page: "page",
                    total: "totalPage",
                    records: "records",
                },
                showList: true,
                prmNames : {
                    page:"page",
                    rows:"rows",
                    order:"order",
                    sort:"sidx",
                    repeatitems: false,
                },
                gridComplete:function(){
                    //隐藏grid底部滚动条
                    $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden"});
                    //禁用排序功能
                    /*var columnNames = $jqGrid.jqGrid('getGridParam','colModel');
                    for (i = 0; i < columnNames.length; i++){
                        $jqGrid.setColProp(columnNames[i].index,{sortable: false});
                    }*/
                },
                onSortCol: function (field,colindex,sortorder){//列排序
                }
            });
            // 设置jqgrid的宽度
            $(window).bind('resize', function(){
                var width = $('.jqGrid_wrapper').width();
                $("#jqGrid").setGridWidth(width);
            });
            $jqGrid.jqGrid('navGrid','#jqGridPager',{edit:false,add:false,del:false,search:false},{},{},{},{multipleSearch:true});
        },
        add : function () {
            layerFn.addOrEdit('添加','#div_add',['400px','300px'],function (index) {
                var params = winFn.formParams('#form_reg');
                layerFn.submit('/user/reg',params,function(data){
                    if (data.code == AppKey.code.code200) {
                        layerFn.loading.hide(index);
                        layerFn.closeEvent(AppKey.code.code200,'恭喜,添加成功',function (){
                            thisPage.search();
                        });
                    } else {
                        layerFn.center(data.code,"添加失败,请重试!");
                    }
                });
            })
        },
        delById : function () {
           var kid = thisPage.getSelectedRow();
            var rowData = $jqGrid.jqGrid('getRowData',kid);//获取数据行
            if(!verifyFn.isEmpty(kid)){
                layerFn.handleVerify('确认要删除['+rowData.account+']吗?',function(){
                    layerFn.submit('/user/del?kid='+kid,'',function(data){
                        if (data.code == AppKey.code.code200) {
                            layerFn.closeEvent(AppKey.code.code200,data.msg,function (){
                                thisPage.search();
                            });
                        } else {
                            layerFn.center(data.code,data.msg);
                        }
                    })
                });
            }
        },
        edit : function(){
          var uid = thisPage.getSelectedRow();
          var rowKey = $jqGrid.getGridParam("selrow");//获取行的主键id
          var rowData = $jqGrid.jqGrid('getRowData',rowKey);//获取数据行
          if(!verifyFn.isEmpty(uid)){
              layerFn.addOrEdit(rowData.account+'修改密码','#editPWd',['400px','300px'],function(index,layero){
                  var params =  winFn.formParams('#form_edit')+'&uid='+uid+'&account='+rowData.account;
                  layerFn.submit('/user/editPass',params,function (data){
                      layerFn.loading.hide(index);
                      if (data.code == AppKey.code.code200){
                          layerFn.closeEvent(AppKey.code.code200,'恭喜,修改成功!',function(){
                              thisPage.search();
                          });
                      } else {
                          layerFn.center(data.code,data.msg);
                      }
                  });
              });
          }
        },
       searchDemo : function (){
           var page = $jqGrid.jqGrid('getGridParam','page');//当前页
           var rows = $jqGrid.jqGrid('getGridParam','rowNum');//当前每页大小
           var sidx = $jqGrid.jqGrid('getGridParam','sortname');//排序字段
           var order = $jqGrid.jqGrid('getGridParam','sortorder');//排序关键字
           var params =  winFn.formParams('#searchForm')+'&page='+page+'&rows='+rows+'&sidx='+sidx+'&order='+order;
           $jqGrid.jqGrid('setGridParam',{
               datatype:'json',
               postData : params,
               page : page,
               rowNum : rows,
               sortname: sidx,
               sortorder: order,
               showList: true,
               search: true,
           }).trigger("reloadGrid");
       },
       search : function (){
           var params =  winFn.formParams('#searchForm');
           var postData = $jqGrid.jqGrid("getGridParam","postData");
           var page = $jqGrid.jqGrid('getGridParam','page');//当前页
           $.extend(postData,{jqGridParams:params});
           $jqGrid.jqGrid("setGridParam",{search:true,datatype:'json',page : page}).trigger("reloadGrid",[{page:page}]);
       },
       getSelectedRow:function(){
           var rowKeys = $jqGrid.getGridParam("selrow");
           if(!rowKeys){
               alert("请选择一条记录");
               return ;
           }
           var selectedIds = $jqGrid.getGridParam("selarrrow");
           if(selectedIds.length > 1){
               alert("只能选择一条记录");
               return ;
           }
           return selectedIds[0];
       }
    }
    thisPage.initDom();
})(jQuery);