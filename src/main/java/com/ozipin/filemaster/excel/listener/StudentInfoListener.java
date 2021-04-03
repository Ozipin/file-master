package com.ozipin.filemaster.excel.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ozipin.filemaster.common.ConstantStr;
import com.ozipin.filemaster.domain.vo.ExcelImportResultVo;
import com.ozipin.filemaster.excel.modal.StudentInfoResultModal;
import com.ozipin.filemaster.excel.modal.StudentInfoModal;
import com.ozipin.filemaster.service.FileService;
import com.ozipin.filemaster.service.UserService;
import com.ozipin.filemaster.utils.ReflectUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
public class StudentInfoListener extends AnalysisEventListener<StudentInfoModal> {

    private static final Validator javaxValidator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final SpringValidatorAdapter validator = new SpringValidatorAdapter(javaxValidator);

    private ExcelImportResultVo excelImportResultVo = new ExcelImportResultVo();

    private UserService userService;

    private FileService fileService;
    //验证通过的数据  最后用于保存
    private List<StudentInfoModal> validModals = Lists.newArrayList();
    //验证结果
    private List<StudentInfoResultModal> resultModals = Lists.newArrayList();

    public StudentInfoListener(UserService userService, FileService fileService){
        this.userService = userService;
        this.fileService = fileService;
    }

    @Override
    public void invoke(StudentInfoModal studentInfoModal, AnalysisContext analysisContext) {
        log.info("invoke#解析到一条数据: {}", studentInfoModal);
        //错误信息合集
        List<String> errorMsgs = Lists.newArrayList();
        //数据基本格式验证
        baseInfoCheck(studentInfoModal, errorMsgs);
        //业务逻辑验证
        logicVerification(studentInfoModal, errorMsgs);
        if(CollectionUtils.isEmpty(errorMsgs)){
            validModals.add(studentInfoModal);
        }
        StudentInfoResultModal resultModal = new StudentInfoResultModal.StudentInfoErrorModalBuilder()
                .modal(studentInfoModal)
                .errorMsgs(errorMsgs).build();
        resultModals.add(resultModal);
        //视数据量决定在这里落库还是在 doAfterAllAnalysed 方法里落库
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("doAfterAllAnalysed#excel数据解析完毕...");
        if(validModals.size() == resultModals.size()){
            //所有的数据验证通过
            excelImportResultVo.setResultDesc("导入成功");
        }else{
            try {
                excelImportResultVo.setResultDesc(String.format("此次导入总数据%s条,成功%s条,失败%s条,详情可见文档", resultModals.size(),
                        validModals.size(), resultModals.size()- validModals.size()));
                // 创建临时文件
                File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ConstantStr.EXCEL_SUFFIX);
                log.info("临时文件所在的本地路径：{}", tempFile.getCanonicalPath());
                EasyExcel.write(tempFile, StudentInfoResultModal.class)
                        .sheet(ConstantStr.STUDENT_INFO_IMPORT_TEMPLATE)
                        .doWrite(resultModals);
                long fileId = fileService.saveFile(tempFile);
                excelImportResultVo.setErrorFileId(fileId);
            } catch (IOException ex) {
                log.error("doAfterAllAnalysed#Exception: {}-{}", ex.getMessage(), ex);
                //抛出其他异常最终都会被处理为ExcelAnalysisException 展示的异常信息会有问题
                //直接抛出ExcelAnalysisException可以解决
                throw new ExcelAnalysisException("导入详情文件生成失败，请联系相关管理员进行处理");
            }
        }
        if(CollectionUtils.isNotEmpty(validModals)){
            userService.saveBatch(validModals);
        }
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //表头校验
        log.info("解析到一条表头：{}", JSONObject.toJSONString(headMap));
        Map<Integer, String> reallyMap = ReflectUtils.getExcelHeadMap(StudentInfoModal.class);
        StringBuffer headErrorStr = new StringBuffer();
        for (int i = 0; i < headMap.size(); i++) {
            if (!headMap.get(i).equals(reallyMap.get(i))) {
                headErrorStr.append(String.format("表头第%s列[%s]应该为[%s]", i + 1, headMap.get(i), reallyMap.get(i)));
            }
        }
        if (StringUtils.isNotBlank(headErrorStr.toString())) {
            throw new ExcelAnalysisException(headErrorStr.toString());
        }
    }

    /**
     * 基本信息检查
     *
     * @param studentInfoModal 学生信息(不为空)
     * @param errorMsgs        错误信息集合(不为空)
     */
    private void baseInfoCheck(@NonNull StudentInfoModal studentInfoModal, @NonNull List<String> errorMsgs){
        log.info("baseInfoCheck# studentInfoModal:{}", studentInfoModal);
        Errors errors = new BeanPropertyBindingResult(studentInfoModal, studentInfoModal.getClass().getName());
        validator.validate(studentInfoModal, errors);
        List<ObjectError> allErrors = errors.getAllErrors();
        if(CollectionUtils.isNotEmpty(allErrors)){
            allErrors.forEach(error -> errorMsgs.add(error.getDefaultMessage()));
        }
        //非注解可完成的可以加在此处
        if(StringUtils.isNotBlank(studentInfoModal.getHeight())){
            if(NumberUtils.isCreatable(studentInfoModal.getHeight())){
                Double height = Double.valueOf(studentInfoModal.getHeight());
                if(height > 5.00D){
                    errorMsgs.add("身高值异常");
                }
            }else{
                errorMsgs.add("身高只能是数字");
            }
        }
        if(StringUtils.isNotBlank(studentInfoModal.getWeight())){
            if(NumberUtils.isCreatable(studentInfoModal.getWeight())){
                Double weight = Double.valueOf(studentInfoModal.getWeight());
                if(weight > 1000.00D){
                    errorMsgs.add("体重值异常");
                }
            }else{
                errorMsgs.add("体重只能是数字");
            }
        }
    }

    /**
     * 业务逻辑验证
     *
     * @param studentInfoModal 学生信息(不为空)
     * @param errorMsgs        错误信息集合(不为空)
     */
    private void logicVerification(@NonNull StudentInfoModal studentInfoModal, @NonNull List<String> errorMsgs){
        Long count = userService.countByNo(studentInfoModal.getNo());
        if(Objects.nonNull(count) && count > 0L){
            errorMsgs.add(String.format("学生[%s]的信息已存在", studentInfoModal.getNo()));
        }
    }
}
