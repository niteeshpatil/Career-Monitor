package com.example.Career.Monitor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "company"))
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Company name cannot be empty")
    private String company;

    @NotBlank(message = "Link cannot be empty")
    @Column(length = 2048)
    @Pattern(regexp = "^(http|https)://.*$", message = "Link must start with http:// or https://")
    private String link;

    @ElementCollection
    @CollectionTable(name = "link_visits", joinColumns = @JoinColumn(name = "link_id"))
    @Column(name = "visit_date")
    private List<LocalDateTime> visitDates;

    private LocalDateTime lastVisit;
}
