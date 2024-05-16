package com.ict.careus.service;

import com.ict.careus.model.transaction.Submission;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface SubmissionService {
    List<Submission> getAllSubmission();
    Submission createSubmission(Submission submission) throws BadRequestException;
    void deleteSubmission(long submissionId);
}
