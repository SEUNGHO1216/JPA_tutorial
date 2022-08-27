package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //jpa가 제공하는 표준 어노테이션 -> spring이 엔티티매니저를 만들어서 주입을 해준다(@PersistenceContext)
    //@RequiredArgsConstructor 사용함으로써 final만 붙여서 쉽게 주입가능
    private final EntityManager em;

    //em을 활용한 메소드 구현
    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findAllByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class) //jpql
                .setParameter("name", name) //파라미터 바인딩 과정
                .getResultList();
    }
}
