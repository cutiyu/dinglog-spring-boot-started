package cn.quantgroup.dinglog.service;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-07 19:58
 */
public interface DingTalkSendMsgService {


    boolean sendTextMsg(String content);

    boolean sendTextMsg(String content, String accessToken);

    boolean sendTextMsg(String content, String accessToken, String keyWords);
}
