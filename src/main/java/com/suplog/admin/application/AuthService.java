package com.suplog.admin.application;

import com.suplog.admin.application.dto.LoginCommand;
import com.suplog.admin.application.dto.LoginResult;
import com.suplog.admin.domain.Admin;
import com.suplog.admin.domain.AdminRepository;
import com.suplog.admin.domain.exception.AdminNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResult login(LoginCommand command) {
        Admin admin = adminRepository.findByUsername(command.username())
                .orElseThrow(AdminNotFoundException::new);

        if (!passwordEncoder.matches(command.password(), admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginResult(admin.getUsername(), true);
    }

}
