package com.nep.service.impl;

import com.nep.entity.AqiFeedback;
import com.nep.io.FileIO;
import com.nep.service.AqiFeedbackService;

import java.util.List;

public class AqiFeedbackServiceImpl implements AqiFeedbackService {
    @Override
    public void saveFeedBack(AqiFeedback afb) {
        // TODO Auto-generated method stub
        List<AqiFeedback> afList = (List<AqiFeedback>)FileIO.readObject("aqifeedback.txt");
        afb.setAfId(afList.size()+1);
        afList.add(afb);
        FileIO.writeObject("aqifeedback.txt", afList);
    }
    @Override
    public void assignGridMember(String afId,String realName) {
        // TODO Auto-generated method stub
        List<AqiFeedback> alist = (List<AqiFeedback>) FileIO.readObject("aqifeedback.txt");
        for (AqiFeedback af : alist) {
            if(af.getAfId().toString().equals(afId)){
                af.setGmName(realName);
                af.setState("已指派");
                break;
            }
        }
        FileIO.writeObject("aqifeedback.txt", alist);
    }
    @Override
    public void confirmData(AqiFeedback afb) {
        // TODO Auto-generated method stub
        System.out.println(afb.getAfId());
        List<AqiFeedback> afList = (List<AqiFeedback>)FileIO.readObject("aqifeedback.txt");
        for(int i = 0; i< afList.size();i++){
            AqiFeedback a = afList.get(i);
            if(a.getGmName() != null && a.getGmName().equals(afb.getGmName()) && a.getAfId().intValue()==afb.getAfId().intValue()){
                a.setState(afb.getState());
                a.setConfirmDate(afb.getConfirmDate());
                a.setCo(afb.getCo());
                a.setSo2(afb.getSo2());
                a.setPm(afb.getPm());
                a.setConfirmLevel(afb.getConfirmLevel());
                a.setConfirmExplain(afb.getConfirmExplain());
                break;
            }
        }
        FileIO.writeObject("aqifeedback.txt", afList);
    }
}
