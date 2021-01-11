# dinglog-spring-boot-started
基于钉钉的webhook,实现钉钉消息日志报警，及封装了钉钉发消息接口

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/maven-plugin/)

#### 使用钉钉发送日志报警
1、添加依赖

```xml
        <dependency>
            <groupId>com.cutiyu.dinglog</groupId>
            <artifactId>dinglog-spring-boot-started</artifactId>
            <version>${version}</version>
        </dependency>
```
***
2、配置logback.xml
***

[注释内容]： 这是注释

* DingTalkAppenderUnSync: 继承了 UnsynchronizedAppenderBase<LoggingEvent> 的appender
* DingTalkAppender，继承了AppenderBase<LoggingEvent> 的appender。两者的区别，在于是否同步处理日志事件。
* dingTalkLogbackParam：为该appender 配置的一些参数：
* sendMsg： true | false , 是否开启日志报警，默认 false 不开启
* markMsg： 配置钉钉机器人webHook，发消息的关键词，匹配该关键词，钉钉才会发送对应的消息，如未配置，需要发送的消息中包含webHook配置的关键词
* webHookAccessToken：钉钉webHook 的accessToken。
* layoutLog： true | false, 是否按照layoutLog 输出告警日志,默认为false,
* warnLevel: 发送钉钉告警的日志级别配置，默认 ERROR级别。



```xml

    <springProperty name="spring.application.name" source="spring.application.name"/>
    <springProperty name="ding.talk.webhook.send.msg" source="ding.talk.webhook.send.msg"/>
    <springProperty name="ding.talk.webhook.mark.msg" source="ding.talk.webhook.mark.msg"/>
    <springProperty name="ding.talk.webhook.access.token" source="ding.talk.webhook.access.token"/>
    
    <appender name="dingTalkAppenderSync" class="DingTalkAppenderUnSync">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>

        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符 -->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
        <!-- 自定义参数 -->
        <dingTalkLogbackParam>
            <sendMsg>${ding.talk.webhook.send.msg}</sendMsg>
            <markMsg>${ding.talk.webhook.mark.msg}</markMsg>
            <webHookAccessToken>${ding.talk.webhook.access.token}</webHookAccessToken>
            <layoutLog>${ding.talk.webhook.log.layout}</layoutLog>
            <warnLevel>ERROR</warnLevel>
        </dingTalkLogbackParam>
    </appender>

    <!-- dingTalk webHook Appender 异步输出 -->
    <appender name="dingTalkAppenderAsync" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>512</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="dingTalkAppenderSync"/>
    </appender>
    
    <!--关闭钉钉内部的异常日志-->
    <logger name="topsdk" level="OFF"/>
    
    <root level="INFO">
         <appender-ref ref="dingTalkAppenderAsync"/>
    </root>
```

完整的配置示例：
````xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>

    <springProperty name="spring.application.name" source="spring.application.name"/>
    <springProperty name="ding.talk.webhook.send.msg" source="ding.talk.webhook.send.msg"/>
    <springProperty name="ding.talk.webhook.mark.msg" source="ding.talk.webhook.mark.msg"/>
    <springProperty name="ding.talk.webhook.access.token" source="ding.talk.webhook.access.token"/>

    <property name="DEV_HOME" value="."/>
    <property name="LOG_LEVEL_PATTERN"
              value="%clr(%5p) %clr([${spring.application.name:-},%X{Z-VISITOR:-},%X{X-B3-TraceId:-},%X{X-B3-SpanId:-},%X{X-Span-Export:-}]){yellow}"/>
    <property name="CONSOLE_LOG_PATTERN"
              value="${CONSOLE_LOG_PATTERN:-%clr(%d{MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%10.10t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>

    <!-- 这里面定义了 CONSOLE_LOG_PATTERN, FILE_LOG_PATTERN 等日志格式, 还定义了一些日志级别 -->
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <include resource="org/springframework/boot/logging/logback/console-appender.xml"/>


    <appender name="FILE-AUDIT"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${DEV_HOME}/debug.log</file>
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout">
                <Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%tid] [%thread] %-5level %logger{36} -%msg%n</Pattern>
            </layout>
        </encoder>
        <!--
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>
                %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
            </Pattern>
        </encoder>
 		-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- rollover daily -->
            <fileNamePattern>${DEV_HOME}/debug.%d{yyyy-MM-dd}.%i.log
            </fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy
                    class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>300MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>

    </appender>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout">
                <Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%tid] [%thread] %-5level %logger{36} -%msg%n</Pattern>
            </layout>
        </encoder>
    </appender>


    <!-- dingTalk webHook Appender -->
    <appender name="dingTalkAppenderSync" class="DingTalkAppenderUnSync">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>

        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符 -->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
        <!-- 自定义参数 -->
        <dingTalkLogbackParam>
            <sendMsg>${ding.talk.webhook.send.msg}</sendMsg>
            <markMsg>${ding.talk.webhook.mark.msg}</markMsg>
            <webHookAccessToken>${ding.talk.webhook.access.token}</webHookAccessToken>
            <layoutLog>${ding.talk.webhook.log.layout}</layoutLog>
            <warnLevel>ERROR</warnLevel>
        </dingTalkLogbackParam>
    </appender>

    <!-- dingTalk webHook Appender 异步输出 -->
    <appender name="dingTalkAppenderAsync" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>512</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="dingTalkAppenderSync"/>
    </appender>

    
    <logger name="org.springframework" level="warn"/>
    <logger name="org.apache.ibatis" level="TRACE"/>
    <!--<logger name="java.sql.Connection" level="DEBUG"/>-->
    <!--<logger name="java.sql.Statement" level="DEBUG"/>-->
    <!--<logger name="java.sql.PreparedStatement" level="DEBUG"/>-->
    <logger name="org.hibernate" level="warn"/>
    <logger name="org.apache" level="warn"/>
    <logger name="ch.qos.logback" level="warn"/>
    <!--关闭钉钉内部的异常日志-->
    <logger name="topsdk" level="OFF"/>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="dingTalkAppenderAsync"/>
    </root>

</configuration>
````

3、注入服务，直接发送钉钉消息

配置钉钉信息
```yaml

dinglog:
  webhook:
    security:
      access-token: 030bxxxxccxxxxxxxx**********9bcc6f74
      key-words: 【任务1】
```

配置信息完整属性

目前只实现了基于关键词的业务，签名和ip段的暂未实现
```java
@ConfigurationProperties(prefix = "dinglog.webhook.security")
public class DingTalkWebHookProperties {

    /**
     * 业务关键词
     */
    private String keyWords;

    /**
     * robot token
     */
    private String accessToken;

    /**
     * 签名的秘钥
     */
    private String signSecret;
    /**
     * ip段
     */
    private String ipSegments;
```

注入依赖，进行测试
```java
        @Autowired
        private DingTalkSendMsgService dingTalkSendMsgService;
    
    
        @GetMapping("/test")
        public void test() {
            log.error("这是一个测试：");
            
            /**
             * 在application.properties 或者 application.yaml 配置了 相关属性，则直接调用
             */
            dingTalkSendMsgService.sendTextMsg("测试发消息");
    
            /**
             * 在application.properties 或者 application.yaml 未配置相关属性规则
             */
            String content = "这是发送的内容";
            String accessToken = "";
            String keyWords = "webHook配置的关键词";
            dingTalkSendMsgService.sendTextMsg(content, accessToken);
            dingTalkSendMsgService.sendTextMsg(content, accessToken,keyWords);
        }
```
