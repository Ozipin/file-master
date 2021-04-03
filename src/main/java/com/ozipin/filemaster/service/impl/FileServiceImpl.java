package com.ozipin.filemaster.service.impl;

import com.google.common.collect.Maps;
import com.ozipin.filemaster.exception.CommonException;
import com.ozipin.filemaster.service.FileService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private Map<Long, File> map = Maps.newHashMap();

    /**
     * 实际情况下的话视项目的文件存储服务而定 重要的是要返回一个能找到此文件的标识
     * 返回值不一定要拘泥于一个文件的id 也可能是一个key或者一个路径等等
     * 重要的是后面用户进行文件下载的时候他给了你这个id或者key或者路径等等你能找到正确的文件返回给他
     * @param tempFile
     * @return
     */
    @Override
    public long saveFile(File tempFile) {
        //此处只做演示
        Long id = System.currentTimeMillis();
        map.put(id, tempFile);
        return id;
    }


    @Override
    public File getFileById(Long id) {
        return map.get(id);
    }
}
