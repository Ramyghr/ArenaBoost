package tn.esprit.pidev.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidev.entities.AthleteProfile;
import tn.esprit.pidev.services.AthleteProfileService;
import tn.esprit.pidev.services.PredictionException;

@RestController
@RequestMapping("/api/athletes")
public class AthleteProfileController {

    private final AthleteProfileService profileService;

    public AthleteProfileController(AthleteProfileService profileService) {
        this.profileService = profileService;
    }


    @PostMapping
    public ResponseEntity<AthleteProfile> createAthleteProfile(
            @Valid @RequestBody AthleteProfile profile
    ) {
        try {
            AthleteProfile createdProfile = profileService.createProfile(profile);
            return ResponseEntity.ok(createdProfile);
        } catch (PredictionException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(profile); // Return profile without prediction
        }
    }
}