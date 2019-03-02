package lala.example.jpa.repository;

import lala.example.jpa.domain.MembershipCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipCardRepository extends JpaRepository<MembershipCard, String> {
}
