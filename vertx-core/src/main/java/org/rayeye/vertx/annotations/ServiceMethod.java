package org.rayeye.vertx.annotations;

import java.lang.annotation.*;


/**
 * 用于标识handler注解
 * @projectName:
 * @package:        org.rayeye.vertx.annotations
 * @className:      ServiceMethod
 * @description:    用于标识handler注解
 * @author:         Neil.Zhou
 * @createDate:     2017/9/21 13:22
 * @updateUser:     Neil.Zhou
 * @updateDate:     2017/9/21 13:22
 * @updateRemark:   The modified content
 * @version:        1.0
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
