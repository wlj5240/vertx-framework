package com.zc.test.router;

import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.vertx.standard.SingleVertxRouter;
import org.rayeye.vertx.verticle.RouterHandlerFactory;

import static io.vertx.core.http.HttpHeaders.*;
import static io.vertx.core.http.HttpHeaders.ACCESS_CONTROL_MAX_AGE;

/**
 * 自定义Router规则
 *
 * @ProjectName: vertx-framework
 * @Package: com.zc.test.router
 * @ClassName: RouterHandler
 * @Description: Describes the function of the class
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/25 9:15
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/25 9:15
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class RouterHandler extends RouterHandlerFactory{
    private static Log logger = LogFactory.get(RouterHandler.class);

    public RouterHandler(String routerScanAddress, String gatewayPrefix) {
        super(routerScanAddress, gatewayPrefix);
    }

    public RouterHandler(String routerScanAddress) {
        super(routerScanAddress);
    }

    public Router specificRouter(){
        Router router= createRouter();
        // TODO: 2017/9/25  可以继续自定义实现
        // 利用order来设置自定义优先处理
        router.route().order(-1).handler(ctx->{
            logger.debug("测试中...");
            ctx.next();
        });
        return router;
    }

}
