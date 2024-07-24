package com.example.Career.Monitor.controller;

import com.example.Career.Monitor.model.Links;
import com.example.Career.Monitor.service.LinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("links")
public class LinksController {
    @Autowired
    LinksService linksService;

    @GetMapping("allLinks")
    public ResponseEntity<List<Links>> getAllLinks(){
        return linksService.getAllLinks();
    }

}
