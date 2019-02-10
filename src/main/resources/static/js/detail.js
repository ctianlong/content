// 文件上传全局设置
FilePond.registerPlugin(
    // FilePondPluginImagePreview,
    // FilePondPluginImageExifOrientation,
    FilePondPluginFileValidateSize,
    FilePondPluginFileValidateType
);
FilePond.setOptions({
    labelIdle: '请拖拽文件到此处或 <span class="filepond--label-action"> 浏览 </span>',
    labelFileWaitingForSize: '正在计算文件大小',
    labelFileSizeNotAvailable: '无法得到文件大小',
    labelFileLoading: '加载中',
    labelFileLoadError: '加载失败',
    labelFileProcessing: '上传中',
    labelFileProcessingComplete: '上传完成',
    labelFileProcessingAborted: '上传已取消',
    labelFileProcessingError: '上传失败',
    labelFileProcessingRevertError: '还原出错',
    labelFileRemoveError: '删除失败',
    labelTapToCancel: '点击取消',
    labelTapToRetry: '点击重试',
    labelTapToUndo: '点击撤销',
    labelButtonRemoveItem: '删除',
    labelButtonAbortItemLoad: '中止',
    labelButtonRetryItemLoad: '重试',
    labelButtonAbortItemProcessing: '取消',
    labelButtonUndoItemProcessing: '撤销',
    labelButtonRetryItemProcessing: '重试',
    labelButtonProcessItem: '上传',
    //文件类型校验
    labelFileTypeNotAllowed: '文件类型无效',
    fileValidateTypeLabelExpectedTypes: '请上传 {allTypes} 文件',
    //文件大小校验
    labelMaxFileSizeExceeded: '文件大小超出限制',
    labelMaxFileSize: '最大文件大小为 {filesize}'
});

$(function () {
    var $pageTitle = $("#page-title");
    var $contentPage = $("#content-page");
    var $successPage = $("#success-page");
    var $id = $("#id");
    var $title = $("#title");
    var $summary = $("#summary");
    var $imgTypeText = $("#imgType-text");
    var $imgTypeUpload = $("#imgType-upload");
    var $imgUrlText = $("#imgUrl-text");
    var $imgUploadwrapper = $("#imgUpload-wrapper");
    var $imgUrlUpload = $("#imgUrl-upload");
    var $imgPre = $("#img-pre");
    var $detailText = $("#detailText");
    var $price = $("#price");

    var $filePond = imgHandleInit();

    var bugFixFlag = false;

    handleLogic();

    $(window).on('hashchange', handleLogic);

    $('#save').click(submitContent);

    function handleLogic() {
        var hashInfo = resolveUrlHash();
        if (!hashInfo) {
            showError("请求出错，请稍后再试");
        }
        switch (hashInfo.path) {
            case '/public' :
                publicInit();
                break;
            case '/edit' :
                editInit(hashInfo.params.id);
                break;
            case '/public/success' :
                publicSuccess(hashInfo.params.id);
                break;
            case '/edit/success' :
                editSuccess(hashInfo.params.id);
                break;
            default :
                showError("请求出错，请稍后再试");
        }
    }

    function showContentPage() {
        $contentPage.show();
        $successPage.hide();
    }
    
    function showSuccessPage() {
        $contentPage.hide();
        $successPage.show();
    }

    function publicInit() {
        showContentPage();
        $pageTitle.text("商品发布");
        $id.val('');
        $title.val('');
        $summary.val('');
        $imgUrlText.val('');
        $imgTypeText.iCheck('check');
        $imgUrlUpload.val('');
        $imgPre.prop('src', '');
        if ($filePond.getFile()) {
            $filePond.removeFile();
        }
        $detailText.val('');
        $price.val('');
    }

    function editInit(id) {
        showContentPage();
        $pageTitle.text("商品编辑");
        $.getJSON(ctxPath + '/api/content/common/baseinfo/' + id)
            .done(resolve(function (c) {
                $id.val(c.id);
                $title.val(c.title);
                $summary.val(c.summary);
                var file = $filePond.getFile();
                if (file) {
                    bugFixFlag = true; // todo 暂时修复
                    $filePond.removeFile();
                }
                var imgUrl = c.imgUrl;
                if (c.imgType === 1) {
                    $imgUrlText.val(imgUrl);
                    $imgTypeText.iCheck('check');
                    $imgUrlUpload.val('');
                    $imgPre.prop('src', imgUrl);
                } else {
                    $imgUrlText.val('');
                    $imgTypeUpload.iCheck('check');
                    $imgUrlUpload.val(imgUrl);
                    $imgPre.prop('src', ctxPath + imgUrl);
                    // $filePond.addFile(ctxPath + imgUrl);
                    var index = imgUrl.lastIndexOf('/') + 1;
                    var urlPrefix = ctxPath + imgUrl.substring(0, index);
                    var name = imgUrl.substring(index, imgUrl.length); // todo 文件上传相关
                    $filePond.setOptions({
                        server: {
                            load: urlPrefix
                        },
                        files: [
                            {
                                source: name,
                                options: {
                                    type: 'local'
                                    // file: {
                                    //     name: name,
                                    //     size: 0,
                                    //     type: 'image/png'
                                    // }
                                }
                            }
                        ]
                    });
                }
                $detailText.val(c.detailText);
                $price.val(c.price);
            }));
    }

    function publicSuccess(id) {
        showSuccessPage();
        $pageTitle.text("商品发布");
        $("#success-title").text('发布成功！')
        $("#show-url").prop('href', ctxPath + '/show?id=' + id);
    }

    function editSuccess(id) {
        showSuccessPage();
        $pageTitle.text("商品编辑");
        $("#success-title").text('编辑成功！')
        $("#show-url").prop('href', ctxPath + '/show?id=' + id);
    }
    
    function submitContent() {
        var title = $title.val().trim();
        var len = title.length;
        if(len < 2 || len > 80) {
            showWarning("标题长度不符合要求");
            return;
        }
        var summary = $summary.val().trim();
        len = summary.length;
        if (len < 2 || len > 140) {
            showWarning("摘要长度不符合要求");
            return;
        }
        var imgType,imgUrl;
        if ($imgTypeText.is(':checked')) {
            imgType = 1;
            imgUrl = $imgUrlText.val().trim();
            if (!/^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/g.test(imgUrl)) {
                if (!imgUrl.startsWith('/img/')) { // todo 对于文件上传返回的url格式进行临时处理
                    showWarning("图片地址信息不符合要求");
                    return;
                }
            }
        } else {
            imgType = 2;
            imgUrl = $imgUrlUpload.val().trim();
            if (imgUrl === '') {
                showWarning("上传文件信息不符合要求");
                return;
            }
        }
        var detailText = $detailText.val().trim();
        len = detailText.length;
        if (len < 2 || len > 1400) {
            showWarning("正文长度不符合要求");
            return;
        }
        var price = $price.val().trim();
        if (!/^\d+(\.\d+)?$/g.test(price)) {
            showWarning("价格信息不符合要求");
            return;
        }
        var data = {
            title: title,
            summary: summary,
            imgType: imgType,
            imgUrl: imgUrl,
            detailText: detailText,
            price: price
        };
        if ($id.val()) {
            data['id'] = $id.val();
            showConfirm('确认修改商品吗？', function (c) {
                if (c) {
                    $.ajax({
                        url: ctxPath + '/api/content/seller/update',
                        type: 'PUT',
                        data: data
                    }).done(resolve(function () {
                        window.location.hash = '#!/edit/success?' + $.param({id: $id.val()});
                    }));
                }
            })
        } else {
            showConfirm('确认发布商品吗？', function (c) {
                if (c) {
                    $.post(ctxPath + '/api/content/seller/add', data)
                        .done(resolve(function (c) {
                            window.location.hash = '#!/public/success?' + $.param({id: c.id});
                        }))
                }
            });
        }
    }


    
    function imgHandleInit() {
        var pond = FilePond.create(document.querySelector('#imgUpload'), {
            server: {
                process: function (fieldName, file, metadata, load, error, progress, abort) {
                    var formData = new FormData();
                    formData.append(fieldName, file, file.name);
                    var request = new XMLHttpRequest();
                    request.timeout = 30000;
                    request.withCredentials = false; // 跨域需要设为true
                    request.open('POST', ctxPath + '/api/content/seller/image/upload', true);
                    request.setRequestHeader(csrf_token_headerName, csrf_token);
                    request.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
                    request.upload.onprogress = function(e) {
                        progress(e.lengthComputable, e.loaded, e.total);
                    };
                    request.onload = function() {
                        var status = request.status;
                        if (status >= 200 && status < 300) {
                            var resp = JSON.parse(request.responseText);
                            if (resp.successful) {
                                var data = resp.data;
                                load(data.uniqueId);
                                $imgPre.prop('src', data.imgUrl);
                                $imgUrlUpload.val(data.imgUrl);
                            } else {
                                showError(resp.error);
                                error(resp.error);
                            }
                        } else {
                            showErrorByStatus(status);
                            error(request.responseText);
                        }
                    };
                    request.ontimeout = function(e) {
                        showError("请求超时，请稍后再试");
                        error("timeout");
                    };
                    request.onerror = function(e) {
                        showError("请求出错，请稍后再试")
                        error("error");
                    };
                    request.send(formData);
                    return {
                        abort: function() {
                            request.abort();
                            abort();
                        }
                    };
                },
                fetch: null,
                revert: null,
                restore: null,
                load: ctxPath + '/img/' // todo 文件上传相关
            },
            acceptedFileTypes: ['image/png', 'image/jpeg', 'image/gif'],
            fileValidateTypeLabelExpectedTypesMap: {
                'image/jpeg': '.jpg',
                'image/png': '.png',
                'image/gif': '.gif'
            },
            maxFileSize: '1MB'
        });
        pond.on('processfile', function(error, file) {
            if (error) {
                console.log('Oh no');
                return;
            }
            console.log('File added', file);
        });
        pond.on('processfileabort', function(file) {
            $imgPre.prop('src', '');
            $imgUrlUpload.val('');
        });
        pond.on('processfileundo', function(file) {
            console.log('File undo', file);
        });
        pond.on('removefile', function(file) {
            if (bugFixFlag) {
                bugFixFlag = false;
                return;
            }
            $imgPre.prop('src', '');
            $imgUrlUpload.val('');
        });

        $imgUrlText.on('input', function () {
            var v = $(this).val().trim();
            // if (v !== '') {
                $imgPre.prop('src', v);
            // }
        });
        $imgTypeText.on('ifToggled', function () {
            if($(this).is(':checked')) {
                $imgUrlText.show();
                $imgPre.prop('src', $imgUrlText.val().trim());
            } else {
                $imgUrlText.hide();
            }
        });
        $imgTypeUpload.on('ifToggled', function () {
            if($(this).is(':checked')) {
                $imgUploadwrapper.show();
                $imgPre.prop('src', $imgUrlUpload.val());
            } else {
                $imgUploadwrapper.hide();
            }
        });
        return pond;
    }


});