package com.nep.service.impl;

import com.nep.entity.GridMember;
import com.nep.io.FileIO;
import com.nep.service.GridMemberService;

import java.util.List;

public class GridMemberServiceImpl implements GridMemberService {
    @Override
    public GridMember login(String loginCode, String password) {
        // TODO Auto-generated method stub
        List<GridMember> glist = (List<GridMember>) FileIO.readObject("gridmember.txt");
        for(GridMember gm : glist){
            if(gm.getLoginCode().equals(loginCode) && gm.getPassword().equals(password)){
                return gm;
            }
        }
        return null;
    }
}
