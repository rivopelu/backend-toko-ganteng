package com.tokoganteng.app.services;

import com.tokoganteng.app.dto.request.RequestSignIn;
import com.tokoganteng.app.dto.request.RequestSignUp;
import com.tokoganteng.app.dto.response.ResponseSignIn;

public interface AuthService {

    String ping();

    String signUp(RequestSignUp req);

    ResponseSignIn signInUser(RequestSignIn req);
}
