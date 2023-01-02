package study.springbootjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springbootjpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
