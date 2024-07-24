package com.example.Career.Monitor.service;

import com.example.Career.Monitor.Dao.LinksDao;
import com.example.Career.Monitor.model.Links;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LinksService {
    @Autowired
    LinksDao linksDao;

    public ResponseEntity<List<Links>> getAllLinks(){
        try {
            return new ResponseEntity<>(linksDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

}
