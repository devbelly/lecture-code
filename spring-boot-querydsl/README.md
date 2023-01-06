## Querydsl

- JpaQueryFactory는 field로 두어 초기화 할 수 있다

  - 내부초기화시 사용되는 `EntityManager`가 Multithread 환경에서 동작하도록 설계되었기 때문이다

- JPQL은 string을 사용하므로 SQL injection에 취약하다

- Querydsl은 PreparedStatment를 통해 작동해서 실행 속도가 빠르다
