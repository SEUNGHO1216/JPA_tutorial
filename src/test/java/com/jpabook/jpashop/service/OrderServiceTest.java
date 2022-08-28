package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.item.Album;
import com.jpabook.jpashop.domain.item.Book;
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
        Member member = new Member();
        member.setName("yujin");
        member.setAddress(new Address("서울","동소문로 248","23953"));
        memberService.join(member);
        Long memberId= member.getId();



        Book book = new Book();
        book.setName("harry potter3");
        book.setPrice(4000);
        book.setIsbn("ASBD1234");
        book.setAuthor("rowling");
        book.setStockQuantity(1000);
        itemService.saveItem(book);
        Long itemId = book.getId();

        int orderCount = 10;
        //when
        Long orderId = orderService.order(memberId, itemId, orderCount);

        orderService.cancelOrder(orderId);
        //then
        Order order = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 재고가 복구", 1000, book.getStockQuantity());
        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL, order.getStatus());

    }
    @Test/*(expected = IllegalStateException.class)*/
    public void 주문취소_배송상태예외처리(){
        //given
        Member member = new Member();
        member.setName("yujin");
        member.setAddress(new Address("서울","동소문로 248","23953"));
        memberService.join(member);
        Long memberId= member.getId();

        Book book = new Book();
        book.setName("harry potter3");
        book.setPrice(4000);
        book.setIsbn("ASBD1234");
        book.setAuthor("rowling");
        book.setStockQuantity(1000);
        itemService.saveItem(book);
        Long itemId = book.getId();

        int orderCount = 10;
        //when
        Long orderId = orderService.order(memberId, itemId, orderCount);
        Order order = orderRepository.findOne(orderId);

        order.getDelivery().setStatus(DeliveryStatus.COMP); //예외가 터지는 지점(배송상태 완료에 따른취소 불가)
        //orderService.cancelOrder(orderId);
        //then
        assertThrows(IllegalStateException.class,()->orderService.cancelOrder(orderId));
        //fail("배송상태가 COMP인 상품은 취소불가 처리해야함");
    }
}
/*단위 테스트 코드를 작성하는 것이 더 의미가 있다.
* 실행 속도의 개선은 물론이고, 어느 부분에서 트러블이 발생했는지 파악 하기 더욱 수월
* 도메인 주도 설계일 경우 도메인에 구현한 로직들을 테스트해보는 것도 가능한 방법이다*/