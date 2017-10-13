package org.rayeye.vertx.annotations;

import java.lang.annotation.*;

/**
 * Router API Mehtod 标识注解
 * @ProjectName:    vertx-core
 * @Package:        org.rayeye.vertx.annotations
 * @ClassName:      RouteMapping
 * @Description:    用来标识具体API使用
 * @Author:         Neil.Zhou
 * @CreateDate:     2017/9/20 13:34
 * @UpdateUser:     Neil.Zhou
 * @UpdateDate:     2017/9/20 13:34
 * @UpdateRemark:   need to permission to verify
 * @Version:        1.0
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
