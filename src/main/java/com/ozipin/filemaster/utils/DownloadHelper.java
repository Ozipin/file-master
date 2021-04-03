package com.ozipin.filemaster.utils;

import lombok.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class DownloadHelper {

    private DownloadHelper(){

    }

    /**
     * 准备下载
     *
     * @param response 响应
     * @param fileName 文件名称
     * @return {@link OutputStream}
     * @throws IOException ioexception
     */
    public static void prepareDownload(@NonNull HttpServletResponse response,
                                               @NonNull String fileName) throws IOException {
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        // 告诉浏览器输出内容为流
        response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
        response.addHeader("Content-Disposition", String.format("attachment;filename=%s", fileName));
    }
}
