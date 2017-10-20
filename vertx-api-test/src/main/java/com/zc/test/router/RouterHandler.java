package com.zc.test.router;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.vertx.http.Request;
import org.rayeye.vertx.http.SenderInvokeHandler;
import org.rayeye.vertx.util.ParamUtil;
import org.rayeye.vertx.verticle.RouterHandlerFactory;

/**
 * 自定义Router规则
 *
 * @projectName: vertx-framework
 * @package: com.zc.test.router
 * @className: RouterHandler
 * @description: Describes the function of the class
 * @author: Neil.Zhou
 * @createDate: 2017/9/25 9:15
 * @updateUser: Neil.Zhou
 * @updateDate: 2017/9/25 9:15
 * @updateRemark: The modified content
 * @version: 1.0
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

    /**
     * 非集群环境，自定义路由处理
     * @method      specificRouter
     * @author      Neil.Zhou
     * @param
     * @return      io.vertx.ext.web.Router
     * @exception
     * @date        2017/9/26 17:22
     */
    public Router specificRouter(){
        Router router= createRouter();
        // TODO: 2017/9/25  可以继续自定义实现
        // 利用order来设置自定义优先处理
        // 测试地址 http://127.0.0.1:8989/api/test1
        router.route().order(-1).handler(ctx->{
            logger.debug("测试中...");
            // 1、可以不用API定义层，但需要有约束
            //SenderInvokeHandler.sendProcess(ctx,ctx.request().path(),"doLogin",new JsonObject());
            // 2、可以不用API定义层、且不需要指定具体的执行函数，但需要严格约束,建议集群才使用
            //SenderInvokeHandler.sendProcess(ctx,ctx.request().path(),new JsonObject(request.toString()));
            ctx.next();
        });
        return router;
    }

    /**
     * 集群环境，自定义路由处理
     * 不用定义API层
     * @method      abstractRouter
     * @author      Neil.Zhou
     * @param
     * @return      io.vertx.ext.web.Router
     * @exception
     * @date        2017/9/26 17:22
     */
    public Router abstractRouter(){
        Router router= createRouter();
        // TODO: 2017/9/25  可以继续自定义实现
        // 利用order来设置自定义优先处理
        router.route().order(-1).handler(ctx->{
            logger.debug("测试中...");
            // 封装请求对象
            Request request=new Request(ctx.request().absoluteURI(),ctx.request().path().replace("",""),ctx.request().path(),ctx.request().host(),ctx.request().rawMethod(), ParamUtil.getRequestParams(ctx));
            SenderInvokeHandler.sendProcess(ctx,ctx.request().path(),new JsonObject(request.toString()));
        });
        return router;
    }

}
