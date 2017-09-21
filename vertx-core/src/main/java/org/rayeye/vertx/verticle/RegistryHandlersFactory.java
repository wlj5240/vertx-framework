package org.rayeye.vertx.verticle;

import io.vertx.core.DeploymentOptions;
import org.apache.commons.lang3.StringUtils;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.vertx.address.EventBusAddress;
import org.rayeye.vertx.annotations.ServiceMethod;
import org.rayeye.vertx.annotations.VerticleMapping;
import org.rayeye.vertx.standard.StandardVertxUtil;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * 处理器注册工厂
 *
 * @ProjectName: vertx-core
 * @Package: org.rayeye.vertx.http
 * @ClassName: RegistryHandlersFactory
 * @Description: 处理器注册工厂
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/20 23:38
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/20 23:38
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class RegistryHandlersFactory {
    private static Log logger = LogFactory.get(RegistryHandlersFactory.class);

    public static volatile String BASE_ROUTER="/";
    // 需要扫描注册的Router路径
    private static volatile Reflections reflections =null;
    public RegistryHandlersFactory(String handlerScanAddress,String appPrefix) {
        Objects.requireNonNull(handlerScanAddress, "The router package address scan is empty.");
        reflections = new Reflections(handlerScanAddress);
        this.BASE_ROUTER=appPrefix;
    }
    public RegistryHandlersFactory(String handlerScanAddress) {
        Objects.requireNonNull(handlerScanAddress, "The router package address scan is empty.");
        reflections = new Reflections(handlerScanAddress);
    }
    /**
     * verticle 服务注册
     * @method      registerVerticle
     * @author      Neil.Zhou
     * @return      void
     * @exception
     * @date        2017/9/21 0:23
     */
    public void registerVerticle() {
        logger.trace("Register Service Verticle...");
        Set<Class<?>> verticles = reflections.getTypesAnnotatedWith(VerticleMapping.class);
        String busAddressPrefix="";
        for (Class<?> service : verticles) {
            try {
                if (service.isAnnotationPresent(VerticleMapping.class)) {
                    VerticleMapping routeHandler = service.getAnnotation(VerticleMapping.class);
                    if (StringUtils.isBlank(routeHandler.value())) {
                        busAddressPrefix = service.getName();
                    } else {
                        busAddressPrefix = routeHandler.value();
                    }
                    if (busAddressPrefix.startsWith("/")) {
                        busAddressPrefix = busAddressPrefix.substring(1, busAddressPrefix.length());
                    }
                    if (!BASE_ROUTER.endsWith("/")) {
                        busAddressPrefix = BASE_ROUTER + "/" + busAddressPrefix;
                    } else {
                        busAddressPrefix = BASE_ROUTER + busAddressPrefix;
                    }
                    if (busAddressPrefix.endsWith("/")) {
                        busAddressPrefix = busAddressPrefix.substring(0, busAddressPrefix.length() - 1);
                    }
                    if (busAddressPrefix.startsWith("/")) {
                        busAddressPrefix = busAddressPrefix.substring(1, busAddressPrefix.length());
                    }
                    if (BASE_ROUTER.equals(busAddressPrefix)) {
                        /***** 每一个方法都部署一个verticle *****/
                        Method[] methods = service.getDeclaredMethods();
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(ServiceMethod.class)) {
                                ServiceMethod serviceMethod = method.getAnnotation(ServiceMethod.class);
                                String methodTarget = serviceMethod.value();
                                if (!methodTarget.startsWith("/")) {
                                    methodTarget = "/" + methodTarget;
                                }
                                logger.trace("[Method] The register processor address is {}", EventBusAddress.positiveFormate(busAddressPrefix + methodTarget));
                                StandardVertxUtil.getStandardVertx().deployVerticle(new VerticleHandlerFactory(toLowerCaseFirstOne(service.getSimpleName()), EventBusAddress.positiveFormate(busAddressPrefix.concat(methodTarget))), new DeploymentOptions());
                            }
                        }
                    } else {
                        logger.trace("The register processor address is {}", EventBusAddress.positiveFormate(busAddressPrefix));
                        StandardVertxUtil.getStandardVertx().deployVerticle(new VerticleHandlerFactory(toLowerCaseFirstOne(service.getSimpleName()), EventBusAddress.positiveFormate(busAddressPrefix)), new DeploymentOptions());
                    }
                }
            } catch (Exception e) {
                logger.error(e,"The {} Verticle register Service is fail，{}", service,e.getMessage());
            }
        }
    }
    /**
     * 获得Spring bean name，交给Spring来提取容器中的bean
     * @method      toLowerCaseFirstOne
     * @author      Neil.Zhou
     * @param serviceName service 名称
     * @return      java.lang.String
     * @exception
     * @date        2017/9/21 0:21
     */
    private static String toLowerCaseFirstOne(String serviceName){
        if(Character.isLowerCase(serviceName.charAt(0))) {
            return serviceName;
        }else {
            return (new StringBuilder()).append(Character.toLowerCase(serviceName.charAt(0))).append(serviceName.substring(1)).toString();
        }
    }
}
