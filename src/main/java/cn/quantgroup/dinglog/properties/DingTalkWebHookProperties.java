package cn.quantgroup.dinglog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-07 17:45
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

}
