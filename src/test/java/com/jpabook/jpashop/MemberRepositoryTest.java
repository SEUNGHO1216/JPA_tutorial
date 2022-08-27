/*
package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

//스프링과 관련된걸 테스트 할 거라는 annotation
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("seungho");
        //when
        Long saveId = memberRepository.save(member);
        Member savedMember = memberRepository.find(saveId);
        //then
        Assertions.assertThat(savedMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(savedMember.getUsername()).isEqualTo(member.getUsername());
        */
/*
        아래의 경우 따로 hashcode 로 equal 처리하지 않았기 때문에 == 비교로 봐도 됨
        * 이때 savedMember 와 member 는 같은 transaction안에서 같은 id값을 갖고 있음
        * 영속성 컨텍스트의 1차캐시 안에 동일하게 저장돼있는 친구
        *//*

        Assertions.assertThat(savedMember).isEqualTo(member);
    }
}
*/
