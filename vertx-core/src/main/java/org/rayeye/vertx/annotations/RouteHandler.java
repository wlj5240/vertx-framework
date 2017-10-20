package org.rayeye.vertx.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Router API类 标识注解
 * @projectName:    vertx-core
 * @package:        org.rayeye.vertx.annotations
 * @className:      RouteHandler
 * @description:    用来标识API类
 * @author:         Neil.Zhou
 * @createDate:     2017/9/20 13:34
 * @updateUser:     Neil.Zhou
 * @updateDate:     2017/9/20 13:34
 * @updateRemark:   need to permission to verify
 * @version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RouteHandler {
    String value() default "";
    boolean isAuth() default false;
    /*** 是否直接暴露(开放平台) ***/
    boolean isOpen() default false;
}
