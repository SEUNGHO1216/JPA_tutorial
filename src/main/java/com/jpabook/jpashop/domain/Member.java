package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    //@NotEmpty //프레젠테이션계층에서의 검증로직이 엔티티에까지 와버린 상황(좋지 않음)
    @Column(unique = true)
    private String name;

    //jpa 내장타입
    @Embedded
    private Address address;

    //이렇게 mappedBy를 적는 순간, 연관관계의 주인이 아니라 주인에 매핑된 거울이라는 의미(양방향)
    @OneToMany(mappedBy = "member")
    private List<Order> orders =new ArrayList<>();
}
