package tn.esprit.pidev.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.entities.*;
import tn.esprit.pidev.services.IProjectService;
import tn.esprit.pidev.services.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectRestController {

    private final IProjectService projectService;
    private final RecommendationService recommendationService; // Ajouter le service de recommandation


    @Autowired
    public ProjectRestController(IProjectService projectService, RecommendationService recommendationService) {
        this.projectService = projectService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/retrieve-all-projects")
    public List<Project> getProjects() {
        return projectService.retrieveAllProjects();
    }

    @GetMapping("/retrieve-project/{project-id}")
    public Project retrieveProject(@PathVariable("project-id") Integer projectId) {
        return projectService.retrieveProject(projectId);
    }

    @PostMapping("/add-project")
    public Project addProject(@RequestBody Project p) {
        return projectService.addProject(p);
    }

    @DeleteMapping("/remove-project/{project-id}")
    public void removeProject(@PathVariable("project-id") Integer projectId) {
        projectService.removeProject(projectId);
    }

    @PutMapping("/modify-project")
    public Project modifyProject(@RequestBody Project p) {
        return projectService.modifyProject(p);
    }

    @PostMapping("/add-list-projects")
    public List<Project> addListProject(@RequestBody List<Project> projects) {
        return projectService.addListProject(projects);
    }

    // Nouveaux endpoints
    @GetMapping("/{project-id}/total-funds")
    public float getTotalFundsCollected(@PathVariable("project-id") Integer projectId) {
        return projectService.calculateTotalFundsCollected(projectId);
    }

    @PostMapping("/{project-id}/add-expense")
    public void addExpense(@PathVariable("project-id") Integer projectId, @RequestBody float expenseAmount) {
        projectService.addExpense(projectId, expenseAmount);
    }

    @GetMapping("/{project-id}/progress")
    public float getProgressPercentage(@PathVariable("project-id") Integer projectId) {
        return projectService.getProgressPercentage(projectId);
    }

    @PostMapping("/{project-id}/contribute")
    public Contribution addContribution(@PathVariable("project-id") Integer projectId, @RequestBody Contribution contribution) {
        return projectService.addContribution(projectId, contribution);
    }

    @PostMapping("/{project-id}/add-event")
    public Event addEvent(@PathVariable("project-id") Integer projectId, @RequestBody Event event) {
        return projectService.addEvent(projectId, event);
    }

    @GetMapping("/{project-id}/analytics")
    public ProjectAnalytics getProjectAnalytics(@PathVariable("project-id") Integer projectId) {
        return projectService.getProjectAnalytics(projectId);
    }

    @GetMapping("/{project-id}/financial-report")
    public ResponseEntity<byte[]> generateFinancialReport(@PathVariable("project-id") Integer projectId) {
        byte[] report = projectService.generateFinancialReport(projectId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report-" + projectId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(report);
    }

    @GetMapping("/search")
    public List<Project> searchProjectsByName(@RequestParam("name") String name) {
        return projectService.searchProjectsByName(name);
    }

    @GetMapping("/{project-id}/predict-outcome")
    public ProjectPrediction predictProjectOutcome(@PathVariable("project-id") Integer projectId) {
        return projectService.predictProjectOutcome(projectId);
    }
    @GetMapping("/{supporter-name}/recommendations")
    public ResponseEntity<List<Project>> getRecommendations(@PathVariable("supporter-name") String supporterName) {
        List<Project> recommendations = recommendationService.recommendProjects(supporterName);
        return ResponseEntity.ok(recommendations);
    }


}