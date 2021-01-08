package cn.quantgroup.dinglog.demo;


/**
 * @Describe: 定义demo的函数实现
 * @Created by tangfeng 2020-05-06 16:39
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
