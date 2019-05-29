//页数跨度（每页的数量）
let pageSize=3;
//总页
let totalPage=0;
//设置当前页数
let currPage=0;
//form的jq对象 （暂定）
let $positionFrom=$("#positionFrom");
//分页的div节点
let $pageList=$("#pageList");
//查询调教表单jq对象 多次访问此对象 提出提升效率
let $paramForm=$("#paramForm");

//填充table的方法
let initPositionTable=function(param,page,pageSize){
	$.ajax({
		url : "/" + location.pathname.split("/")[1] + "/position",
		type : "POST",
		dataType : "JSON",
		data : 'Method=INITPAGE&'+param+"&page="+page+"&pageSize="+pageSize,
		success : function(positions) {
			if(positions===1){
				$.modal.alertWarning("亲出错了哦");
				return;
			}
			//for普通循环效率最快
			for (let index=0;index<positions.length;index++){
				let position =positions[index];
				let $tr=$("<tr></tr>");
				$tr.append("<td>"+position.POSITION_ID+"</td>");
				$tr.append("<td>"+position.POSITION_LEVEL+"</td>");
				$tr.append("<td>"+position.POSITION_NAME+"</td>");
				let CREATE_TIME=new Date(position.CREATE_TIME).format("yyyy-MM-dd hh:mm:ss");
				let UPDATE_TIME=new Date(position.UPDATE_TIME).format("yyyy-MM-dd hh:mm:ss");
				$tr.append("<td>"+CREATE_TIME+"</td>");
				$tr.append("<td>"+UPDATE_TIME+"</td>");
				let $div=$("<div class='col-lg-20'></div>");
				let $updateBtn=$('<button type="button" id="updateBtn" name="updatebtn" class="btn btn-warning btn-sm" onclick="addAndUpdate(table,addPosition,\'update\',this)">修改</button>');
				let $linkBtn=$('<button type="button" id="linkbtn" name="linkbtn" class="btn btn-success btn-sm" onclick="showselectmodel(this)">关联菜单</button>');
				$div.append($updateBtn,$linkBtn);
				$tr.append($("<td></td>").append($div));

				$("#positionTable>tbody").append($tr);
			}
			/* $.modal.msgSuccess("加载成功!"); */
		},
		error : function() {
			$.modal.alertError("失败");
		}
	});
};

//第一次加载-用于初始化table
initPositionTable($paramForm.serialize(),1,pageSize);

//给搜索按钮添加点击事件-搜索
$("#queryBtn").click(function(){
	// 清空表体
	$("#positionTable>tbody").empty();
	// 填充表体
	initPositionTable($paramForm.serialize(),1,pageSize);
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

let pageToggle= function(hidePageNode,showPageNode){
	$(hidePageNode).css("display","none");
	$(showPageNode).css("display","block");
};
//用于返回的方法
let back=function(hidePageNode,showPageNode){
	// 显示页码
	$pageList.css("display","block");
	// 切换页面
	pageToggle(hidePageNode,showPageNode);
	// 重置验证
	$positionFrom.data('bootstrapValidator').destroy();
	$positionFrom.data('bootstrapValidator',null);
	initbootstrapValidator($positionFrom);
	$positionFrom[0].reset();
	
};

//请求页数的方法
let total=function(){
	if(!$.common.isEmpty($("#positionname").val())){
		$.ajax({
			url : "/" + location.pathname.split("/")[1] + "/count",
			type : "POST",
			data : 'Method=PosPageWK&'+$paramForm.serialize(),
			async:false,
			success : function(total) {
				totalPage=parseInt(((parseInt(total) + pageSize -1) / pageSize)+"");
			}
		});
	}else{
		$.ajax({
			url : "/" + location.pathname.split("/")[1] + "/count",
			type : "POST",
			data : 'Method=PosPage',
			async:false,
			success : function(total) {
				totalPage=parseInt(((parseInt(total) + pageSize -1) / pageSize)+"");
			}
		});
	}
};


//分页方法
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
			currPage=page;
			if(if_fistime) {
				if_fistime = false;
			} else {
				$("#positionTable>tbody").empty();
				initPositionTable($paramForm.serialize(),page,pageSize);
			}
		}
	});
};
//调用请求页数 初始化totalPage
total();
//调用分页方法 默认第一页
pageList(totalPage,1);