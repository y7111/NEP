package com.nep.service;

import com.nep.entity.Supervisor;

public interface SupervisorService {
    public Supervisor login(String loginCode,String password);
    public boolean register(Supervisor supervisor);
    // 新增：更新 Supervisor 信息的方法
    public boolean updateSupervisor(Supervisor supervisor);
    // 新增：根据登录码获取 Supervisor 对象（包含密保信息）
    public Supervisor getSupervisorByLoginCode(String loginCode);

}