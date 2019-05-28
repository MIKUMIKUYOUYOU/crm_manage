
function updatepassword() {
	//UPDATE log_in SET EMP_PASSWORD="123456" WHERE EMPLOYEE_ID="1" AND EMP_PASSWORD="12345"
	var EMPLOYEE_ID=$("#employeeId").val();
	var OLD_EMP_PASSWORD=$("#oldpassword").val();
	var Re_EMP_PASSWORD=$("#newpassword").val();
	var EMP_PASSWORD=$("#password").val();
	if ("" == EMPLOYEE_ID || undefined == EMPLOYEE_ID) {
		$.modal.alertWarning("工号不能为空");
		return;
	}
	if ("" == OLD_EMP_PASSWORD || undefined == OLD_EMP_PASSWORD) {
		$.modal.alertWarning("原密码不能为空");
		return;
	}
	if ("" == Re_EMP_PASSWORD || undefined == Re_EMP_PASSWORD) {
		$.modal.alertWarning("新密码不能为空");
		return;
	}
	if ("" == EMP_PASSWORD || undefined == EMP_PASSWORD) {
		$.modal.alertWarning("确认密码不能为空");
		return;
	}
	if(Re_EMP_PASSWORD!=EMP_PASSWORD){
		$.modal.alertWarning("新密码和确认密码不一致");
		return;
	}
	$.ajax({
		type : 'POST',
		url : 'login',
		data : "Method=UPDATE&EMPLOYEE_ID=" + EMPLOYEE_ID + "&OLD_EMP_PASSWORD=" + OLD_EMP_PASSWORD+"&EMP_PASSWORD="+EMP_PASSWORD,
		cache : false,
		success : function(msg) {
			if (0 == msg) {
				$.modal.alertSuccess("修改成功！");
				window.setTimeout(function () {
					window.location.href="login.html";
			    },1000);
				return;
			}
			if (1 == msg) {
				$.modal.alertWarning("修改失败！");
				return;
			}
		},
		error : function() {
			alert("请求失败!");
		}
	});
}