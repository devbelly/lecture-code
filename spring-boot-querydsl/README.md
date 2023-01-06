## Querydsl

- JpaQueryFactory는 field로 두어 초기화 할 수 있다

  - 내부초기화시 사용되는 `EntityManager`가 Multithread 환경에서 동작하도록 설계되었기 때문이다

- JPQL은 string을 사용하므로 SQL injection에 취약하다

- Querydsl은 PreparedStatment를 통해 작동해서 실행 속도가 빠르다

<br>

#### on절

**where vs on**

- where은 조인 이후 검색된 튜플에 대해서 조건을 걸지만

- on은 조인 시 테이블에 조건을 건다

- where,on 은 inner join시 결과가 같지만 outer join시 결과가 다름을 알고 있어야 한다.

<br>

**1) filtering**

**2) 연관 관계가 없는 엔티티 외부조인**

- 기존 theta join은 left join이 불가능 했다

- 위 문제점을 `on`을 통해 해결 가능

- :warning: 기존 `leftJoin`와 문법이 조금 다르다

_기존 방식_

```java
List<Tuple> result = queryFactory
		.select(member, team)
		.from(member)
		.leftJoin(member.team, team)
		.on(team.name.eq("teamA"))
		.fetch();
```

- `.leftJoin(member.team,team)` 처럼 작성하면 id를 기준으로 on절이 작성됨

  ![image](https://user-images.githubusercontent.com/67682840/210944632-30ee2772-b687-4823-bd3f-64f1776e616f.png)

_연관관계 없는 외부조인 시 on 문법_

```java
List<Tuple> result = queryFactory
		.select(member, team)
		.from(member)
		.leftJoin(team).on(member.username.eq(team.name))
		.fetch();
```

- `.leftJoin(team).on()` 처럼 작성하면 on을 기준으로 on절이 작성됨

  ![image](https://user-images.githubusercontent.com/67682840/210945523-ee2d9c31-52d0-420e-9f58-d3c98493c0b4.png)
