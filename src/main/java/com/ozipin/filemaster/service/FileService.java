package com.ozipin.filemaster.service;

import java.io.File;

public interface FileService {

    /**
     * 保存文件
     *
     * @param tempFile 文件(不为空)
     * @return long
     */
    long saveFile(File tempFile);

    /**
     * 通过id获取文件
     *
     * @param id id
     * @return {@link File}
     */
    File getFileById(Long id);
}
