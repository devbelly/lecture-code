package com.blogcode.member;

import com.blogcode.domain.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @GetMapping("/")
    public Member get(){
        return new Member("jojoldu","jojoldu@gmail.com");
    }
}
