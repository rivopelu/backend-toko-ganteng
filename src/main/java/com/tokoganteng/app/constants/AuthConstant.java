package com.tokoganteng.app.constants;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AuthConstant {
    public static String HEADER_X_WHO = "x_who";
    public static String HEADER_X_ROLE = "x_role";


    @Value("${auth.secret}")
    public String SECRET;

    @Value("${auth.expiration_time}")
    public  long EXPIRATION_TIME;
}


