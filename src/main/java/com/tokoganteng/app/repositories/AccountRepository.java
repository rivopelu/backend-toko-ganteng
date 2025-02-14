package com.tokoganteng.app.repositories;

import com.tokoganteng.app.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmailAndActiveIsTrue(String email);

    @Query("select a from Account  as a where a.email = 'rivopelu@gmail.com' and a.active = true")
    Optional<Account> findAccountQuery(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Account> findByPhoneNumber(String phoneNumber);
}
