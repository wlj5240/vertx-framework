package org.rayeye.vertx.http;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.vertx.address.EventBusAddress;
import org.rayeye.vertx.result.ResultOb;
import org.rayeye.vertx.standard.StandardVertxUtil;

/**
 * 事件发送调用处理器
 *
 * @ProjectName: vertx-framework
 * @Package: org.rayeye.vertx.http
 * @ClassName: SenderInvokeHandler
 * @Description: 事件发送调用处理器
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/20 19:27
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/20 19:27
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class SenderInvokeHandler {
    private static Log logger = LogFactory.get(SenderInvokeHandler.class);
    private static final int TIME_OUT=60000;
    private static final String DEFAULT_METHOD_COLUMN="method";
    private static final String DEFAULT_METHOD="execute";
    /**
     * 发送业务处理请求(适用于集群;本地vertx不推荐，需要使用反射)
     * @method      sendProcess
     * @author      Neil.Zhou
     * @param ctx router上下文
     * @param processor 对应的URI路由资源
     * @param params
     * @return      void
     * @exception
     * @date        2017/9/20 23:18
     */
    public static void sendProcess(RoutingContext ctx, String processor,JsonObject params){
        StandardVertxUtil.getStandardVertx().eventBus().<JsonObject>send(EventBusAddress.positiveFormate(processor),params,new DeliveryOptions().addHeader(DEFAULT_METHOD_COLUMN,DEFAULT_METHOD).setSendTimeout(TIME_OUT), resultBody -> {
            if (resultBody.failed()) {
                logger.error("Fail for the process.");
                ctx.fail(resultBody.cause());
                return;
            }
            JsonObject result = resultBody.result().body();
            if (result == null) {
                logger.error("Fail by result is null");
                ctx.fail(500);
                return;
            }
            ctx.response().setStatusCode(200);
            ctx.response().end(result.encode());
        });
    }
    /**
     * 发送业务处理请求(适用于集群、本地vertx)
     * @method      sendProcess
     * @author      Neil.Zhou
     * @param ctx router上下文
     * @param processor 对应的URI路由资源
     * @param params  请求参数
     * @param method 处理器中的方法
     * @return      void
     * @exception
     * @date        2017/9/20 23:18
     */
    public static void sendProcess(RoutingContext ctx, String processor,String method,JsonObject params){
        StandardVertxUtil.getStandardVertx().eventBus().<JsonObject>send(EventBusAddress.positiveFormate(processor),params,new DeliveryOptions().addHeader(DEFAULT_METHOD_COLUMN,method).setSendTimeout(TIME_OUT), resultBody -> {
            if (resultBody.failed()) {
                logger.error("Fail for the process.");
                ctx.fail(resultBody.cause());
                return;
            }
            JsonObject result = resultBody.result().body();
            if (result == null) {
                logger.error("Fail by result is null");
                ctx.fail(500);
                return;
            }
            ctx.response().setStatusCode(200);
            ctx.response().end(result.encode());
        });
    }

    /**
     * 发送业务处理请求(适用于集群、本地vertx)
     * 可自定义回调函数(异步)
     * @method      sendProcess
     * @author      Neil.Zhou
     * @param ctx router上下文
     * @param processor 对应的URI路由资源
     * @param method  处理器中的方法
     * @param params 请求参数
     * @param replyHandler 回调函数
     * @return      void
     * @exception
     * @date        2017/9/20 23:28
     */
    public static void sendProcess(RoutingContext ctx, String processor,String method, JsonObject params, Handler<AsyncResult<Message<JsonObject>>> replyHandler){
        StandardVertxUtil.getStandardVertx().eventBus().<JsonObject>send(EventBusAddress.positiveFormate(processor),params,new DeliveryOptions().addHeader(DEFAULT_METHOD_COLUMN,method).setSendTimeout(TIME_OUT), replyHandler);
    }
    /**
     * 执行异步业务，无需等待结果(默认结果成功)
     * @method      sendNSyncProcess
     * @author      Neil.Zhou
     * @param ctx router上下文
     * @param processor 对应的URI路由资源
     * @param params 请求参数
     * @return      void
     * @exception
     * @date        2017/9/20 23:31
     */
    public static void sendNSyncProcess(RoutingContext ctx, String processor, JsonObject params){
        StandardVertxUtil.getStandardVertx().eventBus().<JsonObject>send(EventBusAddress.positiveFormate(processor),params,new DeliveryOptions().addHeader(DEFAULT_METHOD_COLUMN,DEFAULT_METHOD).setSendTimeout(60000));
        ctx.response().setStatusCode(200);
        ctx.response().end(new ResultOb().toString());
    }
    /**
     * 执行异步业务，无需等待结果(默认结果成功)
     * @method      sendNSyncProcess
     * @author      Neil.Zhou
     * @param ctx router上下文
     * @param processor 对应的URI路由资源
     * @param method 处理器中的方法
     * @param params 请求参数
     * @return      void
     * @exception
     * @date        2017/9/20 23:34
     */
    public static void sendNSyncProcess(RoutingContext ctx, String processor,String method, JsonObject params){
        StandardVertxUtil.getStandardVertx().eventBus().<JsonObject>send(EventBusAddress.positiveFormate(processor),params,new DeliveryOptions().addHeader(DEFAULT_METHOD_COLUMN,method).setSendTimeout(60000));
        ctx.response().setStatusCode(200);
        ctx.response().end(new ResultOb().toString());
    }
}
