package org.rayeye.vertx.annotations;

import java.lang.annotation.*;

/**
 * Verticle 映射，用于发布每个处理器所对应的真实处理服务类
 * @projectName:    vertx-core
 * @package:        org.rayeye.vertx.annotations
 * @className:      AuthInterceptor
 * @description:    Verticle 映射，用于发布每个处理器所对应的真实处理服务类
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
@Documented
public @interface VerticleMapping {
    String value() default "";
    @Deprecated
    boolean isCluster() default false;
    boolean isServiceReg() default false;
}
