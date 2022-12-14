package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    //동적으로 검색하기 위한 메소드 => 추후 쿼리DSL로 처리

    //JPA Criteria로 처리
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
            cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
            TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대1000건
            return query.getResultList();
    }

    public List<Order> findAll(OrderSearch orderSearch){

    OrderStatus status = orderSearch.getOrderStatus();
    String memberName=orderSearch.getMemberName();
    return em.createQuery("select o from Order o join o.member m " +
                    "where o.status = :status " +
                    "and m.name = :memberName", Order.class)
            .setParameter("status", status)
            .setParameter("memberName", memberName)
            .setMaxResults(1000) //최대 1000건 조회
            .getResultList();
    /*동적 쿼리를 받을 수 있으나, null처리가 안 된다
    * Repository말고 service나 controller에서 null을 걸러준다면?*/
    }
}
