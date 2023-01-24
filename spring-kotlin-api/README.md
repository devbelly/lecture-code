# Kotlin Plugin

```gradle
plugins {
    kotlin("plugin.spring") version "1.4.32"
    kotlin("plugin.jpa") version "1.4.32"
}
```

## plugin.spring

#### all-open

아래 어노테이션 사용 시 `all-open`을 자동으로 추가합니다.

- `@Component`
- `@Async`
- `@Transactional`
- `@Cacheable`
- `@SpringbootTest`
- `@Configuration`, `@Controller`, `@RestController`, `@Service`, `@Repository`, `@Component`

```kotlin
@Transactional
class Foo{
    fun test(){}
}
```

```kotlin
@org.springframework.transaction.annotation.Transactional public open class Foo public constructor() {
    public open fun test(): kotlin.Unit { /* compiled code */ }
}
```

코틀린의 특징으로 인해 클래스 빌드 시 기본적으로 `final` 키워드가 있습니다. `plugin.spring` + `@Annotation` 을 사용하면 `final` 키워드 대신 `open`이 되는 것을 확인할 수 있습니다.

```kotlin
allOpen {
    annotation("javax.persistence.Entity")
    ...
}
```

`gradle` 설정으로 특정 어노테이션에 대해서 `allOpen`을 동작시킬 수 있습니다.

#### all-open이 왜 필요할까?

spring boot 2.x 버전부터는 CGLIB Proxy 방식으로 Bean을 관리한다. CGLIB은 Target Class를 상속받아 생성하므로 `open`으로 상속 가능한 상태여야합니다.

```gradle
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}
```

만약 `open`이 아니라면 Jpa 사용시 프록시 객체를 생성할 수 없어 지연로딩이 정상적으로 작동하지 않는 것을 알 수 있습니다. `@Entity`는 `plugin.spring`이 지원하지 않으므로 `allOpen`으로 수동 추가합니다.

#### no-args

코틀린도 자바와 마찬가지로 클래스의 기본생성자를 제공하며 다른 생성자를 만들면 사라지게 됩니다. `no-arg`는 `plugin.jpa` 플러그인과 같이 사용됩니다. 아래 어노테이션을 사용할 시 기본 생성자를 제공하게 됩니다.

- `@Entity`
- `@Embeddable`
- `@MappedSuperclass`

# Kotlin + Spring data jpa

### Repository 01

```java
public interface UserRepository extends CrudRepository<User,Long>{
    User findByUsername(String username);
}
```

```kotlin
interface UserRepository : CrudRepository<User,Long>{
    fun findByUsername(username : String):User?
}
```

- jpa에서 단건조회가 되지 않는다면 null반환
  - kotlin에서 null에 대한 처리를 위해 리턴 타입이 `User?`임을 알 수 있다.

### Repository 02

> 💡Tip💡
>
> 코틀린에서는 null이 될 수 있는 타입을 실행하는 경우가 있습니다. 자바라면 `if(val!=null) s.toUppsercase else null` 와 같은 긴 문장을 `s?.toUppsercase()` 처럼 짧게 표현 가능합니다

- 코틀린에서는 자바의 `Optional`사용이 불편합니다
  - 코틀린에서 지원하는 `?.`을 사용하기 위해 `optionalUser.orElse(null)?.username?:""` 와 같은 문법을 사용해야합니다.
- spring boot 2.1.2부터 `nullable` 타입을 리턴하는 문법을 지원합니다
  - `.findByIdOrNull`
    ```kotlin
    val User : User? = userRepository.findByIdOrNull(1)
    user?.username?:""
    ```
  - 이는 확장함수로 구현되어있다.

### JPA Entity와 Kotlin

```kotlin
@Entity
class Person{
    @Id
    @GeneratedValue
    var id: Long? =null

    @Column
    var name: String? = null

    var phoneNumber: String? =null

    constructor()

    constructor(id: Long?,name:String, phonNumber: String?){
        this.id=id
        this.name=name
        this.phoneNumber=phoneNumber
    }
}
```

- 코틀린의 문법을 사용했지만 장점이 없는 코드
- 주 생성자를 이용해 리팩토링 할 수 있다.

```kotlin
@Entity
class Person(
    @Id
    @GenerateValue
    var id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    var phoneNumber : String? = null
)
```

- 프로퍼티 정의와 프로퍼티를 갖는 생성자를 한거번에 처리가능
- 주생성자의 모든 값에 기본값이 들어있으므로 인자없는 생성자 또한 생성가능

```kotlin
@Entity
class Person(
    @Id
    @GenerateValue
    var id: Long?,

    @Column(nullable = false)
    var name: String,

    var phoneNumber : String?
)
```

- 초기화를 하지 않으면 `no-arg` 플러그인의 도움을 받아 기본생성자를 만들 수 있다.
- `data class` 사용시 `equals` 및 `hashCode`를 올바르게 오버라이딩 하여 사용하자

# Test

#### Mock

- 테스트를 위해 만든 모형
- 실제 객체와 비슷한 모형을 만드는 것을 Mocking이라고 한다
- 모킹한 객체를 메모리에서 얻어내는 것을 Mock up이라고 한다.

#### `@ExtendWith`

- JUnit 4의 `@RunWith`가 JUnit 5에서는 `@ExtendWith`로 변경
- `@SpringBootTest`에 포함되어 있음

#### `@SpringBootTest`

- 전체 빈 중 특정 빈을 선택하여 생성가능
- 특정 빈을 Mock으로 대체가능
- 테스트에 사용할 ApplicationContext를 쉽게 생성하고 조작할 수 있다.
  - MOCK
    `WebApplicationContext`를 로드하며 내장된 서블릿 컨테이너가 아닌 Mock 서블릿을 제공합니다. `@AutoConfigureMockMvc` 어노테이션과 함께 사용하면 별다른 설정없이 `MockMvc`를 사용해 테스트 가능
  - RANDOM_PORT
    `EmbeddedWebApplicationContext`를 로드하며 실제 서블릿 환경을 구성합니다. 생성된 서블릿 컨테이너는 임의이 PORT를 listen합니다.

#### `TestRestTemplate`

- `@SpringBootTest`와 `TestRestTemplate`을 사용한다면 편리하게 웹 통합 테스트 가능
- `@SpringBootTest`에서 Web Environmnet 설정을 했다면 그에 맞게 빈이 생성된다.
- `MockMvc`는 서블릿 컨테이너를 사용하지 않지만 `@SpringBootTest`와 `TestRestTemplate`은 서블릿 컨테이너 사용
- `Mockmvc`는 서버 입장에서 구현한 API를 통해 비즈니스 로직확인, `TestRestTemplate`은 클라이언트 입장에서 API 테스트

#### `@WebMvcTest`

- `@Controller`, `@RestController`, `@ControllerAdvice`, `@JsonComponent`, `Filter`, `WebMvcConfigurer`, `HandlerMethodArgumentResolver` 만 로드된다
- 실제 구동되는 애플리케이션과 똑같이 로드하는 `@SpringBootTest` 보다 가볍게 테스트 할 수 있다.
- Controller Layer만 테스트하고 싶을 때 유용하다.
- `MockMvc`를 자동으로 설정하여 빈으로 등록한다.

#### `@Mock` vs `@MockBean`

- `@Mock`은 `@InjectMocks`에 대해서만 클래스 안에서 정의된 객체를 찾아 의존성 해결
- `@MockBean`은 mock 객체를 컨텍스트에 등록, `@Autowired`를 통해 의존성 주입

# mockk

### Test data

- 테스트마다 독립적으로 작동할 수 있어야한다
- Java에서는 빌더패턴을 활용해 제공가능

#### Wrong way

```kotlin
companion object {
    fun account(): Account {
        return Account(id = UUID.randomUUID(),
            name = "name",
            description = "description",
            currency = Currency.getInstance("USD"),
            amount = 2.0, addedOn = LocalDate.now())
    }
}
```

- 괜찮아 보이지만 다른 테스트에서 "name" 대신 다른 값을 사용하고 싶다면 문제 발생
  - 새로운 테스트 작성 시 scalable하지 않다.
  - 기본값을 바꾸면 테스트가 fail한다

#### Correct way

```kotlin
class AccountFixture {

    companion object {
        fun account(
            id: UUID = UUID.randomUUID(),
            name: String = "name",
            description: String = "description",
            currency: Currency = Currency.getInstance("USD"),
            amount: Double = 1000.0,
            addedOn: LocalDate = LocalDate.now()
        ): Account {
            return Account(id = id, name = name, description = description, currency = currency, amount = amount, addedOn = addedOn)
        }
    }
}
```

- `account` 필요 시, `AccountFixture.account()`
- 다른 `name`이 필요하다면 `AccountFixture.account(name="diff")` 사용

<br>

## 참고

- https://www.youtube.com/watch?v=Ou_-DFaAUhQ&t=517s
- https://github.com/cheese10yun/spring-kotlin-api/blob/master/docs/spring-with-kotlin.md
- https://meetup.nhncloud.com/posts/124
- https://cobbybb.tistory.com/16
