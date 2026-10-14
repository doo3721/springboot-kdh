package net.likelion.bebc25.jpasns.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.jpasns.domain.Member;
import net.likelion.bebc25.jpasns.dto.MemberDto;
import net.likelion.bebc25.jpasns.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberDto.Response signUp(MemberDto.CreateRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("사용중인 이메일입니다. email = " + request.email());
        }
        if (memberRepository.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("사용중인 닉네임입니다. nickname = " + request.nickname());
        }
        // 영속화 된 Member를 반환
        Member member = memberRepository.save(request.toEntity());
        return MemberDto.Response.from(member);
    }

    public MemberDto.Response getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id = " + id));
        return MemberDto.Response.from(member);
    }
}
