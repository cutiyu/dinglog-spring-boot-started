package cn.quantgroup.vcc.dinglog.mo;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-09 13:56
 */
public class ContentMarkDown {


    /**
     * title : 杭州天气
     * text : #### 杭州天气 @150XXXXXXXX
                 > 9度，西北风1级，空气良89，相对温度73%
                 > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)
                 > ###### 10点20分发布 [天气](https://www.dingtalk.com)

     */

    private String title;
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
