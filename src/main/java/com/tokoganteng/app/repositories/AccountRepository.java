package com.tokoganteng.app.repositories;

import com.tokoganteng.app.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
