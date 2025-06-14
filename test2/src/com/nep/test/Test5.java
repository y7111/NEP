package com.nep.test;

import com.nep.entity.Supervisor;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;

public class Test5 {
    public static void main(String[] args) {
        SupervisorService supervisorService = new SupervisorServiceImpl();

        Supervisor s1 = new Supervisor();
        s1.setLoginCode("202501");
        s1.setPassword("123");
        s1.setRealName("tom");
        s1.setSex("男");

        Supervisor s2 = new Supervisor();
        s2.setLoginCode("202502");
        s2.setPassword("123");
        s2.setRealName("jack");
        s2.setSex("男");

        Supervisor s3 = new Supervisor();
        s3.setLoginCode("202503");
        s3.setPassword("123");
        s3.setRealName("rose");
        s3.setSex("女");

        System.out.println("注册用户 tom: " + supervisorService.register(s1));
        System.out.println("注册用户 jack: " + supervisorService.register(s2));
        System.out.println("注册用户 rose: " + supervisorService.register(s3));

        System.out.println("再次注册用户 tom: " + supervisorService.register(s1));

        System.out.println("登录 tom: " + (supervisorService.login("202501", "123") != null ? "成功" : "失败"));
        System.out.println("登录错误用户: " + (supervisorService.login("nonexistent", "password") != null ? "成功" : "失败"));
    }
}