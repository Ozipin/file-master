package com.ozipin.filemaster.service;

import com.ozipin.filemaster.excel.modal.StudentInfoModal;

import java.util.List;

public interface UserService {
    /**
     * 统计指定学号的学生的数量
     *
     * @param no no
     * @return {@link Long}
     */
    Long countByNo(String no);

    /**
     * 批量保存
     *
     * @param validModals 用户信息（不为空）
     */
    void saveBatch(List<StudentInfoModal> validModals);
}
