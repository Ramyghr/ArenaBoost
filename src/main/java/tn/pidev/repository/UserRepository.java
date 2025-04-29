package tn.esprit.pidev.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidev.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
