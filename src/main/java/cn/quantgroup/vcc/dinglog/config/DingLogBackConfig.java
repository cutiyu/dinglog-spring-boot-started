package cn.quantgroup.vcc.dinglog.config;

import cn.quantgroup.vcc.dinglog.demo.DemoService;
import cn.quantgroup.vcc.dinglog.logback.DingTalkAppenderSync;
import cn.quantgroup.vcc.dinglog.properties.DingLogBackProperties;
import cn.quantgroup.vcc.dinglog.properties.WebHookSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Describe:
 * @Created by tangfeng 2020-12-15 15:00
 */
@Configuration
@EnableConfigurationProperties({DingLogBackProperties.class})
@ConditionalOnProperty(
        prefix = "dinglog.logback",
        name = "isopen",
        havingValue = "true",
        matchIfMissing = true
)
public class DingLogBackConfig {

    /*@Autowired
    private DingLogBackProperties dingLogBackProperties;
    @Autowired
    private WebHookSecurityProperties webHookSecurityProperties;

    @Bean(name = "dingTalkAppenderSync")
    public DingTalkAppenderSync dingTalkAppenderSync(){
        return new DingTalkAppenderSync(dingLogBackProperties,webHookSecurityProperties);
    }*/



}
