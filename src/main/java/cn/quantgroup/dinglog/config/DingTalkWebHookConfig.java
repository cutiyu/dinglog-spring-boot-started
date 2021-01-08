package cn.quantgroup.dinglog.config;

import cn.quantgroup.dinglog.demo.DemoService;
import cn.quantgroup.dinglog.properties.DingTalkWebHookProperties;
import cn.quantgroup.dinglog.service.DingTalkSendMsgService;
import cn.quantgroup.dinglog.service.impl.DingTalkSendMsgServiceImpl;
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
