package com.nep.test;

import com.nep.entity.Admin;
import com.nep.service.impl.AdminServiceImpl;

public class Test1 {
    public static void main(String[] args) {
        AdminServiceImpl adminService = new AdminServiceImpl();

        Admin a1 = new Admin();
        a1.setLoginCode("1001");
        a1.setPassword("111");
        a1.setRealName("赵本山");

        Admin a2 = new Admin();
        a2.setLoginCode("1002");
        a2.setPassword("222");
        a2.setRealName("刘德华");

        System.out.println("注册管理员 " + a1.getRealName() + ": " + adminService.register(a1));
        System.out.println("注册管理员 " + a2.getRealName() + ": " + adminService.register(a2));
        System.out.println("再次注册管理员 " + a1.getRealName() + ": " + adminService.register(a1));

        System.out.println("登录管理员 " + a1.getLoginCode() + ": " + adminService.login(a1.getLoginCode(), a1.getPassword()));
        System.out.println("登录管理员 " + a2.getLoginCode() + ": " + adminService.login(a2.getLoginCode(), a2.getPassword()));
        System.out.println("登录错误账号: " + adminService.login("nonexistent", "password"));
    }
}