package net.likelion.bebc25.jpasns.service;

import net.likelion.bebc25.jpasns.domain.Role;
import net.likelion.bebc25.jpasns.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 및 회원 조회")
    @Transactional
    void signUpAndGetMember() {
        String email = "haru@test.com";
        String password = "1234";
        String nickname = "하루";

        MemberDto.CreateRequest request = new MemberDto.CreateRequest(email, password, nickname);
        MemberDto.Response response = memberService.signUp(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.nickname()).isEqualTo(nickname);
        assertThat(response.role()).isEqualTo(Role.USER);
    }
}
