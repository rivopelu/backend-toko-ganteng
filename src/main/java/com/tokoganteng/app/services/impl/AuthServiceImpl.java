package com.tokoganteng.app.services.impl;

import com.tokoganteng.app.dto.request.RequestSignIn;
import com.tokoganteng.app.dto.request.RequestSignUp;
import com.tokoganteng.app.dto.response.ResponseSignIn;
import com.tokoganteng.app.entities.Account;
import com.tokoganteng.app.enums.AccountRoleEnum;
import com.tokoganteng.app.enums.SignUpTypeEnum;
import com.tokoganteng.app.exception.BadRequestException;
import com.tokoganteng.app.exception.SystemErrorException;
import com.tokoganteng.app.repositories.AccountRepository;
import com.tokoganteng.app.services.AuthService;
import com.tokoganteng.app.services.JwtService;
import com.tokoganteng.app.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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

    @Override
    public ResponseSignIn signInUser(RequestSignIn req) {
        Optional<Account> account = accountRepository.findAccountQuery(req.getEmail());
        if (account.isEmpty()) {
            throw new BadRequestException("Sign in failed");
        }
        try {
            return buildSignIn(account.get(), req.getPassword());
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }


    private ResponseSignIn buildSignIn(Account account, String password) {
        Authentication authentication;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(account.getUsername(), password));
        if (!authentication.isAuthenticated()) {
            throw new BadRequestException("Sign in failed");
        }
        try {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            return ResponseSignIn.builder().accessToken(jwt).build();
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }
}
