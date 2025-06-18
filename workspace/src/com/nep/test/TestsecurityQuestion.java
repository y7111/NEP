package com.nep.test;

import com.nep.entity.securityQuestion;
import com.nep.entity.Supervisor;
import com.nep.io.FileIO;

import java.util.ArrayList;
import java.util.List;
public class TestsecurityQuestion {
    public static void main(String[] args) {
        List<Supervisor> slist = new ArrayList<Supervisor>();
        securityQuestion a1 = new securityQuestion("您的出生地是哪里?");
        securityQuestion a2 = new securityQuestion("您母亲的姓名是什么?");
        securityQuestion a3 = new securityQuestion("您宠物的名字是什么?");
        securityQuestion a4 = new securityQuestion("您最喜欢的颜色是什么?");
        List<securityQuestion> elist = new ArrayList<securityQuestion>();
        elist.add(a1);
        elist.add(a2);
        elist.add(a3);
        elist.add(a4);
        FileIO.writeObject("securityQuestion.txt", elist);

    }
}

