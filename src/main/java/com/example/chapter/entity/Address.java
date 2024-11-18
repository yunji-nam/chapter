package com.example.chapter.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class Address {

    private String zipcode;
    private String street;
    private String detail;

    public Address(String zipcode, String street, String detail) {
        this.zipcode = zipcode;
        this.street = street;
        this.detail = detail;
    }
}
