<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>用户主页</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/WEB-INF/common.jsp"%>

<link
	href="${path }/resources/css/plugins/bootstrap-table/bootstrap-table.min.css"
	rel="stylesheet">
<link href="${path }/resources/css/animate.css" rel="stylesheet">
<link href="${path }/resources/css/style.css?v=4.1.0" rel="stylesheet">

<!--  导入的样式和js -->
<link href="${path }/resources/css/fileinput.css" media="all" rel="stylesheet" type="text/css" />
<script src="${path }/resources/js/fileinput.js" type="text/javascript" charset="utf-8"></script>


</head>
<body class="gray-bg">
	<div class="panel-body">
		<div id="toolbar" class="btn-group">
			<c:forEach items="${operationList}" var="oper">
				<privilege:operation operationId="${oper.operationid }" id="${oper.operationcode }" name="${oper.operationname }" clazz="${oper.iconcls }"  color="#093F4D"></privilege:operation>
			</c:forEach>
        </div>
        <div class="row">
			  <div class="col-lg-2">
				<div class="input-group">
			      <span class="input-group-addon">分类名称： </span>
			      <input type="text" name="name" class="form-control" id="txt_search_username" >
				</div>
			  </div>
			  <div class="col-lg-2">
				<div class="input-group">
					<span class="input-group-addon">分类级别</span>
					<select class="form-control" name="txt_search_itemcatid" id = "txt_search_roleid">
						<option value="0">---请选择---</option>
					 	<option value="1">一级分类</option>
					    <option value="2">二级分类</option>
					    <option value="3">三级分类</option>
						
                	</select>
				</div>
			 </div>
            <button id="btn_search" type="button" class="btn btn-default">
            	<span class="glyphicon glyphicon-search" aria-hidden="true"></span>查询
            </button>
	  	</div>
        
        <table id="table_user"></table>
		
	</div>
	
	<!-- 新增和修改对话框 -->
	<div class="modal fade" id="modal_user_edit" role="dialog" aria-labelledby="modal_user_edit" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<form id="form_user" method="post" action="reserveItemcat.htm">
						 <input type="hidden" name="itemcatid" id="hidden_txt_itemcatid" value=""/>
						<table style="border-collapse:separate; border-spacing:0px 10px;">
							<tr>
								<td>分类名称：</td>
								<td><input type="text" id="name" name="name"
									class="form-control" aria-required="true" required/></td>
								<td>&nbsp;&nbsp;</td>
								<!-- <td>密码：</td>
								<td><input type="password" id="password" name="password"
									class="form-control" aria-required="true" required/></td> -->
							</tr>
							<tr>
								<td>分类级别：</td>
								<td colspan="4">
									<select class="form-control" name="level" id = "level" aria-required="true" required
									  onchange="getItemcatByLevel(this.value)">
										<option value="">---请选择---</option>
										<option value="1">---一级分类---</option>
										<option value="2">---二级分类---</option>
										<option value="3">---三级分类---</option>									
				                	</select>
								</td>
							</tr>
							<tr>
								<td>所属上级分类：</td>
								<td colspan="4">
									<select class="form-control" name="parentId" id = "parentId" aria-required="true" required>																		
				                	</select>
								</td>
							</tr>
							<!-- <tr>
								<td valign="middle">备注：</td>
								<td colspan="4"><textarea rows="7" cols="50"
										name="userdescription" id="userdescription"></textarea></td>
							</tr> -->
						</table>
						
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"  id="submit_form_user_btn">保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						</div>
					</form>

				</div>
				
			</div>

		</div>

	</div>
	
	<!-- 导入对话框  -->
	<div class="modal fade" id="modal_log_import" role="dialog" aria-labelledby="modal_log_del" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
			    <form  action="importBus.htm" method="post" class="form-horizontal" enctype="multipart/form-data" onsubmit="document.getElementById('waitpic').style.display = 'block';">
			    <div class="modal-body" id="contentContainer1">
			 
				  <div class="form-group">
				    <label for="nameText1" class="col-lg-4 control-label"></label>
				    <div class="col-lg-7">
				        
				          <label class="control-label">选择文件</label>
				          <input id="file1" name="loginfo" type="file" multiple class="file-loading">
				          <script>
				          $(document).on('ready', function() {
				              $("#file1").fileinput({showCaption: false});
				          });
				          </script>
				
				    </div>
				  </div>
			    
			    </div>
			    <div class="modal-footer" id="buttonContainer">
			    	<a href="model/log_template.xls" name="downfile" target="_self"  style="float:left;" id="downfile" >下载模板</a>
			    </div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>			    
			    </form>
			</div>
		</div>
	</div>
	
	
	
	
	
	<!--删除对话框 -->
	<div class="modal fade" id="modal_user_del" role="dialog" aria-labelledby="modal_user_del" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					 <h4 class="modal-title" id="modal_user_del_head"> 刪除  </h4>
				</div>
				<div class="modal-body">
							删除所选记录？
				</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-danger"  id="del_user_btn">刪除</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
			</div>
		</div>
	</div>
	
	
	<div class="ui-jqdialog modal-content" id="alertmod_table_user_mod"
		dir="ltr" role="dialog"
		aria-labelledby="alerthd_table_user" aria-hidden="true"
		style="width: 200px; height: auto; z-index: 2222; overflow: hidden;top: 274px; left: 534px; display: none;position: absolute;">
		<div class="ui-jqdialog-titlebar modal-header" id="alerthd_table_user"
			style="cursor: move;">
			<span class="ui-jqdialog-title" style="float: left;">注意</span> <a id ="alertmod_table_user_mod_a"
				class="ui-jqdialog-titlebar-close" style="right: 0.3em;"> <span
				class="glyphicon glyphicon-remove-circle"></span></a>
		</div>
		<div class="ui-jqdialog-content modal-body" id="alertcnt_table_user">
			<div id="select_message"></div>
			<span tabindex="0"> <span tabindex="-1" id="jqg_alrt"></span></span>
		</div>
		<div
			class="jqResize ui-resizable-handle ui-resizable-se glyphicon glyphicon-import"></div>
	</div>
	
	<!-- Peity-->
	<script src="${path }/resources/js/plugins/peity/jquery.peity.min.js"></script>
	
	<!-- Bootstrap table-->
	<script src="${path }/resources/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
	<script src="${path }/resources/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

	<!-- 自定义js-->
	<script src="${path }/resources/js/content.js?v=1.0.0"></script>
	
	 <!-- jQuery Validation plugin javascript-->
    <script src="${path }/resources/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${path }/resources/js/plugins/validate/messages_zh.min.js"></script>
   
   	<!-- jQuery form  -->
    <script src="${path }/resources/js/jquery.form.min.js"></script>
    
	<script type="text/javascript">
	/* function fun1(){
		alert(":asdfgh");
	} */
	
	$(function () {
	    init();
	    $("#btn_search").bind("click",function(){
	    	//先销毁表格  
	        $('#table_user').bootstrapTable('destroy');
	    	init();
	    }); 
	    var validator = $("#form_user").validate({
    		submitHandler: function(form){
   		      $(form).ajaxSubmit({
   		    	dataType:"json",
   		    	success: function (data) {
   		    		
   		    		if(data.success && !data.errorMsg ){
   		    			validator.resetForm();
   		    			$('#modal_user_edit').modal('hide');
   		    			$("#btn_search").click();
   		    		}else{
   		    			$("#select_message").text(data.errorMsg);
   		    			$("#alertmod_table_user_mod").show();
   		    		}
                }
   		      });     
   		   }  
	    });
	    
	    // 提交表单
	    $("#submit_form_user_btn").click(function(){
	    	$("#form_user").submit();
	    });
	});
	
	var init = function () {
		//1.初始化Table
	    var oTable = new TableInit();
	    oTable.Init();
	    //2.初始化Button的点击事件
	    var oButtonInit = new ButtonInit();
	    oButtonInit.Init();
	};
	
	var TableInit = function () {
	    var oTableInit = new Object();
	    //初始化Table
	    oTableInit.Init = function () {
	    	//debugger;
	        $('#table_user').bootstrapTable({
	            url: 'itemcatList.htm',         //请求后台的URL（*）
	            method: 'post',                      //请求方式（*）
	            contentType : "application/x-www-form-urlencoded",
	            toolbar: '#toolbar',                //工具按钮用哪个容器
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	            pagination: true,                   //是否显示分页（*）
	            sortable: true,                     //是否启用排序
	            sortName: "userid",
	            sortOrder: "desc",                   //排序方式
	            queryParams: oTableInit.queryParams,//传递参数（*）
	            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
	            pageNumber:1,                       //初始化加载第一页，默认第一页
	            pageSize: 10,                       //每页的记录行数（*）
	            pageList: [10, 25, 50, 75, 100],    //可供选择的每页的行数（*）
	            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
	            strictSearch: true,
	            showColumns: true,                  //是否显示所有的列
	            showRefresh: false,                  //是否显示刷新按钮
	            minimumCountColumns: 2,             //最少允许的列数
	            clickToSelect: true,                //是否启用点击选中行
	           // height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	            uniqueId: "userid",                     //每一行的唯一标识，一般为主键列
	            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
	            cardView: false,                    //是否显示详细视图
	            detailView: false,                   //是否显示父子表
	            columns: [{
	                checkbox: true
	            },
	            {
	                field: 'itemcatid',
	                title: '分类编号',
	                sortable:true
	            },
	            {
	                field: 'name',
	                title: '分类名称',
	                sortable:true
	            }, {
	                field: 'parentId',
	                title: '上级分类编号',
	                sortable:true
	            }, {
	                field: 'level',
	                title: '分类级别',
	                formatter:function(value,row,index){
	                	var text;
	                	if(value==1){
	                		text="一级分类";
	                	}
	                	if(value==2){
	                		text="二级分类";
	                	}
	                	if(value==3){
	                		text="三级分类";
	                	}
	                	// 返回什么值就显示什么值；
	                	return text;
	                }
	            }],
	            onClickRow: function (row) {
	            	$("#alertmod_table_user_mod").hide();
	            }
	        });
	    };
	    /*  */
	    //得到查询的参数
	    oTableInit.queryParams = function (params) {
	        var temp = {//这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
	            limit: params.limit,   //页面大小
	            offset: params.offset,  //页码
	            // itemcat模糊搜索时的参数；
	            name: $("#txt_search_username").val(),
	            level: $("#txt_search_roleid").val(),
	            usertype: $("#txt_search_usertype").val(),
	            search:params.search,
	            order: params.order,
	            ordername: params.sort
	        };
	        return temp;
	    };
	    return oTableInit;
	};
	
	var ButtonInit = function () {
	    var oInit = new Object();
	    var postdata = {};

	    oInit.Init = function () {
	        //初始化页面上面的按钮事件
	    	$("#btn_add").click(function(){
	    		$('#password').attr("readOnly",false).val(getSelection.password);
	    		$("#form_user").resetForm();
	    		/* document.getElementById("hidden_txt_userid").value=''; */
	    		
	    		// 弹框
	    		$('#modal_user_edit').modal({backdrop: 'static', keyboard: false});
				$('#modal_user_edit').modal('show');
	        });
	        
	    	$("#btn_edit").click(function(){
	    		// 获取选中的对象；
	    		var getSelections = $('#table_user').bootstrapTable('getSelections');
	    		if(getSelections && getSelections.length==1){
	    			initEditUser(getSelections[0]);
	    			$('#modal_user_edit').modal({backdrop: 'static', keyboard: false});
					$('#modal_user_edit').modal('show');
	    		}else{
	    			$("#select_message").text("请选择其中一条数据");
	    			$("#alertmod_table_user_mod").show();
	    		}
	    		
	        });
	    	
	    	$("#btn_delete").click(function(){
	    		var getSelections = $('#table_user').bootstrapTable('getSelections');
	    		if(getSelections && getSelections.length>0){
	    			$('#modal_user_del').modal({backdrop: 'static', keyboard: false});
	    			$("#modal_user_del").show();
	    		}else{
	    			$("#select_message").text("请选择数据");
	    			$("#alertmod_table_user_mod").show();
	    		}
	        });
	        
	        
	    };

	    return oInit;
	};
	
	$("#alertmod_table_user_mod_a").click(function(){
		$("#alertmod_table_user_mod").hide();
	});
	
	function initEditUser(getSelection){
		// 修改：隐藏域的itemcatid
		 $('#hidden_txt_itemcatid').val(getSelection.itemcatid);
		
		$('#roleid').val(getSelection.roleid);
		$('#username').val(getSelection.username);
		
		$('#level').val(getSelection.level);
		$('#name').val(getSelection.name);
		
		$('#userdescription').val(getSelection.userdescription);
		$('#password').attr("readOnly",true).val(getSelection.password);
		
		// 上级分类是通过改变事件，动态给的；
		getItemcatByLevel(getSelection.level,getSelection.parentId);
		
		
	}
	
	$("#del_user_btn").click(function(){
		// 获取选中对象
		var getSelections = $('#table_user').bootstrapTable('getSelections');
		var idArr = new Array(); // 数组；
		var ids;// 定义一个字符串；
		getSelections.forEach(function(item){ // 遍历你选中的对象数组；
			idArr.push(item.itemcatid);
		});
		ids = idArr.join(","); // 用逗号分割； [1,2,3]
		$.ajax({
		    url:"deleteItemcat.htm",
		    dataType:"json",
		    data:{"ids":ids},
		    type:"post",
		    success:function(res){
		    	if(res.success){
	    			$('#modal_user_del').modal('hide'); // 隐藏弹框；
	    			$("#btn_search").click(); // 重新搜索
	    		}else{
	    			// 弹出错误信息；
	    			$("#select_message").text(res.errorMsg);
	    			$("#alertmod_table_user_mod").show();
	    		}
		    }
		});
	});
	
	// 改变事件查询上级分类
	function getItemcatByLevel(level,parentId){
		// ajax请求
		$.ajax({
		    url:"getItemcatByLevel.htm",
		    dataType:"json",
		    data:{"level":level-1},
		    type:"post",
		    success:function(res){ // 返回值包含：success：true;  List<Itemcat>集合；
		    	if(res.success){
	    			/* $('#modal_user_del').modal('hide');
	    			$("#btn_search").click(); */
	    			//debugger;
	    			// 动态给第二个select拼接option
	    			$("#parentId").empty();
	    			$("#parentId").append("<option value=''>--请选择--</option>");
	    			
	    			
	    			var parents = res.parents;
	    			parents.forEach(function(parent){
	    				// 1获取select的对象，使用append拼接html代码就可以了；
	    				//$("#parentId").append("<option value='1'>动态拼接</option>");
	    				
	    				var op  = "<option value='"+parent.itemcatid+"'>"+parent.name+"</option>";
	    				
	    				// 传递过来的parentId和查询出来的上级分类的itemcatid比较，相同选中；
	    				if(parentId ==parent.itemcatid){
	    					op = "<option selected='selected' value='"+parent.itemcatid+"'>"+parent.name+"</option>";
	    				}
	    				$("#parentId").append(op);
	    			
	    			
	    			});
	    			
	    		}else{
	    			/* $("#select_message").text(res.errorMsg);
	    			$("#alertmod_table_user_mod").show(); */
	    			alert("失败");
	    		}
		    }
		});
	}
	
	// 导出
	$("#btn_exportBus").click(function(){
		$.ajax({
		    url:"exportBus.htm",
		    dataType:"json",		
		    type:"post",
		    success:function(res){ // 返回值包含：success：true;  List<Itemcat>集合；
		    	if(res.success){
	    			alert("导出成功！");	    				    				    			
	    		}else{
	    			alert("导出失败！");
	    		}
		    }
		});
	});
	
	
	// 导入
	$("#btn_importBus").click(importBus);
	function importBus(){
		//debugger;
		$('#modal_log_import').modal({backdrop: 'static', keyboard: false});
		$("#modal_log_import").show();
	}

	
	</script>

</body>
</html>