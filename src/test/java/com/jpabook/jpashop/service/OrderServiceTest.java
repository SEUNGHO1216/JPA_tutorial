package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.domain.item.Album;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.domain.item.Movie;
import com.jpabook.jpashop.exception.NotEnoughStockException;
import com.jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired MemberService memberService;
    @Autowired ItemService itemService;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문(){
        //given
        Member member = new Member();
        member.setName("seungho");
        member.setAddress(new Address("서울","길음로75","20492"));
        memberService.join(member);
        Long memberId = member.getId();

        Movie movie1 = new Movie();
        movie1.setActor("daniel, rupert, emma");
        movie1.setName("harry potter2");
        movie1.setPrice(5000);
        movie1.setStockQuantity(1000);
        movie1.setDirector("david");
        itemService.saveItem(movie1);
        Long itemId1 = movie1.getId();

        Movie movie2 = new Movie();
        movie2.setActor("ssd, hdd");
        movie2.setName("cpcp");
        movie2.setPrice(1000);
        movie2.setStockQuantity(100);
        movie2.setDirector("young");
        itemService.saveItem(movie2);
        Long itemId2 = movie2.getId();
        //when
        int orderCount = 3;

        Long orderId1 = orderService.order(memberId, itemId1, orderCount);
        Long orderId2 = orderService.order(memberId, itemId2, orderCount);
        Order order1 = orderRepository.findOne(orderId1);
        Order order2 = orderRepository.findOne(orderId2);
        //then
        assertEquals("주문한 상품 항목 갯수", 1, order1.getOrderItems().size());
        assertEquals("주문 상태", OrderStatus.ORDER, order1.getStatus());
        assertEquals("주문 가격(가격*수량)", 5000*orderCount,order1.getTotalPrice());
        assertEquals("영화1 | 남은 영화 재고 수량", 997,movie1.getStockQuantity());
        assertEquals("영화2 | 남은 영화 재고 수량", 97,movie2.getStockQuantity());
        assertEquals("동일 멤버에 의한 주문인지 확인", order1.getMember(), order2.getMember());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과(){
        //given
        Member member = new Member();
        member.setName("seungho");
        member.setAddress(new Address("서울","길음로75","20492"));
        memberService.join(member);
        Long memberId = member.getId();

        Movie movie1 = new Movie();
        movie1.setActor("daniel, rupert, emma");
        movie1.setName("harry potter2");
        movie1.setPrice(5000);
        movie1.setStockQuantity(1000);
        movie1.setDirector("david");
        itemService.saveItem(movie1);
        Long itemId1 = movie1.getId();
        //when
        int orderCount = 1001;

        Long orderId = orderService.order(memberId, itemId1, orderCount);

        //then
        fail("재고 수량 초과 주문 메시지가 나와야한다.");
    }

    @Test
    public void 주문취소(){
        //given

        //when

        //then
    }
}