$(function () {
    if (isLogin && loginUser) {
        if (loginUser.roleCode === 1) {
            contentForBuyer();
        } else if (loginUser.roleCode === 2) {
            contentForSeller();
        } else {
            contentForAnonymous();
        }
    } else {
        contentForAnonymous();
    }

    $("#all").on('click', '.delete-icon', function () {
        var $this = $(this);
        deleteContent($this.data('id'), $this.data('title'));
    });

    function getRenderById(id) {
        return template.compile(document.getElementById(id).innerHTML);
    }

    function contentForAnonymous() {
        $.when($.getJSON(ctxPath + "/api/content/common/list"),
            getRenderById("tpl-content"))
            .done(function (resp, render) {
                var data = resp[0];
                if (data.successful) {
                    $("#all").html(render({
                        type: 0, // 0表示匿名查看所有商品
                        list: data.data
                    }));
                } else {
                    toastr.error(data.error);
                }
            });
    }

    function contentForBuyer() {
        $("#li-unpurchased").show();
        $.when($.getJSON(ctxPath + "/api/content/common/list"),
            $.getJSON(ctxPath + "/api/order/loginuser/content/ids"),
            getRenderById("tpl-content"))
            .done(function (resp1, resp2, render) {
                var data1 = resp1[0];
                var data2 = resp2[0];
                if (data1.successful && data2.successful) {
                    var contents = data1.data;
                    var purchasedIds = data2.data;
                    var unpurchasedContents = [];
                    for (var i = 0; i < contents.length; i++) {
                        var c = contents[i];
                        if (purchasedIds.indexOf(c.id) === -1) {
                            c.purchased = false;
                            unpurchasedContents.push(c);
                        } else {
                            c.purchased = true;
                        }
                    }
                    $("#all").html(render({
                        type: 1, // 1代表买家查看所有商品
                        list: contents
                    }));
                    $("#unpurchased").html(render({
                        type: 0, // 买家未购买商品可使用0，传1造成多余判断
                        list: unpurchasedContents
                    }));
                } else {
                    if (!data1.successful) {
                        toastr.error(data1.error);
                    }
                    if (!data2.successful) {
                        toastr.error(data2.error);
                    }
                }
            });
    }

    function contentForSeller() {
        $.when($.getJSON(ctxPath + "/api/content/seller/list"),
            getRenderById("tpl-content"))
            .done(function (resp, render) {
                var data = resp[0];
                if (data.successful) {
                    $("#all").html(render({
                        type: 2, // 2表示卖家查看所有商品
                        list: data.data
                    }));
                } else {
                    toastr.error(data.error);
                }
            });
    }

    function deleteContent(id, title) {
        BootstrapDialog.confirm({
            title: '提示',
            message: '确认删除商品 '+title+' ？',
            btnCancelLabel: '取消',
            btnOKLabel: '确认',
            type: BootstrapDialog.TYPE_DANGER,
            size: BootstrapDialog.SIZE_SMALL,
            closable: true,
            callback: function(r) {
                if(r) {
                    $.ajax({url: ctxPath + '/api/content/seller/delete/' + id, type: 'DELETE'})
                        .done(function (data) {
                            if (data.successful) {
                                toastr.success("删除成功");
                                $("#item-"+id).remove();
                            } else {
                                toastr.error(data.error);
                            }
                        });
                }
            }
        });
    }

});

