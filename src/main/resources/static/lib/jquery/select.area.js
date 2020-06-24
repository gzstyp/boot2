;(function($){
	window.jQArea = {
		province : "TFPROVINCEID",
		city : "TFCITYID",
		district : "TFDISTRICTID",
		towns : "TFTOWNSID",
		vallage : "TFVALLAGEID",
		area_organize : "area_organize",//组的id
		init:function(){
			jQArea.queryArea('0',jQArea.province);
		},
		/**触发下拉事件处理*/
		onSelectChange : function(obj,dom){
			var _self = this;
			_self.queryArea(obj.value,dom);
			$('#'+dom).attr('value',obj.value);
		},
		/**查询省市县区域列表*/
		queryArea : function(pid,dom){
			var _self = this;
			if(pid != null && pid != ''){
				$(dom).text('');
				layerFn.ajaxHint('area/queryArea','正在读取……',{pId:pid},function(data){
					_self.createViewSelect(data.listData,dom);
				});
			}
		},
		/**动态创建下拉列表,data json数据;dom下拉控件名称*/
		createViewSelect : function(data,dom){
			var _self = this;
			var arr = eval(data);
			if(arr != null && arr.length > 0){
				var obj = document.getElementById(dom);
				obj.innerHTML = "";
				//正常显示
				obj.style.display = 'block';
				var nullOp = document.createElement("option");
				nullOp.setAttribute("value", "");
				_self.selectDom(dom,nullOp);
				obj.appendChild(nullOp);
				for (var k = 0; k < arr.length; k++){
					var op = document.createElement("option");
					var id = arr[k].id;
					var name = arr[k].name;
					op.setAttribute("value",id);
					op.appendChild(document.createTextNode(name));
					obj.appendChild(op);
				}
			} else {
				//判断市
				if(dom == _self.city){
					_self.resetDom(dom);
					_self.resetDom(_self.district);
					_self.resetDom(_self.towns);
					_self.resetDom(_self.vallage);
				}
				//判断区县隐藏
				if(dom == _self.district){
					_self.resetDom(dom);
					_self.resetDom(_self.towns);
					_self.resetDom(_self.vallage);
				}
				//判断乡镇
				if(dom == _self.towns){
					_self.resetDom(dom);
					_self.resetDom(_self.vallage);
				}
				//判断村
				if(dom == _self.vallage){
					_self.resetDom(dom);
				}
				//判断组,最后都要执行的,所以只做判断即可
				if($("#"+_self.area_organize)[0]){
					_self.resetDom(_self.area_organize);
				}
			}
		},
		resetDom : function(dom){
			document.getElementById(dom).innerHTML = "";
			document.getElementById(dom).style.display = 'none';
		},
		selectDom : function(dom,nullOp){
			var _self = this;
			if(dom == _self.province || dom == 'AREA_PROVINCE'){
				nullOp.appendChild(document.createTextNode("请选择省/市"));
				_self.resetDom(_self.city);
				_self.resetDom(_self.district);
				_self.resetDom(_self.towns);
				_self.resetDom(_self.vallage);
			}else if(dom == _self.city || dom == 'AREA_CITY'){
				nullOp.appendChild(document.createTextNode("请选择市/区"));
				_self.resetDom(_self.district);
				_self.resetDom(_self.towns);
				_self.resetDom(_self.vallage);
			}else if(dom == _self.district || dom == 'AREA_COUNTY'){
				nullOp.appendChild(document.createTextNode("请选择区/县"));
			}else if(dom == _self.towns || dom == 'AREA_TOWN'){
				nullOp.appendChild(document.createTextNode("请选择乡/镇"));
			}else if(dom == _self.vallage || dom == 'AREA_VILLAGE'){
				nullOp.appendChild(document.createTextNode("请选择村/组"));
			}else if(dom == _self.area_organize){
				nullOp.appendChild(document.createTextNode("请选择组"));
			}else{
				nullOp.appendChild(document.createTextNode("请选择"));
			}
		},
		/**当选择改变时且有数据调用远程数据*/
		selectChange:function(pid,textShow,empty,domId,select){
			if(pid == null || pid == '')return;
			jQArea.queryAreaByPid(pid,textShow,empty,domId,'id','name',select);
		},
		/**通用的区域查询*/
		queryAreaByPid : function(pid,defaultText,emptyText,domId,value,showText,select){
			if(pid == null || pid == '')return;
			layerFn.ajaxHint('area/queryArea','正在读取……',{pId:pid},function(data){
				winFn.setDataSelect(defaultText,emptyText,domId,data.listData,value,showText,select);
			});
		},
	};
})(jQuery);