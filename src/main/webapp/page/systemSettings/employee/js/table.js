//页数跨度（每页的数量）
let pageSize=5;
//总页
let totalPage=0;
//设置当前页数
let currPage=0;
//获取一次$employeeForm的jq对象多次引用 加强性能 employeeManage.js依赖于此jq对象
//此jq对象用于控制表单如添加、修改
let $employeeForm=$("#employeeForm");
//分页的div节点
let $pageList=$("#pageList");
//查询表单jq对象 多次访问此对象 提出提升效率
let $paramForm=$("#paramForm");

// 填充table的方法
let initEmployeeTable=function(param,page,pageSize){
	$.ajax({
		url : "/" + location.pathname.split("/")[1] + "/employee",
		type : "POST",
		dataType : "JSON",
		data : 'Method=INIT&'+param+"&page="+page+"&pageSize="+pageSize,
		success : function(emmployees) {
			if(emmployees===1){
				$.modal.alertWarning("亲出错了哦");
				return;
			}
			if(emmployees===2){
				$.modal.alertWarning("请输入正确的数字");
				return;
			}
			for(let index=0;index<emmployees.length;index++){
				let emmployee=emmployees[index];
				let tr=$("<tr data-key='1'></tr>");
				tr.append("<td>"+emmployee.EMPLOYEE_ID+"</td>");
				tr.append("<td>"+emmployee.EMPLOYEE_NAME+"</td>");
				tr.append("<td>"+emmployee.DEPARTMENT_NAME+"</td>");
				tr.append("<td>"+emmployee.POSITION_NAME+"</td>");
				tr.append("<td>"+emmployee.PHONE+"</td>");
				tr.append("<td>"+emmployee.EMAIL+"</td>");
				let status=emmployee.STATUS === '1';
				if (status) {
					tr.append("<td><span class='badge badge-success'>正常</span></td>");
				}else{
					tr.append("<td><span class='badge badge-danger'>停用</span></td>");
				}
				let CREATE_TIME=new Date(emmployee.CREATE_TIME).format("yyyy-MM-dd hh:mm:ss");
				let UPDATE_TIME=new Date(emmployee.UPDATE_TIME).format("yyyy-MM-dd hh:mm:ss");
				tr.append("<td>"+CREATE_TIME+"</td>");
				tr.append("<td>"+UPDATE_TIME+"</td>");
				if(status){
					let btntd=$("<td></td>");
					btntd.append('<button type="button" name="forbidbtn" class="btn btn-danger btn-sm" onclick="updateEmployeeStatus('+emmployee.EMPLOYEE_ID+','+emmployee.STATUS+',this)">禁用账户</button>');
					btntd.append('<button type="button" name="updatebtn" class="btn btn-info btn-sm" onclick="addAndUpdate(table,addEmployee,\'update\',this)">修改用户信息</button>');
					tr.append(btntd);
				}else{
					let btntd=$("<td></td>");
					btntd.append('<button type="button" name="forbidbtn" class="btn btn-success btn-sm" onclick="updateEmployeeStatus('+emmployee.EMPLOYEE_ID+','+emmployee.STATUS+',this)">恢复正常</button>');
					tr.append(btntd);
				}
				$("#employeeTable>tbody").append(tr);
			}

			/* $.modal.msgSuccess("加载成功!"); */
		},
		error : function() {
			$.modal.alertError("失败");
		}
	});
};

// 第一次加载-用于初始化table
initEmployeeTable($paramForm.serialize(),1,pageSize);

// 给搜索按钮添加点击事件-搜索
$("#queryBtn").click(function(){
	// 清空表体
	$("#employeeTable>tbody").empty();
	// 填充表体
	initEmployeeTable($paramForm.serialize(),1,pageSize);
	// 删除页码
	$('.pagination').jqPaginator('destroy');
	// 根据查询条件请求页数
	total();
	// 重置页码
	if(totalPage>0){
		pageList(totalPage);
	}else{
		pageList(1);
	}
});

// 更改员工状态的方法 被应用于按钮的点击事件
let updateEmployeeStatus = function (employeeId,status,btnNode) {
	$.ajax({
        url : "/" + location.pathname.split("/")[1] + "/employee",
        type : "POST",
        data : 'Method=UPDATEStatus&EMPLOYEE_ID='+employeeId+'&STATUS='+status,
        success:function(data){
        	if(data==='0'){
        		//状态为number类型
        		let flag=status === 1;
				let $btnParent=$(btnNode.parentNode);
				let statusNode=$(btnNode.parentNode.parentNode).children()[6];
				let update_timeNode=$(btnNode.parentNode.parentNode).children()[8];
				let UPDATE_TIME=new Date().format("yyyy-MM-dd hh:mm:ss");
	        	$btnParent.empty();
	        	if(flag){
	        		update_timeNode.innerHTML=UPDATE_TIME;
	        		statusNode.innerHTML="<td><span class='badge badge-danger'>停用</span></td>";
	        		$btnParent.append('<button type="button" name="forbidbtn" class="btn btn-success btn-sm" onclick="updateEmployeeStatus('+employeeId+',0,this)">恢复正常</button>');
	        	}else{
	        		update_timeNode.innerHTML=UPDATE_TIME;
	        		statusNode.innerHTML="<td><span class='badge badge-success'>正常</span></td>";
	        		$btnParent.append('<button type="button" name="forbidbtn" class="btn btn-danger btn-sm" onclick="updateEmployeeStatus('+employeeId+',1,this)">禁用账户</button>');
	        		$btnParent.append('<button type="button" name="updatebtn" class="btn btn-info btn-sm" onclick="addAndUpdate(table,addEmployee,\'update\',this)">修改用户信息</button>');
	        	}
        	}else{
        		$.modal.alertWarning("亲出错了哦");
        	}
        	
        },
        error:function(){
        	$.modal.alertError("失败");
        }
    });
};

// 用于切换div的方法
let pageToggle= function(hidePageNode,showPageNode){
	$(hidePageNode).css("display","none");
	$(showPageNode).css("display","block");
};

// 用于返回的方法 用于 显示页码 切换div界面 重置添加或更新的表单验证 
let back=function(hidePageNode,showPageNode){
	// 显示页码
	$pageList.css("display","block");
	// 切换页面
	pageToggle(hidePageNode,showPageNode);
	// 重置验证
	$employeeForm.data('bootstrapValidator').destroy();
	$employeeForm.data('bootstrapValidator',null);
	initbootstrapValidator($employeeForm);
	$employeeForm[0].reset();
};

// 请求页数的方法 (同步的ajax操作)
let total=function(){
	if(!$.common.isEmpty($("#emmployeeId").val())||!$.common.isEmpty($("#emmployeename").val())){
		$.ajax({
			url : "/" + location.pathname.split("/")[1] + "/count",
			type : "POST",
			data : 'Method=EmpPageWK&'+$paramForm.serialize(),
			async:false,
			success : function(total) {
				totalPage=parseInt(((parseInt(total) + pageSize -1) / pageSize)+"");
			}
		});
	}else{
		$.ajax({
			url : "/" + location.pathname.split("/")[1] + "/count",
			type : "POST",
			data : 'Method=EmpPage',
			async:false,
			success : function(total) {
				totalPage=parseInt(((parseInt(total) + pageSize -1) / pageSize)+"");
			}
		});
	}
};

// 分页方法
let pageList=function(allPage,currpage){
	let if_fistime = true;
	$(".pagination").jqPaginator({
		// 总页数（pageBean）
		totalPages: allPage,
		// 可见的页数（前段页面可写死）
		visiblePages: 5,
		// 当前页（pageBean）
		currentPage: currpage,
		first: '<li class="first"><a href="javascript:void(0);">第一页</a></li>',
		prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
		next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
		last: '<li class="last"><a href="javascript:void(0);">最后一页</a></li>',
		page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
		onPageChange: function(page) {
			//每次触发变更currPage(当前页)
			currPage=page;
			if(if_fistime) {
				if_fistime = false;
			} else {
				//点击页数触发重置table
				$("#employeeTable>tbody").empty();
				initEmployeeTable($paramForm.serialize(),page,pageSize);
			}
		}
	});
};
//调用请求页数 初始化totalPage
total();
//调用分页方法 默认第一页
pageList(totalPage,1);
