package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /*실무에서 거의 안 씀 -> 딱 이 모양 밖에 안 돼서!
     * ManyToMany로 관계를 맺으면 중간테이블이 생기는데 관계의 주인을 설정하면 이 중간 테이블이랑 이어야한다 -> @JoinTable(중간테이블)
     * 객체에서는 두 엔티티에 각자 컬럼을 넣어주면 관계가 양쪽으로 맺어진 것 같지만 DB에서는 한 쪽을 주인으로 정해야한다
     * -----------------
     * @JoinTable기준으로 joinColumn(joinTable 선언해준 쪽), inverseJoinColumn(그 반대쪽) 설정해줄 수 있다.
     * 그러나 이것도 실무에서는 별로 안 쓰인다..*/
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items =new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child=new ArrayList<>();

    /*연관관걔 편의 메소드*/
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
