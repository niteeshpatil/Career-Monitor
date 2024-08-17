package com.example.Career.Monitor.service;

import com.example.Career.Monitor.Dao.LinksDao;
import com.example.Career.Monitor.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public ResponseEntity<String> visitLink(Integer id) {
        try {
            if (linksDao.existsById(id)) {
                Link link = linksDao.findById(id).orElseThrow(() -> new RuntimeException("Link not found"));

                LocalDateTime today = LocalDate.now().atStartOfDay();
                LocalDateTime currentVisit = LocalDateTime.now();

                // Initialize visitDates if it's null
                if (link.getVisitDates() == null) {
                    link.setVisitDates(new ArrayList<>());
                }

                // Add today's date to visitDates if not present
                boolean alreadyVisitedToday = link.getVisitDates().stream()
                        .anyMatch(date -> date.toLocalDate().equals(LocalDate.now()));
                if (!alreadyVisitedToday) {
                    link.getVisitDates().add(today);
                }

                // Check if LastVisit is not today
                if (link.getLastVisit() == null || !link.getLastVisit().toLocalDate().equals(LocalDate.now())) {
                    // Set LastVisit to today's date
                    link.setLastVisit(currentVisit);
                    System.out.println("Setting LastVisit to: " + currentVisit); // Debug logging
                } else {
                    System.out.println("LastVisit is already set to today: " + link.getLastVisit());
                }

                // Save the updated link
                linksDao.save(link);

                return new ResponseEntity<>("Link visited updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Link not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging purposes
            return new ResponseEntity<>("Unable to update link", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
