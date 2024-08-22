package com.example.Career.Monitor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "data_entries", uniqueConstraints = @UniqueConstraint(columnNames = "date"))
public class DataEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Date cannot be null")
    @Column(nullable = false, unique = true)
    private LocalDate date;

    @NotNull(message = "Count cannot be null")
    @Column(nullable = false)
    private Integer count;
}
