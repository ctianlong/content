package com.netease.homework.content.web.controller;

import com.netease.homework.content.entity.Content;
import com.netease.homework.content.mapper.ContentMapper;
import com.netease.homework.content.web.util.JsonResponse;
import com.netease.homework.content.web.util.ResultCode;
import com.netease.homework.content.web.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/22
 */
@RestController
@RequestMapping("/api/content")
public class ContentController {

    private static final Pattern URL_PATTERN = Pattern.compile("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$");

    private final ContentMapper contentMapper;

    @Value("${web.img-upload-path}")
    private String imgUploadPath;
    @Value("${web.img-url-prefix}")
    private String imgUrlPrefix;

    @Autowired
    public ContentController(ContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    @GetMapping("/common/list")
    public JsonResponse getAllContentList() {
        JsonResponse response = new JsonResponse();
        List<Content> c = contentMapper.listForAnonymous();
        return response.setSuccessful().setData(c);
    }

    @GetMapping("/common/baseinfo/{id}")
    public JsonResponse getBaseinfo(@PathVariable("id") Long id) {
        JsonResponse r = new JsonResponse();
        Content c = contentMapper.getBaseinfoById(id);
        return c == null ? r.setCode(ResultCode.ERROR_UNKNOWN).setError("商品不存在或已下架")
                : r.setSuccessful().setData(c);
    }

    @GetMapping("/seller/fullinfo/{id}")
    public JsonResponse getFullinfo(@PathVariable("id") Long id) throws IOException {
        JsonResponse r = new JsonResponse();
        Content c = contentMapper.getFullinfoById(id);
        if (c == null) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("商品不存在或已下架");
        }
        if (!Objects.equals(c.getUserId(), SessionUtils.getCurrentPrincipalId())) {
            SessionUtils.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        return r.setSuccessful().setData(c);
    }

    @GetMapping("/seller/list")
    public JsonResponse getContentListForSeller() {
        JsonResponse response = new JsonResponse();
        List<Content> c = contentMapper.listBySellerId(SessionUtils.getCurrentPrincipalId());
        return response.setSuccessful().setData(c);
    }

    @DeleteMapping("/seller/delete/{id}")
    public JsonResponse deleteContent(@PathVariable("id") Long id) throws IOException {
        JsonResponse response = new JsonResponse();
        Content c = contentMapper.getFullinfoById(id);
        if (c == null) {
            return response.setCode(ResultCode.ERROR_UNKNOWN).setError("商品不存在或已下架");
        }
        if (!Objects.equals(c.getUserId(), SessionUtils.getCurrentPrincipalId())) {
            SessionUtils.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        if (c.getSalesAmount() > 0) {
            return response.setCode(ResultCode.ERROR_UNKNOWN).setError("商品已卖出，无法删除");
        }
        if (contentMapper.deleteByIdLogic(id) != 1) {
            return response.setCode(ResultCode.ERROR_UNKNOWN).setError("删除失败");
        }
        return response.setSuccessful();
    }

    @PostMapping("/seller/add")
    public JsonResponse addContent(Content c) {
        JsonResponse r = new JsonResponse();
        // 前端已经检查过，后台仍需进行参数合法性检查
        String error = checkContent(c);
        if (error != null) {
            return r.setCode(ResultCode.ERROR_BAD_PARAMETER).setError(error);
        }
        Long uid = SessionUtils.getCurrentPrincipalId();
        Assert.notNull(uid, "add content, userId must not be null");
        c.setUserId(uid);
        // 价格两位小数处理
        double priceFix = ((int) (c.getPrice() * 100)) / 100.0;
        c.setPrice(priceFix);
        int i = contentMapper.save(c);
        if (i != 1) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("添加失败");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("id", c.getId());
        return r.setSuccessful().setData(data);
    }

    @PutMapping("/seller/update")
    public JsonResponse updateContent(Content c) throws IOException {
        JsonResponse r = new JsonResponse();
        // 前端已经检查过，后台仍需进行参数合法性检查
        String error = checkContent(c);
        if (error != null) {
            return r.setCode(ResultCode.ERROR_BAD_PARAMETER).setError(error);
        }
        Content oldContent = contentMapper.getFullinfoById(c.getId());
        if (oldContent == null) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("商品不存在或已下架");
        }
        if (!Objects.equals(oldContent.getUserId(), SessionUtils.getCurrentPrincipalId())) {
            SessionUtils.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        // 价格两位小数处理
        double priceFix = ((int) (c.getPrice() * 100)) / 100.0;
        c.setPrice(priceFix);
        int i = contentMapper.update(c);
        if (i != 1) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("更新失败");
        }
        return r.setSuccessful();
    }

    @PostMapping("/seller/image/upload")
    public JsonResponse imgUploadBySeller(@RequestParam(name = "imgUpload") MultipartFile imgUpload) throws IOException {
        JsonResponse r = new JsonResponse();
        if (imgUpload.isEmpty()) {
            return r.setCode(ResultCode.ERROR_BAD_PARAMETER).setError("请求错误");
        }
        String fileName = imgUpload.getOriginalFilename();
        String contentType = imgUpload.getContentType();
        Set<String> allowContentTypes = new HashSet<>(Arrays.asList("image/jpeg", "image/png", "image/gif"));
        Set<String> allowExtNames = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif"));
        if (fileName.lastIndexOf(".") == -1
                || !allowExtNames.contains(fileName.substring(fileName.lastIndexOf(".") + 1))
                || !allowContentTypes.contains(contentType)) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("上传文件类型不符合要求");
        }
        String extName = fileName.substring(fileName.lastIndexOf("."));
        String md5;
        try (InputStream in = imgUpload.getInputStream()) {
            md5 = DigestUtils.md5DigestAsHex(in);
        }
        String md5FileName = md5 + extName;
        Path extraPath = createExtraPath(md5FileName);
        Path storeDir = Paths.get(imgUploadPath).resolve(extraPath);
        if (Files.notExists(storeDir)) {
            Files.createDirectories(storeDir);
        }
        try (InputStream in = imgUpload.getInputStream()) {
            Files.copy(in, storeDir.resolve(md5FileName), StandardCopyOption.REPLACE_EXISTING);
        }
        Map<String, Object> data = new HashMap<>();
        String imgRelativeUrl = Paths.get(imgUrlPrefix).resolve(extraPath).resolve(md5FileName).toString().replaceAll("\\\\", "/");
        data.put("imgUrl", imgRelativeUrl);
        data.put("uniqueId", md5);
        return r.setSuccessful().setData(data);
    }

    /**
     * 为防止一个目录下面出现太多文件，使用hash算法打散存储，创建文件存储前置额外目录
     *
     * @param fileName  文件名，要根据文件名生成额外存储目录
     * @return 额外存储目录
     */
    private Path createExtraPath(String fileName) {
        int hashcode = fileName.hashCode();
        int dir1 = hashcode & 0xf; //0--15
        int dir2 = (hashcode & 0xf0) >> 4; //0-15
        return Paths.get(String.valueOf(dir1), String.valueOf(dir2));
    }

    private String checkContent(Content c) {
        if (!checkStringLength(c.getTitle(), 2, 80)) {
            return "标题长度应在[2,80]字符内";
        }
        // 图片url校验，文件上传完后也会返回url
        String imgUrl = c.getImgUrl();
        if (imgUrl == null || (!URL_PATTERN.matcher(imgUrl).matches() && !imgUrl.startsWith("/img/"))) {
            return "图片地址信息不符合要求";
        }
        if (!checkStringLength(c.getSummary(), 2, 140)) {
            return "摘要长度应在[2,140]字符内";
        }
        if (!checkStringLength(c.getDetailText(), 2, 1000)) {
            return "正文长度应在[2,1000]字符内";
        }
        return null;
    }

    private boolean checkStringLength(String s, int low, int high) {
        int len = s == null ? 0 : s.length();
        return len >= low && len <= high;
    }

    public static void main(String[] args) throws IOException {
//        Files.copy(Paths.get("./img/8625d9f3-c059-4dc6-b915-37bb5ea7e6a3.jpg"), Paths.get("./img/a/abc.jpg"));
    }

}
