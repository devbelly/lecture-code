package study.springbootjpa.repository;


import study.springbootjpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findCustomByUsername(String username);
}
