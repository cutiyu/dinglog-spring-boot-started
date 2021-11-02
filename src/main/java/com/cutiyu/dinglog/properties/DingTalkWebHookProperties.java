package com.cutiyu.dinglog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Describe:
 *
 */
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

    private ThreadPoolProperties threadPoolProperties;

    public static class ThreadPoolProperties{
        private Integer corePoolSize;
        private Integer maxPoolSize;
        private Long keepAliveTime;
        private TimeUnit timeUnit;

        public Integer getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(Integer corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public Integer getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(Integer maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public Long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(Long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getSignSecret() {
        return signSecret;
    }

    public void setSignSecret(String signSecret) {
        this.signSecret = signSecret;
    }

    public String getIpSegments() {
        return ipSegments;
    }

    public void setIpSegments(String ipSegments) {
        this.ipSegments = ipSegments;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ThreadPoolProperties getThreadPoolProperties() {
        return threadPoolProperties;
    }

    public void setThreadPoolProperties(ThreadPoolProperties threadPoolProperties) {
        this.threadPoolProperties = threadPoolProperties;
    }
}
