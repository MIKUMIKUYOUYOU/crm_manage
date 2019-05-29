//页数跨度（每页的数量）
let pageSize=5;
//总页
let totalPage=0;
//设置当前页数
let currPage=0;
//获取一次$employeeForm的jq对象多次引用 加强性能 employeeManage.js依赖于此jq对象
//此jq对象用于控制表单如添加、修改
let $menuForm=$("#menuForm");
//分页的div节点
let $pageList=$("#pageList");
//查询调教表单jq对象 多次访问此对象 提出提升效率
let $paramForm=$("#paramForm");

// 填充table的方法
let initMenuTable=function(param,page,pageSize){
	$.ajax({
		url : "/" + location.pathname.split("/")[1] + "/menu",
		type : "POST",
		dataType : "JSON",
		data : 'Method=INITPAGE&'+param+"&page="+page+"&pageSize="+pageSize,
		success : function(menus) {
			if(menus === 1){
				$.modal.alertWarning("亲出错了哦");
				return;
			}
			for (let index=0;index<menus.length;index++){
				let menu=menus[index];
				let $tr=$("<tr data-key='1'></tr>");
				$tr.append("<td>"+menu.MENU_ID+"</td>");
				$tr.append("<td>"+menu.MENU_NAME+"</td>");
				$tr.append("<td>"+menu.MENU_URL+"</td>");
				if(typeof(menu.PICTURES) != "undefined"){
					$tr.append("<td>"+menu.PICTURES+"</td>");
				}else{
					$tr.append("<td>无图标</td>");
				}
				let CREATE_TIME=new Date(menu.CREATE_TIME).format("yyyy-MM-dd hh:mm:ss");
				let UPDATE_TIME=new Date(menu.UPDATE_TIME).format("yyyy-MM-dd hh:mm:ss");
				$tr.append("<td>"+CREATE_TIME+"</td>");
				$tr.append("<td>"+UPDATE_TIME+"</td>");
				if(typeof(menu.PARENT_NAME) != "undefined"){
					$tr.append("<td>"+menu.PARENT_NAME+"</td>");
				}else{
					$tr.append("<td>无上级菜单</td>");
				}
				$tr.append('<td><button type="button" id="updatebtn" name="updatebtn" class="btn btn-warning btn-sm" onclick="updateMenu(2)">修改</button></td>');
				$("#menuTable>tbody").append($tr);
			}
			/* $.modal.msgSuccess("加载成功!"); */
		},
		error : function() {
			$.modal.alertError("失败");
		}
	});
};

// 第一次加载-用于初始化table
initMenuTable($paramForm.serialize(),1,pageSize);

// 给搜索按钮添加点击事件-搜索
$("#queryBtn").click(function(){
	// 清空表体
	$("#menuTable>tbody").empty();
	// 填充表体
	initMenuTable($paramForm.serialize(),1,pageSize);
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

//用于切换div的方法
let pageToggle= function(hidePageNode,showPageNode){
	$(hidePageNode).css("display","none");
	$(showPageNode).css("display","block");
};

//请求页数的方法
let total=function(){
	if(!$.common.isEmpty($("#menuName").val())){
		$.ajax({
			url : "/" + location.pathname.split("/")[1] + "/count",
			type : "POST",
			data : 'Method=MenuPageWK&'+$paramForm.serialize(),
			async:false,
			success : function(total) {
				totalPage=parseInt(((parseInt(total) + pageSize -1) / pageSize)+"");
			}
		});
	}else{
		$.ajax({
			url : "/" + location.pathname.split("/")[1] + "/count",
			type : "POST",
			data : 'Method=MenuPage',
			async:false,
			success : function(total) {
				totalPage=parseInt(((parseInt(total) + pageSize -1) / pageSize)+"");
			}
		});
	}
};

// 用于返回的方法
let back=function(hidePageNode,showPageNode){
	// 显示页码
	$pageList.css("display","block");
	// 切换页面
	pageToggle(hidePageNode,showPageNode);
	// 重置验证
	/*$employeeForm.data('bootstrapValidator').destroy();
	$employeeForm.data('bootstrapValidator',null);
	initbootstrapValidator($employeeForm);
	$employeeForm[0].reset();*/
};

let pageList=function(allPage){
	let if_fistime = true;
	$(".pagination").jqPaginator({
		// 总页数（pageBean）
		totalPages: allPage,
		// 可见的页数（前段页面可写死）
		visiblePages: 5,
		// 当前页（pageBean）
		currentPage: 1,
		first: '<li class="first"><a href="javascript:void(0);">第一页</a></li>',
		prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
		next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
		last: '<li class="last"><a href="javascript:void(0);">最后一页</a></li>',
		page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
		onPageChange: function(page) {
			currPage=page;
			if(if_fistime) {
				if_fistime = false;
			} else {
				$("#menuTable>tbody").empty();
				initMenuTable($paramForm.serialize(),page,pageSize);
			}
		}
	});
};
//初始化totalPage
total();
//初始化分页 内部自带第一页 因为不带有创建和修改menu的方法 不提供修改当前页的参数
pageList(totalPage);