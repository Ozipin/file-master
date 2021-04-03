package com.ozipin.filemaster.excel.modal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 学生信息模板
 * 身高与体重可以在后续地方进行验证  或者自定义一个验证注解进行注解
 *
 * @author ozipin
 * @date 2021/03/31
 */
@Data
//@Accessors(chain = true)
@ColumnWidth(15)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentRowHeight(20)
public class StudentInfoModal {

    @HeadStyle(fillForegroundColor = 10)
    @ExcelProperty(value = "学号(必填)")
    @NotBlank(message = "请填写学号")
    @Size(min = 10, max = 10, message = "学号只能为10位")
    private String no;

    @HeadStyle(fillForegroundColor = 10)
    @ExcelProperty(value = "姓名(必填)")
    @NotBlank(message = "请填写姓名")
    @Size(min = 2, max = 5, message = "姓名长度只能在2-5位")
    private String name;

    @HeadStyle(fillForegroundColor = 10)
    @ExcelProperty(value = "性别(必填)(男|女)")
    @NotBlank(message = "请填写性别")
    @Pattern(regexp = "(男|女)", message = "请填写正确的性别字段")
    private String sex;

    @HeadStyle(fillForegroundColor = 13)
    @ExcelProperty(value = "身高(M)")
    private String height;

    @HeadStyle(fillForegroundColor = 13)
    @ExcelProperty(value = "体重(KG)")
    private String weight;

    public static StudentInfoModal getExampleData(){
        StudentInfoModal studentInfoModal = new StudentInfoModal();
        studentInfoModal.setNo("(示例数据)2021111101");
        studentInfoModal.setName("(示例数据)张三");
        studentInfoModal.setSex("(示例数据)男");
        studentInfoModal.setHeight("(示例数据)1.80");
        studentInfoModal.setWeight("(示例数据)70");
        return studentInfoModal;
    }
}
