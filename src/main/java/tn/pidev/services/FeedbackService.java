package tn.esprit.pidev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.entities.Feedback;
import tn.esprit.pidev.entities.Investor;
import tn.esprit.pidev.repository.FeedbackRepository;
import tn.esprit.pidev.repository.InvestorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private InvestorRepository investorRepository;

    // Create a new feedback entry
    public Feedback createFeedback(Feedback feedback, Long investorId) {
        // Fetch the investor by ID
        Optional<Investor> investorOptional = investorRepository.findById(investorId);
        if (investorOptional.isEmpty()) {
            throw new IllegalArgumentException("Investor not found with ID: " + investorId);
        }

        // Set the investor for the feedback
        feedback.setInvestor(investorOptional.get());

        // Save the feedback
        return feedbackRepository.save(feedback);
    }

    // Retrieve all feedback entries
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    // Retrieve feedback by ID
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    public Feedback updateFeedback(Long id, Feedback feedbackDetails) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with ID: " + id));

        // Update only the allowed fields
        feedback.setRating(feedbackDetails.getRating());
        feedback.setComment(feedbackDetails.getComment());

        // Do not overwrite the investor field
        return feedbackRepository.save(feedback);
    }
    // Delete a feedback entry
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}