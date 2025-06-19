package com.nep.service;

import com.nep.entity.Supervisor;

public interface SupervisorService {
    /**
     * 公众监督员登录功能
     */
    public boolean login(String loginCode,String password);

    /**
     * 公众监督员注册功能
     */
    public boolean register(Supervisor supervisor);
    /**
     * 验证用户是否存在
     */
    public boolean checkUserExists(String loginCode, String phone);

    Supervisor getSupervisorByLoginCode(String phone);
}
