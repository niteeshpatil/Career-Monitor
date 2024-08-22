package com.example.Career.Monitor.service;
import com.example.Career.Monitor.Dao.DataEntryDao;
import com.example.Career.Monitor.model.DataEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataEntryService {

    @Autowired
    private DataEntryDao dataEntryDao;

    public ResponseEntity<List<DataEntry>> getAllDataEntries() {
        try {
            List<DataEntry> dataEntries = dataEntryDao.findAll();
            return new ResponseEntity<>(dataEntries, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<DataEntry> getDataEntryByDate(LocalDate date) {
        return dataEntryDao.findByDate(date);
    }

    public DataEntry saveDataEntry(DataEntry dataEntry) {
        return dataEntryDao.save(dataEntry);
    }
}
