package study.springbootjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springbootjpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
