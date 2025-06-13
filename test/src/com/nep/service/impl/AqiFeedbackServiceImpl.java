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
}
