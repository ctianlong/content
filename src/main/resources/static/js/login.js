$(function () {
    $("#header-login-btn").hide();
    $("#username").focus();

    $("#loginForm").submit(function (e) {
        e.preventDefault();
        var username = $("#username").val() || "";
        username = username.trim();
        if (!username) {
            toastr.error("用户名不能为空");
            return;
        }
        var password = $("#password").val() || "";
        password = password.trim();
        if (!password) {
            toastr.error("密码不能为空");
            return;
        }
        var passwordMd5 = md5(password);
        $.ajax({
            url: ctxPath + "/api/user/common/login",
            type: "POST",
            data: $.param({username: username, passwordMd5: passwordMd5}),
            success: function (data, textStatus, jqXHR) {
                if (data.successful) {
                    var redirectUrl = data.data.redirectUrl;
                    $(location).prop("href", redirectUrl);
                } else {
                    toastr.error(data.error);
                }
            }
        });
    });

});