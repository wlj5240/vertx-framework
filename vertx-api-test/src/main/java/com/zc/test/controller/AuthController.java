package com.zc.test.controller;

import com.zc.test.service.AuthService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rayeye.vertx.annotations.RouteHandler;
import org.rayeye.vertx.annotations.RouteMapping;
import org.rayeye.vertx.annotations.RouteMethod;
import org.rayeye.vertx.http.SenderInvokeHandler;
import org.rayeye.vertx.result.ResultOb;
import org.rayeye.vertx.util.ParamUtil;

/**
 * 权限入口
 * @ProjectName:
 * @Package:        com.zc.test.controller
 * @ClassName:      AuthController
 * @Description:    Describes the function of the class
 * @Author:         Neil.Zhou
 * @CreateDate:     2017/9/21 12:35
 * @UpdateUser:     Neil.Zhou
 * @UpdateDate:     2017/9/21 12:35
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/21</p>
 *
 */
@RouteHandler("auth")
public class AuthController extends SenderInvokeHandler{
    private static Logger logger = LogManager.getLogger(AuthController.class.getName());

    @RouteMapping(value = "/test", method = RouteMethod.GET)
    public Handler<RoutingContext> doTest() {
        return ctx -> {
            JsonObject param = ParamUtil.getRequestParams(ctx);
            logger.debug("参数:"+param.encode());
            ctx.response().setStatusCode(200);
            ctx.response().end(ResultOb.build().setMsg("Hello，欢迎使用测试地址.....").toString());
        };
    }


    /**
     * 登录
     * @method      doLogin
     * @author      Neil.Zhou
     * @param
     * @return      io.vertx.core.Handler<io.vertx.ext.web.RoutingContext>
     * @exception
     * @date        2017/9/21 12:36
     */
    @RouteMapping(value = "/login", method = RouteMethod.POST)
    public Handler<RoutingContext> doLogin() {

        return ctx -> {
            JsonObject param = ParamUtil.getRequestParams(ctx);
            param.put("serverIp",ctx.request().localAddress().host());
            param.put("clientIp",ctx.request().remoteAddress().host());
            logger.debug("参数:"+param.encode());
            sendProcess(ctx, AuthService.class.getName(),"doLogin",param);
        };
    }

}


