package com.ict.careus.controller;

import com.ict.careus.model.transaction.Submission;
import com.ict.careus.service.SubmissionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/submission")
    public ResponseEntity<List<Submission>> getAllSubmission(){
        List<Submission> submissions = submissionService.getAllSubmission();
        return ResponseEntity.ok(submissions);
    }

    @PostMapping("/submission/create")
    public ResponseEntity<Submission> createSubmission(Submission submission) throws BadRequestException {
        Submission createdSubmission = submissionService.createSubmission(submission);
        return ResponseEntity.ok(createdSubmission);
    }

    @DeleteMapping("/submission/delete/{submissionId}")
    public void deleteSubmission(long submissionId){
        submissionService.deleteSubmission(submissionId);
    }
}
