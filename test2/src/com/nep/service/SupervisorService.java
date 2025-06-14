package com.nep.service;

import com.nep.entity.Supervisor;

public interface SupervisorService {
    public Supervisor login(String loginCode,String password);
    public boolean register(Supervisor supervisor);
}