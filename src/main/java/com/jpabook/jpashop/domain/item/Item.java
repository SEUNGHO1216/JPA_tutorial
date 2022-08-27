package com.jpabook.jpashop.domain.item;

import com.jpabook.jpashop.exception.NotEnoughStockException;
import com.jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //===비즈니스 로직===
    //ddd(도메인주도설계)에서 데이터를 갖고 있는 쪽에 데이터 조작 메서드가 있는 것이 응집력이 좋다
    /*재고 증가*/
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }
    /*재고 감소*/
    public void removeStock(int quantity){
        int restStock=this.stockQuantity-quantity;
        if(restStock<0){
            //에러 메서드를 create해주는 것인데 이때 runtime에러를 상속해서 오버라이드 해줘야한다
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
