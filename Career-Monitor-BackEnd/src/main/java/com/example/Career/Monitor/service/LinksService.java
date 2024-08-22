package com.example.Career.Monitor.service;

import com.example.Career.Monitor.Dao.DataEntryDao;
import com.example.Career.Monitor.Dao.LinksDao;
import com.example.Career.Monitor.model.DataEntry;
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

    @Autowired
    private DataEntryDao dataEntryDao;

    public ResponseEntity<List<Link>> getAllLinks(){
        try {
            return new ResponseEntity<>(linksDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addLink(Link link) {
        // Check if a Link with the same company name already exists
        if (linksDao.existsByCompany(link.getCompany())) {
            return new ResponseEntity<>("A link with this company name already exists.", HttpStatus.BAD_REQUEST);
        }

        try {
            linksDao.save(link);
            return new ResponseEntity<>("Link added successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Unable to create link due to an internal error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
                saveDataEntry();

                return new ResponseEntity<>("Link visited updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Link not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging purposes
            return new ResponseEntity<>("Unable to update link", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void saveDataEntry() {
        LocalDate today = LocalDate.now();

        // Retrieve the DataEntry for today, or create a new one if it doesn't exist
        DataEntry dataEntry = dataEntryDao.findByDate(today).orElseGet(() -> {
            DataEntry newEntry = new DataEntry();
            newEntry.setDate(today);
            newEntry.setCount(1); // Initialize the count to 1
            return newEntry;
        });

        // Increment the count
        dataEntry.setCount(dataEntry.getCount() + 1);

        // Save the updated DataEntry
        dataEntryDao.save(dataEntry);
    }

    public ResponseEntity<String> deleteLinksByIds(List<Integer> ids) {
        try {
            int deletedCount = 0;
            int notFoundCount = 0;

            for (Integer id : ids) {
                if (linksDao.existsById(id)) {
                    linksDao.deleteById(id);
                    deletedCount++;
                } else {
                    notFoundCount++;
                }
            }

            if (deletedCount > 0) {
                String message = deletedCount + " link(s) deleted successfully.";
                if (notFoundCount > 0) {
                    message += " " + notFoundCount + " link(s) not found.";
                }
                return new ResponseEntity<>(message, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No links found to delete.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging purposes
            return new ResponseEntity<>("Unable to delete links", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
