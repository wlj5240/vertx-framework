package org.rayeye.vertx.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 获取上下文工具
 * @projectName:
 * @package:        org.rayeye.vertx.spring
 * @className:      SpringContextUtil
 * @description:    Spring 获取上下文工具
 * @author:         Neil.Zhou
 * @createDate:     2017/9/20 18:25
 * @updateUser:     Neil.Zhou
 * @updateDate:     2017/9/20 18:25
 * @updateRemark:   The modified content
 * @version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     *
     * @return Object 一个以所给名字注册的bean的实例(必须遵循Spring的生成规则)
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }
    /**
     * 获取对象
     *
     * @return Object 一个以所给名字注册的bean的实例(必须遵循Spring的生成规则)
     */
    public static Object getBean(Class classObj) throws BeansException {
        return applicationContext.getBean(classObj);
    }
}