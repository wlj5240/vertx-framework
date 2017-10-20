package org.rayeye.vertx.spring;

import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.util.exception.BusinessException;
import org.rayeye.util.exception.ServerException;
import org.rayeye.util.exception.TransactionException;
import org.rayeye.util.html.HttpCode;
import org.rayeye.vertx.address.EventBusAddress;
import org.rayeye.vertx.annotations.RouteMethod;
import org.rayeye.vertx.annotations.ServiceMethod;
import org.rayeye.vertx.annotations.VerticleMapping;
import org.rayeye.vertx.result.ResultOb;
import org.rayeye.vertx.verticle.RegistryHandlersFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spring Service必须继承此类
 * 用于调度具体业务执行函数
 *
 * @projectName: vertx-core
 * @package: org.rayeye.vertx.spring
 * @className: AbstractService
 * @description: Describes the function of the class
 * @author: Neil.Zhou
 * @createDate: 2017/9/26 14:03
 * @updateUser: Neil.Zhou
 * @updateDate: 2017/9/26 14:03
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class AbstractService {
    private static Log logger = LogFactory.get(AbstractService.class);
    private static final String PREFIX="/";
    /**
     * 当前类所有函数的入口
     * @method      execute
     * @author      Neil.Zhou
     * @param req
     * @return      io.vertx.core.json.JsonObject
     * @exception   Exception
     * @date        2017/9/26 14:05
     */
    @ServiceMethod
    public JsonObject execute(JsonObject req) throws Exception{
        JsonObject result=new JsonObject();
        ResultOb ob=new ResultOb();
        // 获得当前注册的handler处理器(spring中的bean)
        Class targetClass = this.getClass();
        try {
            String methodStr=null;
            if (this.getClass().isAnnotationPresent(VerticleMapping.class)) {
                VerticleMapping service = this.getClass().getAnnotation(VerticleMapping.class);
                String serviceRouter="";
                if (StringUtils.isBlank(service.value())) {
                    serviceRouter = this.getClass().getName();
                } else {
                    serviceRouter = service.value();
                }
                if (StringUtils.isNotBlank(RegistryHandlersFactory.BASE_ROUTER)&&!RegistryHandlersFactory.BASE_ROUTER.endsWith(PREFIX)) {
                    serviceRouter = RegistryHandlersFactory.BASE_ROUTER + PREFIX + serviceRouter;
                } else {
                    serviceRouter = RegistryHandlersFactory.BASE_ROUTER +  serviceRouter;
                }
                serviceRouter=serviceRouter.replace("//",PREFIX);
                if (serviceRouter.trim().length()>1&&serviceRouter.endsWith(PREFIX)) {
                    serviceRouter = serviceRouter.substring(0, serviceRouter.length() - 1);
                }
                // 替换service router部分，求得函数
                if(req.getString("uri").contains(serviceRouter)){
                    methodStr=req.getString("uri").substring(req.getString("uri").indexOf(serviceRouter)).replace(serviceRouter,"");
                }
                // methodStr is "/xxxxx/....." or "xxxx/....."
                logger.trace("预计要调度的函数router为:{}",methodStr);
                if(StringUtils.isBlank(methodStr)){
                    //NOT_FOUND(HttpCode.HTTP_404, "NOT_FOUND", "数据不存在","数据不存在"),
                    return new JsonObject(ResultOb.build().setCode(HttpCode.HTTP_400).setMsg("[NOT_FOUND]请求资源无效.").toString());
                }
                if(!methodStr.startsWith(PREFIX)){
                    methodStr=PREFIX+methodStr;
                }
            }
            boolean isFound=false;
            // 遍历所有映射的函数
            Method[] methods=targetClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(ServiceMethod.class)) {
                    ServiceMethod serviceMethod = method.getAnnotation(ServiceMethod.class);
                    String methodTarget = serviceMethod.value();
                    if (!methodTarget.startsWith(PREFIX)) {
                        methodTarget =PREFIX + methodTarget;
                    }
                    if(methodTarget.equals(methodStr)){
                        isFound=true;
                        RouteMethod[] routeMethods=serviceMethod.method();
                        Boolean isExecuted=false;
                        // 判断是否是合适的请求类型
                        if(req.containsKey("method")){
                            RouteMethod routeMethod=Enum.valueOf(RouteMethod.class,req.getString("method"));
                            for (RouteMethod methodTag:routeMethods){
                                if(methodTag.equals(RouteMethod.ROUTE)||methodTag.equals(routeMethod)){
                                    isExecuted=true;
                                    Method  mh = ReflectionUtils.findMethod(SpringContextUtil.getBean(EventBusAddress.toLowerCaseFirstOne(this.getClass().getSimpleName())).getClass(),method.getName(),new Class[]{JsonObject.class} );
                                    result=(JsonObject) ReflectionUtils.invokeMethod(mh,SpringContextUtil.getBean(EventBusAddress.toLowerCaseFirstOne(this.getClass().getSimpleName())),req.getJsonObject("params"));
                                    break;
                                }
                            }
                            if(!isExecuted){
                                // METHOD_NOT_ALLOWED(HttpCode.HTTP_405, "METHOD_NOT_ALLOWED","请求方式错误.","请求方式错误."),
                                return new JsonObject(ResultOb.build().setCode(HttpCode.HTTP_405).setMsg("[METHOD_NOT_ALLOWED]请使用正确的请求方式.").toString());
                            }
                        }else{
                            //未知请求类型
                            logger.error("执行函数中没有匹配method 参数.");
                            return new JsonObject(ResultOb.build().setCode(HttpCode.HTTP_405).setMsg("[METHOD_NOT_ALLOWED]暂时不支持"+req.getString("method")+"]请求方式.").toString());
                        }
                        break;
                    }
                }
            }
            // 是否未找到服务
            if(!isFound){
                //SERVICE_UNAVAILABLE(HttpCode.HTTP_503, "SERVICE_UNAVAILABLE", "服务不可用.","服务不可用."),
                logger.error("未找到需要执行的函数：{}",methodStr);
                return new JsonObject(ResultOb.build().setCode(HttpCode.HTTP_503).setMsg("[SERVICE_UNAVAILABLE] 未找到您要的服务.").toString());
            }
        } catch (Exception e) {
            if(e instanceof NoSuchMethodException){
                logger.error(e,"未匹配对应的业务处理服务.",e.getMessage());
                return new JsonObject(ResultOb.build().setCode(HttpCode.HTTP_503).setMsg("[SERVICE_UNAVAILABLE] 未找到您要的服务.").toString());
            }
            if((e instanceof BusinessException)||(e instanceof ServerException)||(e instanceof TransactionException)) {
                logger.trace(e,"业务处理中，已捕获异常转换成服务识别异常,{}",e.getMessage());
                throw e;
            }else{
                logger.error(e,"请联系管理员,程序发生未知异常,{}",e.getMessage());
                throw e;
            }
        }
        return result;
    }
}
