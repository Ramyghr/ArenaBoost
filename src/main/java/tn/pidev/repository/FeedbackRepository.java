package tn.esprit.pidev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidev.entities.Feedback;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findFeedbackById(long id);
}