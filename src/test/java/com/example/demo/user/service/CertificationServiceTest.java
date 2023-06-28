package com.example.demo.user.service;

import com.example.demo.user.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CertificationServiceTest {

    @Test
    void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {
        final var fakeMailSender = new FakeMailSender();
        final var service = new CertificationService(fakeMailSender);

        service.send("a@a.com", 1, "aaaa-aa-aaaaaa");

        assertAll(
                () -> assertThat(fakeMailSender.email).isEqualTo("a@a.com"),
                () -> assertThat(fakeMailSender.title).isEqualTo("Please certify your email address"),
                () -> assertThat(fakeMailSender.content).isEqualTo(
                        "Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaa-aa-aaaaaa"
                )
        );
    }

}
