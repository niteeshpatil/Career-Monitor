package com.example.Career.Monitor.service;

import com.example.Career.Monitor.Dao.LinksDao;
import com.example.Career.Monitor.model.Link;
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

    public ResponseEntity<List<Link>> getAllLinks(){
        try {
            return new ResponseEntity<>(linksDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addLink(Link link) {
        try{
            linksDao.save(link);
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Unable to Create ", HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<String> deleteLinkById(Integer id) {
        try {
            if (linksDao.existsById(id)) {
                linksDao.deleteById(id);
                return new ResponseEntity<>("Link deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Link not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging purposes
            return new ResponseEntity<>("Unable to delete link", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> updateLink(Integer id, Link updatedLink) {
        try {
            if (linksDao.existsById(id)) {
                updatedLink.setId(id); // Ensure the ID is set for the update operation
                linksDao.save(updatedLink);
                return new ResponseEntity<>("Link updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Link not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging purposes
            return new ResponseEntity<>("Unable to update link", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
