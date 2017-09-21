package org.rayeye.vertx.annotations;

import java.lang.annotation.*;


/**
 * 用于标识handler注解
 * @ProjectName:
 * @Package:        org.rayeye.vertx.annotations
 * @ClassName:      ServiceMethod
 * @Description:    用于标识handler注解
 * @Author:         Neil.Zhou
 * @CreateDate:     2017/9/21 13:22
 * @UpdateUser:     Neil.Zhou
 * @UpdateDate:     2017/9/21 13:22
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/21</p>
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceMethod {

    /* the server name mapping */
    String value() default "";
    /* the server method support http request type */
    RouteMethod[] method() default RouteMethod.GET;
    /* the server method descript */
    String descript() default "";
}
