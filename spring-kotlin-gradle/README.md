
- One to Many 관계에서 서로 추가하는 것은 어떻게 하는 걸까 .. 못찾겠다.
- WebFlux..
- jdsl 연습하기
#### JoinColumn

- name: 매핑할 외래키의 이름을 지정합니다. 

  <img width="464" alt="image" src="https://user-images.githubusercontent.com/67682840/216741966-9d8fc946-3b7d-4d17-81cd-dba61a1580c9.png">

  Team 테이블 생성 시 `owner_id` 필드가 생성되는 것을 알 수 있습니다.

  <img width="625" alt="image" src="https://user-images.githubusercontent.com/67682840/216742079-d9a2096e-d30c-403a-82bf-4f57f9b38dca.png">

- `referencedColumnName` : 해당 테이블에 실제로 연관 관계가 매핑됨을 알려줍니다.
- `foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT)`
  <img width="671" alt="image" src="https://user-images.githubusercontent.com/67682840/216743891-0fd3f807-7462-49e8-bc3a-bdbc685c59f8.png">
  - 옵션을 추가하면 JPA가 자동으로 생성하는 DDL에 영향을 미칩니다.
  - 옵션 미사용 시
    ![image](https://user-images.githubusercontent.com/67682840/216743431-4375d2f8-c161-4e6e-9192-04cce6e4f512.png)

    자동으로 DDL이 생성

  - 옵션 사용 시
    <img width="795" alt="image" src="https://user-images.githubusercontent.com/67682840/216743519-23182f17-7eaf-4c78-b672-82d23c112b69.png">
    
    DDL이 생략됨을 알 수 있습니다.
  - 옵션에 대한 본질은 RDB의 FK 사용의 효율성
  - 기본적으로는 해당 옵션을 사용하지 않는 것을 권장
  - 하지만 데이터의 규모가 크거나(1억건) 로그성 처럼 조금 틀려도 되는 경우는 해당 옵션을 추가하는 것도 고려할만하다.

- nullable
  - 기본적으로 true
  - `ManyToOne` 옵션이 true이거나 `JoinColumn`의 nullable 속성이 true 인 경우 Outer Join을 수행한다.
    <img width="622" alt="image" src="https://user-images.githubusercontent.com/67682840/216744431-180c0d2e-fd27-467c-9797-84465dc08b9e.png">
  - 명시적으로 `nullable = false`를 사용하면 inner Join을 사용한다.
  <img width="813" alt="image" src="https://user-images.githubusercontent.com/67682840/216745187-0fd834fe-1bd8-4b3c-823a-9c0124e7c1e0.png">
  <img width="649" alt="image" src="https://user-images.githubusercontent.com/67682840/216745290-205b0a4c-772e-446e-b3c6-831536680772.png">

<br>

#### `JoinColumn` nullable vs `XXXToXXX` optional

- 차이점은 해당 옵션들이 평가되는 시점
- `nullable`은 데이터베이스 칼럼과 관련이 있음
- `optional`은 런타임에 해당 값이 평가되어야함


<br>

#### mappedBy

- 현재 `Team-member` 간 `OnetoMany` 관계를 표현해보자

    <img width="539" alt="image" src="https://user-images.githubusercontent.com/67682840/216751139-75bf9fac-6494-4668-a3d0-1c8bb16d3479.png">

  <img width="480" alt="image" src="https://user-images.githubusercontent.com/67682840/216749439-f2baa00f-9ea7-49be-8a73-f8b04978a755.png">

- `Team.class` 의 테이블 생성 SQL을 보면 `member`와 관련된 내용이 없다.
  
  <img width="457" alt="image" src="https://user-images.githubusercontent.com/67682840/216751250-61e73529-c606-4ef0-b82a-0c75e5f81bb1.png">

- 전체적으로 생성된 테이블도 의도한 대로 생성됨을 확인할 수 있다.

  <img width="192" alt="image" src="https://user-images.githubusercontent.com/67682840/216751355-f0297af8-064c-4228-ab73-80b6e450cb26.png">

- `mappedBy="필드명"` 옵션을 제외하면 어떤 변화가 있는지 살펴보자

  <img width="460" alt="image" src="https://user-images.githubusercontent.com/67682840/216751414-b375ffc2-ba46-41db-bd8d-6c97d09e0323.png">

  <img width="575" alt="image" src="https://user-images.githubusercontent.com/67682840/216751471-82573251-9236-4397-ac90-e8e2adda29f7.png">

  - 의도와 다른 테이블이 하나 생성 되었다.
  - 이는 양방향 관계가 아닌 두개의 단방향 관계. 그중에서도 `OneToMany`를 표현하기 위해 `TEAM_MEMBERS` 테이블이 추가되었다.

<br>

#### Ordering vs Sorting

- Sorting은 `Comparable` 이나 `Comparator`을 구현한 객체에 대해 메모리상에서 정렬을 하도록 한다.
- Ordering은 위 인터페이스를 구현할 필요 없이 정렬에 대한 책임을 데이터 베이스에게 맡긴다
  - 데이터베이스는 이러한 일들에 최적화 되어있으므로 `Ordering`을 사용하는 것이 더 적합하다


## Reference

- https://www.inflearn.com/questions/26726/%EC%99%B8%EB%9E%98%ED%82%A4%EC%84%A4%EC%A0%95-constraintmode
- https://soojong.tistory.com/entry/JPA-mappedBy-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0
- https://thorben-janssen.com/ordering-vs-sorting-hibernate-use/
  
