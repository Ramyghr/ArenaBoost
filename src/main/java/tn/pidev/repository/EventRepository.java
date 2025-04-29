package tn.esprit.pidev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidev.entities.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}