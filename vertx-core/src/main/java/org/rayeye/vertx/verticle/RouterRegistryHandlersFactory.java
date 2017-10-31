package org.rayeye.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.vertx.standard.SingleVertxRouter;
import java.util.Objects;

/**
 * router 扫描注册器
 *
 * @projectName: vertx-core
 * @package: org.rayeye.vertx.verticle
 * @className: RouterRegistryHandlersFactory
 * @description: router 扫描注册器
 * @author: Neil.Zhou
 * @createDate: 2017/9/21 0:26
 * @updateUser: Neil.Zhou
 * @updateDate: 2017/9/21 0:26
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class RouterRegistryHandlersFactory extends AbstractVerticle {
    private static Log logger = LogFactory.get(RouterRegistryHandlersFactory.class);
    protected Router router;
    HttpServer server;
    /** 默认http server端口 **/
    public volatile int port=8080;

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
        logger.trace("To start listening to port {} ......",this.port);
        super.start();
        HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(1000000).setPort(this.port);
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
