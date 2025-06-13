package com.nep.service.impl;

import com.nep.controller.NepsSelectAqiViewController;
import com.nep.entity.Supervisor;
import com.nep.io.FileIO;
import com.nep.service.SupervisorService;

import java.util.List;

public class SupervisorServiceImpl implements SupervisorService {
    @Override
    public boolean login(String loginCode, String password) {
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

    @Override
    public boolean register(Supervisor supervisor) {
        List<Supervisor> slist = (List<Supervisor>)FileIO.readObject("supervisor.txt");
        for(Supervisor s:slist){
            if(s.getLoginCode().equals(supervisor.getLoginCode())&&s.getPassword().equals(supervisor.getPassword())){
//
                return false;
            }
        }
        slist.add(supervisor);
        FileIO.writeObject("supervisor.txt", slist);
        return true;
    }
}
