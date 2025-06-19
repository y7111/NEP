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

        Supervisor s4 = new Supervisor();
        s4.setLoginCode("202509");
        s4.setPassword("supOprah");
        s4.setRealName("Oprah Winfrey");
        s4.setSex("女");

        Supervisor s5 = new Supervisor();
        s5.setLoginCode("202510");
        s5.setPassword("supTaylor");
        s5.setRealName("Taylor Swift");
        s5.setSex("女");

        Supervisor s6 = new Supervisor();
        s6.setLoginCode("202511");
        s6.setPassword("supMessi");
        s6.setRealName("Lionel Messi");
        s6.setSex("男");

        Supervisor s7 = new Supervisor();
        s7.setLoginCode("202512");
        s7.setPassword("supRonaldo");
        s7.setRealName("Cristiano Ronaldo");
        s7.setSex("男");

        Supervisor s8 = new Supervisor();
        s8.setLoginCode("202513");
        s8.setPassword("supAdele");
        s8.setRealName("Adele Adkins");
        s8.setSex("女");

        System.out.println(supervisorService.register(s1));
        System.out.println( supervisorService.register(s2));
        System.out.println( supervisorService.register(s3));
        System.out.println(  supervisorService.register(s4));
        System.out.println(  supervisorService.register(s5));
        System.out.println(supervisorService.register(s6));
        System.out.println(  supervisorService.register(s7));
        System.out.println(supervisorService.register(s8));

        System.out.println("登录 tom: " + (supervisorService.login("202501", "123") != null ? "成功" : "失败"));
        System.out.println("登录错误用户: " + (supervisorService.login("nonexistent", "password") != null ? "成功" : "失败"));
    }
}