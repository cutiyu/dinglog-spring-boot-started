package com.cutiyu.dinglog.service.impl;

import com.cutiyu.dinglog.properties.DingTalkWebHookProperties;
import com.cutiyu.dinglog.service.DingTalkSendMsgService;
import com.cutiyu.dinglog.util.DingTalkSendMsgUtil;
import org.springframework.util.Assert;

/**
 * @Describe:
 *
 */
public class DingTalkSendMsgServiceImpl implements DingTalkSendMsgService {

    private DingTalkWebHookProperties dingTalkWebHookProperties;


    @Override
    public boolean sendTextMsg(String content) {
        Assert.notNull(dingTalkWebHookProperties, DingTalkWebHookProperties.class.toString().concat("不能为空"));
        return sendTextMsg(content, dingTalkWebHookProperties.getAccessToken(), dingTalkWebHookProperties.getKeyWords());
    }

    @Override
    public boolean sendTextMsg(String content, String accessToken) {

        return sendTextMsg(content, accessToken, null);
    }

    @Override
    public boolean sendTextMsg(String content, String accessToken, String keyWords) {
        return DingTalkSendMsgUtil.sendTextMsg(content, accessToken, keyWords);
    }

    public DingTalkSendMsgServiceImpl(DingTalkWebHookProperties dingTalkWebHookProperties) {
        this.dingTalkWebHookProperties = dingTalkWebHookProperties;
    }

    public DingTalkSendMsgServiceImpl() {
    }
}
