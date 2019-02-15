$(function () {
    var $show = $("#c-show");
    var $img = $show.find('.c-img'),
        $name = $show.find('.c-name'),
        $summary = $show.find('.c-summary'),
        $unit = $show.find('.c-unit'),
        $price = $show.find('.c-price'),
        $amount = $show.find('.c-amount'),
        $btn = $show.find('.c-btn'),
        $detail = $show.find('.c-detail');

    var content = {
        id: getUrlParams('id'),
        tradeAmount: 1 //买家默认初始购买量显示为1
    };
    if (!content.id) {
        showError();
        return;
    }

    if (isLogin && loginUser) {
        if (loginUser.roleCode === 1) {
            detailForBuyer();
        } else if (loginUser.roleCode === 2) {
            detailForSeller();
        } else {
            detailForAnonymous();
        }
    } else {
        detailForAnonymous();
    }

    function detailForBuyer() {
        $.when($.getJSON(ctxPath + "/api/content/common/baseinfo/" + content.id),
            $.getJSON(ctxPath + "/api/order/loginuser/content/" + content.id))
            .done(function (resp1, resp2) {
                var data1 = resp1[0];
                var data2 = resp2[0];
                if (data1.successful) {
                    var c = data1.data;
                    renderBaseinfo(c);
                    if (data2.successful) {
                        var o = data2.data;
                        $amount.find('.c-amount-label').text('已购买数量：');
                        $amount.find('input').prop('title', '已购买数量')
                            .val(o.tradeAmount).prop("disabled", true);
                        $amount.show();
                        $btn.find('.c-trade-price span').text(o.tradePrice);
                        $btn.find('button').text('已购买').addClass('disabled');
                        $btn.show();
                    } else {
                        $amount.find('input').val(content.tradeAmount);
                        $amount.show();
                        $("#spinner").spinner('changing', function (e, newVal, oldVal) {
                            content.tradeAmount = newVal;
                        });
                        $btn.find('.c-trade-price').remove();
                        $btn.find('button').text('加入购物车').click(function () {
                            addToShopCart();
                        });
                        $btn.show();
                    }
                } else {
                    showError(data1.error);
                }
            });


    }

    function detailForSeller() {
        $.getJSON(ctxPath + '/api/content/seller/fullinfo/' + content.id)
            .done(resolve(function (c) {
                renderBaseinfo(c);
                $amount.find('.c-amount-label').text('售出数量：');
                $amount.find('input').prop('title', '已售出数量')
                    .val(c.salesAmount).prop("disabled", true);
                $amount.show();
                $btn.find('.c-trade-price').remove();
                $btn.find('button').text('编辑').click(function () {
                    window.location.href = ctxPath + '/detail#!/edit?' + $.param({id: content.id});
                });
                $btn.show();
            }));
    }

    function detailForAnonymous() {
        $.getJSON(ctxPath + "/api/content/common/baseinfo/" + content.id)
            .done(resolve(renderBaseinfo));
        $amount.remove();
        $btn.remove();
    }

    function renderBaseinfo(c) {
        $img.prop('src', c.imgType === 1 ? c.imgUrl : ctxPath + c.imgUrl)
            .prop('alt', c.title);
        $name.text(c.title);
        $summary.text(c.summary);
        $unit.show();
        $price.text(c.price);
        $detail.text(c.detailText);
    }

    function addToShopCart() {
        BootstrapDialog.confirm({
            title: '提示',
            message: '确认加入购物车吗？',
            btnCancelLabel: '取消',
            btnOKLabel: '确认',
            type: BootstrapDialog.TYPE_DANGER,
            size: BootstrapDialog.SIZE_SMALL,
            closable: true,
            callback: function (r) {
                if (r) {
                    $.post(ctxPath + "/api/shopcart/item/add", {
                        id: content.id,
                        amount: content.tradeAmount
                    }).done(resolve(function () {
                        showSuccess("已成功添加到购物车");
                    }));
                }
            }
        });
    }

});

