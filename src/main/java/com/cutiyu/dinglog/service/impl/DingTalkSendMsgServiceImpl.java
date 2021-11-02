package com.cutiyu.dinglog.service.impl;

import com.cutiyu.dinglog.properties.DingTalkWebHookProperties;
import com.cutiyu.dinglog.service.DingTalkSendMsgService;
import com.cutiyu.dinglog.util.DingTalkSendMsgUtil;
import com.sun.source.tree.BreakTree;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Describe:
 */
public class DingTalkSendMsgServiceImpl implements DingTalkSendMsgService {


    private DingTalkWebHookProperties dingTalkWebHookProperties;
    private ThreadPoolExecutor threadPoolExecutor;


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
        return sendTextMsgByThread(content, accessToken, keyWords);
    }

    private boolean sendTextMsgByThread(String content, String accessToken, String keyWords) {
        Future<Boolean> submit = threadPoolExecutor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean succ = DingTalkSendMsgUtil.sendTextMsg(content, accessToken, keyWords);
                return succ;
            }
        });

        Boolean sendSucc = false;
        try {
            sendSucc = submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return sendSucc;
    }

    public DingTalkSendMsgServiceImpl(DingTalkWebHookProperties dingTalkWebHookProperties) {
        this.dingTalkWebHookProperties = dingTalkWebHookProperties;
        this.threadPoolExecutor = createThreadPoolExecutor();
    }

    private ThreadPoolExecutor createThreadPoolExecutor() {
        int coreSize = 10;
        int maxSize = 10;
        long keepAliveTime = 0L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        if (dingTalkWebHookProperties != null && dingTalkWebHookProperties.getThreadPoolProperties() != null) {
            DingTalkWebHookProperties.ThreadPoolProperties poolProperties = dingTalkWebHookProperties.getThreadPoolProperties();
            coreSize = poolProperties.getCorePoolSize() == null ? coreSize : poolProperties.getCorePoolSize();
            maxSize = poolProperties.getMaxPoolSize() == null ? maxSize : poolProperties.getMaxPoolSize();
            keepAliveTime = poolProperties.getKeepAliveTime() == null ? keepAliveTime : poolProperties.getKeepAliveTime();
            timeUnit = poolProperties.getTimeUnit() == null ? timeUnit : poolProperties.getTimeUnit();
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, timeUnit, new LinkedBlockingQueue<Runnable>());
        return threadPoolExecutor;
    }
}
