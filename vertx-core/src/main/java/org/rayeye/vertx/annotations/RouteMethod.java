package org.rayeye.vertx.annotations;

/**
 * Router API 请求处理方式枚举
 * @projectName:    vertx-core
 * @package:        org.rayeye.vertx.annotations
 * @className:      RouteMethod
 * @description:    Router API 请求处理方式
 * @author:         Neil.Zhou
 * @createDate:     2017/9/20 13:34
 * @updateUser:     Neil.Zhou
 * @updateDate:     2017/9/20 13:34
 * @updateRemark:   need to permission to verify
 * @version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
public enum RouteMethod {
    /**
     * CROS 跨域时会优先发起options请求
     * OPTIONS方法是用于请求获得由Request-URI标识的资源在请求/响应的通信过程中可以使用的功能选项
     * 1、获取服务器支持的HTTP请求方法；也是黑客经常使用的方法。
     * 2、用来检查服务器的性能。例如：AJAX进行跨域请求时的预检，需要向另外一个域名的资源发送一个HTTP OPTIONS请求头，用以判断实际发送的请求是否安全。
     */
    OPTIONS,
    /**
     * Http Get Request
     */
    GET,
    /**
     * 请求页面的首部
     */
    HEAD,
    /**
     * 请求服务器接受所指定的文档作为对所标识的URI的新的从属实体。
     */
    POST,
    /**
     * 从客户端向服务器传送的数据取代指定的文档的内容。
     */
    PUT,
    /**
     * 请求服务器删除指定的页面。
     */
    DELETE,
    /**
     * 请求服务器在响应中的实体主体部分返回所得到的内容。
     */
    TRACE,
    /**
     * 服务连接
     */
    CONNECT,
    /**
     * 实体中包含一个表，表中说明与该URI所表示的原内容的区别。
     */
    PATCH,
    /**
     * 所有请求方式合体
     */
    ROUTE
}
