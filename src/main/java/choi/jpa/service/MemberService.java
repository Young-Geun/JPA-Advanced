package choi.jpa.service;

import choi.jpa.domain.Member;
import choi.jpa.repository.MemberJpaRepository;
import choi.jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // readOnly = true : 단순조회일 경우, 해당 옵션을 주면 JPA에서는 최적화가 된다.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberJpaRepository.findAll();
    }

    /**
     * 회원 조회
     */
    public Member findOne(Long memberId) {
        return memberJpaRepository.findById(memberId).get();
    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberJpaRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberJpaRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberJpaRepository.findById(id).get();
        member.setName(name);
    }

}
