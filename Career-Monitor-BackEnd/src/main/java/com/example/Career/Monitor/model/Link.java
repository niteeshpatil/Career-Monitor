package com.example.Career.Monitor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Company name cannot be empty")
    private String Company;

    @NotBlank(message = "Link cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Link must start with http:// or https://")
    private String link;
}
