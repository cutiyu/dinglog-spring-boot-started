package com.cutiyu.dinglog.logback;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.spi.ContextAwareBase;
import com.cutiyu.dinglog.util.DingTalkSendMsgUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @Describe:
 */
public class DingTalkLogbackSendMsg {

    public static void sendMsg(ContextAwareBase base, PatternLayoutEncoder encoder, LoggingEvent event, String appenderName, DingTalkLogbackParam dingTalkLogbackParam) {

        //dingTalk sdk 内部日志异常，直接过滤掉，避免出现error日志死循环发钉钉消息处理
        if (event.getLoggerName().equalsIgnoreCase("topsdk")) {
            return;
        }

        if (dingTalkLogbackParam == null) {
            return;
        }

        boolean sendMsg = dingTalkLogbackParam.isSendMsg();
        if (!sendMsg) {
            return;
        }

        if (!event.getLevel().levelStr.equalsIgnoreCase(dingTalkLogbackParam.getWarnLevel())) {
            return;
        }

        String accessToken = dingTalkLogbackParam.getWebHookAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            base.addInfo("No dingTalk accessToken for the appender named [" + appenderName + "].");
            return;
        }

        try {
            //按照格式化日志形式输出

            if (dingTalkLogbackParam.isLayoutLog() && encoder.getLayout() != null) {
                String layoutLog = encoder.getLayout().doLayout(event);
                if (StringUtils.isNotEmpty(dingTalkLogbackParam.getMarkMsg())) {
                    layoutLog = dingTalkLogbackParam.getMarkMsg().concat("---").concat(layoutLog);
                }
                DingTalkSendMsgUtil.sendTextMsg(layoutLog, accessToken);
                return;
            }

            //简易形式输出
            String message = event.getFormattedMessage();
            IThrowableProxy throwableProxy = event.getThrowableProxy();
            StringBuffer sbExMsg = new StringBuffer();
            if (throwableProxy != null) {
                sbExMsg = sbExMsg.append(throwableProxy.getClassName());
                String exMessage = throwableProxy.getMessage();
                if (StringUtils.isNotEmpty(exMessage)) {
                    sbExMsg.append(": ").append(exMessage).append("\n");
                }

                StackTraceElementProxy[] stackTraceElementProxyArray = throwableProxy.getStackTraceElementProxyArray();
                if (stackTraceElementProxyArray != null && stackTraceElementProxyArray.length > 0) {
                    for (int i = 0; i < stackTraceElementProxyArray.length; i++) {
                        StackTraceElementProxy st = stackTraceElementProxyArray[i];
                        if (StringUtils.isNotEmpty(dingTalkLogbackParam.getLogExStackTracePackage()) && st.toString().contains(dingTalkLogbackParam.getLogExStackTracePackage()) ) {
                            sbExMsg.append(st.toString()).append("\n");
                        }
                    }
                }

                message = message.concat(", ").concat(sbExMsg.toString());
            }

            if (StringUtils.isNotEmpty(dingTalkLogbackParam.getMarkMsg())) {
                message = dingTalkLogbackParam.getMarkMsg().concat("---").concat(message);
            }
            DingTalkSendMsgUtil.sendTextMsg(message, accessToken);
        } catch (Exception e) {
            base.addInfo("ding talk logback send msg error:{}", e);
        }
    }
}
