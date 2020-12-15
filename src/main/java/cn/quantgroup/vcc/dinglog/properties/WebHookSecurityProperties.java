package cn.quantgroup.vcc.dinglog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-07 17:45
 */
@ConfigurationProperties(prefix = "dinglog.webhook.security")
public class WebHookSecurityProperties {

    /**
     * 业务关键词
     */
    private String keyWords;

    //private Map<String,String> appenderNameToken;
    /**
     * 签名的秘钥
     */
    private String signSecret;
    /**
     * ip段
     */
    private String ipSegments;
    /**
     * robot token
     */
    private String accessToken;
    /**
     * robot send url
     */
    private String url = "https://oapi.dingtalk.com/robot/send";

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /*public Map<String, String> getAppenderNameToken() {
        return appenderNameToken;
    }

    public void setAppenderNameToken(Map<String, String> appenderNameToken) {
        this.appenderNameToken = appenderNameToken;
    }*/

    @Override
    public String toString() {
        return "WebHookSecurityProperties{" +
                "keyWords='" + keyWords + '\'' +
                ", signSecret='" + signSecret + '\'' +
                ", ipSegments='" + ipSegments + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
