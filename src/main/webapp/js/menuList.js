
//根据登录用户初始化菜单
var initMenuList=function(){
	$.ajax({
		url : "/" + location.pathname.split("/")[1] + "/menu",
		type:"POST",
		data : 'Method=INITMENU',
		dataType : "JSON",
		success(menuList){
			if(menuList!=""){
				$("#userNameText").text(menuList[0].userName);
				var treeData=$.common.setTreeData(menuList,"MENU_ID","PARENT_MENU_ID","children");
				var $menuList=$("#menuList");
				for (let menu of treeData) {
					if(typeof(menu.children)=="undefined"){
						var $li=$("<li class=''></li>");
						var $a=$("<a class='' href='"+menu.MENU_URL+"'></a>");
						$a.append('<span aria-hidden="true" class="'+menu.PICTURES+'"></span>'+menu.MENU_NAME);
						$li.append($a);
						$menuList.append($li);
					}else{
						var $li=$("<li class=''></li>");
						var $a=$("<a class='' href='"+menu.MENU_URL+"' data-toggle='dropdown' data-hover='dropdown'></a>");
						$a.append('<span aria-hidden="true" class="'+menu.PICTURES+'"></span>'+menu.MENU_NAME);
						$a.append('<b class="caret"></b>');
						$li.append($a);
						var $ul=$('<ul class="dropdown-menu"></ul>')
						for (let childMenu of menu.children) {
							$childLi=$("<li></li>");
							$childLi.append('<a href="'+childMenu.MENU_URL+'" class="">'+childMenu.MENU_NAME+'</a>');
							$ul.append($childLi);
						}
						$li.append($ul);
						$menuList.append($li);
					}
				}
			}
		},
		error(){
			$.modal.alertError("请求失败");
		}
	});
}
initMenuList();