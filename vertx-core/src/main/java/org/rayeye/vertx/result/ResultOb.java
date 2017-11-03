package org.rayeye.vertx.result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 *  响应消息类型
 * @projectName:    vertx-core
 * @package:        org.rayeye.vertx.result
 * @className:      ResultOb
 * @description:    响应消息类型
 * @author:         Neil.Zhou
 * @createDate:     2017/9/20 13:34
 * @updateUser:     Neil.Zhou
 * @updateDate:     2017/9/20 13:34
 * @updateRemark:   相应消息
 * @version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
public class ResultOb<T> {
    /** 状态 **/
    private int code=200 ;
    /** 消息 **/
    private String msg="SUCCESS";
    private T data;

    public T getData() {
        return data;
    }

    public ResultOb setData(T data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ResultOb setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultOb setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    @Override
    public String toString(){
       // return Json.encode(this);
        return JSON.toJSONString(this, SerializerFeature.BrowserCompatible);
    }

    public static ResultOb build(){
        return new ResultOb();
    }
}
