package com.nep.service.impl;

import com.nep.controller.NepsSelectAqiViewController;
import com.nep.entity.Supervisor;
import com.nep.io.FileIO;
import com.nep.service.SupervisorService;

import java.util.ArrayList;
import java.util.List;

public class SupervisorServiceImpl implements SupervisorService {
    @Override

    public boolean login(String loginCode,String password) {
        // TODO Auto-generated method stub
        List<Supervisor> slist =(List<Supervisor>) FileIO.readObject("supervisor.txt");
        boolean isLogin = false;
        for(Supervisor s:slist){
            if(s.getLoginCode().equals(loginCode) && s.getPassword().equals(password)){
                NepsSelectAqiViewController.supervisor=s;
                return true;
            }
        }
        return false;
    }


    /**
     * 公共监督员注册功能
     * 子类重写父类的方法，实现类重写接口方法
     * 访问权限修饰符
     *
     * @param supervisor
     */
    @Override
    public boolean register(Supervisor supervisor) {
        List<Supervisor > list = (ArrayList<Supervisor>) FileIO.readObject("supervisor.txt");
        for(Supervisor s : list){
            if(s.getLoginCode().equals(supervisor.getLoginCode())){
                return false;
            }
        }

        list.add(supervisor);
        FileIO.writeObject("supervisor.txt", list);
        return true;
    }

}