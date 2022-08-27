package com.jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

/*값 타입은 변경 불가능하게 설계해야 한다. 따라서 Setter를 다 열어두기보단
* 상황에 맞는 생성자를 통하여 받아오는 것이 맞다.
* 특히 이런 정해져있는 값 타입에서는 생성자 하나를 만들면 그 형식으로
* 값을 받아올 수 있어서 변경 불가에 유리하다*/
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    public Address(String city, String street, String zipcode){
        this.city=city;
        this.street=street;
        this.zipcode=zipcode;
    }

    //public으로 설정시 모든 클래스에서나 접근 가능하다.
    //그나마 접근에 제한을 걸어서 동일 패키지에서만 허용
    protected Address() {
    }
}
