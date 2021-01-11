package com.cutiyu.dinglog.logback;

/**
 * @Describe:
 *
 */
public class DingTalkLogbackParam {

    private boolean sendMsg = false;

    private boolean layoutLog = false;

    private String warnLevel = "ERROR";

    private String logExStackTracePackage = "cutyiyu";

    private String webHookAccessToken;

    private String markMsg;

    public boolean isSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(boolean sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getWebHookAccessToken() {
        return webHookAccessToken;
    }

    public void setWebHookAccessToken(String webHookAccessToken) {
        this.webHookAccessToken = webHookAccessToken;
    }

    public String getMarkMsg() {
        return markMsg;
    }

    public void setMarkMsg(String markMsg) {
        this.markMsg = markMsg;
    }

    public String getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }

    public boolean isLayoutLog() {
        return layoutLog;
    }

    public void setLayoutLog(boolean layoutLog) {
        this.layoutLog = layoutLog;
    }

    public String getLogExStackTracePackage() {
        return logExStackTracePackage;
    }

    public void setLogExStackTracePackage(String logExStackTracePackage) {
        this.logExStackTracePackage = logExStackTracePackage;
    }
}
