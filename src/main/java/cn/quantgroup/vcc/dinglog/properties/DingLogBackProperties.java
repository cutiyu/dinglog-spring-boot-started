package cn.quantgroup.vcc.dinglog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Describe:
 * @Created by tangfeng 2020-12-15 14:56
 */
@ConfigurationProperties(prefix = "dinglog.logback")
public class DingLogBackProperties {

    private String accessToken;

    private String appenderName;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppenderName() {
        return appenderName;
    }

    public void setAppenderName(String appenderName) {
        this.appenderName = appenderName;
    }
}
