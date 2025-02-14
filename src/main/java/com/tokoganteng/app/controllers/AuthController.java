package com.tokoganteng.app.controllers;

import com.tokoganteng.app.annotations.BaseController;
import com.tokoganteng.app.annotations.PublicAccess;
import com.tokoganteng.app.dto.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;

@BaseController("auth")
public interface AuthController {

    @PublicAccess
    @GetMapping("ping")
    BaseResponse ping();

}
