package com.ozipin.filemaster.controller;

import com.ozipin.filemaster.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 文件控制器
 *
 * @author ozipin
 * @date 2021/04/02
 */
@RestController
@RequestMapping(value = "/file")
@Api(value = "文件相关接口", tags = {"文件相关接口"})
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 此处不一定要返回一个file类型的对象，只是此处演示的时候没用用到数据库或者oss服务，所以这么做
     * 同样的，在实际情况中由于项目的方案的不同，根据id获取文件并下载的方式也不相同
     * @param id id
     * @return
     */
    @ApiOperation(value = "文件下载")
    @PostMapping(value = "/download/{id}")
    public void download(HttpServletResponse response, @PathVariable("id") Long id) throws IOException {
        File file = fileService.getFileById(id);
        // 设置编码格式
        String fileName = java.net.URLEncoder.encode(file.getName(), "UTF-8");
        // 告诉浏览器输出内容为流
        response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
        response.addHeader("Content-Disposition", String.format("attachment;filename=%s", fileName));
        OutputStream outputStream = response.getOutputStream();
        IOUtils.copy(new FileInputStream(file), outputStream);
        outputStream.flush();
    }
}
