package com.cutiyu.dinglog.demo;


/**
 * @Describe: 定义demo的函数实现
 *
 */
public class DemoService {

    /**
     * 配置的属性
     */
    private DemoProperties demoProperties;

    /**
     * 构造器
     */
    public DemoService(DemoProperties demoProperties) {
        this.demoProperties = demoProperties;
    }


    /**
     * 可注入实现的函数
     */
    public String say() {
        return demoProperties.getSayWhat() + "!  " + demoProperties.getToWho();
    }
}
