package study.springbootjpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@ToString(of={"id","username","age"})
public class Member {

    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    public Member(String username){
        this.username=username;
    }

    public Member(String member1, int age, Team teamA) {
        this.username=member1;
        this.age=age;
        if(teamA!=null)
            changeTeam(teamA);
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
