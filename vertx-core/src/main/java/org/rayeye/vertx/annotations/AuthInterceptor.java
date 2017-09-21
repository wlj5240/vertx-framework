package org.rayeye.vertx.annotations;

import java.lang.annotation.*;

/**
 * 权限拦截器注解
 * @ProjectName:    [vertx-framework]
 * @Package:        [org.rayeye.vertx.annotations]
 * @ClassName:      [AuthInterceptor]
 * @Description:    [拦截器注解，标识那些方法需要被拦截，可进一步使用]
 * @Author:         [Neil.Zhou]
 * @CreateDate:     [2017/9/20 13:34]
 * @UpdateUser:     [Neil.Zhou]
 * @UpdateDate:     [2017/9/20 13:34]
 * @UpdateRemark:   [need to permission to verify]
 * @Version:        [1.0]
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthInterceptor {
    boolean isVerify() default true;
}
