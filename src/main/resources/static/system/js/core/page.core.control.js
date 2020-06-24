;(function($){
	thisPage = {
		init : function(){
			thisPage.addEvent();
			winFn.iePlaceholder();
		},
		addEvent : function(){
			thisPage.logout();
			thisPage.alterPwd();
		},
		logout : function(){
			/**退出登录操作*/
			$('#logout').on('click',function(){
				layerFn.confirm('您确定要退出系统吗?',function(){window.location.href = "user/logout";});
			});
		},
		alterPwd : function(){
			$('#editPassword').on('click',function(){
				$("#div_edit_pwd").css("visibility","visible");
				layerFn.addOrEdit('修改密码','#div_edit_pwd',['340px','222px'],function(index){
					if (verifyFn.inputRequired('#key_uid')){
						layerFn.center(AppKey.code.code204,'获取登录信息失败');
						return;
					}
					if (verifyFn.inputRequired('#password_current')){
						layerFn.center(AppKey.code.code201,'请输入当前密码');
						return;
					}
					if (verifyFn.inputRequired('#password_new')){
						layerFn.center(AppKey.code.code201,'请输入新的密码');
						return;
					}
					if (verifyFn.inputRequired('#password_confirm')){
						layerFn.center(AppKey.code.code201,'请输入确认密码');
						return;
					}
					var pwd_current = winFn.getDomValue('#password_current');
					var pwd_new = winFn.getDomValue('#password_new');
					var pwd_confirm = winFn.getDomValue('#password_confirm');
					if(verifyFn.checkEqual(pwd_new,pwd_confirm)){
						layerFn.center(AppKey.code.code204,'输入的两次密码不一致');
						return;
					}
					if(!verifyFn.checkEqual(pwd_current,pwd_confirm)){
						layerFn.center(AppKey.code.code204,'新密码和旧密码不能相同');
						return;
					}
					var params = {
						uid : winFn.getDomValue('#key_uid'),
						pass_current : pwd_current,
						pass_new : pwd_new,
						pass_confirm : pwd_confirm
					};
					layerFn.submit('user/alterPwd',params,function(data){
				 		layerFn.layerClose(index);
				 		layerFn.showCT(data.code,'操作成功');
				 	});
				});
				winFn.clearFormData('#div_edit_pwd');
			});
		}
	};
	thisPage.init();
})(jQuery);

/**切换原帐号登录*/
function switcher(){
	layerFn.confirm('确定切换原帐号登录吗?',function(){
		layerFn.ajaxRequestHint('user/switcher',{},function(data){
			layerFn.pageControl(data.code,data.msg);
		},'正在切换,请稍候……');
	});
}

/**代码生成*/
function code_craete(){
	$("#code_create").css("visibility","visible");
	layerFn.addOrEdit('<span style="color:red;">代码生成</span>','#code_create',['430px','230px'],function(index){
		if(verifyFn.inputRequired('#nameSpace')){
			layerFn.center(AppKey.code.code204,'请输入mybatis映射文件的命名空间,如:user');
			return;
		}
		if(verifyFn.inputRequired('#className')){
			layerFn.center(AppKey.code.code201,'请输入实体类名且首字母大写,如:User');
			return;
		}
		if(verifyFn.inputRequired('#table')){
			layerFn.center(AppKey.code.code201,'小哥,请输入表名!');
			return;
		}
		if(verifyFn.inputRequired('#keyId')){
			layerFn.center(AppKey.code.code201,'请输入表的主键字段');
			return;
		}
		var params = {
			table : winFn.getDomValue('#table'),
			nameSpace : winFn.getDomValue('#nameSpace'),
			className : winFn.getDomValue('#className'),
			keyId : winFn.getDomValue('#keyId')
		};
		layerFn.ajaxGetHint('code/queryExistTable','正在操作……',params,function(data){
			if(data.code === AppKey.code.code200){
				$("#form_code").submit();/**form表单提交*/
				layerFn.layerClose(index);
				layerFn.showCT(AppKey.code.code200,'处理完成,请等待下载!');
			}else{
				layerFn.center(data.code,data.msg);
			}
		},function(err){
			layerFn.center(AppKey.code.code201,AppKey.msg_error);
		});
	});
	winFn.clearFormData('#code_create');
}