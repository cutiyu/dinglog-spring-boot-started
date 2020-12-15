package cn.quantgroup.vcc.dinglog.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import cn.quantgroup.vcc.dinglog.demo.DemoProperties;
import cn.quantgroup.vcc.dinglog.properties.WebHookSecurityProperties;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @Describe:
 * @Created by tangfeng 2020-12-14 14:50
 */

public class DingTalkAppenderSync extends AppenderBase<LoggingEvent> {

    PatternLayoutEncoder encoder;

    //WebHookSecurityProperties webHookSecurityProperties;


    @Override
    public void start() {
        if (this.encoder == null) {
            //name ： logback.xml 中 appender 标签 配置的name值
            addError("No encoder set for the appender named [" + name + "].");
            return;
        }
        super.start();
    }

    @Override
    public void stop() {
        //释放相关资源，如数据库连接，redis线程池等等
        System.out.println("stop方法被调用");
        if (!isStarted()) {
            return;
        }
        super.stop();
    }

    @Override
    protected void append(LoggingEvent eventObject) {
        Level level = eventObject.getLevel();

        /**
         * 指定只有 error 级别的日志，才发消息
         * 后续可以根据配置指定，此处暂不实现
         */
        //System.out.println("test2:" + JSONObject.toJSONString(webHookSecurityProperties));
        String loggerName = eventObject.getLoggerName();
        System.out.println("loggerName:" + loggerName+"=========appenderName"+name);


        if (level.ERROR.levelInt == level.levelInt) {


            //Map<String, String> appenderNameToken = webHookSecurityProperties.getAppenderNameToken();
            String accessToken = null;
            /*for (Map.Entry<String, String> entry : appenderNameToken.entrySet()) {
                if (entry.getKey().equals(name)) {
                    accessToken = entry.getValue();
                }
            }*/
            if (StringUtils.isEmpty(accessToken)) {
                System.out.println("没有匹配的name和token");
                return;
            }

            //System.out.println("DingTalkAppenderSync==============================================  要发钉钉消息了");
            /**
             * 发钉钉消息的逻辑，参看钉钉文档，暂不实现
             * 后续计划会开发一个 dingtalk-log-springboot-start 包
             */

            try {
                OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
                text.setContent("告警任务：验证日志输出同时发钉钉消息");

                //https://oapi.dingtalk.com/robot/send?access_token=030bcf64f152dd8d53b5da9c3dd92a0bcb1c2269f41a3ba584b060339bcc6f74
                //String accessToken = "{填入自己钉钉群自定义机器人的 webhook token}";
                String tokenURL = "https://oapi.dingtalk.com/robot/send?access_token=".concat(accessToken);
                DingTalkClient client = new DefaultDingTalkClient(tokenURL);
                OapiRobotSendRequest request = new OapiRobotSendRequest();
                request.setMsgtype("text");
                request.setText(text);

                OapiRobotSendResponse response = client.execute(request);
                System.out.println(JSONObject.toJSONString(response));
            } catch (Exception e) {

            }
        }
    }


    /*public DingTalkAppenderSync(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

    public DingTalkAppenderSync(DingLogBackProperties dingLogBackProperties) {
        this.dingLogBackProperties = dingLogBackProperties;
    }

    public DingTalkAppenderSync(WebHookSecurityProperties webHookSecurityProperties) {
        this.webHookSecurityProperties = webHookSecurityProperties;
    }



    public DingTalkAppenderSync(PatternLayoutEncoder encoder, DingLogBackProperties dingLogBackProperties, WebHookSecurityProperties webHookSecurityProperties) {
        this.encoder = encoder;
        this.dingLogBackProperties = dingLogBackProperties;
        this.webHookSecurityProperties = webHookSecurityProperties;
    }*/




    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }


    /*public DingTalkAppenderSync(WebHookSecurityProperties webHookSecurityProperties) {
        this.webHookSecurityProperties = webHookSecurityProperties;
    }
    public WebHookSecurityProperties getWebHookSecurityProperties() {
        return webHookSecurityProperties;
    }

    public void setWebHookSecurityProperties(WebHookSecurityProperties webHookSecurityProperties) {
        this.webHookSecurityProperties = webHookSecurityProperties;
    }*/
}

