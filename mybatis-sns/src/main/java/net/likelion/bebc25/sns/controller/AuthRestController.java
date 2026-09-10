package net.likelion.bebc25.sns.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.sns.dto.LoginRequest;
import net.likelion.bebc25.sns.dto.TokenResponse;
import net.likelion.bebc25.sns.security.jwt.JwtProvider;
import net.likelion.bebc25.sns.security.principal.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        // 1. 클라이언트가 입력한 이메일과 비밀번호로 미인증 토큰 생성
        Authentication unauthenticatedToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        // 2. AuthenticationManager를 통한 인증 검증 위임
        Authentication authentication = authenticationManager.authenticate(unauthenticatedToken);

        // 3. 인증된 Principal로부터 회원 상세 정보 추출
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = userDetails.getMember().getId();
        String email = userDetails.getUsername();
        String role = userDetails.getMember().getRole();

        // 4. JWT 토큰 생성
        String accessToken = jwtProvider.createAccessToken(memberId, email, role);
        String refreshToken = jwtProvider.createRefreshToken(memberId);

        // 5. 발급된 토큰 응답 반환 (Access Token 유효기간 1시간 = 3600초)
        TokenResponse response = TokenResponse.of(accessToken, refreshToken, 3600L);
        return ResponseEntity.ok(response);
    }
}