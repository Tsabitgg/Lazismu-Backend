package com.ict.careus.service;

import com.ict.careus.model.transaction.Submission;
import com.ict.careus.model.user.User;
import com.ict.careus.repository.SubmissionRepository;
import com.ict.careus.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Submission> getAllSubmission() {
        return submissionRepository.findAll();
    }

    @Override
    public Submission createSubmission(Submission submission) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));
            submission.setUser(existingUser);
        }
        submission.setApproved(false);
        submission.setSubmissionDate(new Date());
        return submissionRepository.save(submission);
    }

    @Override
    public void deleteSubmission(long submissionId) {
        submissionRepository.deleteById(submissionId);
    }
}