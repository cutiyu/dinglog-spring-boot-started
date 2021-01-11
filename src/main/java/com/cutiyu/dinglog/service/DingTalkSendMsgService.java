package com.cutiyu.dinglog.service;

/**
 * @Describe:
 *
 */
public interface DingTalkSendMsgService {


    boolean sendTextMsg(String content);

    boolean sendTextMsg(String content, String accessToken);

    boolean sendTextMsg(String content, String accessToken, String keyWords);
}
