package com.zc.test;

import com.zc.test.router.RouterHandler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.common.log.dialect.log4j2.Log4j2LogFactory;
import org.rayeye.vertx.DeployVertxServer;
import org.rayeye.vertx.standard.StandardVertxUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Vertx main启动
 * @projectName:
 * @package:        com.zc.test
 * @className:      StartRunner
 * @description:   Vertx main启动
 * @author:         Neil.Zhou
 * @createDate:     2017/9/21 12:37
 * @updateUser:     Neil.Zhou
 * @updateDate:     2017/9/21 12:37
 * @updateRemark:   The modified content
 * @version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/21</p>
 *
 */
public class StartClusterRunner {

    public static void main(String[] args) throws Exception {
        // 设置使用日志类型
        LogFactory.setCurrentLogFactory(new Log4j2LogFactory());
        Log logger = LogFactory.get(StartClusterRunner.class.getName());
         ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.debug("=======================Runner  Deployment======================");
        StandardVertxUtil.setClusterVertx(Vertx.vertx(new VertxOptions()),"192.168.10.186");
        /***** 设置扫描器 api、handler(service) ***/
        // 直接使用框架默认模式启动
        //DeployVertxServer.startDeploy(new RouterHandlerFactory("com.zc.test.controller","api").abstractRouter(),"com.zc.test.service",8989);
        //添加自定义Router处理（这个建议是只用在httpserver应用中）
        DeployVertxServer.startDeploy(new RouterHandler("com.zc.test.controller","/api").abstractRouter(),"com.zc.test.service","/api",8989);

        // 如果是业务应用层，可直接发布
       // DeployVertxServer.startDeploy("com.zc.test.service","/api/center");

    }
}
