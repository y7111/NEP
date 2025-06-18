package com.nep.service.impl;

import com.nep.entity.Admin;
import com.nep.io.FileIO;
import com.nep.service.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    @Override
    public boolean login(String loginCode, String password) {
        // TODO Auto-generated method stub
        List<Admin> alist =(List<Admin>) FileIO.readObject("admin.txt");
        for(Admin a : alist){
            if(a.getLoginCode().equals(loginCode) && a.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
}