package org.rayeye.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.rayeye.util.exception.BusinessException;
import org.rayeye.util.exception.TransactionException;
import org.rayeye.vertx.result.ResultOb;
import org.rayeye.vertx.spring.SpringContextUtil;
import org.rayeye.vertx.standard.StandardVertxUtil;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * verticle 处理器工厂
 * 支持集群、单例
 *
 * @ProjectName: vertx-core
 * @Package: org.rayeye.vertx.verticle
 * @ClassName: VerticleHandlerFactory
 * @Description: verticle 处理器工厂
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/20 18:22
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/20 18:22
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class VerticleHandlerFactory extends AbstractVerticle {
    private final Object service;
    private String busAddress;

    public VerticleHandlerFactory(String beanName, String eventBusAddress) {
        ApplicationContext applicationContext=SpringContextUtil.getApplicationContext();
        Objects.requireNonNull(applicationContext, "The spring container has not been initialized to complete or has failed.");
        service = applicationContext.getBean(beanName);
        Objects.requireNonNull(service, "Could't find the service processor class in spring context.");
        this.busAddress=eventBusAddress;
    }
    /**
     * 针对verticle处理返回信息解析，主要针对错误信息识别成json数据，用户明文返回出去
     * 依赖与org.util中的exception
     * @method      setResult
     * @author      Neil.Zhou
     * @version
     * @param errorObj
     * @param result
     * @return      org.rayeye.vertx.result.ResultOb
     * @exception
     * @date        2017/9/20 18:34
     */
    private ResultOb setResult(Throwable errorObj, ResultOb result){
        try{
            JsonObject error= new JsonObject(errorObj.getMessage());
            if(error.containsKey("httpStatus")){
                result.setCode(error.getInteger("httpStatus",500));
            }
            /**** 服务正忙 默认是服务异常，为了友好性和调试方便，将错误信息解析后放到msg信息中 ***/
            if(error.containsKey("code")){
                result.setData(error.getString("code","服务正忙,稍后再试.").concat("[").concat(error.getString("detailMsg")).concat("]"));
            }
            if(error.containsKey("message")){
                result.setMsg(error.getString("message","服务正忙,稍后再试."));
            }
        }catch(Exception ex){
            Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
            Matcher matcher=p.matcher(errorObj.getMessage());
            if(matcher.find()){
                result.setMsg(errorObj.getMessage());
                result.setData(errorObj.getLocalizedMessage());
            }else{
                result.setMsg("服务正忙,稍后再试.");
                result.setData(errorObj.getLocalizedMessage());
            }
        }
        return result;
    }
    /**
     * 消息处理handler
     * @method      msgHandler
     * @author      Neil.Zhou
     * @param
     * @return      io.vertx.core.Handler<io.vertx.core.eventbus.Message<io.vertx.core.json.JsonObject>>
     * @exception
     * @date        2017/9/20 18:37
     */
    private Handler<Message<JsonObject>> msgHandler() {
        ResultOb result=new ResultOb();
        return msg -> {
            String m = msg.headers().get("method");
            try {
                JsonObject message = (JsonObject) service.getClass().getMethod(m, JsonObject.class).invoke(service, msg.body());
                msg.reply(message);
            } catch (Exception e) {
                result.setCode(500);
                if(e instanceof NoSuchMethodException){
                    msg.reply(new JsonObject(ResultOb.build().setMsg("[NO_HANDLERS] 当前请求资源无效.").setCode(500).setData(e.getMessage()).toString()));
                    return;
                }
                Throwable errorObj=((InvocationTargetException)e).getTargetException();
                if(errorObj instanceof BusinessException ||errorObj instanceof TransactionException){
                    if (StringUtils.isNotBlank(errorObj.getMessage())){
                        setResult(errorObj,result);
                    }else{
                        result.setMsg("服务正忙,稍后再试.");
                        result.setData(errorObj.getLocalizedMessage());
                    }
                }else if(errorObj.getCause() instanceof BusinessException||errorObj.getCause() instanceof TransactionException){
                    if (StringUtils.isNotBlank(errorObj.getCause().getMessage())){
                        setResult(errorObj.getCause(),result);
                    }else{
                        result.setMsg("服务正忙,稍后再试.");
                        result.setData(errorObj.getCause().getLocalizedMessage());
                    }
                }else{
                    Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
                    if(e.getCause()!=null&& StringUtils.isNotBlank(e.getCause().getMessage())){
                        Matcher matcher=p.matcher(e.getCause().getMessage());
                        if(matcher.find()){
                            result.setMsg(errorObj.getMessage());
                            result.setData(errorObj.getLocalizedMessage());
                        }else{
                            result.setMsg("服务正忙,稍后再试.");
                            result.setData(errorObj.getMessage());
                        }
                    }else{
                        result.setMsg("服务正忙,稍后再试.");
                        result.setData(errorObj.getMessage());
                    }
                }
                msg.reply(new JsonObject(Json.encode(result)));
            }
        };
    }
    /**
     * 注册事件驱动并
     * @method      start
     * @author      Neil.Zhou
     * @version
     * @see #start()
     * @param
     * @return      void
     * @exception Exception
     * @date        2017/9/20 18:43
     */
    @Override
    public void start() throws Exception {
        super.start();
        StandardVertxUtil.getStandardVertx().eventBus().<JsonObject>consumer(busAddress).handler(msgHandler());
    }
}
