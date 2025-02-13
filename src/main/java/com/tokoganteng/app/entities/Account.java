package com.tokoganteng.app.entities;


import com.tokoganteng.app.enums.AccountRoleEnum;
import com.tokoganteng.app.enums.SignUpTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private AccountRoleEnum role;

    @Column(name = "sign_up_type")
    @Enumerated
    private SignUpTypeEnum signUpType;

}
