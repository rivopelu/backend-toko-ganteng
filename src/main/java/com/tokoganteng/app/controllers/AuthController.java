package com.tokoganteng.app.controllers;

import com.tokoganteng.app.annotations.BaseController;
import com.tokoganteng.app.annotations.PublicAccess;
import com.tokoganteng.app.dto.request.RequestSignIn;
import com.tokoganteng.app.dto.request.RequestSignUp;
import com.tokoganteng.app.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@BaseController("auth")
public interface AuthController {

    @PublicAccess
    @GetMapping("ping")
    BaseResponse ping();

    @PublicAccess
    @PostMapping("v1/user/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    BaseResponse signUp(@RequestBody() RequestSignUp req);

    @PublicAccess
    @PostMapping("v1/user/sign-in")
    BaseResponse signInUser(@RequestBody() RequestSignIn req);

}
