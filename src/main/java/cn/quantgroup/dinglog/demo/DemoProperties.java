package cn.quantgroup.dinglog.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Describe: 定义demo的配置文件属性
 * @Created by tangfeng 2020-05-06 16:24
 */
@ConfigurationProperties(prefix = "demo1")
public class DemoProperties {
    /**
     * 可以设置默认值，引用方不配置，则使用该默认值，若配置，则覆盖默认值
     */
    private String sayWhat = "hello";
    private String toWho;

    public String getSayWhat() {
        return sayWhat;
    }

    public void setSayWhat(String sayWhat) {
        this.sayWhat = sayWhat;
    }

    public String getToWho() {
        return toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }
}
