package org.rayeye.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.vertx.annotations.RouteHandler;
import org.rayeye.vertx.annotations.RouteMapping;
import org.rayeye.vertx.annotations.RouteMethod;
import org.rayeye.vertx.standard.SingleVertxRouter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.vertx.core.http.HttpHeaders.*;
import static io.vertx.core.http.HttpHeaders.ACCESS_CONTROL_MAX_AGE;

/**
 * router 扫描注册器
 *
 * @ProjectName: vertx-core
 * @Package: org.rayeye.vertx.verticle
 * @ClassName: RouterRegistryHandlersFactory
 * @Description: router 扫描注册器
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/21 0:26
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/21 0:26
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class RouterRegistryHandlersFactory extends AbstractVerticle {
    private static Log logger = LogFactory.get(RouterRegistryHandlersFactory.class);
    protected Router router;
    HttpServer server;
    // 默认http server端口
    public static volatile int port=8080;

    /** Constructs a new RouterRegistryHandlersFactory. */
    public RouterRegistryHandlersFactory(int port) {
        this.router= SingleVertxRouter.getRouter();
        if(port>0){
            this.port=port;
        }
    }
    public RouterRegistryHandlersFactory(Router router) {
        Objects.requireNonNull(router, "The router is empty.");
        this.router=router;
    }
    public RouterRegistryHandlersFactory(Router router,int port) {
        this.router=router;
        if(port>0){
            this.port=port;
        }
    }

    /**
     * 重写启动verticle
     * @method      start
     * @author      Neil.Zhou
     * @param future
     * @return      void
     * @exception
     * @date        2017/9/21 0:33
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        logger.trace("To start listening to port {} ......",port);
        super.start();
        HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(1000000).setPort(port);
        server = vertx.createHttpServer(options);
        server.requestHandler(router::accept);
        server.listen(result -> {
            if (result.succeeded()) {
                future.complete();
            } else {
                future.fail(result.cause());
            }
        });
    }

    /**
     * 重写停止verticle
     * @method      start
     * @author      Neil.Zhou
     * @param future
     * @return      void
     * @exception
     * @date        2017/9/21 0:33
     */
    @Override
    public void stop(Future<Void> future) {
        if (server == null) {
            future.complete();
            return;
        }
        server.close(result -> {
            if (result.failed()) {
                future.fail(result.cause());
            } else {
                future.complete();
            }
        });
    }

}
