package com.devbelly.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class PriceDownEvent {

    public static final String MESSAGE = "price-down";

    private Long id;
    private String titie;

}
