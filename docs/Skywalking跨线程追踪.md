Skywalking 官方不支持 Log4j 的异步线程打印 traceId，官方给出的答复是：用户通过线程间 traceid 传递解决。

![image-20221121141757601](https://technotes.oss-cn-shenzhen.aliyuncs.com/2022/image-20221121141757601.png)

Issue 地址：https://github.com/apache/skywalking/issues/2938

同时官方也给出了跨线程追踪的解决方案，详见：[跨线程追踪](https://skyapm.github.io/document-cn-translation-of-skywalking/zh/8.0.0/setup/service-agent/java-agent/Application-toolkit-trace-cross-thread.html)。具体有以下几种方法。

### 方法1：@TraceCrossThread

```java
@TraceCrossThread
public static class MyCallable<String> implements Callable<String> {
    @Override
    public String call() throws Exception {
        return null;
    }
}
...
ExecutorService executorService = Executors.newFixedThreadPool(1);
executorService.submit(new MyCallable());
```

### 方法2：CallableWrapper/RunnableWrapper

```java
ExecutorService executorService = Executors.newFixedThreadPool(1);
executorService.submit(CallableWrapper.of(new Callable<String>() {
    @Override
    public String call() throws Exception {
        return null;
    }
}));
```

或者

```java
ExecutorService executorService = Executors.newFixedThreadPool(1);
executorService.execute(RunnableWrapper.of(new Runnable() {
    @Override
    public void run() {
        //your code
    }
}));
```

### 方法3：SupplierWrapper

```java
@TraceCrossThread
public class MySupplier<String> implements Supplier<String> {
    @Override
    public String get() {
        return null;
    }
}
 
CompletableFuture.supplyAsync(new MySupplier<String>());
```

或者

```java
CompletableFuture.supplyAsync(SupplierWrapper.of(() -> {
            return "SupplierWrapper";
    })).thenAccept(System.out::println);
```

### 方法4：@Async

社区对 Spring 的 @Async 注解进行了增强，支持子线程的 traceId 打印。代码实现详见：https://github.com/apache/skywalking/pull/2902

在应用启动类上添加 @EnableAsync 注解：

```java
@EnableAsync
@SpringBootApplication
public class WebApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
 
}
```

接口定义如下：

```java
@Slf4j
@RestController
public class ExampleController {
 
    @Autowired
    private ExampleService exampleService;
 
    @GetMapping("/traceid")
    public void traceid() {
        log.info("main thread");
        exampleService.asyncMethod();
    }
 
}
```

使用 @Async 注解：

```java
@Slf4j
@Service
public class ExampleService {
 
    @Async
    public void asyncMethod() {
        log.info("async method call.");
    }
 
}
```

日志打印如下：

![image-20221121142135815](https://technotes.oss-cn-shenzhen.aliyuncs.com/2022/image-20221121142135815.png)

