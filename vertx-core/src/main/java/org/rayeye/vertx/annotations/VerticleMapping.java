package org.rayeye.vertx.annotations;

import java.lang.annotation.*;

/**
 * Verticle 映射，用于发布每个处理器所对应的真实处理服务类
 * @ProjectName:    vertx-core
 * @Package:        org.rayeye.vertx.annotations
 * @ClassName:      AuthInterceptor
 * @Description:    Verticle 映射，用于发布每个处理器所对应的真实处理服务类
 * @Author:         Neil.Zhou
 * @CreateDate:     2017/9/20 13:34
 * @UpdateUser:     Neil.Zhou
 * @UpdateDate:     2017/9/20 13:34
 * @UpdateRemark:   need to permission to verify
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VerticleMapping {
    String value() default "";
    boolean isCluster() default false;
    boolean isServiceReg() default false;
}
