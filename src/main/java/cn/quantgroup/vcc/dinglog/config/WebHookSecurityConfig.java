package cn.quantgroup.vcc.dinglog.config;

import cn.quantgroup.vcc.dinglog.logback.DingTalkAppenderSync;
import cn.quantgroup.vcc.dinglog.properties.DingLogBackProperties;
import cn.quantgroup.vcc.dinglog.properties.WebHookSecurityProperties;
import cn.quantgroup.vcc.dinglog.service.WebHookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-07 19:56
 */
@Configuration
@EnableConfigurationProperties(value = {WebHookSecurityProperties.class})
@ConditionalOnProperty(matchIfMissing = true)
public class WebHookSecurityConfig {

    @Autowired
    private WebHookSecurityProperties webHookSecurityProperties;


    @Bean(name = "webHook")
    public WebHookServiceImpl demoService(){
        return new WebHookServiceImpl(webHookSecurityProperties);
    }


    /*@Bean(name = "dingTalkAppenderSync")
    @ConditionalOnMissingBean(DingTalkAppenderSync.class)
    public DingTalkAppenderSync dingTalkAppenderSync(){
        return new DingTalkAppenderSync(webHookSecurityProperties);
    }*/
}
