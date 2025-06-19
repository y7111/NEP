package com.nep.service.impl;

//import com.nep.controller.NepsFeedbackViewController;
//import com.nep.controller.NepsSelectAqiViewController;
import com.nep.entity.Supervisor;
import com.nep.io.FileIO;
import com.nep.service.SupervisorService;

import java.util.ArrayList;
import java.util.List;

public class SupervisorServiceImpl implements SupervisorService {
    @Override
    public boolean login(String loginCode, String password) {
        List<Supervisor> slist =(List<Supervisor>) FileIO.readObject("supervisor.txt");
        boolean isLogin = false;
        for(Supervisor s:slist){
            if(s.getLoginCode().equals(loginCode) && s.getPassword().equals(password)){
                //NepsSelectAqiViewController.supervisor=s;
                //NepsFeedbackViewController.supervisor = s;
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

    @Override
    public boolean checkUserExists(String loginCode, String phone) {
        List<Supervisor> supervisors = (List<Supervisor>) FileIO.readObject("supervisor.txt");
        if (supervisors == null) {
            supervisors = new ArrayList<>();
        }
        for (Supervisor supervisor : supervisors) {
            if (supervisor.getLoginCode().equals(loginCode) && supervisor.getLoginCode().equals(phone)) {
                return true;
            }
        }
        return false;
    }
    public Supervisor getSupervisorByLoginCode(String loginCode) {
        List<Supervisor> slist = (List<Supervisor>) FileIO.readObject("supervisor.txt");
        for (Supervisor s : slist) {
            if (s.getLoginCode().equals(loginCode)) {
                return s;
            }
        }
        return null;
    }
}

