package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    /*OneToOne에서 연관관계의 주인을 정할 때는 둘 중 아무데나 와도 상관 없지만
    access많은 쪽에 보통 주인으로 둔다 -> 지금으로서는 Order!*/
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    //[READY, COMP]
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
