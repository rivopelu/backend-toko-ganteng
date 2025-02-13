package com.tokoganteng.app.controllers.impl;

import com.tokoganteng.app.annotations.BaseController;
import com.tokoganteng.app.annotations.BaseControllerImpl;
import com.tokoganteng.app.controllers.AuthController;
import com.tokoganteng.app.dto.response.BaseResponse;

@BaseControllerImpl
public class AuthControllerImpl implements AuthController {
    @Override
    public BaseResponse ping() {
        return null;
    }
}
