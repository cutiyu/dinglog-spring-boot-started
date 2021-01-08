package cn.quantgroup.dinglog.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-07 19:58
 */

public class DingTalkSendMsgUtil {

    private static final String dingTalkWebHookDomain = "https://oapi.dingtalk.com/robot/send?access_token=";

    /**
     * 发送json串消息
     */
    public static boolean sendTextMsg(String content, String accessToken) {

        return sendTextMsg(content, accessToken, null);
    }

    public static boolean sendTextMsg(String content, String accessToken, String keyWords) {

        Assert.notNull(content, "消息内容不能为空");
        Assert.notNull(accessToken, "accessToken不能为空");
        if (StringUtils.isNotEmpty(keyWords)) {
            content = keyWords.concat(content);
        }
        try {
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent(content);
            String tokenURL = dingTalkWebHookDomain.concat(accessToken);
            DingTalkClient client = new DefaultDingTalkClient(tokenURL);
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            request.setText(text);

            OapiRobotSendResponse response = client.execute(request);
            //System.out.println(JSONObject.toJSONString(response));
        } catch (Exception e) {
            System.out.println("========");
        }
        return true;
    }
}
