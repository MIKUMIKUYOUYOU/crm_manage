//填充部门
var initDepartment = function() {
	$.ajax({
		url : "/" + location.pathname.split("/")[1] + "/department",
		type : "POST",
		dataType : "JSON",
		data : 'Method=INIT',
		success : function(departments) {
			var $department=$("#DEPARTMENT");
			for (let department of departments) {
				$department.append("<option value='"+department.DEPARTMENT_ID+"'>"+department.DEPARTMENT_NAME+"</option>");
			}
		},
		error : function() {
			console.log("部门加载失败");
		}
	});
}
initDepartment();

// 填充职位
var initPosition = function() {
	$.ajax({
		url : "/" + location.pathname.split("/")[1] + "/position",
		type : "POST",
		dataType : "JSON",
		data : 'Method=INIT',
		success : function(positions) {
			var $position=$("#POSITION");
			for (let position of positions) {
				$position.append("<option value='"+position.POSITION_ID+"'>"+position.POSITION_NAME+"</option>");
			}
		},
		error : function() {
			console.log("职位加载失败");
		}
	});
}
initPosition();

// 初始化表单验证插件
var initbootstrapValidator = function(fm){
	fm.bootstrapValidator({
		// 默认的提示消息
		message: 'This value is not valid',
		// 表单框里右侧的icon
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			EMPLOYEE_NAME: {
				message: '用户名验证失败',
				validators: {
					notEmpty: {
						message: '用户名不能为空'
					}
				}
			},
			PHONE: {
				message: '手机号验证失败',
				validators: {
					notEmpty: {
						message: '手机号不能为空'
					}, 
					regexp: { 
						// 正则表达式
						regexp: /^1[34578]\d{9}$/,
						message: '请输入正确的手机号'
					}
				}
			},
			DEPARTMENT_ID: {
				message: '部门验证失败',
				validators: {
					notEmpty: {
						message: '部门不能为空'
					},
					callback: {
						message: '必须选择一个部门',
						callback: function(value, validator) {
							if (value == 0) {
								return false;
							} else {
								return true;
							}
						}
					}
				}
			},
			POSITION_ID: {
				message: '职位验证失败',
				validators: {
					notEmpty: {
						message: '职位不能为空'
					},
					callback: {
						message: '必须选择一个职位',
						callback: function(value, validator) {
							if (value == 0) {
								return false;
							} else {
								return true;
							}
						}
					}
				}
			},
			PARENT_EMPLOYEE_ID:{
				message: '上级员工编号验证失败',
				validators: {
					notEmpty: {
						message: '上级员工编号不能为空'
					}, 
					regexp: { 
						// 正则表达式
						regexp: /^\d*$/,
						message: '编号职只能为数字'
					},
					stringLength: {  // 长度限制
			              max: 11,
			              message: '编号过长'
					}
				}
			},
			EMAIL: {
				validators: {
					notEmpty: {
						message: '邮箱地址不能为空'
					},
					emailAddress: {
						message: '邮箱地址格式有误'
					}
				}
			}
		}
	}).on('success.form.bv', function(e) {
		// 阻止表单提交
		e.preventDefault();
		var param=$employeeForm.serialize();
		if($("#method").val().toUpperCase()=="ADD"){
			$.ajax({
				url : "/" + location.pathname.split("/")[1] + "/employee",
				type : "POST",
				data : param,
				success:function(data){
					if(data=='0'){
						// 清空表体
						$("#employeeTable>tbody").empty();
						// 删除页码
						$('.pagination').jqPaginator('destroy');
						// 根据查询条件请求页数
						total();
						// 当前页丢失最后一条数据或大量数据时 如数据库内容被他人删除
						if(totalPage<currPage){
							currPage=totalPage;
						}
						// 重置页码
						if(totalPage>0){
							pageList(totalPage,currPage);
						}else{
							pageList(1,1);
						}
						// 填充表体
						initEmployeeTable($paramForm.serialize(),currPage,pageSize);
						$.modal.msgSuccess("添加成功~");
						// 切换页面 重置验证 显示页码
						back(addEmployee,table);
					}else{
						// 清空表体
						$("#employeeTable>tbody").empty();
						// 填充表体
						initEmployeeTable($paramForm.serialize(),1,pageSize);
						$.modal.alertError("添加失败");
						// 切换页面 重置验证 显示页码
						back(addEmployee,table);
					}
				}	
			});
		}else if($("#method").val().toUpperCase()=="UPDATE"){
			if(!$.common.isEmpty(updateEMPNAME)&&!$.common.isEmpty(updateEMPID)){
				$.ajax({
					url : "/" + location.pathname.split("/")[1] + "/employee",
					type : "POST",
					data : param+"&updateEMPNAME="+updateEMPNAME+"&updateEMPID="+updateEMPID,
					success:function(data){
						if(data=='0'){
							// 清空表体
							$("#employeeTable>tbody").empty();
							// 删除页码
							$('.pagination').jqPaginator('destroy');
							// 根据查询条件请求页数
							total();
							// 当前页丢失最后一条数据或大量数据时
							if(totalPage<currPage){
								currPage=totalPage;
							}
							// 重置页码
							if(totalPage>0){
								pageList(totalPage,currPage);
							}else{
								pageList(1,1);
							}
							// 填充表体
							initEmployeeTable($paramForm.serialize(),currPage,pageSize);
							$.modal.msgSuccess("修改成功~");
							// 切换页面 重置验证 显示页码
							back(addEmployee,table);
						}else{
							// 清空表体
							$("#employeeTable>tbody").empty();
							// 填充表体 失败后回到第一页
							initEmployeeTable($paramForm.serialize(),1,pageSize);
							$.modal.alertError("修改失败");
							// 切换页面 重置验证 显示页码
							back(addEmployee,table);
						}
					}	
				});
			}
			// 清空NAME和ID 防止意外bug发生
			// 如:先点击修改再点击添加然后被恶意篡改(method)隐藏域中的值 这两个值不清空将会被保留
			updateEMPNAME="";
			updateEMPID="";
		}
	}); 
}
initbootstrapValidator($employeeForm);

// 刷新清空表单
$employeeForm[0].reset();

// 用于添加和修改的页面跳转
var addAndUpdate=function(hidePageNode,showPageNode,method,btnNode){
	pageToggle(hidePageNode,showPageNode);
	$pageList.css("display","none");
	if(method=="add"){
		$("#passwordBlock").css("display","block");
		$("#msg").text("添加员工");
		$("#mysubmit").text("添加");
		$("#method").val("ADD");
		// 隐藏input域用于修改页面的任务
	}
	if(method=="update"){
		$("#passwordBlock").css("display","none");
		$("#msg").text("修改员工");
		$("#mysubmit").text("修改");
		$("#method").val("UPDATE");
		// 获取当前按钮 然后根据相对位置获取ID的位置内容
		var EMPLOYEE_ID=btnNode.parentNode.parentNode.childNodes[0].textContent;
		// 将ID input框的值回填为获取的ID
		$("#EMPLOYEE_ID").val(EMPLOYEE_ID);
		$.ajax({
			url : "/" + location.pathname.split("/")[1] + "/employee",
			type : "POST",
			dataType : "JSON",
			data : 'Method=QE&EMPLOYEE_ID='+EMPLOYEE_ID,
			success:function(employee){
				if(employee=='1'){
					$.modal.msgError("亲发生错误了哦~");
					return;
				}
				if(employee=='2'){
					$.modal.msgError("请输入数字哦~");
					return;
				}
				if(employee=='3'){
					$.modal.msgError("没查到用户哦~");
					return;
				}
				$("#EMPLOYEE_NAME").val(employee.EMPLOYEE_NAME);
				$("#DEPARTMENT").val(employee.DEPARTMENT_ID);
				$("#POSITION").val(employee.POSITION_ID);
				$("#PHONE").val(employee.PHONE);
				$("#EMAIL").val(employee.EMAIL);
				$("#PARENT_EMPLOYEE").val(employee.PARENT_EMPLOYEE_ID);
				// 定义回填时候的name和id 根据回填数据查找数据库更新
				updateEMPNAME=employee.EMPLOYEE_NAME;
				updateEMPID=employee.EMPLOYEE_ID;
			}
		});
	}
}
