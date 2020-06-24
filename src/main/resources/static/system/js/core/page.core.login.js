/**
 * 登录页面的js文件
 * @作者 田应平
 * @QQ号 444141300
 * @Web  www.fwtai.com
 * @创建时间 2016年12月27日 01:07:23
*/
;(function($){
thisPage = {
	/**初始化*/
	init:function(){
		this.addEvent();
		winFn.iePlaceholder();
	},
	/**登录*/
	login : function(){
		var _self = this;
		var uname = winFn.getDomValue('#uname');
		var upwd = winFn.getDomValue('#upwd');
		var code = winFn.getDomValue('#code');
		if (verifyFn.inputRequired('#uname')){
			layerFn.closeEvent(AppKey.code.code199,'请输入登录账号',function(){
				winFn.focus('#uname');
			});
			return false;
		}
		if (verifyFn.inputRequired('#upwd')){
			layerFn.closeEvent(AppKey.code.code199,'登录密码不能为空',function(){
				winFn.focus('#upwd');
			});
			return !1;
		}
		if (verifyFn.inputRequired('#code')){
			layerFn.closeEvent(AppKey.code.code199,'请输入图形验证码',function(){
				winFn.focus('#code');
			});
			return false;
		}
		paramas = {
			account:uname,
			pwd:upwd,
			code:code
		};
		layerFn.ajaxPostHint('user/userLogin','正在登录,请稍候……',paramas,function(data){
			if (data.code == AppKey.code.code200){
				window.location.href = AppKey.control;
				return;
			}else if(data.code == AppKey.code.code198){
				layerFn.closeEvent(data.code,data.msg,function(){
					_self.codeRefresh();
					winFn.focus('#code');
				});
				return;
			}else{
				layerFn.closeEvent(data.code,data.msg,function(){
					_self.codeRefresh();
				});
				return;
			}
		},function(){
			layerFn.alert(AppKey.msg_error);
		});
	},
	/**重置*/
	reset:function(){
		window.winFn.setDomValue('#uname');
		winFn.setDomValue('#upwd');
		winFn.setDomValue('#code');
		this.codeRefresh();
	},
	/**绑定事件*/
	addEvent:function(){
		var _self = this;
		$("#button-login").on("click", function(){
			_self.login();
		});
		$("#button-reset").on("click", function(){
			_self.reset();
		});
		$("#imgCode").on("click", function(){
			_self.codeRefresh();
		});
		$(document).keyup(function(event){
		    if (event.keyCode == 13){
		        $("#button-login").trigger("click");
		    }
		});
	},
	/**刷新验证码*/
	codeRefresh : function(){
		winFn.setDomValue('#code');
		winFn.refreshCode('#imgCode',"imgCode");
	}
};
thisPage.init();/*初始化*/
})(jQuery);