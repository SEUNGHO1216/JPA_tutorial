package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    /*중요! @XToOne 인 것들은 기본 fetch 타입이 Eager(즉시로딩)이다.
    * 이는 JPQL에서 N+1문제를 발생시킬 수 있다.
    * ->1번의 쿼리를 날릴 때 N번의 쿼리가 의도치 않게 추가로 날라가는 것*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    //주문가격(할인, 쿠폰 등등을 적용하든 안 하든 그 당시 구매한 상품 가격)
    private int orderPrice;
    //주문수량
    private int count;

    //jpa는 protected까지 기본생성자를 허용해준다
    protected OrderItem(){}

    //==생성 메소드==//
    public static OrderItem createOrder(Item item, int orderPrice, int count){
        //주문한 당시의 아이템 가격을 알기 위해서 item.get 하지 않고 따로 수량과 가격을 받는다
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        //주문 시 주문한 만큼 아이템이 재고에서 까여야 함
        item.removeStock(count);
        return  orderItem;
    }

    //==비즈니스 로직(주문 취소에 따른 재고 증가,(getItem등 공유변수에 대한 고민 필요하지 않나))==//
    public void cancel() {
        getItem().addStock(count); //근데 여기서 count는 각 Item으로 불러온 것의 갯수를 말하는것인지?
    }

    //==조회 로직(주문 총합 구하기)==//
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
