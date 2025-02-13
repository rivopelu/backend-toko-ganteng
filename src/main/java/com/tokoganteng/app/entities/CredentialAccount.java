package com.tokoganteng.app.entities;

import com.tokoganteng.app.enums.CredentialTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credential_account")
public class CredentialAccount {
    @Id
    private String id;

    @Column(name = "credential")
    private String credential;

    @Column(name = "credential_type")
    private CredentialTypeEnum type;

    @JoinColumn(name = "account_id")
    @ManyToOne
    private Account account;

}
