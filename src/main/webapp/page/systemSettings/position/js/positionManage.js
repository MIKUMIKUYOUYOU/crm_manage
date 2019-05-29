
//创建空的更新动作(职位名)
let updatePOSITION_NAME = "";

//表单验证插件
let initbootstrapValidator = function (fm) {
    fm.bootstrapValidator(
        {
            // 默认的提示消息
            message: 'This value is not valid',
            // 表单框里右侧的icon
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                POSITION_LEVEL: {
                    message: '职位等级失败',
                    validators: {
                        notEmpty: {
                            message: '职位等级不能为空'
                        },
                        callback: {
                            message: '必须选择一个职位等级',
                            callback: function (value) {
                                return value !== '0'
                            }
                        }
                    }
                },
                POSITION_NAME: {
                    validators: {
                        notEmpty: {
                            message: '职位名不能为空'
                        },
                        remote: {
                            url: "/" + location.pathname.split("/")[1]
                                + "/position",// 验证地址
                            message: '用户已存在',// 提示消息
                            delay: 500,// 每输入一个字符，就发ajax请求，服务器压力还是太大，设置0.5秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                            type: 'POST',// 请求方式
                            data: function () {
                                return {
                                    Method: "SAME",
                                    updateParam: updatePOSITION_NAME
                                };
                            }
                        }
                    }
                }
            }
        }).on(
        'success.form.bv',
        function (e) {
            // 阻止表单提交
            e.preventDefault();
            let param = $positionFrom.serialize();
            let method=$("#method").val();
            if (method.toUpperCase() === "ADD") {
                $.ajax({
                    url: "/" + location.pathname.split("/")[1]
                        + "/position",
                    type: "POST",
                    data: param,
                    success: function (data) {
                        if (data === '0') {
                            // 清空表体
                            $("#positionTable>tbody").empty();
                            // 删除页码
                            $('.pagination').jqPaginator('destroy');
                            // 根据查询条件请求页数
                            total();
                            // 当前页丢失最后一条数据或大量数据时
                            // 解决了数据库突然丢失数据分页错误的问题
                            if (totalPage < currPage) {
                                currPage = totalPage;
                            }
                            // 重置页码
                            if (totalPage > 0) {
                                pageList(totalPage, currPage);
                            } else {
                                pageList(1, 1);
                            }
                            // 填充表体
                            initPositionTable($paramForm.serialize(), currPage, pageSize);
                            $.modal.msgSuccess("添加成功~");
                            // 切换页面 重置验证 显示页码
                            back(addPosition, table);
                        } else {
                            // 清空表体
                            $("#employeeTable>tbody").empty();
                            // 填充表体
                            initEmployeeTable($paramForm.serialize(), 1, pageSize);
                            $.modal.alertError("添加失败");
                            // 切换页面 重置验证 显示页码
                            back(addPosition, table);
                        }
                    }
                });
            } else if (method.toUpperCase() === "UPDATE") {
                $.ajax({
                    url: "/" + location.pathname.split("/")[1]
                        + "/position",
                    type: "POST",
                    data: param,
                    success: function (data) {
                        if (data === '0') {
                            // 清空表体
                            $("#positionTable>tbody").empty();
                            // 删除页码
                            $('.pagination').jqPaginator('destroy');
                            // 根据查询条件请求页数
                            total();
                            // 当前页丢失最后一条数据或大量数据时
                            if (totalPage < currPage) {
                                currPage = totalPage;
                            }
                            // 重置页码
                            if (totalPage > 0) {
                                pageList(totalPage, currPage);
                            } else {
                                pageList(1, 1);
                            }
                            // 填充表体
                            initPositionTable($paramForm.serialize(), currPage, pageSize);
                            $.modal.msgSuccess("更新成功~");
                            // 切换页面 重置验证 显示页码
                            back(addPosition, table);
                        } else {
                            // 清空表体
                            $("#employeeTable>tbody").empty();
                            // 填充表体
                            initEmployeeTable($paramForm.serialize(), 1, pageSize);
                            $.modal.alertError("更新失败");
                            // 切换页面 重置验证 显示页码
                            back(addPosition, table);
                        }
                    }
                });
                // 清除更新动作标 更新结束后 防止恶意操作
                updatePOSITION_NAME = "";
            }
        });
};

// 初始化验证
initbootstrapValidator($positionFrom);

// 刷新清空表单
$positionFrom[0].reset();

// 添加或更新
let addAndUpdate = function (hidePageNode, showPageNode, method, btnNode) {
    $pageList.css("display", "none");
    // 清除更新动作标
    updatePOSITION_NAME = "";
    pageToggle(hidePageNode, showPageNode);
    if (method === "add") {
        $("#msg").text("添加职位");
        $("#mysubmit").text("创建");
        // 隐藏input域用于修改页面的任务
        $("#method").val("ADD");
    }
    if (method === "update") {
        $("#msg").text("修改职位");
        $("#mysubmit").text("修改");
        // 隐藏input域用于修改页面的任务
        $("#method").val("UPDATE");
        let trChildNode = btnNode.parentNode.parentNode.parentNode.childNodes;
        $("#POSITION_ID").val(trChildNode[0].textContent);
        $("#POSITION_LEVEL").val(trChildNode[1].textContent);
        let position_name=trChildNode[2].textContent;
        $("#POSITION_NAME").val(position_name);
        updatePOSITION_NAME = position_name;
    }
};

let $referencepositionId = $("#referencepositionId");

// 关联菜单按钮点击方法
function showselectmodel(btn) {
    $("#myModal").modal("show");
    $referencepositionId.val(btn.parentNode.parentNode.parentNode.childNodes[0].textContent);
    // ztree配置项
    let options = {
        treeName: "tree",
        checkFlag: true,
        param: "Method=ZTREE&POSITION_ID=" + $referencepositionId.val(),
        url: "/" + location.pathname.split("/")[1] + "/position",
        idKey: "MENU_ID", // 自身id
        pIdKey: "PARENT_MENU_ID", // 父id
        name: "MENU_NAME",
        // click: zOnclick
    };
    $.trees.init(options);
}

function updateEPositionWithMenu() {
    let checkedNodes = $.trees.myTree.getNodesByParam("checked", true);
    let param = "";
    for (let index=0;index<checkedNodes.length;index++){
        param = param + "MENU_ID:" +checkedNodes[index].MENU_ID+",";
    }
    $.ajax({
        url: "/" + location.pathname.split("/")[1] + "/positionMenuRelations",
        type: "POST",
        data: "linkData=" + param.substring(0, param.length - 1) + "&POSITION_ID=" + $referencepositionId.val(),
        success(data) {
            if (data === '0') {
                $.modal.msgSuccess("修改成功~");
                $("#myModal").modal("hide");
                // 刷新menu菜单
                $("#menu").load("/" + location.pathname.split("/")[1] + "/menu.html");
            }
            if (data === '1') {
                $.modal.msgError("修改失败！！！");
            }
        }
    });
}

// function zOnclick(event, treeName, treeNode) {
// // $("input[name='deptId']").val();
// // $("input[name='deptName']").val();
// vm.user.dept.deptId = treeNode.deptId;
// vm.user.dept.deptName = treeNode.deptName;
// setTimeout(function () {
// $.table.refresh();
// },5);
// }

