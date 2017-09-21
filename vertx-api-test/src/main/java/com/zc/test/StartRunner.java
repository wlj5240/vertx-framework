package com.zc.test;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.common.log.dialect.log4j2.Log4j2LogFactory;
import org.rayeye.vertx.DeployVertxServer;
import org.rayeye.vertx.standard.StandardVertxUtil;
import org.rayeye.vertx.verticle.RouterHandlerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Vertx main启动
 * @ProjectName:
 * @Package:        com.zc.test
 * @ClassName:      StartRunner
 * @Description:   Vertx main启动
 * @Author:         Neil.Zhou
 * @CreateDate:     2017/9/21 12:37
 * @UpdateUser:     Neil.Zhou
 * @UpdateDate:     2017/9/21 12:37
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/21</p>
 *
 */
public class StartRunner{

    public static void main(String[] args) throws Exception {
        // 设置使用日志类型
        LogFactory.setCurrentLogFactory(new Log4j2LogFactory());
        Log logger = LogFactory.get(StartRunner.class.getName());
         ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.debug("=======================Runner  Deployment======================");
        StandardVertxUtil.getStandardVertx(Vertx.vertx(new VertxOptions()));
        // 设置扫描器 api、handler(service)
        DeployVertxServer.startDeploy(new RouterHandlerFactory("com.zc.test.controller","api").createRouter(),"com.zc.test.service",8989);
    }
}
