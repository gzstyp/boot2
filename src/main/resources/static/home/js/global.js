/**
 * 所有页面的通用js文件
 * @作者 田应平
 * @创建时间 2016年5月30日 14:42:08
*/

/**
 * 给input赋值
*/
function fnInputSetValue(inputId,value){
	$('#'+inputId).val(value);
}

/**
 * 给input取值
*/
function fnInputGetValue(inputId){
	return $('#'+inputId).val();
}

/**
 * 判断或验证是否为空
 * @param value
 * @returns {Boolean}
*/
function checkNull(value){
	if(value == undefined || value == null || value.length <= 0 || value == '' || $.trim(value).length <= 0)return true;
	return false;
}

/**
 * 获取项目的访问路径
 * @returns
*/
function fnBaseUrl(){
	return window.document.location.href; 
}

/**
 * 给元素设置指定px的高度
 * @作者 田应平
*/
function tagSetHeight(flgId,height){
	$("#"+flgId).css({"height":height});
}

/**
 * 给元素设置指定px的宽度
 * @作者 田应平
*/
function tagSetWidth(flgId,width){
	$("#"+flgId).css({"width":width});
}

/**
 * 给元素设置指定px的高度和宽度
 * @作者 田应平
*/
function tagSetHeightWidth(flgId,height,width){
	$("#"+flgId).css({"height":height,"width":width});
}

/**
 * 返回屏幕或浏览器的高度
 * @作者 田应平
 * @创建时间 2016年5月11日 09:28:27
*/
function fnWindowHeight(){
	return $(document.body).height();
}

/**
 * 返回屏幕或浏览器的宽度
 * @作者 田应平
 * @创建时间 2016年5月11日 09:29:20
*/
function fnWindowWidth(){
	return $(document.body).width();
}

/**
 * 屏幕分辨率的高
 * @作者 田应平
*/
function fnScreenHeight(){
	return window.screen.height;
}

/**
 * 屏幕分辨率的宽
 * @作者 田应平
*/
function fnScreenWidth(){
	return window.screen.width;
}

/**
 * 屏幕可用工作区高度-不含最下边的工具栏
 * @作者 田应平
*/
function fnScreenAvailHeight(){
	return window.screen.availHeight;
}

/**
 * 屏幕可用工作区宽度
 * @作者 田应平
*/
function fnScreenAvailWidth(){
	return window.screen.availWidth;
}

/**
 * 网页正文全文宽
 * @作者 田应平
*/
function fnScreenScrollWidth(){
	return document.body.scrollWidth;
}

/**
 * 网页正文全文高
 * @作者 田应平
*/
function fnScreenSrollHeight(){
	return document.body.scrollHeight;
}

/**
 * 控制html元素或标签的高度
 * @param tagId
 * @param size
 * @创建时间 2016年5月3日 11:37:46
 * @作者 田应平
 */
function htmlTagHeight(tagId,size){
	var divHeight = $(document.body).height() * size;
	$("#"+tagId).css("height",divHeight+"px");
}

/**设置body主内容*/
function setBody(src){
	$('#content_body').attr("src",src);
}

/**设置body主内容含标题*/
function setContentTile(url,title){
	$('#content_body').attr("src",url);
	$('#row_col_context').text(title);
}