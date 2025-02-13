package com.tokoganteng.app.repositories;

import com.tokoganteng.app.entities.CredentialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialAccountRepository extends JpaRepository<CredentialAccount, String> {
}
