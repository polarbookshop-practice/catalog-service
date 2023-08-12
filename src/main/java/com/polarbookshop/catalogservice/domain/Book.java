package com.polarbookshop.catalogservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public record Book (
    @Id
    Long id, //  Identifies the field as the primary key for the entity
    @NotBlank(message = "The book ISBN must be defined.")
    @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN format must be valid.")
    String isbn,

    @NotBlank(message = "The book title must be defined.")
    String title,

    @NotBlank(message = "The book author must be defined.")
    String author,

    @NotNull(message = "The book price must be defined.")
    @Positive(message = "The book price must be greater than zero.")
    Double price,

    @Version
    int version //  The entity version number, which is used for optimistic locking
    
){

        public static Book of(String isbn, String title, String author, Double price) {
            // An entity is considered new when the ID is null and the version is 0.
            return new Book(null,isbn, title, author, price,0);
        }
}
