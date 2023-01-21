## Kotlin Plugin

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

```
@Transactional
class Foo{
    fun test(){}
}
```

```
@org.springframework.transaction.annotation.Transactional public open class Foo public constructor() {
    public open fun test(): kotlin.Unit { /* compiled code */ }
}
```

코틀린의 특징으로 인해 클래스 빌드 시 기본적으로 `final` 키워드가 있습니다. `plugin.spring` + `@Annotation` 을 사용하면 `final` 키워드 대신 `open`이 되는 것을 확인할 수 있습니다.

```
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
  






  