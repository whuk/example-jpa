package lala.example.jpa.repository;

import lala.example.jpa.domain.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelerRepository extends JpaRepository<Traveler, String> {
}
