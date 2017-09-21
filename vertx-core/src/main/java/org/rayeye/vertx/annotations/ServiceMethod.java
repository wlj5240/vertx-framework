package org.rayeye.vertx.annotations;

import java.lang.annotation.*;


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
