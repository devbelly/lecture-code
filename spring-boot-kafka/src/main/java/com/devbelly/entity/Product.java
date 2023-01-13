package com.devbelly.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class Product {

    @Id @GeneratedValue
    private Long id;

    private String title;
    private Long price;

    public static Product of(String title,Long price){
        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        return product;
    }

}
