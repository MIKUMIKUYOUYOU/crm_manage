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
            alert(content, icon) {
                if ($.common.isEmpty(icon)) {

                    return layer.alert(content);
                    ;
                }
                return layer.alert(content, {
                    title: "系统提示",
                    icon: icon,
                    skin: 'layui-layer-molv',
                    btn: ['确认'],
                });
            },
            msg(content, icon, shade, time) {
                if ($.common.isEmpty(icon)) {
                    layer.msg(content);
                    return false;
                }
                layer.alert(content, {
                    time: time == false ? false : 300,
                    icon: icon,
                    shade: $.common.isEmpty(shade) ? 0.3 : shade,
                    anim: 5
                });
            }
            ,
            alertWarning(content) {
                this.alert(content, 3);
            },
            alertError(content) {
                this.alert(content, 2);
            },
            alertSuccess(content) {
                this.alert(content, 1);
            },
            msgWarning(content) {
                this.msg(content, 3);
            },
            msgError(content) {
                this.msg(content, 2);
            },
            msgSuccess(content) {
                this.msg(content, 1);
            },
            loading(content) {
                return this.msg(content, 16, 0.3, false)
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
            myTree:{},
            init(options) {
                var setting = {
                    view: {
                        dblClickExpand: false,
                        showLine: true,      // 显示行
                        selectedMulti: options.selectedMulti == null ? false : options.selectedMulti // 多选
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: options.idKey == null ? "id" : options.idKey,    // 自身id
                            pIdKey: options.pIdKey == null ? "pId" : options.pIdKey, // 父id
                            rootPId: options.rootPId == null ? "0" : options.rootPId   // 根id
                        },
                        key: {
                            name: options.name == null ? "name" : options.name
                        }
                    },
                    check: {
                        enable: options.checkFlag == null ? false : options.checkFlag,
                        chkStyle: options.chkStyle == null ? "checkbox" : options.chkStyle,
                        nocheckInherit: options.nocheckInherit == null ? true : options.nocheckInherit
                    },
                    callback: {
                        onClick: options.click
                    }
                };
                // 初始化ztree
                $.ajax({
                    type: "POST",
                    url: options.url,
                    data:options.param,
                    dataType: "JSON",
                    success:function(data) {
                    	$.each(data,function(index,ele){
                        	if(typeof(ele.POSITION_ID) != "undefined"){
                        		ele.checked=true;
                            }
                        })
                        $.trees.myTree = $.fn.zTree.init($("#" + options.treeName), setting, data);
                    	$.trees.myTree.expandAll(true);
                    }
                });
            }
        }
    });
})(jQuery)