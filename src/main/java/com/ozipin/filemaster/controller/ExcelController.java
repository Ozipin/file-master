package com.ozipin.filemaster.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.google.common.collect.Lists;
import com.ozipin.filemaster.common.ConstantStr;
import com.ozipin.filemaster.domain.vo.CommonResultVo;
import com.ozipin.filemaster.domain.vo.CommonResultVoBuilder;
import com.ozipin.filemaster.domain.vo.ExcelImportResultVo;
import com.ozipin.filemaster.excel.listener.StudentInfoListener;
import com.ozipin.filemaster.excel.modal.StudentInfoModal;
import com.ozipin.filemaster.exception.CommonException;
import com.ozipin.filemaster.service.FileService;
import com.ozipin.filemaster.service.UserService;
import com.ozipin.filemaster.utils.DownloadHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * excel控制器
 *
 * @author ozipin
 * @date 2021/03/30
 */
@RestController
@RequestMapping(value = "/excel")
@Api(value = "excel相关接口", tags = {"excel相关接口"})
@Slf4j
public class ExcelController {

    /**
     * 下载时实时生成
     */
    @ApiOperation(value = "模板下载-实时生成的")
    @PostMapping(value = "/download/from-generated")
    public void downloadFromGenerated(HttpServletResponse response) throws IOException{
        String sheetMame = ConstantStr.STUDENT_INFO_IMPORT_TEMPLATE;
        String fileName = String.format("%s%s", sheetMame, ConstantStr.EXCEL_SUFFIX);
        DownloadHelper.prepareDownload(response, fileName);
        EasyExcelFactory.write(response.getOutputStream(), StudentInfoModal.class)
                .sheet(sheetMame)
                .doWrite(Lists.newArrayList(StudentInfoModal.getExampleData()));
    }

    /**
     * 从静态资源下载
     */
    @ApiOperation(value = "模板下载-从静态资源")
    @PostMapping(value = "/download/from-resources")
    public void downloadFromResources(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //静态资源路径
        ClassPathResource classPathResource = new ClassPathResource("templates/import/学生信息导入模板.xlsx");
        String fileName = classPathResource.getFilename();
        // 设置编码格式
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        // 告诉浏览器输出内容为流
        response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
        response.addHeader("Content-Disposition", String.format("attachment;filename=%s", fileName));
        OutputStream outputStream = response.getOutputStream();
        IOUtils.copy(classPathResource.getInputStream(), outputStream);
        outputStream.flush();
    }

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    /**
     * 文件导入
     */
    @ApiOperation(value = "文件导入")
    @PostMapping(value = "/import")
    public CommonResultVo<ExcelImportResultVo> importExcel(@ApiParam(value = "file", required = true) MultipartFile file) {
        StudentInfoListener studentInfoListener = new StudentInfoListener(userService, fileService);
        try {
            EasyExcel.read(file.getInputStream(), StudentInfoModal.class, studentInfoListener).sheet().doRead();
        }catch (IOException ex){
            log.error("importExcel#IOException: {}-{}", ex.getMessage(), ex);
            throw new CommonException(ex.getMessage());
        }catch (ExcelAnalysisException ex){
            log.error("importExcel#BaseException: {}-{}", ex.getMessage(), ex);
            throw new CommonException(ex.getMessage());
        }
        return new CommonResultVoBuilder<ExcelImportResultVo>().success(studentInfoListener.getExcelImportResultVo()).build();
    }
}
