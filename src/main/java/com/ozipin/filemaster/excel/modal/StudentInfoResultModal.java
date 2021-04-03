package com.ozipin.filemaster.excel.modal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 学生信息错误模板
 *
 * @author ozipin
 * @date 2021/04/01
 */
@Data
@ColumnWidth(15)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentRowHeight(20)
@NoArgsConstructor
@AllArgsConstructor()
public class StudentInfoResultModal extends StudentInfoModal{

    @ExcelProperty(value = "导入结果", index = 5)
    public String result;

    @ExcelProperty(value = "错误信息", index = 6)
    public String errorMsg;

    /**
     * 学生信息错误模板构建器
     *
     * @author ozipin
     * @date 2021/04/01
     */
    public static class StudentInfoErrorModalBuilder{

        private StudentInfoModal studentInfoModal = new StudentInfoModal();

        private String result;

        private String errorMsg;

        public StudentInfoErrorModalBuilder modal(StudentInfoModal studentInfoModal){
            this.studentInfoModal = studentInfoModal;
            return this;
        }

        public StudentInfoErrorModalBuilder errorMsgs(List<String> errorMsgs){
            if(CollectionUtils.isNotEmpty(errorMsgs)){
                this.result = "导入失败";
                this.errorMsg = String.join(";\n", errorMsgs);
            }else{
                this.result = "导入成功";
            }
            return this;
        }

        public StudentInfoResultModal build(){
            StudentInfoResultModal studentInfoResultModal = new StudentInfoResultModal(this.result, this.errorMsg);
            BeanUtils.copyProperties(this.studentInfoModal, studentInfoResultModal);
            return studentInfoResultModal;
        }
    }
}
