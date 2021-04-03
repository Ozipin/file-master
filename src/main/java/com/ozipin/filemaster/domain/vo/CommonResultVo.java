package com.ozipin.filemaster.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回结果
 *
 * @author ozipin
 * @date 2021/03/31
 */
@ApiModel(value = "通用返回结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResultVo<T> {

    @ApiModelProperty(value = "状态码", example = "200")
    private Integer code;

    @ApiModelProperty(value = "是否成功", example = "true")
    private Boolean success;

    @ApiModelProperty(value = "时间戳")
    private String timestamp = String.valueOf(System.currentTimeMillis());

    @ApiModelProperty(value = "数据", example = "{}")
    private T data;
}
