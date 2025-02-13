package com.tokoganteng.app.services.impl;

import com.tokoganteng.app.services.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String ping() {
        return "PONG";
    }
}
