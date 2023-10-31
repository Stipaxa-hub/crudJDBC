package org.crud.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Book {
    private int id;
    private String title;
    private BigDecimal price;
}
