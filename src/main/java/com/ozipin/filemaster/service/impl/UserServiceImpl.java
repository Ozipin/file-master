package com.ozipin.filemaster.service.impl;

import com.ozipin.filemaster.excel.modal.StudentInfoModal;
import com.ozipin.filemaster.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public Long countByNo(String no) {
        //实际业务请自行实现 此处只做模拟
        if(Objects.equals(no, "2021111101")){
            return 1L;
        }
        return 0L;
    }

    @Override
    public void saveBatch(List<StudentInfoModal> validModals) {
        //视持久层框架自行实现
        return;
    }
}
