package com.example.demo.user.service.port;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource("classpath:application.yml")
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void findByIdStatus_로_유저_데이터를_조회한다() {
        final var actual = repository.findByIdAndStatus(1, UserStatus.ACTIVE);

        assertThat(actual.isPresent()).isTrue();
    }

    @Test
    void findByIdStatus_는_데이터가_없으면_Optional_Empty를_반환한다() {
        final var actual = repository.findByIdAndStatus(1L, UserStatus.PENDING);

        assertThat(actual).isEmpty();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_조회한다() {
        final var actual = repository.findByEmailAndStatus("a@a.com", UserStatus.ACTIVE);

        assertThat(actual.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_Empty를_반환한다() {
        final var actual = repository.findByEmailAndStatus("a@a.com", UserStatus.PENDING);

        assertThat(actual).isEmpty();
    }

}
