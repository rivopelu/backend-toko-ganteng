package com.tokoganteng.app.services;

import com.tokoganteng.app.dto.request.RequestSignUp;

public interface AuthService {

    String ping();

    String signUp(RequestSignUp req);
}
