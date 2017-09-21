package org.rayeye.vertx;

import io.vertx.ext.web.Router;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.vertx.standard.StandardVertxUtil;
import org.rayeye.vertx.verticle.RegistryHandlersFactory;
import org.rayeye.vertx.verticle.RouterRegistryHandlersFactory;

import java.io.IOException;

/**
 * 开始注册vertx相关服务
 * @ProjectName:	vertx-core
 * @Package:        org.rayeye.vertx
 * @ClassName:      DeployVertxServer
 * @Description:    Describes the function of the class
 * @Author:         Neil.Zhou
 * @CreateDate:     2017/9/21 10:15
 * @UpdateUser:     Neil.Zhou
 * @UpdateDate:     2017/9/21 10:15
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/21</p>
 *
 */
public class DeployVertxServer {
	private static Log logger = LogFactory.get(DeployVertxServer.class);

	public static void startDeploy(int port) throws IOException {
		logger.trace("Start Deploy....");
		StandardVertxUtil.getStandardVertx().deployVerticle(new RouterRegistryHandlersFactory(port));
	}
	public static void startDeploy(Router router,int port) throws IOException {
		logger.trace("Start Deploy....");
		StandardVertxUtil.getStandardVertx().deployVerticle(new RouterRegistryHandlersFactory(router,port));
	}
	public static void startDeploy(Router router,String handlerScan,int port) throws IOException {
		logger.trace("Start Deploy....");
		StandardVertxUtil.getStandardVertx().deployVerticle(new RouterRegistryHandlersFactory(router,port));
		logger.trace("Start registry handler....");
		new RegistryHandlersFactory(handlerScan).registerVerticle();
	}
	public static void startDeploy(Router router,String handlerScan,String appPrefix,int port) throws IOException {
		logger.trace("Start Deploy....");
		StandardVertxUtil.getStandardVertx().deployVerticle(new RouterRegistryHandlersFactory(router,port));
		logger.trace("Start registry handler....");
		new RegistryHandlersFactory(handlerScan,appPrefix).registerVerticle();
	}
	public static void startDeploy(String handlerScan,String appPrefix) throws IOException {
		logger.trace("Start registry handler....");
		new RegistryHandlersFactory(handlerScan,appPrefix).registerVerticle();
	}
}
