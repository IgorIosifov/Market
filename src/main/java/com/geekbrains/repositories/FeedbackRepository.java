package com.geekbrains.repositories;

import com.geekbrains.entites.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Feedback findByProductIdAndUserId(Long prodId, Long userId);
}