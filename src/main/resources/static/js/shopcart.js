$(function () {

    $.when($.getJSON(ctxPath + "/api/shopcart/item/list"),
        getRenderById("tpl-shopcart"))
        .done(function (resp, render) {
            var data = resp[0];
            if (data.successful) {
                $("#item-list").html(render({
                    list: data.data
                }));
            } else {
                showError(data.error);
            }
        });

});