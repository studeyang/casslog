# 为什么做这个？

第一，重复配置。公司用的是微服务架构，服务很多，每个服务都要配置`log4j.xml`，配置内容基本都是一样的；但也担心不一样，不同的日志 Pattern 会给日志采集增加难度。

第二，各司其职。减少业务团队对基础日志技术的学习成本，使其专注于业务迭代；

第三，技术演进。抽象出日志组件，统一做日志的配置和升级。主要原因也是 2021-12-10 日发生的`Log4j2`的版本漏洞。[漏洞回顾](docs/Apache%20Log4j2远程代码执行漏洞.md)

# 功能简介

### 功能一：统一日志输出 Pattern

控制台输出样式：

![image-20221119174545655](https://technotes.oss-cn-shenzhen.aliyuncs.com/2022/image-20221119174545655.png)

文件输出样式：

![image-20221119174517686](https://technotes.oss-cn-shenzhen.aliyuncs.com/2022/image-20221119174517686.png)

### 功能二：扩展日志配置

casslog 日志组件不仅提供了统一的日志配置，还支持配置扩展。例如：我想把服务的所有 API 访问日志记录到 access.log 文件里，可以通过以下步骤实现。

第一，配置要扩展的日志：

```xml
<Configuration status="WARN">
    <Appenders>
 
        <!-- 想在控制台打印，请保留此段 -->
        <Console name="Console" target="SYSTEM_OUT" follow="false">
            <PatternLayout pattern="${sys:CONSOLE_PATTERN}" />
        </Console>
 
        <!-- casslog 扩展日志 -->
        <RollingFile name="ACCESS_LOG" fileName="logs/access.log"
                     filePattern="logs/$${date:yyyy-MM}/access-%d{yyyy-MM-dd}-%i.log.gz">
            <PatternLayout>
                <Pattern>%d{yyyy-MM-dd HH:mm:ss,SSS} [%c{1.}:%4line] - %m%n</Pattern>
            </PatternLayout>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
                <SizeBasedTriggeringPolicy size="256MB"/>
            </Policies>
            <DefaultRolloverStrategy max="1000"/>
        </RollingFile>
    </Appenders>
 
    <Loggers>
        <Root level="INFO"/>
        <Logger name="com.casstime.open.xx.ExampleController" level="debug" additivity="false">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="ACCESS_LOG"/>
        </Logger>
    </Loggers>
</Configuration>
```

第二，加载日志扩展文件：

```java
package com.github.open.casslog.example.logextend;

@Component
public class TestLogExtend extends AbstractLogExtend {
    @Override
    public String logConfig() {
        return "classpath:com/github/open/casslog/example/logextend/access-log4j.xml";
    }
}
```

> 详情可参考这篇文章：[实战：如何优雅地扩展Log4j配置？](https://mp.weixin.qq.com/s/FMdvXFzthXPsjqFpL9xAbg)
>
> 示例工程：[casslog-example-base](example/casslog-example-base)

### 功能三：（动态）修改日志级别

修改日志级别可使用下面配置。

```yaml
logging:
  level:
    com.github.open.casslog.example.ExampleController: warn
    com.studeyang.OtherLogger: error
```

或

```yaml
casslog:
  level:
    com.github.open.casslog.example.ExampleController: warn
    com.studeyang.OtherLogger: error
```

- `casslog.level.root`：表示应用 Root Logger 的日志级别。
- `casslog.level.com.studeyang`：表示`com.studeyang`包下的所有类的日志级别。
- `casslog.level.com.studeyang.OtherLogger`：表示`com.studeyang.OtherLogger`类的日志级别。

> 需要注意的是：logging.level 的加载在 casslog.level 之前。加载顺序为：logging.level > log extend > casslog.level。

动态修改日志级别需使用`Nacos`配置中心。

详情步骤可参考：[动态修改日志级别](docs/动态修改日志级别.md)。

示例工程：[casslog-example-nacos](example/casslog-example-nacos)。

### 功能四：打印 Skywalking traceId

如果两个服务都使用了 Skywalking，那么跨服务间调用将打印 Skywalking traceId。

![image-20221119175906656](https://technotes.oss-cn-shenzhen.aliyuncs.com/2022/image-20221119175906656.png)

![image-20221119175918888](https://technotes.oss-cn-shenzhen.aliyuncs.com/2022/image-20221119175918888.png)

如果是异步线程，请留意日志中是否打印了 traceId。解决方法见：[Skywalking跨线程追踪](docs/Skywalking跨线程追踪.md)。

### 功能五：低版本检测

Casslog 对低于 2.16.0 log4j 版本会进行检测。

![image-20221119180508766](https://technotes.oss-cn-shenzhen.aliyuncs.com/2022/image-20221119180508766.png)

# 快速使用

### 引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>io.github.studeyang</groupId>
        <artifactId>casslog-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

### 启动应用

```java
@SpringBootApplication
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
```

启动后效果如下图所示：

![image-20221121140626130](https://technotes.oss-cn-shenzhen.aliyuncs.com/2022/image-20221121140626130.png)

