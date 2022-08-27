package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
/*데이터 변경에 관련된 것은 transaction 안에서 진행되어야한다.(그래야 lazy loading도 가능)
* 단순 조회(읽기)에서는 transaction을 readOnly = true로 주는 것이 더 최적화이다
* 그런데 클래스단에서 선언을 해버리면 쓰기나 변경 작업에서도 다 적용되니 메소드 단에서 따로 @Transactional 선언해줌*/
@Transactional(readOnly = true)
@RequiredArgsConstructor //final이 붙은 것만 생성자의 인자로 넣어줌(AllArgsConstructor와 다른점)
public class MemberService {

    //변경 어차피 안 할 거니까 final 처리하는게 compile시에도 에러체크 도움이 된다
    private final MemberRepository memberRepository;

    //회원가입
    @Transactional
    public Long join(Member member){
        //중복된 유저인지 확인
        validDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    //중복 검사
    private void validDuplicatedMember(Member member) {
        List<Member> foundMember=memberRepository.findAllByName(member.getName());
        //동시에 같은 name이 들어올 수도 있으니 아예 엔티티에서 unique조건을 달아주는게 좋다!
        if(!foundMember.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    //회원 단건 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
