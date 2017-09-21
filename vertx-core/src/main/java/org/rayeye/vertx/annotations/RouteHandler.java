package org.rayeye.vertx.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Router API类 标识注解
 * @ProjectName:    [vertx-framework]
 * @Package:        [org.rayeye.vertx.annotations]
 * @ClassName:      [RouteHandler]
 * @Description:    [用来标识API类]
 * @Author:         [Neil.Zhou]
 * @CreateDate:     [2017/9/20 13:34]
 * @UpdateUser:     [Neil.Zhou]
 * @UpdateDate:     [2017/9/20 13:34]
 * @UpdateRemark:   [need to permission to verify]
 * @Version:        [1.0]
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RouteHandler {
    String value() default "";
    /*** 是否直接暴露(开放平台) ***/
    boolean isOpen() default false;
}
