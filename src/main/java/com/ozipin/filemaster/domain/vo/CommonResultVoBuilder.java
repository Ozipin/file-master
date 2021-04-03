package com.ozipin.filemaster.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

/**
 * 通用返回结果构造器
 *
 * @author ozipin
 * @date 2021/03/31
 */
@ApiModel(value = "通用返回结果构造器")
public class CommonResultVoBuilder<T> {

    @ApiModelProperty(value = "状态码", example = "200")
    private Integer code;

    @ApiModelProperty(value = "是否成功", example = "true")
    private Boolean success;

    @ApiModelProperty(value = "时间戳")
    private String timestamp = String.valueOf(System.currentTimeMillis());

    @ApiModelProperty(value = "数据", example = "{}")
    private T data;

    public CommonResultVoBuilder<T> success(T data){
        this.success = true;
        this.code = HttpStatus.OK.value();
        this.data = data;
        return this;
    }

    public CommonResultVoBuilder<T> fail(T data){
        this.success = false;
        this.data = data;
        return this;
    }

    public CommonResultVoBuilder<T> fail(T data, int code){
        this.code = code;
        return fail(data);
    }

    public CommonResultVo<T> build(){
        return new CommonResultVo<T>(code, success, timestamp, data);
    }
}
