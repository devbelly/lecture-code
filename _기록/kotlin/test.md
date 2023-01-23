## 01. Why Coroutines

- 코루틴은 루틴의 일종
- 협동 루틴이라고 할 수 있다.

#### 코루틴

- 코투린은 이전에 자신이 실행이 중단했던 다음 장소부터 실행가능
- 구글에서 비동기 처리에 코루틴 사용 권장
  - 콜백지옥으로 되어있는 코드를 순차적으로 작성가능

#### 예시

```kotlin
fun loadUser(){
	val user = api.fetchUser()
	show(user)
}
```

- 작동한다면 Dream code
- main thread가 blocking되므로 UI업데이트 불가

<br>

```kotlin
fun loadUser(){
	val user = api.fetchUser()
	show(user)
}
```

- 정상 작동
- 드림코드에 가깝지는 않다.

<br>

```kotlin
suspend fun loadUser(){
	val user = api.fetchUser()
	show(user)
}
```

- suspend 추가
- 순차적인 코드로 작성이 가능해졌다.

<br>

## 02. BASICS

```kotlin
fun main(){
    GlobalScope.launch{
        delay(1000L)
        println("world!")
    }
    println("hello, ")
    Thread.sleep(2000)
}
```

> 출력결과<br>
> hello,
> world!

- `launch()`는 코루틴 빌더, 내부적으로 코루틴을 만들어 반환한다.
- `launch()`를 실행하라면 coroutine scope가 필요하다.

  ```kotlin
    public object GlobalScope : CoroutineScope {
    /**
     * Returns [EmptyCoroutineContext].
     */
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext

  }
  ```

  - 객체이며 CoroutinScope를 반환함을 알 수 있다.

```kotlin
fun main(){
   thread {
       Thread.sleep(1000)
       println("world!")
   }
   println("hello, ")
   Thread.sleep(2000)
}
```

- `launch()`를 thread로 바꾸어도 제대로 동작한다.
- 코루틴은 light-weight thread

#### Thread.sleep, delay

- `delay`는 suspend function이므로 coroutinScope 안에서 작동하거나 다른 suspend function 안에서 작동

- 위 예제에서 메인 쓰레드는 coroutine scope가 아니므로 `delay`를 사용할 수 없다.

- 블로킹을 할 수 있는 스코프를 만들기 위해 `runBlocking`을 사용해보자.

  ```kotlin
  fun main(){
    GlobalScope.launch{
        delay(1000L)
        println("world!")
    }
    println("hello, ")
    runBlocking {
        delay(2000L)
    }
  }
  ```

  - 처음 예제와 동일한 결과를 얻을 수 있다..
  - `runBlocking` 또한 스코프 빌더
  - 만약 블로킹이 일어나지 않는다면 `hello,` 출력이후 즉시 종료된다.
  - 메인함수의 모든 내용이 실행되기 전까지 메인쓰레드가 종료되지 않기를 원하므로 `runBlocking` 전체를 코드로 감쌀 수 있다.

  ```kotlin
  fun main() = runBlocking {
    GlobalScope.launch{
        delay(1000L)
        println("world!")
    }
    println("hello, ")
    runBlocking {
        delay(2000L)
    }
  }
  ```
