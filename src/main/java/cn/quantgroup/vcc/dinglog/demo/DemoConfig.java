package cn.quantgroup.vcc.dinglog.demo;

import cn.quantgroup.vcc.dinglog.logback.DingTalkAppenderSync;
import cn.quantgroup.vcc.dinglog.properties.WebHookSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Describe: 把demoService
 * @Created by tangfeng 2020-05-06 16:40
 */
@Configuration
@EnableConfigurationProperties({DemoProperties.class, WebHookSecurityProperties.class})
@ConditionalOnProperty(
        prefix = "demo",
        name = "isopen",
        //可与name组合使用，比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置
        //即 demo.isopen=true 与havingValue = "true" 是否匹配
        havingValue = "true",
        //缺少该property时是否可以加载。如果为true，没有该property也会正常加载；反之报错
        matchIfMissing = true
)
public class DemoConfig {
    @Autowired
    private DemoProperties demoProperties;
    @Autowired
    private WebHookSecurityProperties webHookSecurityProperties;

    @Bean(name = "demo")
    @ConditionalOnMissingBean(DemoService.class)
    public DemoService demoService(){
        return new DemoService(demoProperties,webHookSecurityProperties);
    }

    /*@Bean(name = "dintlog")
    @ConditionalOnMissingBean(DingTalkAppenderSync.class)
    public DingTalkAppenderSync dingTalkAppenderSync(){
        return new DingTalkAppenderSync(webHookSecurityProperties);
    }*/
}
