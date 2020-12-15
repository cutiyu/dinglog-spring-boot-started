package cn.quantgroup.vcc.dinglog.mo;

import java.util.List;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-09 10:13
 */
public class MsgAt {

    /**
     * 发送消息@的人手机号
     */
    private List<String> atMobiles;

    /**
     * 是否@ 所有人
     */
    private boolean isAtAll;

    public List<String> getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(List<String> atMobiles) {
        this.atMobiles = atMobiles;
    }

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setAtAll(boolean atAll) {
        isAtAll = atAll;
    }
}
