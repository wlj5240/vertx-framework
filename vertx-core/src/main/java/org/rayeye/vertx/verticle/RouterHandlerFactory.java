package org.rayeye.vertx.verticle;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
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
 * Router 对象创建
 *
 * @ProjectName: vertx-core
 * @Package: org.rayeye.vertx.verticle
 * @ClassName: RouterHandlerFactory
 * @Description: Describes the function of the class
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/21 10:42
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/21 10:42
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class RouterHandlerFactory {
    private static Log logger = LogFactory.get(RouterHandlerFactory.class);

    // 需要扫描注册的Router路径
    private static volatile Reflections reflections = new Reflections("com.rayeye");
    // 默认api前缀
    public static volatile String GATEWAY_PREFIX="/";

    public RouterHandlerFactory(String routerScanAddress,String gatewayPrefix) {
        Objects.requireNonNull(routerScanAddress, "The router package address scan is empty.");
        reflections = new Reflections(routerScanAddress);
        this.GATEWAY_PREFIX=gatewayPrefix;
    }
    public RouterHandlerFactory(String routerScanAddress) {
        Objects.requireNonNull(routerScanAddress, "The router package address scan is empty.");
        reflections = new Reflections(routerScanAddress);
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
        router.route().handler(ctx -> {
            logger.trace("The HTTP service request address information ===>path:{}, uri:{}, method:{}",ctx.request().path(),ctx.request().absoluteURI(),ctx.request().method());
            logger.trace("The requester auth information ===>Authorization:{}, Version:{}, Dev:{}",ctx.request().headers().get("Authorization"),ctx.request().headers().get("Version"),ctx.request().headers().get("Dev"));
            ctx.response().headers().add(CONTENT_TYPE, "application/json; charset=utf-8");
            ctx.response().headers().add(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            ctx.response().headers().add(ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, PUT, DELETE, HEAD");
            ctx.response().headers().add(ACCESS_CONTROL_ALLOW_HEADERS, "X-PINGOTHER, Origin,Content-Type, Accept, X-Requested-With,Dev,Authorization,Version,orgCode");
            ctx.response().headers().add(ACCESS_CONTROL_MAX_AGE, "1728000");
            ctx.next();
        });
        Set<HttpMethod> method = new HashSet<HttpMethod>(){{
            add(HttpMethod.GET);
            add(HttpMethod.POST);
            add(HttpMethod.OPTIONS);
            add(HttpMethod.PUT);
            add(HttpMethod.DELETE);
            add(HttpMethod.HEAD);
        }};
        /** 添加跨域的方法 **/
        router.route().handler(CorsHandler.create("*").allowedMethods(method));
        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());
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
        return router;
    }

    private void registerNewHandler(Router router,Class<?> handler) throws Exception {
        String root = GATEWAY_PREFIX;
        if(!root.startsWith("/")){
            root="/"+root;
        }
        if(!root.endsWith("/")){
            root=root+"/";
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
                String url =root.concat("/" +routeUrl);
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
                        router.route(url).handler(methodHandler);// get、post、delete、put
                        break;
                    case GET: // fall through
                    default:
                        router.get(url).handler(methodHandler);
                        break;
                }
            }
        }
    }
}
