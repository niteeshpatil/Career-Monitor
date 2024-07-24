package com.example.Career.Monitor.controller;

import com.example.Career.Monitor.model.Link;
import com.example.Career.Monitor.service.LinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("links")
public class LinksController {
    @Autowired
    LinksService linksService;

    @GetMapping("allLinks")
    public ResponseEntity<List<Link>> getAllLinks(){
        return linksService.getAllLinks();
    }

    @PostMapping("addLinks")
    public ResponseEntity<String> addLink(@RequestBody Link link){
        return linksService.addLink(link);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteQuestionsById(@PathVariable Integer id){
        return linksService.deleteLinkById(id);
    }

    @PutMapping("/updateLink/{id}")
    public ResponseEntity<String> updateLink(@PathVariable Integer id, @RequestBody Link updatedLink) {
        return linksService.updateLink(id, updatedLink);
    }
}
