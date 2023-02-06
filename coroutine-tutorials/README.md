> 출처 [새차원, 코루틴 코틀린](https://www.youtube.com/watch?v=Vs34wiuJMYk&list=PLbJr8hAHHCP5N6Lsot8SAnC28SoxwAU5A)을 보고 정리한 글 & 코드입니다.

## 01. Why Coroutines

- JVM은 메인 쓰레드를 비롯한 여러 쓰레드로 구성될 수 있다.
- 메인 쓰레드가 블로킹되거나 종료된다면 프로그램 실행에 영향을 끼침
- 이러한 이유로 메인 쓰레드에 대한 부담을 줄이기 위해 여러 방법 제시

  - 새로운 쓰레드 생성
  - 새로운 쓰레드풀 생성
  - Rx

- 결국 쓰레드 단위의 작업이므로 비효율적인 면이 있다.

#### 코루틴s

- 코투린은 이전에 자신이 실행을 중단했던 다음 장소부터 실행가능

  ![image](https://user-images.githubusercontent.com/67682840/215238405-2963297f-edfd-4480-bdb8-5b239b348647.png)

- 하나의 쓰레드는 여러 코루틴으로 이루어질 수 있다.
  
  <img width="845" alt="image" src="https://user-images.githubusercontent.com/67682840/215239137-cba71d88-b1f2-4331-b7d7-2d2bbfe683a4.png">

  업의 단위가 쓰레드 일 경우, 하나의 쓰레드가 다른 쓰레드의 결과에 의존적이라면 쓰레드가 블로킹되고 컨텍스트 스위칭이 발생한다.
  
   <br> 

  <img width="841" alt="image" src="https://user-images.githubusercontent.com/67682840/215239216-ff08ebef-8e28-4c65-b30d-5f9ff4838c87.png">
  
  하지만 작업의 단위가 코루틴일 경우, 현재 쓰레드가 블로킹되면 컨텍스트 스위칭을 통해 다른 쓰레드가 실행되는 대신 현재 쓰레드에서 다른 코루틴을 사용하도록 하면 되므로 컨텍스트 스위칭 발생이 현저히 줄어든다. 

```kotlin
fun main() = runBlocking{
    launch {
        repeat(5){i->
            println("Coroutine A, $i")
        }
    }

    launch {
        repeat(5){i->
            println("Coroutine B, $i")
        }
    }
    println("Coroutine outer")
}

fun <T> println(msg: T){
    kotlin.io.println("$msg [${Thread.currentThread().name}]")
}
```

```
> 출력결과
Coroutine outer [main]
Coroutine A, 0 [main]
Coroutine A, 1 [main]
Coroutine A, 2 [main]
Coroutine A, 3 [main]
Coroutine A, 4 [main]
Coroutine B, 0 [main]
Coroutine B, 1 [main]
Coroutine B, 2 [main]
Coroutine B, 3 [main]
Coroutine B, 4 [main]
```

- 하나의 쓰레드가 여러개의 코루틴을 사용하는 것을 확인할 수 있다.

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
- 코루틴 빌더를 사용하기 위해서는 코루틴 스코프가 필요하다.
- `launch()`는 메인쓰레드를 블로킹하지 않으므로 hello가 먼저 출력됨을 알 수 있다.
- 쓰레드와 비슷한지 비교해보기 위해 아래 코드처럼 변경해보자


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
- 코루틴은 light-weight thread임을 알 수 있다.

<br>

#### Thread.sleep, delay

- `delay`는 suspend function이므로 coroutineScope 안에서 작동하거나 다른 suspend function 안에서 작동

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

- `runBlocking` 또한 스코프 빌더
- `runBlocking`은 메인 쓰레드를 블로킹한 채 새로운 쓰레드를 생성하여 작업을 할당한다.
- 메인 쓰레드 종료 방지를 위해 아래 `.runBlocking`을 사용했지만 딜레이가 1000이 아닌 3000이 되면 `hello,`만 출력이 된다.

```kotlin
fun main() = runBlocking {
    val job = GlobalScope.launch {
        delay(3000)
        println("world!")
    }
    println("hello, ")
    job.join()
}
```

- `.launch()`는 실행결과로 `job`을 반환한다
- `job.join()`은 코루틴의 실행결과를 기다린다.
- 매번 `.launch()`를 할 때마다 `.join()`을 하는 문제가 있다.

<br>

#### Structured concurrency

- 위 문제의 원인은 메인 쓰레드의 `runBlockig`과 `GlobalScope`가 아무런 관련이 없기 때문이다.
- `runBlocking`의 스코프에서 `launch`를 해주자
  - 이는 Top level 코루틴을 만드는 것이 아닌 부모 코루틴-자식 코루틴을 만드는 것
  - 부모 코루틴이 자식 코루틴이 완료되는 것을 기다려 준다.
  - `.join`을 사용할 필요가 없음을 알 수 있다.

```kotlin
fun main() = runBlocking {
    launch {
        delay(3000)
        println("world!")
    }
    println("hello, ")
}
```

<br>

## Cancellation and Timeouts

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()

    val job = launch (Dispatchers.Default){
        var nextPrintTime = startTime
        var i = 0
        while (i<5){
            if(System.currentTimeMillis()>=nextPrintTime){
                println("job: I'm sleeping..${i++}")
                nextPrintTime+=500
            }
        }
    }

    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: now I'm quit")
}
```

- 위 예제를 실행해보면 `cancel`이 정상적으로 작동하지 않는다
- `job`이 정상적으로 종료되려면 코루틴도 "협조적"이여야 한다.
  - 협조방법1) 코루틴 내부에서 `suspend` 함수 호출
  - 협조방법2) 확장 프로피터를 활용
- 코루틴 내부에 `suspend` 함수가 없으므로 (물론 확장 프로퍼티를 활용하지도 않음) 정상적으로 작동하지 않았던 것!

<br>

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()

    val job = launch (Dispatchers.Default){
        var nextPrintTime = startTime
        var i = 0
        while (i<5){
            if(System.currentTimeMillis()>=nextPrintTime){
                yield() // 추가됨
                println("job: I'm sleeping..${i++}")
                nextPrintTime+=500
            }
        }
    }

    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: now I'm quit")
}
```

- `suspend` 함수를 주기적으로 호출해서 코루틴의 상태를 알 수 있다.

<br>

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()

    val job = launch (Dispatchers.Default){
        var nextPrintTime = startTime
        var i = 0
        while (isActive){
            if(System.currentTimeMillis()>=nextPrintTime){
                println("job: I'm sleeping..${i++}")
                nextPrintTime+=500
            }
        }
    }

    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: now I'm quit")
}
```

- 위 예제와 마찬가지로 정상적으로 종료
- `suspend` 예제와 달리 예외를 던지지 않는다.

<br>

#### timeout

```kotlin
fun main() = runBlocking {
    withTimeout(1300){
        repeat(1000){i->
            println("I'm running...${i}")
            delay(500)
        }
    }
}
```

<img width="1002" alt="image" src="https://user-images.githubusercontent.com/67682840/215251920-f8d94954-b290-4be0-8042-51f3e3de45c4.png">


- 특정시간이 지나면 코루틴이 종료된다
- `launch()` 내부에서 실행된 것이 아니므로 예외가 발생해 메인쓰레드가 비정상적으로 종료된다
- 위 문제점을 해결하기 위해 `withTimeoutOrNull` 메서드 제공. 예외를 던지지 않고 Null을 리턴할 수 있다.

<br>

## 04. Comsposing Suspending Functions

```kotlin
fun main()= runBlocking<Unit> {
    val time = measureTimeMillis {
        val one = doSomethingOne()
        val two = doSomethingTwo()
        log("The answer is ${one+two}")
    }
    log("Completed in $time ms")
}

suspend fun doSomethingOne():Int{
    log("one start...")
    delay(1000L)
    log("one end...")
    return 13
}

suspend fun doSomethingTwo():Int{
    log("two start...")
    delay(1000L)
    log("two end...")
    return 13
}


private fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}
```

```
>실행결과
[main] : one start...
[main] : one end...
[main] : two start...
[main] : two end...
[main] : The answer is 26
[main] : Completed in 2024 ms
```

- 1개의 코루틴에서 두개의 함수가 실행되었다. one start...이후에 `delay`를 호출하더라도 자원을 양보할 코루틴이 존재하지 않으므로 순차적으로 실행되어 2024ms가 걸린다
  - 실행옵션 `-Dkotlinx.coroutines.debug`를 통해 어떤 코루틴에서 실행되는지 확인할 수 있다.
- **메인 쓰레드는 블로킹하지 않는다**
   - 이러한 이유로 UI에 영향을 끼치지 않는다(중요)

<br>

`doSomethingOne`이후에 `doSomethingTwo` 가 순차적으로 실행될 필요가 없다. 두 함수 실행에 dependency가 없으므로 개선을 해보자!

```kotlin
fun main()= runBlocking<Unit> {
    val time = measureTimeMillis {
        val one = async {doSomethingOne()}
        val two = async { doSomethingTwo() }
        log("The answer is ${one.await()+two.await()}")
    }
    log("Completed in $time ms")
}

suspend fun doSomethingOne():Int{
    log("one start...")
    delay(1000L)
    log("one end...")
    return 13
}

suspend fun doSomethingTwo():Int{
    log("two start...")
    delay(1000L)
    log("two end...")
    return 13
}
```

```
>실행결과
[main @coroutine#2] : one start...
[main @coroutine#3] : two start...
[main @coroutine#2] : one end...
[main @coroutine#3] : two end...
[main @coroutine#1] : The answer is 26
[main @coroutine#1] : Completed in 1034 ms
```

- `async` 비동기 키워드를 통해 호출하자마자 결과를 돌려받으므로 다음 라인을 실행할 수 있다.
- 실행결과를 돌려받기 위해 `await` 호출
- 다른 코루틴에서 실행될 것이라 예상했는데 예상대로 새로운 코루틴이 생성되는 것을 확인할 수 있다.
- 실행이 두배 빨라진 모습을 확인할 수 있다.

> async는 서버의 관점에서 보면 값을 즉시 돌려주는 것이라고 할 수 있다. 이때, 클라이언트는 계산되지 않은 변수를 돌려받기 위해 callback을 설정하거나 명시적으로 await를 호출할 수 있다.

<br> 

#### CoroutinContext

아래 요소들을 포함합니다.

- 코루틴을 실행하는 환경인 Dispatcher
- 예외처리를 하는 CoroutinExceptionHandler
- 요약하자면 Dispatcher은 코루틴의 실행을 결정하는 요소이고 CoroutineContext는 Dispatcher를 포함한다.

<br>

#### withContext

- 안드로이드는 사용자 입력을 받기 위해 Main Thread를 항상 사용하고 있다.
- 비동기 작업을 기다리기 위해 Main Thread를 블로킹하면 사용자로부터 입력을 받지 못하므로 예외 발생
- 이러한 문제점을 해결하기 위해 withContext를 사용
- 현재 코루틴을 중지한 채, 다른 쓰레드에서 현재 코루틴을 수행한다.
  - 메인 쓰레드가 중지되는 문제를 해결할 수 있다.

```kotlin
fun main(args: Array<String>) = runBlocking {

    launch{
        log("메인 쓰레드에서 코루틴 실행중...")
        withContext(Dispatchers.IO){
            log("같은 코루틴이지만 다른 쓰레드에서 실행...")
        }
        log("메인 쓰레드는 블로킹으로부터 안전..")
    }
    println("")
}

private fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}
```

실행결과

<img width="629" alt="image" src="https://user-images.githubusercontent.com/67682840/216823791-0bc4f2ce-d9d8-4a49-a150-383e5b50c0dc.png">



## Reference

- https://aaronryu.github.io/2019/05/27/coroutine-and-thread/ 