package com.example.Career.Monitor.Dao;

import com.example.Career.Monitor.model.DataEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DataEntryDao extends JpaRepository<DataEntry, Integer> {
    Optional<DataEntry> findByDate(LocalDate date);
}
