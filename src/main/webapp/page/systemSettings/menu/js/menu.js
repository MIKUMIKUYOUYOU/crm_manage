//
////表单验证插件
//var initbootstrapValidator = function(fm) {
//	fm.bootstrapValidator({
//		// 默认的提示消息
//		message : 'This value is not valid',
//		// 表单框里右侧的icon
//		feedbackIcons : {
//			valid : 'glyphicon glyphicon-ok',
//			invalid : 'glyphicon glyphicon-remove',
//			validating : 'glyphicon glyphicon-refresh'
//		},
//		fields : {
//			MENU_NAME : {
//				message : '菜单名验证失败',
//				validators : {
//					notEmpty : {
//						message : '菜单名不能为空'
//					}
//				}
//			}
//		}
//	}).on('success.form.bv', function(e) {
//		// 阻止表单提交
//		e.preventDefault();
//		var param = $menuFrom.serialize();
//		if ($("#method").val().toUpperCase() == "ADD") {
//			
//		} else if ($("#method").val().toUpperCase() == "UPDATE") {
//			
//		}
//	});
//}
//
////初始化验证
//initbootstrapValidator($menuForm);
//
//// 添加或更新
//var addAndUpdate = function(hidePageNode, showPageNode, method, btnNode) {
//	$pageList.css("display", "none");
//	pageToggle(hidePageNode, showPageNode);
//	if (method == "add") {
//	/*	$("#msg").text("添加职位");
//		$("#mysubmit").text("创建");
//		// 隐藏input域用于修改页面的任务
//		$("#method").val("ADD");*/
//	}
//	if (method == "update") {
//		/*$("#msg").text("修改职位");
//		$("#mysubmit").text("修改");
//		// 隐藏input域用于修改页面的任务
//		$("#method").val("UPDATE");*/
//	}
//}