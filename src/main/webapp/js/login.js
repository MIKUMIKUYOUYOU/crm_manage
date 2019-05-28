function login() {
	var employeeId = $("#employeeId").val();
	var password = $("#password").val();
	if ("" == employeeId || undefined == employeeId) {
		$.modal.alertWarning("工号不能为空");
		return;
	}
	if ("" == password || undefined == password) {
		$.modal.alertWarning("密码不能为空");
		return;
	}
	$.ajax({
		type : 'POST',
		url : 'login',
		data : "Method=LOGIN&EMPLOYEE_ID=" + employeeId + "&EMP_PASSWORD="+ password,
		cache : false,
		success : function(msg) {
			if (0 == msg) {
				window.location.href = "/" + location.pathname.split("/")[1];
				return;
			}
			if (1 == msg) {
				$.modal.alertWarning("工号或密码错误");
				return;
			}
			if (2 == msg) {
				window.location.href = "updatepassword.html";
				return;
			}
		},
		error : function() {
			$.modal.alertError("请求失败!");
		}
	});
}

function loginOut(){
	$.ajax({
		type : 'POST',
		url : 'login',
		data : "Method=LOGOUT",
		cache : false,
		success : function(msg) {
			if(msg==0){
				$.modal.msgSuccess("登出成功");
				window.setTimeout(function(){
					window.location.href = "/" + location.pathname.split("/")[1]+"/login.html";
				},500);
			}
		},
		error : function() {
			$.modal.alertError("请求失败!");
		}
	});
}