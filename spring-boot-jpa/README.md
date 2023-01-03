## spring-data-jpa

---

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

<br>

---

<br>

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

- Spring Data jpa가 제공하는 Paging

  - 리턴타입이 `Page<>` 인경우
    - 추가적으로 Count 쿼리 발생
  - 리턴타입이 'Slice<>` 인경우

    - 추가쿼리 없음 (핸드폰에서 '더보기' 구현시 사용)

  - `Page<Member> findByAge(int age, Pageable pageable);`
  - ```java
    PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));
    Page<Member> page = memberRepository.findByAge(10,pageRequest);
    ```

- Page 사용시 주의사항

  - Page는 추가적으로 count query가 발생
  - 이는 원래 데이터를 가져오는 query에 의존하므로 원래 query가 join을 사용하면 count query도 join 사용

    - _원래쿼리_

      ![image](https://user-images.githubusercontent.com/67682840/210199846-a039ec33-e41f-4fdd-a291-328ee05c329e.png)

    - _카운트쿼리_

      ![image](https://user-images.githubusercontent.com/67682840/210199879-f4a259d0-15fc-420f-a00a-993a9df84048.png)

  - count query가 join 사용 시 성능 저하 발생, 이를 최적화하기 위해 countQuery를 분리하는 기능을 제공한다.

    - ```java
      @Query(value="select m from Member m left join m.team t",countQuery="select count(m) from Member m")
      Page<Member> findByAge(int age, Pageable pageable);
      ```

  - Entity 대신 Dto를 노출, Page.map 권장

<br>

---

<br>

- 벌크성 수정쿼리는 데이터를 읽어와서 업데이트 하는 대신 직접 데이터베이스에 SQL를 날리는 것이 좋다
- Update 작성시 `@Modifying` 필요

  - :warning:주의

    벌크성 수정쿼리는 직접 SQL에 변경을 주는 것이므로 1차캐시에 저장된 내용과는 다르다
    :arrow_right: `flush`, `clear`을 통해 1차캐시를 완전히 없앤 후 조회

  - Spring Data Jpa에서는...

    - JPQL은 flush 이후에 실행되므로 굳이 `em.flush` :x:
    - `@Modifying` 을 통해 `em.clear` 제공

      - `@Modifying(clearAutomatically = true)`
