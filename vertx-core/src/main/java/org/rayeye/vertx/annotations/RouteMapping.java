package org.rayeye.vertx.annotations;

import java.lang.annotation.*;

/**
 * Router API Mehtod 标识注解
 * @projectName:    vertx-core
 * @package:        org.rayeye.vertx.annotations
 * @className:      RouteMapping
 * @description:    用来标识具体API使用
 * @author:         Neil.Zhou
 * @createDate:     2017/9/20 13:34
 * @updateUser:     Neil.Zhou
 * @updateDate:     2017/9/20 13:34
 * @updateRemark:   need to permission to verify
 * @version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RouteMapping {

    String value() default "";
    /**** 是否覆盖 *****/
    boolean isCover() default true;
   boolean isAuth() default false;
    /**** 使用http method *****/
    RouteMethod method() default RouteMethod.GET;
    /**** 接口描述 *****/
    String descript() default "";

}
