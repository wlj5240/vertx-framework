package org.rayeye.vertx.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 获取上下文工具
 * @ProjectName:
 * @Package:        org.rayeye.vertx.spring
 * @ClassName:      SpringContextUtil
 * @Description:    Spring 获取上下文工具
 * @Author:         Neil.Zhou
 * @CreateDate:     2017/9/20 18:25
 * @UpdateUser:     Neil.Zhou
 * @UpdateDate:     2017/9/20 18:25
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法
     */
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