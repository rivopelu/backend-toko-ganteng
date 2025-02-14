package com.tokoganteng.app.services.impl;

import com.tokoganteng.app.dto.request.RequestSignUp;
import com.tokoganteng.app.entities.Account;
import com.tokoganteng.app.enums.AccountRoleEnum;
import com.tokoganteng.app.enums.SignUpTypeEnum;
import com.tokoganteng.app.exception.BadRequestException;
import com.tokoganteng.app.exception.SystemErrorException;
import com.tokoganteng.app.repositories.AccountRepository;
import com.tokoganteng.app.services.AuthService;
import com.tokoganteng.app.utils.EntityUtils;
import jakarta.transaction.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String ping() {
        return "PONG";
    }

    @Override
    public String signUp(RequestSignUp req) {
        String encodedPassword = passwordEncoder.encode(req.getPassword());

        boolean checkExistingEmail = accountRepository.existsByEmail(req.getEmail());
        boolean checkExistingPhoneNumber = accountRepository.existsByPhoneNumber(req.getPhoneNumber());

        if (checkExistingEmail) {
            throw new BadRequestException("Email Already Exists");
        }

        if (checkExistingPhoneNumber) {
            throw new BadRequestException("Phone Number Already Exists");
        }

        Account account = Account.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(encodedPassword)
                .phoneNumber(req.getPhoneNumber())
                .role(AccountRoleEnum.USER)
                .signUpType(SignUpTypeEnum.EMAIL)
                .build();
        EntityUtils.created(account, "SYSTEM");
        try {
            accountRepository.save(account);
            return "Account success created";

        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }
}
