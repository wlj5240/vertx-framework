package org.rayeye.vertx.annotations;

import java.lang.annotation.*;

/**
 * 权限拦截器注解
 * @projectName:    vertx-core
 * @package:        org.rayeye.vertx.annotations
 * @className:      AuthInterceptor
 * @description:    拦截器注解，标识那些方法需要被拦截，可进一步使用
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
public @interface AuthInterceptor {
    boolean isVerify() default true;
}
