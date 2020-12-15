package cn.quantgroup.vcc.dinglog.service;

import cn.quantgroup.vcc.dinglog.properties.WebHookSecurityProperties;
import cn.quantgroup.vcc.dinglog.util.HttpClientUitl;
import cn.quantgroup.vcc.dinglog.util.HttpUtil;
import cn.quantgroup.vcc.dinglog.util.WebHookSignUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.file.WatchEvent;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-07 19:58
 */
public class WebHookServiceImpl {
    Logger log = LoggerFactory.getLogger(WebHookServiceImpl.class);

    private WebHookSecurityProperties webHookSecurityProperties;

    public WebHookServiceImpl() {
    }

    public WebHookServiceImpl(WebHookSecurityProperties webHookSecurityProperties) {
        this.webHookSecurityProperties = webHookSecurityProperties;
    }

    /**
     * 发送json串消息
     */
    public boolean sendDingMsg(String json) throws Exception{



        try {
            String sendUrl = getSendUrl();
            //String s = HttpUtil.doPost(sendUrl,null);
            String s = HttpClientUitl.doGet("http://talos-vcc.liangkebang.net/ex/login/getVccToken?phone=17346526631&systemKey=vcc&loginFrom=159860");
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("发出的消息内容是：" + json+ "________"+ JSONObject.toJSONString(webHookSecurityProperties));
        log.info("json:{},webHookProperties:{}",json, webHookSecurityProperties.toString());
        return true;
    }

    private String getSendUrl()throws Exception{

        checkWebHookPreoperties();

        String url = webHookSecurityProperties.getUrl().concat("?access_token=").concat(webHookSecurityProperties.getAccessToken());
        long timeStamp = System.currentTimeMillis();
        String signPath = null;
        if (!StringUtils.isEmpty(webHookSecurityProperties.getSignSecret())) {
            String sign = WebHookSignUtil.sign(timeStamp, webHookSecurityProperties.getSignSecret());
            signPath = String.format("&timestamp=%s&sign=%s", timeStamp, sign);
        }

        if (!StringUtils.isEmpty(signPath)) {
            url = url.concat(signPath);
        }
        return url;
    }

    private void checkWebHookPreoperties(){
        Assert.notNull(webHookSecurityProperties.getAccessToken(),"accessToken is null");
    }

}
