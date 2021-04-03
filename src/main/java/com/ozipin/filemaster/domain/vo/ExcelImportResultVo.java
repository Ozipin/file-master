package com.ozipin.filemaster.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传签证官
 *
 * @author ozipin
 * @date 2021/03/31
 */
@ApiModel(value = "excel导入结果")
@Data
public class ExcelImportResultVo {

    /**
     * 概述导入成功的条数和失败的条数
     */
    @ApiModelProperty(value = "失败描述")
    public String resultDesc;

    /**
     * 对导入失败的每一行数据进行备注失败原因，生成新的文件的id，用于下载
     */
    @ApiModelProperty(value = "错误信息文件id")
    private Long errorFileId;
}
