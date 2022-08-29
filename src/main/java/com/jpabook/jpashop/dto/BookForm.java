package com.jpabook.jpashop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {
    //상품수정을 위함
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
