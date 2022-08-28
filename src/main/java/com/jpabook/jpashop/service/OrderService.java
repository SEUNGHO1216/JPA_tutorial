package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Delivery;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.repository.ItemRepository;
import com.jpabook.jpashop.repository.MemberRepository;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    public Long order(Long memberId, Long itemId, int count){
        //누가
        Member member = memberRepository.findOne(memberId);
        //어떤 상품을(정보)
        Item item = itemRepository.findOne(itemId);
        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        //얼마나 사서
        OrderItem orderItem = OrderItem.createOrder(item, item.getPrice(), count);

        //jpa는 protected 접근 제한자까지 기본생성자를 허용해준다
        //생성자를 따로 선언해서 저장하는 로직을 방지하기 위해서 접근제한자로 생성자 방지
        // OrderItem orderItem1 = new OrderItem();

        //주문은 했냐
        Order order = Order.createOrder(member, delivery, orderItem);
        //주문 저장
        orderRepository.save(order);

        return order.getId();
        /*근데 여기서 잘 보면 Delivery 와 orderItem은 따로 repository에 save 시키지 않고  set만 시켜놓은 상태에서
        * 그 모든 것을 받은 order만 repository에 저장한다.
        * 이렇게만 해도 가능한 이유는 cascadeType.All 때문이다
        * cascade 걸려있는 매핑한 엔티티들에 대해 같이 persist를 시켜준다*/
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 id로 찾아서
        Order order = orderRepository.findOne(orderId);
        //주문 취소시킨다
        order.cancelOrder();
        /*여기서 jpa의 강력함이 한번 더 나오는데, 로직에서 정보가 변했지만 update를 하기 위한 다른 쿼리를 날리거나save를 다시 시키고 있지 않다.
        그러나 @Transactional을 달아 놓음으로써 jpa 영속성 때문인지 데이터 변화를 감지하고 알아서 update 쿼리를 날려줘서 수정해준다.
        ex) 수량 증가, status 변경 등등*/
    }
    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
/*주문 서비스의 주문과 주문 취소 메서드를 보면 비즈니스 로직 대부분이 엔티티에 있다. 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다.
이처럼 엔티티가 비즈니스 로직을 가지고 객체 지 향의 특성을 적극 활용하는 것을 도메인 모델 패턴(http://martinfowler.com/eaaCatalog/ domainModel.html)이라 한다.
반대로 엔티티에는 비즈니스 로직이 거의 없고 서비스 계층에서 대부분 의 비즈니스 로직을 처리하는 것을
트랜잭션 스크립트 패턴(http://martinfowler.com/eaaCatalog/ transactionScript.html)이라 한다.
=> 한 프로젝트 안에서 둘다 양립해서 사용가능하며(겹치는 건 지양, 문맥 보고 결정)*/
