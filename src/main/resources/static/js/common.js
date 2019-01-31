// bootstrap table 默认配置
$.extend($.fn.bootstrapTable.defaults, {
    showColumns: true, //是否显示所有的列
    showRefresh: true, //是否显示刷新按钮
    minimumCountColumns: 3,  //最少允许的列数
    clickToSelect: true, //设置 true 将在点击行时，自动选择 rediobox 和 checkbox
    sidePagination: "server", //服务端处理分页
    showPaginationSwitch: false, //是否显示切换分页按钮
    silentSort: false, //设置为 true 则在点击列的排序时不显示加载中字样。仅在 sidePagination设置为 server时生效。
    showToggle: false,  //是否显示切换视图（table/card）按钮。
    cardView: false,  //设置为 true将显示card视图，适用于移动设备。否则为table试图，适用于pc端。
    detailView: false, //设置为 true 可以显示详细页面模式。父子表
    icons: {
        paginationSwitchDown: "glyphicon-collapse-down icon-chevron-down",
        paginationSwitchUp: "glyphicon-collapse-up icon-chevron-up",
        refresh: "glyphicon-refresh icon-refresh",
        toggleOff: "glyphicon-list-alt icon-list-alt",
        toggleOn: "glyphicon-list-alt icon-list-alt",
        columns: "glyphicon-th icon-th",
        detailOpen: "glyphicon-plus icon-plus",
        detailClose: "glyphicon-minus icon-minus",
        fullscreen: "glyphicon-fullscreen",
        toggle: 'glyphicon-list-alt icon-list-alt' // 默认情况下貌似该样式没有，无奈只能手动写一遍，若不使用则忽略
    },
    dataType: 'json',
    singleSelect: true, //是否只能单选
    striped: true, //是否显示行间隔色
    cache: false, //是否使用缓存，默认为true
    sortable: true, //是否启用排序
    pagination: true,   //显示分页按钮
    pageNumber: 1, //初始化加载第一页，默认第一页
    pageSize: 10,   //默认显示的每页个数
    pageList: [10, 25, 50, 100],    //可供选择的每页的行数（*）
    queryParamsType: '', //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort // 设置为 '' 在这种情况下传给服务器的参数为：pageSize,pageNumber
    responseHandler: function(res){ // 使用ajax时也会执行，该数据处理可在ajax内进行，也可在此处进行
        //加载服务器数据之前的处理程序，可以用来格式化数据。res为从服务器请求到的数据。
        return res;
    },
    search: false, //显示搜索框
    searchOnEnterKey: false, //设置为 true时，按回车触发搜索方法，否则自动触发搜索方法
    searchTimeOut: 1500,
    formatLoadingMessage: function() {
        return '<i class="fa fa-spinner fa-spin"></i> 加载中'
    },
    formatRecordsPerPage: function(a) {
        return "每页显示 " + a + " 条记录"
    },
    formatShowingRows: function(a, b, c) {
        return "第 " + a + " 到 " + b + " 条记录，总共 " + c + " 条记录"
    },
    formatSearch: function() {
        return "模糊搜索"
    },
    formatNoMatches: function() {
        return "没有找到匹配的记录"
    },
    formatPaginationSwitch: function() {
        return "隐藏/显示分页"
    },
    formatRefresh: function() {
        return "刷新"
    },
    formatToggle: function() {
        return "切换"
    },
    formatColumns: function() {
        return "列"
    },
    formatExport: function() {
        return "导出数据"
    },
    formatClearFilters: function() {
        return "清空过滤"
    }
});

$(function () {
    $(".select2").select2({
        minimumResultsForSearch:-1,
        language:"zh-CN"
    });
    $('input').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_square-blue',
        increaseArea: '20%' // optional
    });
    NProgress.configure({
        minimum: 0.25,
        showSpinner: false
    });
    toastr.options = {
        closeButton: false,
        debug: false,
        progressBar: false,
        positionClass: "toast-bottom-right",
        onclick: null,
        showDuration: "300",
        hideDuration: "1000",
        timeOut: "5000",
        extendedTimeOut: "1000",
        showEasing: "swing",
        hideEasing: "linear",
        showMethod: "fadeIn",
        hideMethod: "fadeOut"
    };
    var csrf_token_headerName = $("#csrf_token_header").attr("content");
    var csrf_token = $("#csrf_token").attr("content");
    var headers = {};
    headers[csrf_token_headerName] = csrf_token;
    $.ajaxSetup({
        headers: headers,
        beforeSend: function () {
            NProgress.start();
        },
        complete: function () {
            NProgress.done();
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.error(textStatus);
            var code = jqXhr.status||"";
            if (code === 401) {
                toastr.warning("您当前未登录");
            } else if (code === 403) {
                toastr.warning("您的访问权限不足");
            } else {
                showError();
            }
        }
    });

});

function logout() {
    $.ajax({
        url: ctxPath + "/api/user/common/logout",
        type: "POST",
        success: function (data, textStatus, jqXHR) {
            $(location).prop("href",ctxPath+"/login");
        }
    });
}

function formatTime(timestamp){
    var date = new Date(timestamp);//如果timestamp为13位不需要乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    var m = (date.getMinutes() <10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    var s = (date.getSeconds() <10 ? '0' + date.getSeconds() : date.getSeconds());
    return Y+M+D+h+m+s;
}

function getUrlParams(name, url) {
    if (url === undefined) url = window.location.href;
    var regex = /[?&]+([^=?&#]+)(=([^&#]*))?/g;
    var params = {};
    var match;
    while(match = regex.exec(url)) {
        var key = decodeURIComponent(match[1].replace('/\+/g', ' '));
        var val = match[3] !== undefined ? decodeURIComponent(match[3].replace('/\+/g', ' ')) : '';
        if (params.hasOwnProperty(key)) {
            var oldVal = params[key];
            if (Array.isArray(oldVal))
                oldVal.push(val);
            else
                params[key] = [oldVal, val];
        } else {
            params[key] = val;
        }
    }
    if (name !== undefined) return params[name] !== undefined ? params[name] : null;
    return params;
}

function resolve(fn) {
    return function (data) {
        if (data.successful) {
            fn(data.data);
        } else {
            toastr.error(data.error);
        }
    };
}

function showError() {
    toastr.error("请求出错，请稍后再试");
}