package cn.quantgroup.vcc.dinglog.mo;

import java.util.List;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-09 13:57
 */
public class ContentActionCardMore {


    /**
     * title : 乔布斯 20 年前想打造一间苹果咖啡厅，而它正是 Apple Store 的前身
     * text : ![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png)   ### 乔布斯 20 年前想打造的苹果咖啡厅   Apple Store 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划
     * btnOrientation : 0
     * btns : [{"title":"内容不错","actionURL":"https://www.dingtalk.com/"},{"title":"不感兴趣","actionURL":"https://www.dingtalk.com/"}]
     */

    /**
     * 首屏会话透出的展示内容
     */
    private String title;
    /**
     * markdown格式的消息
     */
    private String text;
    private String btnOrientation;

    /**
     * 按钮
     */
    private List<BtnContent> btns;

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

    public String getBtnOrientation() {
        return btnOrientation;
    }

    public void setBtnOrientation(String btnOrientation) {
        this.btnOrientation = btnOrientation;
    }

    public List<BtnContent> getBtns() {
        return btns;
    }

    public void setBtns(List<BtnContent> btns) {
        this.btns = btns;
    }

    public static class BtnContent {
        /**
         * title : 内容不错
         * actionURL : https://www.dingtalk.com/
         */

        /**
         * 按钮标题
         */
        private String title;

        /**
         * 按钮跳转URL
         */
        private String actionURL;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getActionURL() {
            return actionURL;
        }

        public void setActionURL(String actionURL) {
            this.actionURL = actionURL;
        }
    }
}
