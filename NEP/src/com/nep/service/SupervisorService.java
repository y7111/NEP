package com.nep.service;

import com.nep.entity.Supervisor;

public interface SupervisorService {
    /**
     * 公众监督员登录功能
     * @return
     */
    public boolean login(String loginCode,String password);
    /**
     * 公共监督员注册功能
     * 子类重写父类的方法，实现类重写接口方法
     * 访问权限修饰符
     */
    boolean register(Supervisor supervisor);
}