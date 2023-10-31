package org.crud.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author {
    private int id;
    private String lastName;
    private String firstName;
    private int age;
    private int bookId;
}
