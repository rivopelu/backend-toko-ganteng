package com.tokoganteng.app.controllers.impl;

import com.tokoganteng.app.annotations.BaseController;
import com.tokoganteng.app.annotations.BaseControllerImpl;
import com.tokoganteng.app.controllers.AuthController;
import com.tokoganteng.app.dto.response.BaseResponse;
import com.tokoganteng.app.services.AuthService;
import com.tokoganteng.app.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;

@BaseControllerImpl
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public BaseResponse ping() {
        return ResponseHelper.createBaseResponse(authService.ping());
    }
}
