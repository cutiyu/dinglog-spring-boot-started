package com.cutiyu.dinglog.config;

import com.cutiyu.dinglog.properties.DingTalkWebHookProperties;
import com.cutiyu.dinglog.service.DingTalkSendMsgService;
import com.cutiyu.dinglog.service.impl.DingTalkSendMsgServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Describe:
 *
 */
@Configuration
@EnableConfigurationProperties(value = {DingTalkWebHookProperties.class})
@ConditionalOnProperty(prefix = "dinglog.webhook.security",
        name = "accessToken",
        matchIfMissing= true)
public class DingTalkWebHookConfig {

    @Autowired
    private DingTalkWebHookProperties webHookSecurityProperties;


    @Bean(name = "webHook")
    @ConditionalOnMissingBean(DingTalkSendMsgService.class)
    public DingTalkSendMsgService dingTalkSendMsgService(){
        return new DingTalkSendMsgServiceImpl(webHookSecurityProperties);
    }


    /*@Bean(name = "dingTalkAppenderSync")
    @ConditionalOnMissingBean(DingTalkAppenderSync.class)
    public DingTalkAppenderSync dingTalkAppenderSync(){
        return new DingTalkAppenderSync(webHookSecurityProperties);
    }*/
}
