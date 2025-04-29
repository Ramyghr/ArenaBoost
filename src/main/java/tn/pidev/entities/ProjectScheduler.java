package tn.esprit.pidev.entities;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.pidev.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProjectScheduler {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectScheduler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Scheduled(fixedRate = 86400000) // 86400000 ms = 24 heures
    public void updateDaysRemaining() {
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            if (project.getDays_remaining() > 0) {
                project.setDays_remaining(project.getDays_remaining() - 1);
                projectRepository.save(project);
            }
        }
    }
}