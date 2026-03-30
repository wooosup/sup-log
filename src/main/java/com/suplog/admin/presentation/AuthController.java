package com.suplog.admin.presentation;

import com.suplog.admin.application.AuthService;
import com.suplog.admin.application.dto.LoginResult;
import com.suplog.admin.presentation.request.LoginRequest;
import com.suplog.admin.presentation.response.LoginResponse;
import com.suplog.global.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpServletRequest
    ) {
        LoginResult result = authService.login(request.toCommand());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        result.username(),
                        null,
                        AuthorityUtils.createAuthorityList("ROLE_ADMIN")
                );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpSession existingSession = httpServletRequest.getSession(false);
        if (existingSession != null) {
            existingSession.invalidate();
        }
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return ApiResponse.ok(LoginResponse.from(result));
    }

}
