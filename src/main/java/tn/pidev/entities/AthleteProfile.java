package tn.esprit.pidev.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "athlete_profiles")
public class AthleteProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;  // Relation avec User
    // Personal Information
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Past
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotBlank
    private String cin; // National Identification Number

    @Email
    private String contactEmail;

    @Pattern(regexp = "^\\+216\\d{8}$")
    private String phoneNumber;

    // Physical Attributes
    @Min(100)
    @Max(250)
    private Integer height; // in cm

    @Min(30)
    @Max(150)
    private Double weight; // in kg

    private Double bmi;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    // Sport-Specific Information
    @NotBlank
    private String sportDiscipline; // e.g., Athletics, Football, Handball

    private String position; // Position in team sports

    @Min(1)
    private Integer yearsOfExperience;

    private String currentClub;

    // Performance Metrics
    private String personalBest; // JSON string or related entity

    private String recentPerformanceResults;

    @Min(1)
    @Max(10)
    private Integer coachRating;

    private Double enduranceTestScore; // e.g., beep test result

    // Training Data
    private Integer trainingHoursPerWeek;

    private Integer trainingFrequency;

    private Integer recoveryTime; // in hours

    // Medical Information
    private String injuryHistory;

    private String currentMedications;

    private Boolean vaccinationStatus;

    // Nutritional Information
    @Enumerated(EnumType.STRING)
    private DietType dietType;

    private Integer hydrationLevel; // 1-10 scale

    // Psychological Factors
    @Min(1)
    @Max(10)
    private Integer motivationLevel;

    private String stressFactors;

    // Equipment Data
    private String equipmentUsed;

    private String preferredGearBrands;

    // Environmental Factors
    private String city;

    private String trainingEnvironment; // indoor/outdoor

    private Double averageTemperature; // during training

    // Social Factors
    @Min(1)
    @Max(10)
    private Integer familySupportLevel;

    private String academicPerformance;

    // Consent Flags
    private Boolean dataProcessingConsent;

    private Boolean medicalDataConsent;

    // Social Media
    private String socialMediaProfile;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum BodyType {
        ECTOMORPH, MESOMORPH, ENDOMORPH
    }

    public enum DietType {
        VEGETARIAN, HIGH_PROTEIN, BALANCED, KETO, OTHER
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public String getSportDiscipline() {
        return sportDiscipline;
    }

    public void setSportDiscipline(String sportDiscipline) {
        this.sportDiscipline = sportDiscipline;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getCurrentClub() {
        return currentClub;
    }

    public void setCurrentClub(String currentClub) {
        this.currentClub = currentClub;
    }

    public String getPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(String personalBest) {
        this.personalBest = personalBest;
    }

    public String getRecentPerformanceResults() {
        return recentPerformanceResults;
    }

    public void setRecentPerformanceResults(String recentPerformanceResults) {
        this.recentPerformanceResults = recentPerformanceResults;
    }

    public Integer getCoachRating() {
        return coachRating;
    }

    public void setCoachRating(Integer coachRating) {
        this.coachRating = coachRating;
    }

    public Double getEnduranceTestScore() {
        return enduranceTestScore;
    }

    public void setEnduranceTestScore(Double enduranceTestScore) {
        this.enduranceTestScore = enduranceTestScore;
    }

    public Integer getTrainingHoursPerWeek() {
        return trainingHoursPerWeek;
    }

    public void setTrainingHoursPerWeek(Integer trainingHoursPerWeek) {
        this.trainingHoursPerWeek = trainingHoursPerWeek;
    }

    public Integer getTrainingFrequency() {
        return trainingFrequency;
    }

    public void setTrainingFrequency(Integer trainingFrequency) {
        this.trainingFrequency = trainingFrequency;
    }

    public Integer getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(Integer recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    public String getInjuryHistory() {
        return injuryHistory;
    }

    public void setInjuryHistory(String injuryHistory) {
        this.injuryHistory = injuryHistory;
    }

    public String getCurrentMedications() {
        return currentMedications;
    }

    public void setCurrentMedications(String currentMedications) {
        this.currentMedications = currentMedications;
    }

    public Boolean getVaccinationStatus() {
        return vaccinationStatus;
    }

    public void setVaccinationStatus(Boolean vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }

    public DietType getDietType() {
        return dietType;
    }

    public void setDietType(DietType dietType) {
        this.dietType = dietType;
    }

    public Integer getHydrationLevel() {
        return hydrationLevel;
    }

    public void setHydrationLevel(Integer hydrationLevel) {
        this.hydrationLevel = hydrationLevel;
    }

    public Integer getMotivationLevel() {
        return motivationLevel;
    }

    public void setMotivationLevel(Integer motivationLevel) {
        this.motivationLevel = motivationLevel;
    }

    public String getStressFactors() {
        return stressFactors;
    }

    public void setStressFactors(String stressFactors) {
        this.stressFactors = stressFactors;
    }

    public String getEquipmentUsed() {
        return equipmentUsed;
    }

    public void setEquipmentUsed(String equipmentUsed) {
        this.equipmentUsed = equipmentUsed;
    }

    public String getPreferredGearBrands() {
        return preferredGearBrands;
    }

    public void setPreferredGearBrands(String preferredGearBrands) {
        this.preferredGearBrands = preferredGearBrands;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTrainingEnvironment() {
        return trainingEnvironment;
    }

    public void setTrainingEnvironment(String trainingEnvironment) {
        this.trainingEnvironment = trainingEnvironment;
    }

    public Double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(Double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public Integer getFamilySupportLevel() {
        return familySupportLevel;
    }

    public void setFamilySupportLevel(Integer familySupportLevel) {
        this.familySupportLevel = familySupportLevel;
    }

    public String getAcademicPerformance() {
        return academicPerformance;
    }

    public void setAcademicPerformance(String academicPerformance) {
        this.academicPerformance = academicPerformance;
    }

    public Boolean getDataProcessingConsent() {
        return dataProcessingConsent;
    }

    public void setDataProcessingConsent(Boolean dataProcessingConsent) {
        this.dataProcessingConsent = dataProcessingConsent;
    }

    public Boolean getMedicalDataConsent() {
        return medicalDataConsent;
    }

    public void setMedicalDataConsent(Boolean medicalDataConsent) {
        this.medicalDataConsent = medicalDataConsent;
    }

    public String getSocialMediaProfile() {
        return socialMediaProfile;
    }

    public void setSocialMediaProfile(String socialMediaProfile) {
        this.socialMediaProfile = socialMediaProfile;
    }
}