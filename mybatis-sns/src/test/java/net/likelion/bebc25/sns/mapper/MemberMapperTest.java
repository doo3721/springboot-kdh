package net.likelion.bebc25.sns.mapper;

import net.likelion.bebc25.sns.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("이메일로 회원 정보 조회 테스트")
    void findByEmailTest() {
        // given
        String email = "user1@example.com";

        // when
        Member member = memberMapper.findByEmail(email);

        // then
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
    }

}
