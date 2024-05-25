package com.ict.careus.repository;

import com.ict.careus.model.transaction.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByApproved(boolean approved)
;}
