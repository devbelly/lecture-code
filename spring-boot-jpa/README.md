## spring-data-jpa

#### 2023-01-01

- Entity는 `public`, `protected`를 접근제어자로 하는 기본생성자 필요

  - JPA에서 객체에 값 매핑 시 기본 생성자로 객체를 생성 후 Reflection을 사용

  - 지연로딩을 사용할 경우 프록시 객체 생성을 위해 기본 생성자 필요

- `@SpringBootTest` 는 `@Transactional` 존재 시 기본적으로 Rollback

- SQL parameter 확인하기

  - `build.gradle`에 `implementation ‘com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.7’` 추가

- 연관관계 설정 시 주의할 점

  - 연관관계 주인? foregin key를 가지고 있는 쪽

  - 연관관계 주인에 값을 입력하지 않으면 DB에 제대로 반영되지 않는다
  - 객체 양방향 매핑을 하지 않으면 조회 시 문제 발생
  - https://catsbi.oopy.io/ed9236a0-6521-471d-8a0d-b852147b5980

- 모든 연관관계는 지연로딩으로 설정해야 최적화가 쉽다.

#### 2023-01-02

- `getOne(Id)`: 엔티티를 프록시로 조회한다. 내부에서 `EntityManager.getReference()` 호출

- Data jpa는 `@NamedQuery`를 편리하게 사용하도록 제공한다

  - 실무에서는 잘 사용하지 않는다
  - 애플리케이션 로딩시점에 문법적인 오류를 잡는 장점이 있다

- Member 대신 MemberDto 조회하기

```java
  @Query("select new study.springbootjpa.dto.MemberDto(m.id,m.username, t.name) from Member m join m.team t")
  List<MemberDto> findMemberDto();
```

- 다양한 리턴타입 지원

  - `List<Member>`에 반환되는 객체가 없다면
    null이 아닌 empty collection이 리턴된다.

    - 실무에서 `!= null` 와 같은 코드는 지양

  - 단건조회의 경우 반환되는객체가 없다면 null이 리턴

    - JPA는 noResultException, Data Jpa는 이 에러를 캐치해서 null 리턴
    - Optional을 통해 처리하자

- 단건 조회요청, 여러건 조회
  - Exception 발생
