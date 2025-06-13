package com.nep.service.impl;

import com.nep.entity.AqiFeedback;
import com.nep.io.FileIO;
import com.nep.service.AqiFeedBackService;

import java.util.List;

public class AqiFeedBackServiceImpl implements AqiFeedBackService {
    @Override
    public void saveFeedBack(AqiFeedback afb) {
        List<AqiFeedback> list = (List<AqiFeedback>)FileIO.readObject("aqifeedback.txt");
        afb.setAfId(list.size()+1);
        list.add(afb);
        FileIO.writeObject("aqifeedback.txt",list);
    }
}
