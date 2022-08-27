package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)//spring이랑 엮어서 실행할 때
@SpringBootTest //위 2가지가 있어야 데이터베이스 연동해서 테스트 가능
@Transactional//test코드에서 @Transactional은 기본적으로 rollback이 true이다. 때문에 persist는 되지만 flush가 안 돼서 db저장 안 된다.
/*@Rollback(value = false)//그래서 이 어노테이션을 붙여줘야 insert쿼리가 나가면서 저장이 된다. 근데 여기 붙이면 모든 테스트코드에 다 적용됨...*/
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
  /*  @Autowired
    EntityManager em;*/

    @Test
    @Rollback(value = false)
    public void join() throws Exception{
        //given
        Member member = new Member();
        member.setName("Seungho");
        //when
        Long savedId = memberService.join(member);

        //then
        /*em.flush();//flush를 통해 commit은 진행하여 db에 들어가는 쿼리까지 보여주긴함. 그래도 rollback이 true여서 db저장은 안 됨.*/
        assertEquals(member, memberRepository.findOne(savedId));
        assertEquals(member, memberService.findOne(savedId));
        assertNotNull(member);

    }

    @Test(expected = IllegalStateException.class)//예외 테스트 시 유용
//    @Rollback(value = false)
    public void duplicatedMember() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("Seungho");

        Member member2 = new Member();
        member2.setName("Seungho");
        //when
   /*     try{
   */
            memberService.join(member1);

            memberService.join(member2);
  /*      }catch (IllegalStateException e){
            assertEquals(e.getMessage(),"이미 존재하는 회원입니다.");
            System.out.println(e.getMessage());
            return;
        }*/
        //이 귀찮은 과정을 위에 (expect = exception)으로 처리하면 쉽게 변환 가능하다

        //then
        fail("예외가 안 발생해서 여기까지 오면 무조건 에러를 발생시키는 메소드. 즉 예외테스트 실패시 이 문장이 뜬다!");
    }

}