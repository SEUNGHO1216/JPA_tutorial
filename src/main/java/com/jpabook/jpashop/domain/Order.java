package com.jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //protected 기본생성자를 만든 것과 같은 효과
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    /*외래키는 연관관계의 주인쪽에 생긴다. 즉 DB에서 member_id 외래키는 order 테이블에 생긴다
    외래키는 1:M에서 M쪽에 생긴다*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /*cascade 옵션은 클래스 안의 필드가 따로 persist 해야하는 과정을 같은 영속성 유지를 통해 해결*/
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems =new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //주문 시간
    private LocalDateTime orderDate;

    //주문의 상태 enum [ORDER, CANCEL]
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /*연관관계 편의 메소드
    * -양방향 관계에서 사용하는 편 -> set 시킬 때 매번 양 엔티티에서 다 set 이나 add를 해줘야하는 문제를 메소드로 해결
    * -실수로 한 쪽에서 set못해주는 문제 해결 가능
    * -위치는 주로 작업량이 많은 부분에 작성해주는 것이 좋다*/
    public void setMember(Member member){
        this.member=member;
        member.getOrders().add(this);
    }

    public void addOrderItems(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }

    //==생성메소드==//
    //근데 어차피 new Order 할거면 static 이 아니라 그냥 생성자로하면 안되나?
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem: orderItems){
            order.addOrderItems(orderItem);
        }
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);
        //재고가 남아있지 않다면 생성메소드가 통하지 않아야 한다! -> orderItem에서 처리해야 할 듯
        return order;
    }

    //==비즈니스 로직==//
    /*주문 취소(재고가 늘어야 함)*/
    public void cancelOrder(){

        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송이 완료된 상품은 주문 취소가 불가능합니다");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : this.orderItems){
            orderItem.cancel();
        }
    }

    //==조회 로직(주문 총합)==//
    public int getTotalPrice(){
        int totalPrice = 0 ;
        for(OrderItem orderItem : this.orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;

        //return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum(); //stream 문법 적용시!
    }



}
