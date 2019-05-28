(function ($) {
    $.extend({
        modal: {
            layerOpen: function (title, content, width, height, method) {
                layer.open({
                    type: 1,
                    title: title,
                    maxmin: true,
                    area: [width + 'px', height + 'px'],
                    skin: 'layui-layer-molv', // 样式类名
                    closeBtn: 1, // 不显示关闭按钮
                    anim: 2,
                    shadeClose: true, // 开启遮罩关闭
                    content: content,
                    btn: ['确认', '取消'],
                    yes: method
                });
            },
            layerConfirm: function (message, method) {
                layer.confirm(message, {
                    title: "提示",
                    icon: 3,
                    skin: 'layui-layer-molv',
                    btn: ['确认', '取消'],
                    // btnClass: ["btn btn-primary", "btn btn-danger"]
                }, function (index) {
                    layer.close(index);
                    method();
                });
            },
            alert({content="", icon=1}={}) {
                return layer.alert(content, {
                    title: "系统提示",
                    icon: icon,
                    skin: 'layui-layer-molv',
                    btn: ['确认'],
                });
            },
            msg({content="", icon=1, shade=0.3, time=300}={}) {
                layer.alert(content, {
                    time: time,
                    icon: icon,
                    shade:shade,
                    anim: 5
                });
            }
            ,
            alertWarning(content) {
                this.alert({content:content,icon:3});
            },
            alertError(content) {
                this.alert({content:content,icon:2});
            },
            alertSuccess(content) {
                this.alert({content:content,icon:1});
            },
            msgWarning(content) {
                this.msg({content:content,icon:3});
            },
            msgError(content) {
                this.msg({content:content,icon:2});
            },
            msgSuccess(content) {
                this.msg({content:content,icon:1});
            },
            loading(content) {
                return this.msg({content:content,icon:16,time:false})
            },
            closeLoading(layerLoadingMsg) {
                layer.close(layerLoadingMsg);
            }
        },
        common: {
            /* tree数据的转换 id pid==> id children */
            setTreeData(source, id, parentId, children) {
                let cloneData = JSON.parse(JSON.stringify(source));
                let tree = cloneData.filter(father => {
                    let branchArr = cloneData.filter(child => {
                        return father[id] == child[parentId]
                    });
                    if (branchArr.length > 0) {
                        father[children] = branchArr
                    }
                    return father[parentId] == 0    // 如果第一层不是parentId=0，请自行修改
                });
                return tree;
            },
            isEmpty: function (value) {
                if (value == null || this.trim(value) == "" || value == undefined) {
                    return true;
                }
                return false;
            },
            trim: function (value) {
                if (value == "")
                    return "";
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            }
        },
        trees: {
            myTree: {},
            init({
                     selectedMulti = false,
                     idKey = "id",
                     pIdKey = "pId",
                     rootPId = "0",
                     name = "name",
                     checkFlag = false,
                     chkStyle = "checkbox",
                     nocheckInherit = true,
                     click = function () {},
                     url="",
                     param="",
                     treeName="zTree"
                 } = {}) {
                let setting = {
                    view: {
                        dblClickExpand: false,
                        showLine: true,      // 显示行
                        selectedMulti: selectedMulti // 多选
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: idKey,    // 自身id
                            pIdKey: pIdKey, // 父id
                            rootPId: rootPId   // 根id
                        },
                        key: {
                            name: name
                        }
                    },
                    check: {
                        enable: checkFlag,
                        chkStyle: chkStyle,
                        nocheckInherit: nocheckInherit
                    },
                    callback: {
                        onClick: click
                    }
                };
                // 初始化ztree
                $.ajax({
                    type: "POST",
                    url: url,
                    data: param,
                    dataType: "JSON",
                    success: function (data) {
                        $.each(data, function (index, ele) {
                            if (typeof (ele.POSITION_ID) != "undefined") {
                                ele.checked = true;
                            }
                        })
                        $.trees.myTree = $.fn.zTree.init($("#" + treeName), setting, data);
                        $.trees.myTree.expandAll(true);
                    }
                });
            }
        }
    });
})(jQuery)