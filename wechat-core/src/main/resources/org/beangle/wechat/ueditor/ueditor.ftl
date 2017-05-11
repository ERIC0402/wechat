[#ftl]
[#macro ueditorJs]
	<script type="text/javascript" charset="utf-8" src="${base}/static/scripts/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${base}/static/scripts/ueditor/ueditor.all.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="${base}/static/scripts/ueditor/lang/zh-cn/zh-cn.js"></script>
[/#macro]
[#macro ueditor id="editor" name="textarea" width="100%" height="100%" value="" maxlength=10000 isWordCount=true sourceEditorFirst=false callback=""
	toolbar = "['fullscreen', 'source', 'undo', 'redo'],
				    [
				    	'bold', //加粗
				        'indent', //首行缩进
				        'snapscreen', //截图
				        'italic', //斜体
				        'underline', //下划线
				        'strikethrough', //删除线
				    	'justifyleft', //居左对齐
				        'justifyright', //居右对齐
				        'justifycenter', //居中对齐
				        'justifyjustify', //两端对齐
				        'forecolor', 
				        'backcolor', 
				        'insertorderedlist', 
				        'insertunorderedlist', 
				        'selectall', 
				        'cleardoc'
				    ],
				    [
				    	'fontfamily', //字体
				        'fontsize', //字号
				        'paragraph', //段落格式
				        'simpleupload', //单图上传
				        'insertimage', //多图上传
				        'edittable', //表格属性
				        'edittd', //单元格属性
				        'link', //超链接
				        'emotion', //表情
				        'spechars', //特殊字符
				        'searchreplace', //查询替换
				        'inserttable', //插入表格
				        'removeformat', //清除格式
				        'formatmatch', //格式刷子
				        'pasteplain', //纯文本粘贴
				    ]"
]
	<!-- 加载编辑器的容器 -->
	<script id="editor${id!}" type="text/plain">
      	 ${value!}
    </script>
	[#nested]
	<script type="text/javascript">
		var urlPrefix = "${(w.basePath())!}";
	    //实例化编辑器
	    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	    var ue${(id)!} = UE.getEditor(
	    	'editor${(id)!}',
	    	{
	    		UEDITOR_HOME_URL: urlPrefix + "/static/scripts/ueditor/",
	    		serverUrl: urlPrefix + "/ueditor/controller.jsp",
	    		toolbars: [
				    ${(toolbar)!}
				],
				allowDivTransToP: false, //允许将Div标签自动转换成P标签
				enableAutoSave: false, //是否自动保存
				wordCount: ${isWordCount?c}, //是否开启字数统计
				maximumWords: ${maxlength}, //允许的最大字符数 
				textarea : '${name}',
				initialFrameWidth : '${width}',
	        	initialFrameHeight : '${height}',
	        	autoFloatEnabled:false,
	        	catchRemoteImageEnable:false,
	        	sourceEditorFirst:${sourceEditorFirst?c}
	    	}
	    );
	    ue${(id)!}.ready(function() {
	    	ue${(id)!}.options.imageUrlPrefix = urlPrefix;
	    	ue${(id)!}.options.snapscreenUrlPrefix = urlPrefix;
	    	ue${(id)!}.options.imagePathFormat = "/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}";
	    	ue${(id)!}.options.imageManagerListPath = "/upload/image/";
	    	ue${(id)!}.options.fileAllowJsCssFiles = [".js", ".css", ".zip", ".png", ".jpg", ".jpeg", ".gif"];
	    	eval("${(callback)!}");
		});
		
	    function getRootPath(){
	        //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	        var curWwwPath=window.document.location.href;
	        //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	        var pathName=window.document.location.pathname;
	        var pos=curWwwPath.indexOf(pathName);
	        //获取主机地址，如： http://localhost:8083
	        var localhostPath=curWwwPath.substring(0,pos);
	        //获取带"/"的项目名，如：/uimcardprj
	        //return(localhostPath+projectName);
	        return(localhostPath+"${base}");
    	}
	</script>
[/#macro]
