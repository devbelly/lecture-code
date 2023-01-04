package study.springbootjpa.repository;

import lombok.RequiredArgsConstructor;
import study.springbootjpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;
    @Override
    public List<Member> findCustomByUsername(String username) {
        return em.createQuery("select m from Member m").getResultList();
    }
}
