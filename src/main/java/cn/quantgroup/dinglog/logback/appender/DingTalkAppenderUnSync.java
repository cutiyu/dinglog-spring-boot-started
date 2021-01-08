package cn.quantgroup.dinglog.logback.appender;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import cn.quantgroup.dinglog.logback.DingTalkLogbackParam;
import cn.quantgroup.dinglog.logback.DingTalkLogbackSendMsg;

/**
 * @Describe:
 * @Created by tangfeng 2020-12-14 14:50
 */

public class DingTalkAppenderUnSync extends UnsynchronizedAppenderBase<LoggingEvent> {

    PatternLayoutEncoder encoder;

    DingTalkLogbackParam dingTalkLogbackParam;


    @Override
    public void start() {
        if (this.encoder == null) {
            //name ： logback.xml 中 appender 标签 配置的name值
            addError("No encoder set for the appender named [" + name + "].");
            return;
        }
        addInfo("DingTalkAppenderUnSync start方法被调用");
        super.start();
    }

    @Override
    public void stop() {
        //释放相关资源，如数据库连接，redis线程池等等
        addInfo("DingTalkAppenderUnSync stop方法被调用");
        if (!isStarted()) {
            return;
        }
        super.stop();
    }

    @Override
    protected void append(LoggingEvent event) {
        DingTalkLogbackSendMsg.sendMsg(this,encoder, event, name, dingTalkLogbackParam);
    }


    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

    public DingTalkLogbackParam getDingTalkLogbackParam() {
        return dingTalkLogbackParam;
    }

    public void setDingTalkLogbackParam(DingTalkLogbackParam dingTalkLogbackParam) {
        this.dingTalkLogbackParam = dingTalkLogbackParam;
    }
}

