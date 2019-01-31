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

    var content = {};
    content.id = getUrlParams('id');
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
        $("#spinner").spinner('changing', function (e, newVal, oldVal) {
            content.tradeAmount = newVal;
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
                    window.location.href = ctxPath + '/edit?id=' + content.id;
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

});

