package org.rayeye.vertx.verticle;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.sockjs.*;
import org.apache.commons.lang3.StringUtils;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.vertx.annotations.RouteHandler;
import org.rayeye.vertx.annotations.RouteMapping;
import org.rayeye.vertx.annotations.RouteMethod;
import org.rayeye.vertx.standard.SingleVertxRouter;
import org.rayeye.vertx.standard.StandardVertxUtil;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.vertx.core.http.HttpHeaders.*;
import static io.vertx.core.http.HttpHeaders.ACCESS_CONTROL_MAX_AGE;

/**
 * Router 对象创建
 *
 * @projectName: vertx-core
 * @package: org.rayeye.vertx.verticle
 * @className: RouterHandlerFactory
 * @description: Describes the function of the class
 * @author: Neil.Zhou
 * @createDate: 2017/9/21 10:42
 * @updateUser: Neil.Zhou
 * @updateDate: 2017/9/21 10:42
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class RouterHandlerFactory {
    private static Log logger = LogFactory.get(RouterHandlerFactory.class);

    /** 需要扫描注册的Router路径 **/
    private volatile Reflections reflections = new Reflections("com.rayeye");
    /** 默认api前缀 **/
    public volatile String GATEWAY_PREFIX="/";

    private final String PREFIX="/";

    public RouterHandlerFactory(String routerScanAddress,String gatewayPREFIX) {
        Objects.requireNonNull(routerScanAddress, "The router package address scan is empty.");
        this.reflections = new Reflections(routerScanAddress);
        this.GATEWAY_PREFIX=gatewayPREFIX;
    }
    public RouterHandlerFactory(String routerScanAddress) {
        Objects.requireNonNull(routerScanAddress, "The router package address scan is empty.");
        this.reflections = new Reflections(routerScanAddress);
    }
    /**
     * 开始扫描并注册handler
     * @method      createRouter
     * @author      Neil.Zhou
     * @param
     * @return      io.vertx.ext.web.Router
     * @exception
     * @date        2017/9/21 9:53
     */
    public Router createRouter() {
        Router router = SingleVertxRouter.getRouter();
        // 如果需要特色设置，可自行在具体的api中修改
        setHeader(router);
        Set<HttpMethod> method = new HashSet<HttpMethod>(){{
            add(HttpMethod.GET);
            add(HttpMethod.POST);
            add(HttpMethod.OPTIONS);
            add(HttpMethod.PUT);
            add(HttpMethod.DELETE);
            add(HttpMethod.HEAD);
        }};
        /** 添加跨域的方法 **/
        router.route().order(-9).handler(CorsHandler.create("*").allowedMethods(method));
        router.route().order(-8).handler(CookieHandler.create());
        router.route().order(-7).handler(BodyHandler.create());
        try {
            logger.trace("Register available request handlers...");
            Set<Class<?>> handlers = reflections.getTypesAnnotatedWith(RouteHandler.class);
            for (Class<?> handler : handlers) {
                try {
                    registerNewHandler(router,handler);
                } catch (Exception e) {
                    logger.error(e,"Error register {}", handler);
                }
            }
        } catch (Exception e) {
            logger.error(e,"Manually Register Handler Fail，Error details："+e.getMessage());
        }
        // 加入socketJS
        joinSocketJS();
        return router;
    }

    /**
     * @method      createSocketRouter
     * @author      Neil.Zhou
     * @param inbound 接收客户端消息的地址匹配
     * @param outbound 发送客户端消息的地址匹配
     * @return      io.vertx.ext.web.Router
     * @exception
     * @date        2017-10-13 19:35
     */
    public Router createSocketRouter(String inbound,String outbound) {
        Router router = SingleVertxRouter.getRouter();
        // 如果需要特色设置，可自行在具体的api中修改
        setHeader(router);
        Set<HttpMethod> method = new HashSet<HttpMethod>(){{
            add(HttpMethod.GET);
            add(HttpMethod.POST);
            add(HttpMethod.OPTIONS);
            add(HttpMethod.PUT);
            add(HttpMethod.DELETE);
            add(HttpMethod.HEAD);
        }};
        /** 添加跨域的方法 **/
        router.route().order(-9).handler(CorsHandler.create("*").allowedMethods(method));
        router.route().order(-8).handler(CookieHandler.create());
        router.route().order(-7).handler(BodyHandler.create());
        try {
            logger.trace("Register available request handlers...");
            Set<Class<?>> handlers = reflections.getTypesAnnotatedWith(RouteHandler.class);
            for (Class<?> handler : handlers) {
                try {
                    registerNewHandler(router,handler);
                } catch (Exception e) {
                    logger.error(e,"Error register {}", handler);
                }
            }
        } catch (Exception e) {
            logger.error(e,"Manually Register Handler Fail，Error details："+e.getMessage());
        }
        // 加入socketJS
        joinSocketJS(inbound,outbound);
        return router;
    }

    /**
     * 想Router加入SocketJS支持
     * @method      joinSocketJS
     * @author      Neil.Zhou
     * @param inbound 接收客户端消息的地址匹配
     * @param outbound 发送客户端消息的地址匹配
     * @return      io.vertx.ext.web.Router
     * @exception
     * @date        2017-10-13 15:40
     */
    public Router joinSocketJS(String inbound,String outbound) {
        Router router = SingleVertxRouter.getRouter();
        // ping 2s
        SockJSHandlerOptions options = new SockJSHandlerOptions().setInsertJSESSIONID(true).setSessionTimeout(2*1000*1000).setHeartbeatInterval(2000);
        SockJSHandler sockJSHandler = SockJSHandler.create(StandardVertxUtil.getLocalVertx(), options);
        // 允许通过的规则
        BridgeOptions bridgeOptions = new BridgeOptions();
        if(StringUtils.isNotBlank(inbound)){
            bridgeOptions.addInboundPermitted(new PermittedOptions().setAddressRegex(inbound));
        }
        if(StringUtils.isNotBlank(outbound)){
            bridgeOptions.addOutboundPermitted(new PermittedOptions().setAddressRegex(outbound));
        }
        sockJSHandler.bridge(bridgeOptions, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                //create bus
                logger.trace("The socket channel to establish.");
            }else if (event.type() == BridgeEventType.REGISTER) {
                logger.trace("Register [{}] event on the eventbus",event.getRawMessage().getString("address"));
            }else if (event.type() == BridgeEventType.RECEIVE) {
                logger.trace("The Socket client to the server sends the request,the message is {}",event.getRawMessage().encode());
            }else if (event.type() == BridgeEventType.SEND) {
                //3、发送消息
                logger.trace("The Socket server to the client sends the request,the message is {}",event.getRawMessage().encode());
            }else if (event.type() == BridgeEventType.PUBLISH) {
                //4、发布消息
                logger.trace("The Socket server to the client publish the request,the message is {}",event.getRawMessage().encode());
            }else if (event.type() == BridgeEventType.UNREGISTER) {
                //5、注销事件
                logger.trace("The [{}] socket channel to unregister.",event.getRawMessage().getString("address"));
            }else if (event.type() == BridgeEventType.SOCKET_CLOSED) {
                //6、关闭
                logger.trace("The socket channel to closed.");
            }
            event.complete(true);
        });
        router.route(this.GATEWAY_PREFIX+"/ws-bus/*").handler(sockJSHandler);
        return router;
    }
    /**
     * 想Router加入SocketJS支持(默认所有地址)
     * @method      joinSocketJS
     * @author      Neil.Zhou
     * @return      io.vertx.ext.web.Router
     * @exception
     * @date        2017-10-13 15:40
     */
    public Router joinSocketJS() {
        Router router = SingleVertxRouter.getRouter();
        // ping 2s
        SockJSHandlerOptions options = new SockJSHandlerOptions().setSessionTimeout(2*1000*1000).setHeartbeatInterval(2000);
        SockJSHandler sockJSHandler = SockJSHandler.create(StandardVertxUtil.getLocalVertx(), options);
        // 允许通过的规则
        BridgeOptions bridgeOptions = new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddressRegex("^([A-Za-z0-9_]|([/]|[\\.]))*$"))
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("^([A-Za-z0-9_]|([/]|[\\.]))*$"));
        sockJSHandler.bridge(bridgeOptions, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                //create bus
                logger.trace("The socket channel to establish.");
            }else if (event.type() == BridgeEventType.REGISTER) {
                logger.trace("Register [{}] event on the eventbus",event.getRawMessage().getString("address"));
            }else if (event.type() == BridgeEventType.RECEIVE) {
                logger.trace("The Socket client to the server sends the request,the message is {}",event.getRawMessage().encode());
            }else if (event.type() == BridgeEventType.SEND) {
                //3、发送消息
                logger.trace("The Socket server to the client sends the request,the message is {}",event.getRawMessage().encode());
            }else if (event.type() == BridgeEventType.PUBLISH) {
                //4、发布消息
                logger.trace("The Socket server to the client publish the request,the message is {}",event.getRawMessage().encode());
            }else if (event.type() == BridgeEventType.UNREGISTER) {
                //5、注销事件
                logger.trace("The socket channel to unregister.");
            }else if (event.type() == BridgeEventType.SOCKET_CLOSED) {
                //6、关闭
                logger.trace("The socket channel to closed.");
            }
            event.complete(true);
        });
        router.route(this.GATEWAY_PREFIX+"/ws-bus/*").handler(sockJSHandler);
        return router;
    }

    /**
     * 设置header，目前所有请求就会优先被初始设置response header
     * @method      setHeader
     * @author      Neil.Zhou
     * @param router
     * @return      void
     * @exception
     * @date        2017/9/25 13:24
     */
    private void setHeader(Router router){
        router.route().order(-10).handler(ctx -> {
            logger.trace("The HTTP service request address information ===>path:{}, uri:{}, method:{}",ctx.request().path(),ctx.request().absoluteURI(),ctx.request().method());
            logger.trace("The requester auth information ===>Authorization:{}, Version:{}, Dev:{}",ctx.request().headers().get("Authorization"),ctx.request().headers().get("Version"),ctx.request().headers().get("Dev"));
            ctx.response().headers().add(CONTENT_TYPE, "application/json; charset=utf-8");
            ctx.response().headers().add(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            ctx.response().headers().add(ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, PUT, DELETE, HEAD");
            ctx.response().headers().add(ACCESS_CONTROL_ALLOW_HEADERS, "X-PINGOTHER, Origin,Content-Type, Accept, X-Requested-With,Dev,Authorization,Version,orgCode");
            ctx.response().headers().add(ACCESS_CONTROL_MAX_AGE, "1728000");
            ctx.next();
        });
    }
    /**
     * 开始注册router==》handler
     * @method      registerNewHandler
     * @author      Neil.Zhou
     * @param router
     * @param handler
     * @return      void
     * @exception
     * @date        2017/9/25 13:26
     */
    private void registerNewHandler(Router router,Class<?> handler) throws Exception {
        String root = this.GATEWAY_PREFIX;
        if(!root.startsWith(this.PREFIX)){
            root=this.PREFIX+root;
        }
        if(!root.endsWith(this.PREFIX)){
            root=root+this.PREFIX;
        }
        if (handler.isAnnotationPresent(RouteHandler.class)) {
            RouteHandler routeHandler = handler.getAnnotation(RouteHandler.class);
            root =root+routeHandler.value();
        }
        Object instance = handler.newInstance();
        Method[] methods = handler.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RouteMapping.class)) {
                RouteMapping mapping = method.getAnnotation(RouteMapping.class);
                RouteMethod routeMethod = mapping.method();
                String routeUrl="";
                if(mapping.value().startsWith("/:")){
                    routeUrl=(method.getName()+mapping.value());
                }else{
                    routeUrl=(mapping.value().endsWith(method.getName())?mapping.value():(mapping.isCover()?mapping.value():mapping.value()+method.getName()));
                    if(routeUrl.startsWith("/")){
                        routeUrl=routeUrl.substring(1);
                    }
                }
                String url =root.concat("/" +routeUrl).replace("//","/");
                Handler<RoutingContext> methodHandler = (Handler<RoutingContext>) method.invoke(instance);
                logger.trace("Register New Handler -> {}:{}", routeMethod, url);
                switch (routeMethod) {
                    case POST:
                        router.post(url).handler(methodHandler);
                        break;
                    case PUT:
                        router.put(url).handler(methodHandler);
                        break;
                    case DELETE:
                        router.delete(url).handler(methodHandler);
                        break;
                    case ROUTE:
                        // get、post、delete、put
                        router.route(url).handler(methodHandler);
                        break;
                    case GET:
                        // fall through
                    default:
                        router.get(url).handler(methodHandler);
                        break;
                }
            }
        }
    }
}
