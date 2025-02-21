package com.tokoganteng.app.constants;


import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AuthConstant {
    public static String HEADER_X_WHO = "x_who";
    public static String HEADER_X_ROLE = "x_role";

}


