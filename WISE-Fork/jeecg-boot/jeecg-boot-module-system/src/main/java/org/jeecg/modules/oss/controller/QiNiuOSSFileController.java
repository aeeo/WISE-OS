package org.jeecg.modules.oss.controller;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oss.QiNiuUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/bbs/qiniuoss")
@Api(tags = "七牛OSS")
public class QiNiuOSSFileController {


    /**
     * 通用上传，后台管理系统
     * @param request
     * @return
     */
    @AutoLog(value = "上传文件")
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件")//, @RequestParam(value = "fileType") String fileType
    public Result<?> upload(HttpServletRequest request) {
        Result<?> result = new Result<>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取上传文件对象
        MultipartFile multipartFile = multipartRequest.getFile("file");

        String fileName = QiNiuUtil.uploadStreamFile(multipartFile, "jfif");
        result.setMessage(fileName);
        result.setSuccess(true);
        return result;
    }
    /**
     * 删除文件
     */
    @AutoLog(value = "OSS-删除")
    @DeleteMapping("/upload")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    public Result<?> delete(HttpServletRequest request) {
        Result<?> result = new Result<>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取上传文件对象
        MultipartFile multipartFile = multipartRequest.getFile("file");

        String fileName = QiNiuUtil.uploadStreamFile(multipartFile, "jfif");
        result.setMessage(fileName);
        result.setSuccess(true);
        return result;
    }

    /**
     * 获取uptoken，用于微信小程序客户端上传
     *
     * @return
     */
    @AutoLog(value = "获取OSS-uptoken")
    @GetMapping(value = "/getUptoken")
    public JSONObject getUptoken() {
        JSONObject jsonObject = new JSONObject();
        //jsonObject.append("uptoken", QiNiuUtil.getUptoken());
        jsonObject.putOnce("uptoken", QiNiuUtil.getUptoken());
        return jsonObject;
    }

}
