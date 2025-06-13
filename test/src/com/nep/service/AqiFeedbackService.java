package com.nep.service;

import com.nep.entity.AqiFeedback;

public interface AqiFeedbackService {
    /**
     * 添加反馈信息
     * @param afb
     */
    public void saveFeedBack(AqiFeedback afb);
}
