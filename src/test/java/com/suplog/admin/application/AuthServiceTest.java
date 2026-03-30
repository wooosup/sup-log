package com.suplog.admin.application;

import com.suplog.admin.application.dto.LoginCommand;
import com.suplog.admin.application.dto.LoginResult;
import com.suplog.admin.domain.Admin;
import com.suplog.admin.infrastructure.persistence.JpaAdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private JpaAdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("로그인 성공")
    @Test
    void loginSuccess() {
        //given
        adminRepository.save(Admin.builder()
                .username("wss3325")
                .password(passwordEncoder.encode("1234"))
                .build());

        //when
        LoginResult response = authService.login(new LoginCommand("wss3325", "1234"));

        //then
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("wss3325");
        assertThat(response.authenticated()).isTrue();
    }

    @DisplayName("비밀번호가 일치하지 않으면 로그인이 실패한다.")
    @Test
    void loginFail() {
        //given
        adminRepository.save(Admin.builder()
                .username("wss3325")
                .password(passwordEncoder.encode("1234"))
                .build());

        //expected
        assertThatThrownBy(() -> authService.login(new LoginCommand("wss3325", "333333")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("존재하지 않는 관리자로 로그인하면 예외가 발생한다.")
    @Test
    void loginFailWhenAdminMissing() {
        assertThatThrownBy(() -> authService.login(new LoginCommand("missing", "1234")))
                .hasMessage("관리자를 찾을 수 없습니다.");
    }
}
