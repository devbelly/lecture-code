package study.springbootquerydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springbootquerydsl.dto.MemberRepositoryCustom;
import study.springbootquerydsl.entity.Member;

import java.util.List;


public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom {

    List<Member> findByUsername(String username);
}
