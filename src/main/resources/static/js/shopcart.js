$(function () {

    var $wrapper = $("#item-list");
    var items = {};
    var $btnBuy = $("#buy");
    
    $.when($.getJSON(ctxPath + "/api/shopcart/item/list"),
        getRenderById("tpl-shopcart"))
        .done(function (resp, render) {
            var data = resp[0];
            if (data.successful) {
                var list = data.data;
                $wrapper.html(render({
                    list: list
                }));
                if (list && list.length > 0) {
                    $btnBuy.show();
                    for (var i = 0, len = list.length; i < len; i++) {
                        var item = list[i];
                        items[item.id] = item;
                    }
                    $('[data-trigger="spinner"]').spinner('changed', function (e, newVal, oldVal) {
                        var $this = $(this);
                        var item = items[$this.closest('tr').data('id')];
                        if (item) {
                            var updateTime = new Date().getTime();
                            $.ajax({
                                url: ctxPath + "/api/shopcart/item/update",
                                type: "PUT",
                                data: {
                                    contentId: item.contentId,
                                    amount: newVal,
                                    updateTime: updateTime
                                }
                            }).done(function (data) {
                                if (updateTime > item.updateTime) {
                                    if (data.successful) {
                                        $this.closest('td').next().children(':last').text((item.price * newVal).toFixed(2));
                                        item.amount = newVal;
                                        item.updateTime = updateTime;
                                        computePriceSum();
                                    } else {
                                        showError(data.error);
                                        $this.val(item.amount);
                                    }
                                }
                            });
                        }
                    });
                }
            } else {
                showError(data.error);
            }
        });

    $wrapper.on("click", ".btn-delete", function (e) {
        var $this = $(this);
        var item = items[$this.closest('tr').data('id')];
        if (item) {
            showConfirm("确认删除？", function (r) {
                if (r) {
                    $.ajax({
                        url: ctxPath + "/api/shopcart/item/delete/" + item.contentId,
                        type: "DELETE"
                    }).done(resolve(function (data) {
                        $this.closest('tr').remove();
                        delete items[item.id];
                        if ($.isEmptyObject(items)) {
                            $wrapper.find('tfoot').remove();
                            $btnBuy.remove();
                            $wrapper.find('tbody').append('<tr><td colspan="6" class="text-center">购物车为空</td></tr>');
                        } else {
                            computePriceSum();
                        }
                    }));
                }
            });
        }
    });

    $btnBuy.click(function () {
        showConfirm("确认购买？", function (r) {
            if (r) {
                var data = [];
                var id,item;
                for (id in items) {
                    item = items[id];
                    var t = {};
                    t.contentId = item.contentId;
                    t.amount = item.amount;
                    data.push(t);
                }
                $.ajax({
                    url: ctxPath + "/api/order/shopcart/settle",
                    type: "POST",
                    contentType: "application/json; charset=UTF-8",
                    data: JSON.stringify(data)
                }).done(resolve(function (data) {
                    showSuccess("购买成功，3秒后跳转至账务页面");
                    $("#spin-mask").show();
                    setTimeout(function () {
                        window.location.href = ctxPath + "/account";
                    }, 3000);
                }));
            }
        })
    });

    $("#back").click(function () {
        window.href = window.history.back();
    });
    
    function computePriceSum() {
        var id,item;
        var priceSum = 0;
        for (id in items) {
            item = items[id];
            if (item.status === 0) {
                priceSum += item.amount * item.price;
            }
        }
        $("#priceSum").text(priceSum.toFixed(2));
    }

});