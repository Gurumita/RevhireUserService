package com.revhire.userservice.Controllers;

import com.revhire.userservice.Services.SummaryService;
import com.revhire.userservice.models.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/summaries")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @PostMapping("/create")
    public ResponseEntity<Summary> createSummary(@RequestBody Summary summary) {
        Summary createdSummary = summaryService.createSummary(summary);
        return new ResponseEntity<>(createdSummary, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Summary>> getAllSummaries() {
        List<Summary> summaries = summaryService.getAllSummaries();
        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Summary> getSummaryByUserId(@PathVariable Long userId) {
        Summary summary = summaryService.getSummaryByUserId(userId);
        if (summary != null) {
            return new ResponseEntity<>(summary, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
