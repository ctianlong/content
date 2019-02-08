package com.netease.homework.content.web.controller;

import com.netease.homework.content.entity.Content;
import com.netease.homework.content.entity.User;
import com.netease.homework.content.mapper.ContentMapper;
import com.netease.homework.content.web.util.JsonResponse;
import com.netease.homework.content.web.util.ResultCode;
import com.netease.homework.content.web.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/22
 */
@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentMapper contentMapper;

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
        return c == null ? r.setCode(ResultCode.ERROR_UNKNOWN).setError("商品不存在")
                : r.setSuccessful().setData(c);
    }

    @GetMapping("/seller/fullinfo/{id}")
    public JsonResponse getFullinfo(@PathVariable("id") Long id) throws IOException {
        JsonResponse r = new JsonResponse();
        Content c = contentMapper.getFullinfoById(id);
        if (c == null) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("商品不存在");
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
            return response.setCode(ResultCode.ERROR_UNKNOWN).setError("商品不存在");
        }
        if (!Objects.equals(c.getUserId(), SessionUtils.getCurrentPrincipalId())) {
            SessionUtils.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        if (c.getSalesAmount() > 0) {
            return response.setCode(ResultCode.ERROR_UNKNOWN).setError("商品已卖出，无法删除");
        }
        if (contentMapper.deleteById(id) != 1) {
            return response.setCode(ResultCode.ERROR_UNKNOWN).setError("删除失败");
        }
        return response.setSuccessful();
    }

    @PostMapping("/seller/add")
    public JsonResponse addContent(Content c) {
        JsonResponse r = new JsonResponse();
        Long uid = SessionUtils.getCurrentPrincipalId();
        Assert.notNull(uid, "add content, userId must not be null");
        c.setUserId(uid);
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
        Content oldContent = contentMapper.getFullinfoById(c.getId());
        if (oldContent == null) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("商品不存在");
        }
        if (!Objects.equals(oldContent.getUserId(), SessionUtils.getCurrentPrincipalId())) {
            SessionUtils.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        int i = contentMapper.update(c);
        if (i != 1) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("更新失败");
        }
        return r.setSuccessful();
    }

    @PostMapping("/seller/image/upload")
    public JsonResponse imgUploadBySeller(@RequestParam(name = "imgUpload") MultipartFile imgUpload) {

        JsonResponse r = new JsonResponse();
        System.out.println(imgUpload.getName());
        System.out.println(imgUpload.getContentType());
        System.out.println(imgUpload.getOriginalFilename());
        System.out.println(imgUpload.getSize());

//        return r.setCode(ResultCode.ERROR_BAD_PARAMETER).setError("文件类型无效");
        Map<String, Object> data = new HashMap<>();
        data.put("imgUrl", "/images/upload/18bOOOPIC9c.jpg");
        data.put("uniqueId", "123456");
        return r.setSuccessful().setData(data);
    }

}
