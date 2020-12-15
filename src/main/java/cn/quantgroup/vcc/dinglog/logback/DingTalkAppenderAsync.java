package cn.quantgroup.vcc.dinglog.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
/**
 * @Describe:
 * @Created by tangfeng 2020-12-14 14:50
 */
public class DingTalkAppenderAsync extends UnsynchronizedAppenderBase<ILoggingEvent> {

    Layout<ILoggingEvent> layout;

    //自定义配置
    String printString;

    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    public String getPrintString() {
        return printString;
    }

    public void setPrintString(String printString) {
        this.printString = printString;
    }

    @Override
    public void start(){
        /**
         * 这里可以做些初始化判断 比如layout不能为null
         */
        if(layout == null) {
            addWarn("Layout was not defined");
        }
        /**
         * 或者写入数据库 或者redis时 初始化连接等等
         */
        super.start();
    }

    @Override
    public void stop()
    {
        /**
         * 释放相关资源，如数据库连接，redis线程池等等
         */
        System.out.println("logback-stop方法被调用");
        if(!isStarted()) {
            return;
        }
        super.stop();
    }

    @Override
    public void append(ILoggingEvent event) {
        if (event == null || !isStarted()){
            return;
        }

        /**
         * 自定义日志输出的逻辑
         * 比如 操作数据库  redis kafka
         * 当然，我的本质目的是发钉钉消息
         */
        /*System.out.print("获取输出值---"+event.getFormattedMessage());
        System.out.println("格式化输出日志："+printString + "：" + layout.doLayout(event));
        System.out.println("==============================================  MyLogbackAppender要发消息了");*/
        //发钉钉消息： 具体处理逻辑，参考钉钉的文档，此处暂不实现
        //后续计划会开发一个 dingtalk-log-springboot-start 包

        try {
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent("这是一个测试");

            //https://oapi.dingtalk.com/robot/send?access_token=
            String accessToken = "{填入自己钉钉群自定义机器人的 webhook token}";
            String tokenURL = "https://oapi.dingtalk.com/robot/send?access_token=".concat(accessToken);
            DingTalkClient client = new DefaultDingTalkClient(tokenURL);
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            request.setText(text);

            //OapiRobotSendResponse response = client.execute(request);
        } catch (Exception e) {

        }
    }
}

