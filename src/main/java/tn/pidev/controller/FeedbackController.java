package tn.esprit.pidev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.entities.Feedback;
import tn.esprit.pidev.services.FeedbackService;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedbackRequest) {
        Feedback feedback = new Feedback();
        feedback.setRating(feedbackRequest.getRating());
        feedback.setComment(feedbackRequest.getComment());

        Feedback createdFeedback = feedbackService.createFeedback(feedback, feedbackRequest.getInvestor().getInvestorId());
        return ResponseEntity.ok(createdFeedback);
    }
    // Retrieve all feedback entries
    @GetMapping("/all")
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbackList);
    }

    // Retrieve feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        if (feedback == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(feedback);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Feedback> updateFeedback(
            @PathVariable Long id,
            @RequestBody Feedback feedbackDetails) {
        Feedback updatedFeedback = feedbackService.updateFeedback(id, feedbackDetails);
        return ResponseEntity.ok(updatedFeedback);
    }

    // Delete a feedback entry
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}