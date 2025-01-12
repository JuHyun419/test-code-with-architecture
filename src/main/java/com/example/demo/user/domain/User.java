package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;
import java.util.UUID;

@Getter
public class User {

    private final Long id;
    private final String email;
    private String nickname;
    private String address;
    private final String certificationCode;
    private final UserStatus status;
    private final Long lastLoginAt;

    @Builder
    public User(Long id, String email, String nickname, String address, String certificationCode, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static User from(UserCreate userCreate) {
        return User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .certificationCode(UUID.randomUUID().toString())
                .build();
    }

    public void update(UserUpdate userUpdate) {
        this.nickname = userUpdate.getNickname();
        this.address = userUpdate.getAddress();
    }

    public User login() {
        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .status(status)
                .certificationCode(certificationCode)
                .lastLoginAt(Clock.systemUTC().millis())
                .build();
    }

    public User certificate(String certificationCode) {
        if (!this.certificationCode.equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .status(status)
                .certificationCode(certificationCode)
                .lastLoginAt(lastLoginAt)
                .build();
    }
}
