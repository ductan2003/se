package com.elearningweb.library.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    @Column(length = 5000)
    private String body;
    private Date dateCreated;
    private String image;
    @Size(min = 3, max = 1000, message = "Invalid description!(3-1000 characters)")
    private String description;

    public PostDto(String title, String body, String description) {
        this.title = title;
        this.body = body;
        this.description = description;
    }


    public PostDto(Long id, String title, String body, String description) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.description = description;
    }
}

