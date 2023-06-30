package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService service;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아온다() {
        final var expected = "a@a.com";

        final var actual = service.getByEmail(expected);

        assertThat(actual.getEmail()).isEqualTo(expected);
    }

    @Test
    void getByEmail은_PENDING_상태인_유저는_찾아올_수_없다() {
        final var expected = "b@a.com";

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getByEmail(expected)
        );
    }

    @Test
    void UserCreateDto_를_이용하여_유저를_생성할_수_있다() {
        final var dto = UserCreate.builder()
                .email("c@a.com")
                .address("Gumi")
                .nickname("Hyun3")
                .build();

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        final var actual = service.createUser(dto);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getStatus()).isEqualTo(UserStatus.PENDING)
        );
    }

    @Test
    void user가_로그인하면_마지막_로그인_시간이_변경된다() {
        service.login(2);

        final var actual = service.getById(2);

        // TODO: 값 확인
        assertThat(actual.getLastLoginAt()).isGreaterThan(0L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        service.verifyEmail(2, "aaaaaa-aaaa-aaaa-aaaaa-aaaaaaaaaaaa");

        final var actual = service.getById(2);

        assertThat(actual.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        assertThrows(
                CertificationCodeNotMatchedException.class,
                () -> service.verifyEmail(3, "aaaaaa-aaaa-aaaa-aaaaa-aaaaaaaaaaac")
        );
    }

}
