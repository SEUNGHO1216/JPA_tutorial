package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String name;

    //jpa 내장타입
    @Embedded
    private Address address;

    //이렇게 mappedBy를 적는 순간, 연관관계의 주인이 아니라 주인에 매핑된 거울이라는 의미(양방향)
    @OneToMany(mappedBy = "member")
    private List<Order> orders =new ArrayList<>();
}
